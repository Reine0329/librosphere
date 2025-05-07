package com.sakura.backend.controller;

import com.sakura.backend.model.Book;
import com.sakura.backend.service.BookService;
import com.sakura.backend.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private FileService fileService;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/book/get/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/book/create")
    public ResponseEntity<Book> createBook(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("description") String description,
            @RequestParam("category") String category) {

        if (file == null
                || file.isEmpty()
                || !StringUtils.hasText(title)
                || !StringUtils.hasText(author)
                || !StringUtils.hasText(description)
                || !StringUtils.hasText(category)) {
            return ResponseEntity.badRequest().body(null);
        }

        String fileName = fileService.uploadFile(file);
        if (fileName == null) {
            return ResponseEntity.badRequest().build();
        }

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setFileName(fileName);
        book.setCategory(category);

        return ResponseEntity.ok(bookService.createBook(book));
    }

    @PutMapping("/book/update/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("description") String description,
            @RequestParam("category") String category) {

        if (!StringUtils.hasText(title)
                || !StringUtils.hasText(author)
                || !StringUtils.hasText(description)
                || !StringUtils.hasText(category)) {
            return ResponseEntity.badRequest().body(null);
        }

        String fileName = null;
        if (file != null && !file.isEmpty()) {
            fileName = fileService.uploadFile(file);
            if (fileName == null) {
                return ResponseEntity.badRequest().build();
            }
        }

        Book bookDetails = new Book();
        bookDetails.setTitle(title);
        bookDetails.setAuthor(author);
        bookDetails.setDescription(description);
        bookDetails.setFileName(fileName);
        bookDetails.setCategory(category);

        try {
            Book updatedBook = bookService.updateBook(id, bookDetails);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/book/delete/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/books/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        return bookService.searchBooks(keyword);
    }

    @GetMapping("/books/category/{category}")
    public List<Book> getBooksByCategory(@PathVariable String category) {
        return bookService.getBooksByCategory(category);
    }
}