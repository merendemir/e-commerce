package com.e.commerce.model;

import com.e.commerce.dto.AddressDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UpdateTimestamp
    private Date updatedAt;

    @CreationTimestamp
    private Date createdAt = new Date();

    private String title;

    private String country;

    private String city;

    private String district;

    private String openAddress;

    public Address(AddressDto addressDto) {
        this.title = addressDto.getTitle();
        this.country = addressDto.getCountry();
        this.city = addressDto.getCity();
        this.district = addressDto.getDistrict();
        this.openAddress = addressDto.getOpenAddress();
    }
}
