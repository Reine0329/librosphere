import axios from 'axios'
import { ElLoading } from 'element-plus'
import { getUserLoginToken } from '@/utils/storage.js'

// 自定义加载界面的选项
const options = {
    lock: true,
    background: 'rgba(20, 20, 20, 0.6)'
}

const request = axios.create({
    baseURL: 'http://127.0.0.1:8080/',
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json'
    }
})

let loadingInstance

// 添加请求拦截器
request.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    loadingInstance = ElLoading.service(options)
    config.headers.Authorization = getUserLoginToken()
    return config;
}, function (error) {
    // 对请求错误做些什么
    console.log(error)
    if (loadingInstance) loadingInstance.close()
    return Promise.reject(error)
});

// 添加响应拦截器
request.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    if (loadingInstance) loadingInstance.close()
    return response;
}, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    if (error.response && error.response.data && error.response.data.message === 'NOT_LOGIN') {
        if (loadingInstance) loadingInstance.close()
        return error.response
    }
    if (loadingInstance) loadingInstance.close()
    return Promise.reject(error)
});

export default request
