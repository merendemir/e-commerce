package com.e.commerce.service;

import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.exceptions.GenericException;
import com.e.commerce.model.Image;
import com.e.commerce.repository.ImageRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ImagesServiceTest {

    private ImagesService imagesService;

    private ImageRepository imageRepository;

    @Before
    public void setUp() {
        imageRepository = mock(ImageRepository.class);

        imagesService = new ImagesService(imageRepository);
    }

    @Test
    public void whenCreateAndSaveImagesCalledWithNonExistsUrl_itShouldReturnImage() {
        //given
        Image image = Image.builder()
                .url("image.jpg.com")
                .build();

        //when
        when(imageRepository.existsByUrl(image.getUrl())).thenReturn(false);
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        //then
        Image actual = imagesService.createAndSaveImages(image.getUrl());

        assertEquals(image, actual);

        verify(imageRepository, times(1)).existsByUrl(image.getUrl());
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test(expected = GenericException.class)
    public void whenCreateAndSaveImagesCalledWithExistsUrl_itShouldThrowGenericException() {
        //given
        String imageUrl = "image.jpg.com";

        //when
        when(imageRepository.existsByUrl(imageUrl)).thenReturn(true);

        //then
        Image actual = imagesService.createAndSaveImages(imageUrl);

        assertNull(actual);

        verify(imageRepository, times(1)).existsByUrl(imageUrl);
        verifyNoInteractions(imageRepository.save(any(Image.class)));
    }

    @Test
    public void whenSaveImageCalled_itShouldReturnImage() {
        //given
        Image image = Image.builder()
                .url("image.jpg.com")
                .build();

        //when
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        //then
        Image actual = imagesService.saveImages(image);

        assertEquals(image, actual);

        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    public void whenIsUrlExistsCalledWithExistsUrl_itShouldReturnTrue() {
        //given
        String imageUrl = "image.jpg.com";

        //when
        when(imageRepository.existsByUrl(imageUrl)).thenReturn(true);

        //then
        Boolean actual = imagesService.isUrlExists(imageUrl);

        assertEquals(true, actual);

        verify(imageRepository, times(1)).existsByUrl(imageUrl);
    }

    @Test
    public void whenIsUrlExistsCalledWithNonExistsUrl_itShouldReturnFalse() {
        //given
        String imageUrl = "image.jpg.com";

        //when
        when(imageRepository.existsByUrl(imageUrl)).thenReturn(false);

        //then
        Boolean actual = imagesService.isUrlExists(imageUrl);

        assertEquals(false, actual);

        verify(imageRepository, times(1)).existsByUrl(imageUrl);
    }

    @Test
    public void whenFindImageByIdOrElseThrowCalledWithExistImage_thenItShouldReturnImage() {
        //given
        Image image = Image.builder()
                .id(1L)
                .url("image.jpg.com")
                .build();

        //when
        when(imageRepository.findById(image.getId())).thenReturn(Optional.of(image));

        //then
        Image actual = imagesService.findImageByIdOrElseThrow(image.getId());

        assertEquals(image, actual);

        verify(imageRepository, times(1)).findById(image.getId());
    }

    @Test(expected = DataNotFoundException.class)
    public void whenFindImageByIdOrElseThrowCalledWithNotExistImage_thenItShouldThrowDataNotFoundException() {
        //given
        Long imageId = -1L;

        //when
        when(imageRepository.findById(imageId)).thenThrow(new DataNotFoundException("Image not found by id :" + imageId));

        //then
        Image actual = imagesService.findImageByIdOrElseThrow(imageId);

        assertNull(actual);

        verify(imageRepository, times(1)).findById(imageId);
    }

    @Test
    public void whenUpdateImageCalled_thenItShouldReturnUpdatedImage() {
        //given
        String newUrl = "image2.jpg.com";

        Image image = Image.builder()
                .id(1L)
                .url("image.jpg.com")
                .build();

        Image updatedImage = Image.builder()
                .id(image.getId())
                .url(newUrl)
                .build();
        //when
        when(imageRepository.findById(image.getId())).thenReturn(Optional.of(image));
        when(imageRepository.save(updatedImage)).thenReturn(updatedImage);

        //then
        Image actual = imagesService.updateImage(image.getId(), newUrl);

        assertEquals(updatedImage, actual);

        verify(imageRepository, times(1)).findById(image.getId());
        verify(imageRepository, times(1)).save(updatedImage);
    }






}