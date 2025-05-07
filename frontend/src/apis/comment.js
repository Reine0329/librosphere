import request from '@/utils/request.js';

// 获取所有评论
export const getAllComments = () => {
    return request.get('/comments');
};

// 获取单个评论
export const getCommentById = (id) => {
    return request.get(`/comment/get/${id}`);
};

// 获取特定书籍的所有评论
export const getCommentsByBook = (bookId) => {
    return request.get(`/comment/book/${bookId}`);
};

// 获取特定用户的所有评论
export const getCommentsByUser = (userId) => {
    return request.get(`/comment/user/${userId}`);
};

// 添加评论
export const createComment = (comment) => {
    return request.post('/comment', comment);
};

// 删除评论
export const deleteComment = (id) => {
    return request.delete(`/comment/delete/${id}`);
};
