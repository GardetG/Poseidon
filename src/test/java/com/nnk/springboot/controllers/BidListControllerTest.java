package com.nnk.springboot.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.services.BidListService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = BidListController.class)
class BidListControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BidListService bidListService;

  @DisplayName("GET /bidList/list should return view with list of BidList as attribute")
  @Test
  void homeTest() throws Exception {
    // GIVEN
    List<BidListDto> DtoList = new ArrayList<>();
    DtoList.add(new BidListDto(1,"Account 1", "Type 1", 10d));
    DtoList.add(new BidListDto(2,"Account 2", "Type 2", 20d));
    when(bidListService.findAll()).thenReturn(DtoList);

    // WHEN
    mockMvc.perform(get("/bidList/list"))

    // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("bidList/list"))
        .andExpect(model().attributeExists("bidLists"))
        .andExpect(model().attribute("bidLists", DtoList));
  }

  @DisplayName("GET /bidList/list with no BidList in database should return view with empty list as attribute")
  @Test
  void homeWhenEmptyTest() throws Exception {
    // GIVEN
    when(bidListService.findAll()).thenReturn(Collections.emptyList());

    // WHEN
    mockMvc.perform(get("/bidList/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("bidList/list"))
        .andExpect(model().attributeExists("bidLists"))
        .andExpect(model().attribute("bidLists", Collections.emptyList()));
  }
}
