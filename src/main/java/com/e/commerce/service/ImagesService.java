package com.e.commerce.service;

import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.exceptions.GenericException;
import com.e.commerce.model.Image;
import com.e.commerce.repository.ImageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ImagesService {

private  final ImageRepository imageRepository;

    public ImagesService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image createAndSaveImages(String imageUrl) {
        if (this.isUrlExists(imageUrl)) {
            throw new GenericException(HttpStatus.BAD_REQUEST, "Data already exists by url:" + imageUrl);
        }

       return this.saveImages(new Image(imageUrl));
    }

    public Image saveImages(Image image) {
        return imageRepository.save(image);
    }

    public boolean isUrlExists(String url) {
        return imageRepository.existsByUrl(url);
    }

    public Image findImageByIdOrElseThrow(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(
                        () ->  new DataNotFoundException("Image not found by id :" + imageId));
    }

    public Image updateImage(Long imageId, String url) {
        Image image = this.findImageByIdOrElseThrow(imageId);
        image.setUrl(url);

        return this.saveImages(image);
    }

    public void deleteImage(Long imageId) {
        imageRepository.deleteById(imageId);
    }
}
