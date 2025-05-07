import { createStore } from 'vuex'

const store = createStore({
    state: {
        role: '',       // 登录用户的角色
        token: '',      // 用户token
        name: '',       // 用户名
        id: ''          // 用户id
    },
    mutations: {
        setRole(state, role) {
            state.role = role
        },
        setToken(state, token) {
            state.token = token
        },
        setName(state, name) {
            state.name = name
        },
        setId(state, id) {
            state.id = id
        }
    },
    actions: {
        updateRole({ commit }, role) {
            commit('setRole', role)
        },
        updateToken({ commit }, token) {
            commit('setToken', token)
        },
        updateName({ commit }, name) {
            commit('setName', name)
        },
        updateId({ commit }, id) {
            commit('setId', id)
        }
    },
    getters: {
        getRole: (state) => state.role,
        getToken: (state) => state.token,
        getName: (state) => state.name,
        getId: (state) => state.id
    }
})

export default store