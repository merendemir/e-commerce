package com.e.commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private String title;

    @NotNull
    private String country;

    @NotNull
    private String city;

    @NotNull
    private String district;

    @NotNull
    private String openAddress;

}
