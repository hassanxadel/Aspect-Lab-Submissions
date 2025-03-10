package com.example.lab3Final.Bookmarks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @GetMapping
    public List<Bookmark> getAll() {
        return bookmarkService.getAllBookmarks();
    }

    @GetMapping("/{id}")
    public Bookmark getById(@PathVariable Long id) {
        return bookmarkService.getBookmarkById(id);
    }

    @PostMapping("/{readerId}")
    public Bookmark create(@RequestBody Bookmark bookmark, @PathVariable Long readerId) {
        return bookmarkService.createBookmark(bookmark, readerId);
    }

    @PutMapping("/{id}")
    public Bookmark update(@PathVariable Long id, @RequestBody Bookmark bookmark) {
        return bookmarkService.updateBookmark(id, bookmark);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookmarkService.deleteBookmark(id);
    }
}
