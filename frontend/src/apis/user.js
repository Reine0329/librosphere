import request from '@/utils/request.js'

//获取所有用户
export const getUsers = () => {
    return request.get('/getUsers')
}

//获取用户
export const getUser = (id) => {
    return request.get(`/getUser/${id}`)
}

//添加用户
export const addUser = (form) => {
    return request.post('/addUser', form)
}

//更新用户
export const updateUser = (id, form) => {
    return request.put(`/updateUser/${id}`, form)
}

//删除用户
export const deleteUser = (id) => {
    return request.delete(`/deleteUser/${id}`)
}

// 修改密码
export const updatePassword = (id, form) => {
    return request.put(`/updatePassword/${id}`, form)
}

// 通过名字获取用户
export const getUserByName = (name) => {
    return request.get(`/name/${name}`);
};