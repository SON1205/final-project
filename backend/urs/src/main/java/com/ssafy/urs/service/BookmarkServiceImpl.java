package com.ssafy.urs.service;

import com.ssafy.urs.dto.BookmarkDto;
import com.ssafy.urs.entity.Bookmark;
import com.ssafy.urs.entity.BookmarkId;
import com.ssafy.urs.repository.BookmarkRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    @Override
    public BookmarkDto getBookmarkById(String userId, int routeId) {
        Bookmark bookmark = bookmarkRepository.findById(new BookmarkId(userId, routeId))
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));
        return new BookmarkDto(bookmark.getId().getUserId(), bookmark.getId().getRouteId(), bookmark.getCreatedAt());
    }

    @Override
    public List<BookmarkDto> getAllBookmarksByUser(String userId) {
        return bookmarkRepository.findAllByUserId(userId).stream()
                .map(bookmark -> new BookmarkDto(bookmark.getId().getUserId(), bookmark.getId().getRouteId(),
                        bookmark.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public BookmarkDto createBookmark(BookmarkDto bookmarkDto) {
        Bookmark bookmark = new Bookmark(new BookmarkId(bookmarkDto.getUserId(), bookmarkDto.getRouteId()),
                bookmarkDto.getCreatedAt());
        bookmarkRepository.save(bookmark);
        return bookmarkDto;
    }

    @Override
    public void deleteBookmark(String userId, int routeId) {
        bookmarkRepository.deleteById(new BookmarkId(userId, routeId));
    }
}