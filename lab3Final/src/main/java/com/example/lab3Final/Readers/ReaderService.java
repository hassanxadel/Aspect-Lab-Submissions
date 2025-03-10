package com.example.lab3Final.Readers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaderService {

    @Autowired
    private ReaderRepository readerRepository;

    public List<Reader> getAllReaders() {
        return readerRepository.findAll();
    }

    public Reader getReaderById(Long id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reader not found with id: " + id));
    }

    public Reader createReader(Reader reader) {
        return readerRepository.save(reader);
    }

    public Reader updateReader(Long id, Reader updatedReader) {
        Reader reader = readerRepository.findById(id).orElseThrow(() -> new RuntimeException("Reader not found"));
        reader.setName(updatedReader.getName());
        reader.setEmail(updatedReader.getEmail());
        return readerRepository.save(reader);
    }

    public void deleteReader(Long id) {
        readerRepository.deleteById(id);
    }
}
