package com.example.lab3Final.Readers;

import com.example.lab3Final.Bookmarks.Bookmark;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Bookmark> bookmarks;
}
