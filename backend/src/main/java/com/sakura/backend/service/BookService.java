package com.sakura.backend.service;

import com.sakura.backend.model.Book;
import com.sakura.backend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private FileService fileService;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("书籍未找到"));

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setDescription(bookDetails.getDescription());
        if (bookDetails.getFileName() != null) {
            fileService.deleteFile(book.getFileName());
            book.setFileName(bookDetails.getFileName());
        }
        book.setCategory(bookDetails.getCategory());

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("书籍未找到"));
        fileService.deleteFile(book.getFileName());
        bookRepository.delete(book);
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getTitle().contains(keyword) ||
                        book.getAuthor().contains(keyword) ||
                        book.getDescription().contains(keyword))
                .collect(Collectors.toList());
    }

    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}

