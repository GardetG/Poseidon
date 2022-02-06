package com.nnk.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.services.RatingService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RatingController.class)
class RatingControllerTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private RatingService ratingService;

  @Captor
  private ArgumentCaptor<RatingDto> dtoCaptor;

  @DisplayName("GET /rating/list should return view with list of Rating as attribute")
  @Test
  void homeTest() throws Exception {
    // GIVEN
    List<RatingDto> DtoList = new ArrayList<>();
    DtoList.add(new RatingDto(1, "Moody's Rating 1", "S&P Rating 1", "Fitch Rating 1", 10));
    DtoList.add(new RatingDto(2, "Moody's Rating 2", "S&P Rating 2", "Fitch Rating 2", 20));
    when(ratingService.findAll()).thenReturn(DtoList);

    // WHEN
    mockMvc.perform(get("/rating/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("rating/list"))
        .andExpect(model().attributeExists("ratings"))
        .andExpect(model().attribute("ratings", DtoList));
  }

  @DisplayName("GET /rating/list with no Rating in database should return view with empty list as attribute")
  @Test
  void homeWhenEmptyTest() throws Exception {
    // GIVEN
    when(ratingService.findAll()).thenReturn(Collections.emptyList());

    // WHEN
    mockMvc.perform(get("/rating/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("rating/list"))
        .andExpect(model().attributeExists("ratings"))
        .andExpect(model().attribute("ratings", Collections.emptyList()));
  }

  @DisplayName("GET /rating/add should return view")
  @Test
  void addRatingFormTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/rating/add"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("rating/add"));
  }

  @DisplayName("POST valid DTO on /rating/validate should persist Rating then return view")
  @Test
  void validateTest() throws Exception {
    // GIVEN
    RatingDto expectedDto = new RatingDto(null, "Moody's Rating 1", "S&P Rating 1", "Fitch Rating 1", 10);

    // WHEN
    mockMvc.perform(post("/rating/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("moodysRating", expectedDto.getMoodysRating())
            .param("sandpRating", expectedDto.getSandpRating())
            .param("fitchRating", expectedDto.getFitchRating())
            .param("orderNumber", String.valueOf(expectedDto.getOrderNumber()))
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/rating/list"));
    verify(ratingService, times(1)).add(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST invalid DTO on /rating/validate should return form view")
  @Test
  void validateWhenInvalidTest() throws Exception {
    // WHEN
    mockMvc.perform(post("/rating/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("moodysRating", "This Moody's Rating exceed the 125 characters limit to test the validation of the Dto" +
                "when the add Rating form is submit by the user")
            .param("sandpRating", "")
            .param("fitchRating", "Fitch Rating")
            .param("orderNumber", String.valueOf(-5))
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("rating/add"))
        .andExpect(model().attributeHasFieldErrors("ratingDto", "moodysRating"))
        .andExpect(model().attributeHasFieldErrors("ratingDto", "sandpRating"))
        .andExpect(model().attributeHasFieldErrors("ratingDto", "orderNumber"));
    verify(ratingService, times(0)).add(any(RatingDto.class));
  }
  
}
