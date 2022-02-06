package com.nnk.springboot.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RatingController.class)
class RatingControllerTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private RatingService ratingService;

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

}
