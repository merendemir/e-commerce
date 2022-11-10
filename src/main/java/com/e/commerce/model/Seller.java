package com.e.commerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UpdateTimestamp
    private Date updatedAt;

    @CreationTimestamp
    private Date createdAt = new Date();

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

}
