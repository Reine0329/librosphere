<template>
  <div class="comment-management">
    <el-form :inline="true" :model="filterForm" class="filter-form">
      <el-form-item label="书本">
        <el-select v-model="filterForm.bookId" placeholder="选择书本">
          <el-option v-for="book in books" :key="book.id" :label="book.title" :value="book.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="用户">
        <el-select v-model="filterForm.userId" placeholder="选择用户">
          <el-option v-for="user in users" :key="user.id" :label="user.name" :value="user.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="时间范围">
        <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            >
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchComments">查询</el-button>
        <el-button @click="resetFilter">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="filteredComments" style="width: 100%" class="comment-table">
      <el-table-column prop="book.title" label="书本"></el-table-column>
      <el-table-column prop="user.name" label="用户"></el-table-column>
      <el-table-column prop="content" label="内容"></el-table-column>
      <el-table-column prop="rating" label="评分"></el-table-column>
      <el-table-column prop="time" label="时间" sortable></el-table-column>
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button type="danger" @click="deleteComment(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="filteredComments.length">
    </el-pagination>
  </div>
</template>

<script>
import { getAllComments, deleteComment } from '@/apis/comment.js';
import { getUsers } from '@/apis/user.js';
import { getAllBooks } from '@/apis/book.js';

export default {
  data() {
    return {
      comments: [],
      books: [],
      users: [],
      filterForm: {
        bookId: null,
        userId: null,
        dateRange: []
      },
      currentPage: 1,
      pageSize: 10
    };
  },
  computed: {
    filteredComments() {
      let filtered = this.comments;

      if (this.filterForm.bookId) {
        filtered = filtered.filter(comment => comment.book.id === this.filterForm.bookId);
      }

      if (this.filterForm.userId) {
        filtered = filtered.filter(comment => comment.user.id === this.filterForm.userId);
      }

      if (this.filterForm.dateRange && this.filterForm.dateRange.length) {
        const [start, end] = this.filterForm.dateRange;
        filtered = filtered.filter(comment => new Date(comment.time) >= new Date(start) && new Date(comment.time) <= new Date(end));
      }

      return filtered;
    }
  },
  created() {
    this.fetchComments();
    this.fetchBooks();
    this.fetchUsers();
  },
  methods: {
    async fetchComments() {
      try {
        const response = await getAllComments();
        this.comments = response.data;
      } catch (error) {
        console.error(error);
        this.$message.error('获取评论失败');
      }
    },
    async fetchBooks() {
      try {
        const response = await getAllBooks();
        console.log(response);
        this.books = response;
        console.log(this.books);
      } catch (error) {
        console.error(error);
        this.$message.error('获取书本列表失败');
      }
    },
    async fetchUsers() {
      try {
        const response = await getUsers();
        console.log(response)
        this.users = response.data;
        console.log(this.users);
      } catch (error) {
        console.error(error);
        this.$message.error('获取用户列表失败');
      }
    },
    async deleteComment(id) {
      try {
        await deleteComment(id);
        this.$message.success('评论删除成功');
        await this.fetchComments();
      } catch (error) {
        console.error(error);
        this.$message.error('评论删除失败');
      }
    },
    resetFilter() {
      this.filterForm = {
        bookId: null,
        userId: null,
        dateRange: []
      };
      this.fetchComments();
    },
    handleSizeChange(size) {
      this.pageSize = size;
    },
    handleCurrentChange(page) {
      this.currentPage = page;
    }
  }
};
</script>

<style scoped>
.comment-management {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.filter-form {
  margin-bottom: 20px;
}

.el-table {
  background-color: #ffffff;
  border-radius: 8px;
}

.el-table th, .el-table td {
  text-align: center;
}
</style>
