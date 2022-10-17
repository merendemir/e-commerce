package com.e.commerce.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UpdateTimestamp
    private Date updatedAt;

    @CreationTimestamp
    private Date createdAt = new Date();

    @Column(unique = true)
    private String url;

    public Image(String url) {
        this.url = url;
    }
}
