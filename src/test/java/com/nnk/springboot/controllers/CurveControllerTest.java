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

import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.services.CurvePointService;
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

@WebMvcTest(CurveController.class)
class CurveControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CurvePointService curvePointService;

  @Captor
  ArgumentCaptor<CurvePointDto> dtoCaptor;

  @DisplayName("GET /curvePoint/list should return view with list of CurvePoint as attribute")
  @Test
  void homeTest() throws Exception {
    // GIVEN
    List<CurvePointDto> DtoList = new ArrayList<>();
    DtoList.add(new CurvePointDto(1,10, 20d, 40d));
    DtoList.add(new CurvePointDto(2,20, 30d, 60d));
    when(curvePointService.findAll()).thenReturn(DtoList);

    // WHEN
    mockMvc.perform(get("/curvePoint/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("curvePoint/list"))
        .andExpect(model().attributeExists("curvePoints"))
        .andExpect(model().attribute("curvePoints", DtoList));
  }

  @DisplayName("GET /curvePoint/list with no BidList in database should return view with empty list as attribute")
  @Test
  void homeWhenEmptyTest() throws Exception {
    // GIVEN
    when(curvePointService.findAll()).thenReturn(Collections.emptyList());

    // WHEN
    mockMvc.perform(get("/curvePoint/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("curvePoint/list"))
        .andExpect(model().attributeExists("curvePoints"))
        .andExpect(model().attribute("curvePoints", Collections.emptyList()));
  }

  @DisplayName("GET /curvePoint/add should return view")
  @Test
  void addBidFormTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/curvePoint/add"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("curvePoint/add"));
  }

  @DisplayName("POST valid DTO on /curvePoint/validate should persist CurvePoint then return view")
  @Test
  void validateTest() throws Exception {
    // GIVEN
    CurvePointDto expectedDto = new CurvePointDto(null,10,10d,30d);

    // WHEN
    mockMvc.perform(post("/curvePoint/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("curveId", String.valueOf(expectedDto.getCurveId()))
            .param("term", String.valueOf(expectedDto.getTerm()))
            .param("value", String.valueOf(expectedDto.getValue()))
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/curvePoint/list"));
    verify(curvePointService, times(1)).add(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST invalid DTO on /curvePoint/validate should return form view")
  @Test
  void validateWhenInvalidTest() throws Exception {
    // WHEN
    mockMvc.perform(post("/curvePoint/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("curveId","")
            .param("term", String.valueOf(10d))
            .param("value", String.valueOf(30d))
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("curvePoint/add"))
        .andExpect(model().attributeHasFieldErrors("curvePointDto", "curveId"));
    verify(curvePointService, times(0)).add(any(CurvePointDto.class));
  }

}
