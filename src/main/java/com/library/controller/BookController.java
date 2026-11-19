package com.library.controller;

import com.library.model.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    // Collection used as in-memory storage (no database needed)
    private final List<Book> books = new ArrayList<>();

    public BookController() {
        books.add(new Book(1, "Java: The Complete Reference", "Herbert Schildt", "Programming", 5));
        books.add(new Book(2, "Clean Code", "Robert C. Martin", "Software Engineering", 3));
    }

    // b) Add a new book
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        for (Book b : books) {
            if (b.getId().equals(book.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Book with id " + book.getId() + " already exists");
            }
        }
        books.add(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    // c) Get all books
    @GetMapping
    public List<Book> getAllBooks() {
        return books;
    }

    // c) Get one book by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Integer id) {
        for (Book b : books) {
            if (b.getId().equals(id)) {
                return ResponseEntity.ok(b);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found with id " + id);
    }

    // d) Update a book
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Integer id, @RequestBody Book updated) {
        for (Book b : books) {
            if (b.getId().equals(id)) {
                b.setTitle(updated.getTitle());
                b.setAuthor(updated.getAuthor());
                b.setCategory(updated.getCategory());
                b.setAvailableCopies(updated.getAvailableCopies());
                return ResponseEntity.ok(b);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found with id " + id);
    }

    // e) Delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Integer id) {
        boolean removed = books.removeIf(b -> b.getId().equals(id));
        if (removed) {
            return ResponseEntity.ok("Book deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found with id " + id);
    }
}