package com.e.commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private String title;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    private String district;

    @NotBlank
    private String openAddress;
}
