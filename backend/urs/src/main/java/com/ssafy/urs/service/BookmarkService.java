package com.ssafy.urs.service;

import com.ssafy.urs.dto.BookmarkDto;
import java.util.List;

public interface BookmarkService {
    BookmarkDto getBookmarkById(String userId, int routeId);

    List<BookmarkDto> getAllBookmarksByUser(String userId);

    BookmarkDto createBookmark(BookmarkDto bookmarkDto);

    void deleteBookmark(String userId, int routeId);
}