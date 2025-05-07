import request from '@/utils/request.js'

//登录接口
export const login = (form) => {
    return request.post('/login', form)
}

//检查用户登录状态
export const checkLogin = (form) => {
    return request.post('/check/login', form)
}