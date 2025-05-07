import request from '@/utils/request.js';

// 上传书籍
export const uploadBook = async (bookData) => {
    const formData = new FormData();
    formData.append('file', bookData.file);
    formData.append('title', bookData.title);
    formData.append('author', bookData.author);
    formData.append('description', bookData.description);
    formData.append('category', bookData.category);

    try {
        const response = await request.post('/book/create', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        return response.data;
    } catch (error) {
        throw error;
    }
};

// 获取所有书籍
export const getAllBooks = async () => {
    const response = await request.get(`/books`);
    return response.data;
};

// 获取单本书籍
export const getBookById = async (id) => {
    const response = await request.get(`/book/get/${id}`);
    return response.data;
};

// 更新书籍
export const updateBook = async (id, bookData) => {
    const formData = new FormData();
    if (bookData.file) {
        formData.append('file', bookData.file);
    }
    formData.append('title', bookData.title);
    formData.append('author', bookData.author);
    formData.append('description', bookData.description);
    formData.append('category', bookData.category);

    const response = await request.put(`/book/update/${id}`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
    return response.data;
};

// 删除书籍
export const deleteBook = async (id) => {
    await request.delete(`/book/delete/${id}`);
};

// 模糊查询书籍
export const searchBooks = async (keyword) => {
    const response = await request.get(`/books/search`, {
        params: { keyword }
    });
    return response.data;
};

// 根据类别查询书籍
export const getBooksByCategory = async (category) => {
    const response = await request.get(`/books/category/${category}`);
    return response.data;
};
