<template>
  <div class="user-management">
      <el-button type="primary" @click="showCreateUserDialog">新增用户</el-button>
      <el-table :data="users" style="width: 100%; margin-top: 20px">
        <el-table-column prop="id" label="ID" width="50"></el-table-column>
        <el-table-column prop="account" label="账号"></el-table-column>
        <el-table-column prop="password" label="密码"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="roleName" label="身份"></el-table-column>
        <el-table-column prop="phone" label="手机号"></el-table-column>
        <el-table-column label="操作" width="180">
          <template v-slot="scope">
            <el-button size="default" @click="showEditUserDialog(scope.row)">编辑</el-button>
            <el-button size="default" type="danger" @click="confirmDeleteUser(scope.row.id)" :disabled="scope.row.authority === '1'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-dialog v-model="isDialogVisible" title="用户信息">
        <el-form :model="currentUser" :rules="rules" ref="userForm">
          <el-form-item label="账号" :label-width="formLabelWidth" prop="account">
            <el-input v-model="currentUser.account"></el-input>
          </el-form-item>
          <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
            <el-input v-model="currentUser.password" type="password"></el-input>
          </el-form-item>
          <el-form-item label="姓名" :label-width="formLabelWidth" prop="name">
            <el-input v-model="currentUser.name"></el-input>
          </el-form-item>
          <el-form-item label="手机号" :label-width="formLabelWidth" prop="phone">
            <el-input v-model="currentUser.phone"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="isDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveUser">保存</el-button>
        </div>
      </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';
import { getUsers, addUser, updateUser, deleteUser } from '@/apis/user.js';

export default {
  name: 'UserView',
  setup() {
    const users = ref([]);
    const isDialogVisible = ref(false);
    const userForm = ref(null);
    const currentUser = reactive({
      id: null,
      account: '',
      password: '',
      name: '',
      phone: '',
      authority: '2' // 默认为用户
    });
    const formLabelWidth = '80px';
    const rules = {
      account: [
        { required: true, message: '请输入账号', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ],
      name: [
        { required: true, message: '请输入姓名', trigger: 'blur' }
      ],
      phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' }
      ]
    };

    const fetchUsers = async () => {
      try {
        const response = await getUsers();
        users.value = response.data.map(user => ({
          ...user,
          roleName: user.authority === '1' ? 'ADMIN' : 'USER'
        }));
      } catch (error) {
        ElMessage.error('获取用户列表失败');
      }
    };

    const showCreateUserDialog = () => {
      Object.assign(currentUser, {
        id: null,
        account: '',
        password: '',
        name: '',
        phone: '',
        authority: '2' // 默认为用户
      });
      isDialogVisible.value = true;
    };

    const showEditUserDialog = (user) => {
      Object.assign(currentUser, user);
      isDialogVisible.value = true;
    };

    const saveUser = async () => {
      userForm.value.validate(async (valid) => {
        if (valid) {
          try {
            const response = await getUsers();
            const existingUser = response.data.find(user => user.account === currentUser.account && user.id !== currentUser.id);

            if (existingUser) {
              ElMessage.error('账号已存在');
              return;
            }

            if (currentUser.id === null) {
              await addUser(currentUser);
              ElMessage.success('用户创建成功');
            } else {
              await updateUser(currentUser.id, currentUser);
              ElMessage.success('用户更新成功');
            }
            isDialogVisible.value = false;
            await fetchUsers();
          } catch (error) {
            ElMessage.error('保存用户失败');
          }
        } else {
          ElMessage.error('请检查表单填写是否正确');
        }
      });
    };

    const confirmDeleteUser = (id) => {
      ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteUser(id).then(() => {
          ElMessage.success('用户删除成功');
          fetchUsers();
        }).catch(() => {
          ElMessage.error('删除用户失败');
        });
      }).catch(() => {
        ElMessage.info('已取消删除');
      });
    };

    onMounted(fetchUsers);

    return {
      users,
      isDialogVisible,
      currentUser,
      userForm,
      formLabelWidth,
      rules,
      showCreateUserDialog,
      showEditUserDialog,
      saveUser,
      confirmDeleteUser
    };
  }
};
</script>

<style scoped>
.user-management {
  padding: 20px;
  margin: auto;
  border-radius: 10px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #f9f9f9;
}

.dialog-footer {
  text-align: right;
}
</style>
