package com.e.commerce.repository;

import com.e.commerce.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Boolean existsByUrl(String url);

    Optional<Image> findByUrl(String url);
}
