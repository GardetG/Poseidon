package com.nnk.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class RatingServiceTest {

  @Autowired
  private RatingService ratingService;

  @MockBean
  private RatingRepository ratingRepository;

  @Captor
  private ArgumentCaptor<Rating> ratingArgumentCaptor;

  private Rating ratingTest;
  private RatingDto ratingDtoTest;

  @BeforeEach
  void setUp() {
    ratingTest = new Rating("Moodys Rating", "S&P Rating", "Fitch Rating", 10);
    ratingTest.setId(1);
    ratingDtoTest = new RatingDto(1, "Moodys Rating", "S&P Rating", "Fitch Rating", 10);
  }

  @DisplayName("Find all should return a list of RatingDto")
  @Test
  void findAllTest() {
    // GIVEN
    when(ratingRepository.findAll()).thenReturn(Collections.singletonList(ratingTest));

    // WHEN
    List<RatingDto> actualDtoList = ratingService.findAll();

    // THEN
    assertThat(actualDtoList).usingRecursiveFieldByFieldElementComparator()
        .containsExactly(ratingDtoTest);
    verify(ratingRepository, times(1)).findAll();
  }

  @DisplayName("Find all when no Rating in database should return an empty list")
  @Test
  void findAllWhenEmptyTest() {
    // GIVEN
    when(ratingRepository.findAll()).thenReturn(Collections.emptyList());

    // WHEN
    List<RatingDto> actualDtoList = ratingService.findAll();

    // THEN
    assertThat(actualDtoList).isEmpty();
    verify(ratingRepository, times(1)).findAll();
  }
  
  @DisplayName("Find by id should return the corresponding RatingDto")
  @Test
  void findByIdTest() throws ResourceNotFoundException {
    // GIVEN
    when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(ratingTest));

    // WHEN
    RatingDto actualDto = ratingService.findById(1);

    // THEN
    assertThat(actualDto).usingRecursiveComparison().isEqualTo(ratingDtoTest);
    verify(ratingRepository, times(1)).findById(1);
  }

  @DisplayName("Find by id when the corresponding Rating is not found should throw an exception")
  @Test
  void findByIdWhenNotFoundTest() {
    // GIVEN
    when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());

    // WHEN
    assertThatThrownBy(() -> ratingService.findById(9))

        // THEN
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("This rating is not found");
    verify(ratingRepository, times(1)).findById(9);
  }
  
  @DisplayName("Add a new Rating should persist it into database")
  @Test
  void addTest() {
    // GIVEN
    RatingDto newRating = new RatingDto(0, "Moodys Rating", "S&P Rating", "Fitch Rating", 10);
    Rating expectedRating = new Rating("Moodys Rating", "S&P Rating", "Fitch Rating", 10);

    // WHEN
    ratingService.add(newRating);

    // THEN
    verify(ratingRepository, times(1)).save(ratingArgumentCaptor.capture());
    assertThat(ratingArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedRating);
  }
  
}
