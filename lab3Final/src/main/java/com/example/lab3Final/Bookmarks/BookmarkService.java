package com.example.lab3Final.Bookmarks;

import com.example.lab3Final.Readers.Reader;
import com.example.lab3Final.Readers.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private ReaderRepository readerRepository;

    public List<Bookmark> getAllBookmarks() {
        return bookmarkRepository.findAll();
    }

    public Bookmark getBookmarkById(Long id) {
        return bookmarkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bookmark not found with id: " + id));
    }

    public Bookmark createBookmark(Bookmark bookmark, Long readerId) {
        Reader reader = readerRepository.findById(readerId).orElseThrow(() -> new RuntimeException("Reader not found"));
        bookmark.setReader(reader);
        return bookmarkRepository.save(bookmark);
    }

    public Bookmark updateBookmark(Long id, Bookmark updatedBookmark) {
        Bookmark bookmark = bookmarkRepository.findById(id).orElseThrow(() -> new RuntimeException("Bookmark not found"));
        bookmark.setPage(updatedBookmark.getPage());
        bookmark.setSurah(updatedBookmark.getSurah());
        bookmark.setAyah(updatedBookmark.getAyah());
        return bookmarkRepository.save(bookmark);
    }

    public void deleteBookmark(Long id) {
        bookmarkRepository.deleteById(id);
    }
}
