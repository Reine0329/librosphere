const USER_TOKEN_KEY = 'token'
const USER_INFO_KEY = 'user_info'

// 存储用户登录token
export const saveUserLoginToken = (token) => {
    localStorage.setItem(USER_TOKEN_KEY, JSON.stringify(token))
}

// 读取用户登录token
export const getUserLoginToken = () => {
    const token = localStorage.getItem(USER_TOKEN_KEY)
    return token ? JSON.parse(token) : ''
}

// 清除用户登录token
export const removeUserLoginToken = () => {
    localStorage.removeItem(USER_TOKEN_KEY)
}

// 存储用户登录信息
export const saveUserInfo = (info) => {
    localStorage.setItem(USER_INFO_KEY, JSON.stringify(info))
}

// 读取用户登录信息
export const getUserInfo = () => {
    const userInfo = localStorage.getItem(USER_INFO_KEY)
    return userInfo ? JSON.parse(userInfo) : ''
}

// 清除用户登录信息
export const removeUserInfo = () => {
    localStorage.removeItem(USER_INFO_KEY)
}
