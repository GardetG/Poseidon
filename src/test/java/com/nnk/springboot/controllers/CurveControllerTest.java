package com.nnk.springboot.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.services.CurvePointService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CurveController.class)
class CurveControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CurvePointService curvePointService;

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

}
