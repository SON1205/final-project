package com.ssafy.urs.controller;

import com.ssafy.urs.dto.BookmarkDto;
import com.ssafy.urs.service.BookmarkService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping("/{userId}/{routeId}")
    public ResponseEntity<BookmarkDto> getBookmarkById(@PathVariable String userId, @PathVariable int routeId) {
        try {
            return ResponseEntity.ok(bookmarkService.getBookmarkById(userId, routeId));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookmarkDto>> getAllBookmarksByUser(@PathVariable String userId) {
        return ResponseEntity.ok(bookmarkService.getAllBookmarksByUser(userId));
    }

    @Transactional
    @PostMapping
    public ResponseEntity<BookmarkDto> createBookmark(@RequestBody BookmarkDto bookmarkDto) {
        if (bookmarkDto.getCreatedAt() == null) {
            bookmarkDto.setCreatedAt(LocalDateTime.now());
        }

        System.out.println("bookmarkDto = " + bookmarkDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookmarkService.createBookmark(bookmarkDto));
    }

    @Transactional
    @DeleteMapping("/{userId}/{routeId}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable String userId, @PathVariable int routeId) {
        try {
            bookmarkService.deleteBookmark(userId, routeId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}