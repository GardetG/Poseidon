package com.nnk.springboot.controllers;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.services.TradeService;
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
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TradeController.class)
class TradeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TradeService tradeService;

  @Captor
  private ArgumentCaptor<TradeDto> dtoCaptor;

  @DisplayName("GET /trade/list should return view with list of Trade as attribute")
  @Test
  void homeTest() throws Exception {
    // GIVEN
    List<TradeDto> DtoList = new ArrayList<>();
    DtoList.add(new TradeDto(1, "Trade Account 1", "Type 1", 10d));
    DtoList.add(new TradeDto(2, "Trade Account 2", "Type 2", 20d));
    when(tradeService.findAll()).thenReturn(DtoList);

    // WHEN
    mockMvc.perform(get("/trade/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("trade/list"))
        .andExpect(model().attributeExists("trades"))
        .andExpect(model().attribute("trades", DtoList));
  }

  @DisplayName("GET /trade/list with no Trade in database should return view with empty list as attribute")
  @Test
  void homeWhenEmptyTest() throws Exception {
    // GIVEN
    when(tradeService.findAll()).thenReturn(Collections.emptyList());

    // WHEN
    mockMvc.perform(get("/trade/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("trade/list"))
        .andExpect(model().attributeExists("trades"))
        .andExpect(model().attribute("trades", Collections.emptyList()));
  }

}
