import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import Login from '@/views/LoginView.vue'
import Layout from '@/views/Layout.vue'
import Home from '@/views/HomeView.vue'
import User from '@/views/UserView.vue'
import Book from '@/views/BookView.vue'
import Comment from '@/views/CommentView.vue'
import Info from '@/views/InfoView.vue'

import { checkLogin } from '@/apis/login.js'
import { getUserInfo } from '@/utils/storage.js'

const routes = [
    {
        path: '/',
        redirect: '/home'
    },
    {
        path: '/login',
        component: Login
    },
    {
        path: '/layout',
        component: Layout,
        children: [
            { path: '/home', component: Home },
            { path: '/user', component: User },
            { path: '/book', component: Book },
            { path: '/comment', component:Comment },
            { path: '/info', component:Info }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})

// 配置游客可以访问的界面
const touristVisitUrl = ['/login']

// 全局路由前置守卫
router.beforeEach(async (to, from, next) => {
    if (touristVisitUrl.includes(to.path)) {
        next()
    } else {
        const userInfo = getUserInfo()
        console.log('User Info:', userInfo)

        if (!userInfo || !userInfo.token) {
            ElMessage.error('请先登录！')
            next('/login')
            return
        }

        try {
            const result = await checkLogin(userInfo)
            if (result.data.message === 'NOT_LOGIN') {
                ElMessage.error('登录超时，请重新登录！')
                next('/login')
            } else {
                next()
            }
        } catch (error) {
            ElMessage.error('检查登录状态时出错！')
            next('/login')
        }
    }
})

export default router
