<template>
  <div class="home-view">
    <div class="sidebar">
      <el-menu default-active="all" class="el-menu-vertical-demo">
        <el-menu-item index="all" @click="filterByCategory('全部')">全部</el-menu-item>
        <el-menu-item index="literature" @click="filterByCategory('文学')">文学</el-menu-item>
        <el-menu-item index="novel" @click="filterByCategory('小说')">小说</el-menu-item>
        <el-menu-item index="education" @click="filterByCategory('教育')">教育</el-menu-item>
        <el-menu-item index="life" @click="filterByCategory('纪实')">纪实</el-menu-item>
        <el-menu-item index="art" @click="filterByCategory('艺术')">艺术</el-menu-item>
        <el-menu-item index="other" @click="filterByCategory('其他')">其他</el-menu-item>
      </el-menu>
    </div>
    <div class="main-content">
      <el-input v-model="searchKeyword" placeholder="按书名、作者、描述关键词进行搜索" class="search-input">
        <template #append>
          <el-button @click="fetchBooks">搜索</el-button>
        </template>
      </el-input>
      <el-row :gutter="20" class="book-list">
        <el-col :span="4" v-for="(book, index) in filteredBooks" :key="book.id">
          <el-card shadow="hover" style="margin-bottom: 20px;" class="book-card">
            <div class="book-cover-container" @mouseenter="showTitle(index)" @mouseleave="hideTitle">
              <img :src="book.coverUrl" class="book-cover" @click="showBookDetails(book)" alt="image">
              <div v-if="currentTitle === index" class="tooltip">{{ book.title }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-dialog v-model="dialogVisible" width="50%" :before-close="handleClose" :destroy-on-close="true">
        <template #header>
          <div class="dialog-title">{{ selectedBook?.title }}</div>
        </template>
        <el-scrollbar class="dialog-content">
          <div v-if="selectedBook">
            <el-divider></el-divider>
            <p><strong>作者:</strong> {{ selectedBook.author }}</p>
            <p><strong>描述:</strong> {{ selectedBook.description }}</p>
            <el-divider></el-divider>

            <div class="comments-section">
              <div class="add-comment">
                <el-input type="textarea" v-model="myComments.content" placeholder="添加评论..." rows="3"></el-input>
                <el-rate v-model="myComments.rating"
                         :texts="['糟糕', '有点失望', '一般', '还行', '爆赞']"
                         :colors="['#409eff', '#67c23a', '#FF9900']"
                         show-text
                         clearable
                ></el-rate>
                <br>
                <el-button type="primary" @click="submitComment">提交评论</el-button>
              </div>
              <el-divider></el-divider>
              <el-tabs v-model="activeTab" type="border-card">
                <el-tab-pane label="全部评论" name="all"></el-tab-pane>
                <el-tab-pane label="我的评论" name="mine"></el-tab-pane>
                <el-tab-pane label="隐藏评论" name="hidden"></el-tab-pane>
                <div class="comments-list">
                  <el-row v-for="comment in filteredComments" :key="comment.id" class="comment-item">
                    <el-col :span="18">
                      <p><strong>{{ comment.user.name }}:</strong></p>
                      <p>{{ comment.content }}</p>
                      <el-rate v-model="comment.rating"
                               :colors="['#409eff', '#67c23a', '#FF9900']"
                               disabled
                      ></el-rate>
                    </el-col>
                    <el-col :span="6" class="comment-date">
                      <p>{{ formatDate(comment.time) }}</p>
                      <el-icon v-if="comment.user.name === getUserInfo().name"
                               @click="handleDeleteComment(comment.id)"
                               size="20px"
                               style="cursor: pointer; position: absolute; bottom: 5px; right: 50px;">
                        <delete />
                      </el-icon>
                    </el-col>
                  </el-row>
                </div>
              </el-tabs>
            </div>
            <el-divider></el-divider>

            <div class="toc">
              <el-button class="toc-toggle" @click="toggleToc">{{ showToc ? '收起目录' : '展开目录' }}</el-button>
              <div v-if="showToc" class="toc-content">
                <h3>目录</h3>
                <el-tree
                    :data="toc"
                    :props="defaultProps"
                    node-key="id"
                    @node-click="navigateTo"
                    accordion
                    highlight-current>
                </el-tree>
              </div>
            </div>
            <div id="viewer" class="epub-viewer"></div>
            <div class="controls">
              <el-button @click="previousPage">上一页</el-button>
              <el-button @click="nextPage">下一页</el-button>
              <el-button @click="downloadFile" type="primary">下载</el-button>
            </div>
          </div>
        </el-scrollbar>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { getAllBooks, searchBooks } from '@/apis/book.js';
import Epub from 'epubjs';
import {createComment, deleteComment, getCommentsByBook} from '@/apis/comment.js';
import {getUserInfo} from '@/utils/storage.js';
import placeholderCover from '@/assets/img/cover.jpg';
import {ElMessage} from 'element-plus';
import { Delete } from '@element-plus/icons-vue';

export default {
  components: {Delete},
  data() {
    return {
      books: [],
      searchKeyword: '',
      dialogVisible: false,
      selectedBook: null,
      bookRendition: null,
      currentCategory: '全部',
      toc: [],
      showToc: false,
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      currentTitle: null,
      allComments: [],
      myComments:{
        book: {},
        user: {
          name: '',
        },
        content: '',
        rating: 0,
        time: null
      },
      placeholderCover,
      activeTab: 'all',
    };
  },
  computed: {
    filteredBooks() {
      if (this.currentCategory === '全部') {
        return this.books;
      } else {
        return this.books.filter(book => book.category === this.currentCategory);
      }
    },
    filteredComments() {
      if (this.activeTab === 'all') {
        return this.allComments;
      } else if (this.activeTab === 'mine') {
        return this.allComments.filter(comment => comment.user.name === getUserInfo().name);
      } else {
        return null;
      }
    }
  },
  methods: {
    getUserInfo,
    async fetchBooks() {
      try {
        if (this.searchKeyword) {
          this.books = await searchBooks(this.searchKeyword);
        } else {
          this.books = await getAllBooks();
        }
        this.books = await Promise.all(this.books.map(async book => {
          book.coverUrl = this.placeholderCover;
          this.loadBookCover(book.fileName).then(coverUrl => {
            book.coverUrl = coverUrl;
          }).catch(() => {
            book.coverUrl = this.placeholderCover;
          });
          return book;
        }));
      } catch (error) {
        console.error('书本加载错误:', error);
      }
    },
    showTitle(title) {
      this.currentTitle = title;
    },
    hideTitle() {
      this.currentTitle = null;
    },
    async filterByCategory(category) {
      this.currentCategory = category;
      await this.fetchBooks();
    },
    async loadBookCover(fileName) {
      const encodedFileName = encodeURIComponent(fileName);
      const epubUrl = `http://localhost:8080/file/${encodedFileName}`;

      const book = Epub(epubUrl);
      await book.ready;
      return await book.coverUrl();
    },
    showBookDetails(book) {
      this.selectedBook = book;
      this.dialogVisible = true;
      this.toc = [];
      this.loadBookPreview(book.fileName);
      this.loadComment(book);
    },
    async loadComment(book){
      const response = await getCommentsByBook(book.id);
      this.allComments = response.data;
    },
    async submitComment(){
      if (!this.myComments.content || !this.myComments.rating) {
        this.$message.warning('请填写评论内容和评分');
        return;
      }

      this.myComments.book = this.selectedBook;
      this.myComments.user = { name: getUserInfo().name};
      this.myComments.time = new Date();
      try {
        await createComment(this.myComments);
        this.$message.success('评论成功');
        this.myComments.content = '';
        this.myComments.rating = 0;
        await this.loadComment(this.selectedBook);
      } catch (error) {
        console.error('Error submitting comment:', error);
        this.$message.error('评论失败');
      }
    },
    async handleDeleteComment(id) {
      try {
        await deleteComment(id);
        ElMessage.success('评论已删除');
        await this.loadComment(this.selectedBook);
      } catch (error) {
        console.error('Error deleting comment:', error);
        ElMessage.error('删除评论失败');
      }
    },
    async loadBookPreview(fileName) {
      const encodedFileName = encodeURIComponent(fileName);
      const epubUrl = `http://localhost:8080/file/${encodedFileName}`;

      try {
        const book = Epub(epubUrl);
        await book.ready;

        const navigation = await book.loaded.navigation;
        this.toc = this.formatToc(navigation.toc);

        this.bookRendition = book.renderTo("viewer", {
          width: "100%",
          height: "670px"
        });

        try {
          await this.bookRendition.display();
        } catch (error) {
          console.error('章节加载错误:', error);
        }
      } catch (error) {
        console.error('书本加载错误:', error);
      }
    },
    formatToc(toc) {
      return toc.map(item => ({
        id: item.id,
        label: item.label,
        href: item.href,
        children: item.subitems ? this.formatToc(item.subitems) : []
      }));
    },
    previousPage() {
      if (this.bookRendition) {
        this.bookRendition.prev();
      }
    },
    nextPage() {
      if (this.bookRendition) {
        this.bookRendition.next();
      }
    },
    navigateTo(node) {
      if (this.bookRendition) {
        this.bookRendition.display(node.href);
      }
    },
    toggleToc() {
      this.showToc = !this.showToc;
    },
    downloadFile() {
      const encodedFileName = encodeURIComponent(this.selectedBook.fileName);
      const epubUrl = `http://localhost:8080/file/${encodedFileName}`;
      //将获取到的二进制文件转成epub下载
      fetch(epubUrl).then(response => response.blob()).then(blob => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = this.selectedBook.fileName;
        a.click();
        URL.revokeObjectURL(url);
      });
    },
    formatDate(date) {
      const options = { year: 'numeric', month: 'numeric', day: 'numeric', hour: 'numeric', minute: 'numeric' };
      return new Date(date).toLocaleDateString('zh-CN', options);
    },
    handleClose() {
      this.dialogVisible = false;
      if (this.bookRendition) {
        this.bookRendition.destroy();
        this.bookRendition = null;
      }
    }
  },
  mounted() {
    this.fetchBooks();
  }
};
</script>

<style>
.home-view {
  display: flex;
  padding: 20px;
  margin: auto;
  border: 1px solid #e0e0e0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #f9f9f9;
  border-radius: 5px;
}

.sidebar {
  width: 200px;
  height: 95vh;
  padding: 20px;
}

.el-menu-vertical-demo {
  height: 100%;
  border-right: 1px solid #e8e8e8;
}

.main-content {
  flex: 1;
  padding: 20px;
}

.search-input {
  margin-bottom: 20px;
}

.book-list {
  display: flex;
  flex-wrap: wrap;
}

.book-card {
  height: 220px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border-radius: 10px;
  overflow: hidden;
  transition: transform 0.3s;
}

.book-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.2);
}

.book-cover-container {
  position: relative;
  flex: 1;
}

.book-cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-bottom: 1px solid #e8e8e8;
}

.tooltip {
  position: absolute;
  background: rgba(0, 0.1, 0.2, 0.7);
  color: white;
  padding: 5px 10px;
  border-radius: 4px;
  z-index: 10;
  top: 30%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 85%;
  text-align: center;
  font-size: 14px;
  opacity: 0;
  transition: opacity 0.3s;
}

.book-cover-container:hover .tooltip {
  opacity: 1;
}

.comment-item {
  margin-bottom: 10px;
}

.epub-viewer {
  width: 100%;
  height: 670px;
  border: 1px solid #e8e8e8;
  border-radius: 10px;
  padding: 10px;
  background-color: #f7f7f7;
}

.controls {
  margin-top: 10px;
}

.toc {
  margin-bottom: 20px;
}

.toc-toggle {
  margin-top: 20px;
}

.toc-content {
  margin-top: 10px;
}

.dialog-title {
  font-size: 18px;
  font-weight: bold;
  font-style: italic;
}

.dialog-content p {
  margin: 10px 0;
  font-size: 16px;
  line-height: 1.5;
}
</style>