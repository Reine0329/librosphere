<template>
  <div class="book-management">
    <el-form :model="bookForm" ref="bookForm" label-width="120px" class="book-form">
      <el-form-item label="书名">
        <el-input v-model="bookForm.title"></el-input>
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="bookForm.author"></el-input>
      </el-form-item>
      <el-form-item label="简介">
        <el-input type="textarea" v-model="bookForm.description"></el-input>
      </el-form-item>
      <el-form-item label="类别">
        <el-select v-model="bookForm.category" placeholder="请选择类别">
          <el-option
              v-for="category in categories"
              :key="category"
              :label="category"
              :value="category">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="文件">
          <div class="upload-wrapper">
            <el-upload
                ref="upload"
                action="http://127.0.0.1:8080/file/upload"
                :auto-upload="false"
                :limit="1"
                :on-change="handleFileChange"
                :before-upload="beforeUpload"
                accept=".epub"
                drag>
              <el-icon size="20px"><UploadFilled /></el-icon>
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              <div class="el-upload__tip" slot="tip">请上传一份epub格式文件</div>
            </el-upload>
          </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="books" style="margin-top: 20px" class="book-table">
      <el-table-column prop="title" label="书名"></el-table-column>
      <el-table-column prop="author" label="作者"></el-table-column>
      <el-table-column prop="description" label="简介"></el-table-column>
      <el-table-column prop="category" label="类别"></el-table-column>
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button @click="editBook(scope.row)">编辑</el-button>
          <el-button type="danger" @click="deleteBook(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { uploadBook, getAllBooks, deleteBook, updateBook } from '@/apis/book.js';
import {UploadFilled} from "@element-plus/icons-vue";

export default {
  components: {UploadFilled},
  data() {
    return {
      bookForm: {
        id: null,
        title: '',
        author: '',
        description: '',
        category: '',
        file: null,
        fileName: ''
      },
      books: [],
      categories: ['文学', '小说', '教育', '纪实', '艺术', '其他']
    };
  },
  created() {
    this.fetchBooks();
  },
  methods: {
    handleFileChange(file) {
      this.bookForm.file = file.raw;
    },
    beforeUpload(file) {
      if (this.bookForm.file) {
        this.$refs.upload.clearFiles();
      }
      this.bookForm.file = file;
      return false;
    },
    async submitForm() {
      try {
        if (this.bookForm.id) {
          await updateBook(this.bookForm.id, this.bookForm);
          this.$message.success('书籍更新成功');
        } else {
          await uploadBook(this.bookForm);
          this.$message.success('书籍上传成功');
        }
        this.resetForm();
        await this.fetchBooks();
      } catch (error) {
        console.error(error);
        this.$message.error('书籍保存失败，请完善并检查输入信息');
      }
    },
    async fetchBooks() {
      try {
        this.books = await getAllBooks();
      } catch (error) {
        console.error(error);
        this.$message.error('获取书籍列表失败');
      }
    },
    async deleteBook(id) {
      try {
        await deleteBook(id);
        this.$message.success('书籍删除成功');
        await this.fetchBooks();
      } catch (error) {
        console.error(error);
        this.$message.error('书籍删除失败');
      }
    },
    editBook(book) {
      this.bookForm = { ...book, file: null };
      this.$nextTick(() => {
        this.$refs.bookForm.$el.scrollIntoView({ behavior: 'smooth' });
      });
    },
    resetForm() {
      this.bookForm = {
        id: null,
        title: '',
        author: '',
        description: '',
        category: '',
        file: null,
        fileName: ''
      };
      this.$refs.upload.clearFiles();
    }
  }
};
</script>

<style scoped>
.book-management {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.book-form {
  margin-bottom: 20px;
}

.upload-wrapper {
  width: 100%;
}

.el-table th, .el-table td {
  text-align: center;
}
</style>
