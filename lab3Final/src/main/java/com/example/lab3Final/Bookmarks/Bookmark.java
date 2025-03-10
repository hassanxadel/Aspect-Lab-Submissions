package com.example.lab3Final.Bookmarks;

import com.example.lab3Final.Readers.Reader;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String page;
    private String surah;
    private String ayah;
    @ManyToOne
    @JoinColumn(name = "reader_id")
    @JsonBackReference
    private Reader reader;
}
