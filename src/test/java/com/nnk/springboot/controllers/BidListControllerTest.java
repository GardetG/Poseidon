package com.nnk.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.BidListService;
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

@WebMvcTest(value = BidListController.class)
class BidListControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BidListService bidListService;

  @Captor
  private ArgumentCaptor<BidListDto> dtoCaptor;

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

  @DisplayName("GET /bidList/add should return view")
  @Test
  void addBidFormTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/bidList/add"))

    // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("bidList/add"));
  }

  @DisplayName("POST valid DTO on /bidList/validate should persist BidList then return view")
  @Test
  void validateTest() throws Exception {
    // GIVEN
    BidListDto expectedDto = new BidListDto(null,"Account Test","Type Test", 10d);

    // WHEN
    mockMvc.perform(post("/bidList/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("account",expectedDto.getAccount())
            .param("type", expectedDto.getType())
            .param("bidQuantity", String.valueOf(expectedDto.getBidQuantity()))
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/bidList/list"));
    verify(bidListService, times(1)).add(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST invalid DTO on /bidList/validate should return form view")
  @Test
  void validateWhenInvalidTest() throws Exception {
    // WHEN
    mockMvc.perform(post("/bidList/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("account","Account Test exceed 30 characters")
            .param("type", "")
            .param("bidQuantity", String.valueOf(-10d))
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("bidList/add"))
        .andExpect(model().attributeHasFieldErrors("bidListDto", "account"))
        .andExpect(model().attributeHasFieldErrors("bidListDto", "type"))
        .andExpect(model().attributeHasFieldErrors("bidListDto", "bidQuantity"));
    verify(bidListService, times(0)).add(any(BidListDto.class));
  }

  @DisplayName("GET /bidList/update should return view")
  @Test
  void showUpdateFormTest() throws Exception {
    // GIVEN
    BidListDto bidListDto = new BidListDto(1,"Account Test","Type Test",10d);
    when(bidListService.findById(anyInt())).thenReturn(bidListDto);

    // WHEN
    mockMvc.perform(get("/bidList/update/1"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("bidList/update"))
        .andExpect(model().attributeExists("bidListDto"))
        .andExpect(model().attribute("bidListDto", bidListDto));
    verify(bidListService, times(1)).findById(1);
  }

  @DisplayName("GET /bidList/update when BidList not found should return view with error message")
  @Test
  void showUpdateFormWhenNotFoundTest() throws Exception {
    // GIVEN
    doThrow(new ResourceNotFoundException("This bidList is not found")).when(bidListService).findById(anyInt());

    // WHEN
    mockMvc.perform(get("/bidList/update/9"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/bidList/list"))
        .andExpect(flash().attributeExists("error"));
    verify(bidListService, times(1)).findById(9);
  }

  @DisplayName("POST valid DTO on /bidList/update should persist BidList then return view")
  @Test
  void updateBidTest() throws Exception {
    // GIVEN
    BidListDto expectedDto = new BidListDto(1,"Update Account Test","Update Type Test", 10d);

    // WHEN
    mockMvc.perform(post("/bidList/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("account",expectedDto.getAccount())
            .param("type", expectedDto.getType())
            .param("bidQuantity", String.valueOf(expectedDto.getBidQuantity()))
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/bidList/list"));
    verify(bidListService, times(1)).update(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST invalid DTO on /bidList/update should return from view")
  @Test
  void updateBidWhenInvalidTest() throws Exception {
    // WHEN
    mockMvc.perform(post("/bidList/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("account","Account Test exceed 30 characters")
            .param("type", "")
            .param("bidQuantity", String.valueOf(-10d))
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("bidList/update"))
        .andExpect(model().attributeHasFieldErrors("bidListDto", "account"))
        .andExpect(model().attributeHasFieldErrors("bidListDto", "type"))
        .andExpect(model().attributeHasFieldErrors("bidListDto", "bidQuantity"));
    verify(bidListService, times(0)).update(any(BidListDto.class));
  }

  @DisplayName("POST DTO on /bidList/update when BidList not found should return view with error message")
  @Test
  void updateBidWhenNotFoundTest() throws Exception {
    // GIVEN
    BidListDto expectedDto = new BidListDto(9,"Update Account Test","Update Type Test", 10d);
    doThrow(new ResourceNotFoundException("This bidList is not found")).when(bidListService).update(any(BidListDto.class));

    // WHEN
    mockMvc.perform(post("/bidList/update/9")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("account",expectedDto.getAccount())
            .param("type", expectedDto.getType())
            .param("bidQuantity", String.valueOf(expectedDto.getBidQuantity()))
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/bidList/list"))
        .andExpect(flash().attributeExists("error"));
    verify(bidListService, times(1)).update(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("GET /bidList/delete should delete bidList then return view")
  @Test
  void deleteBidTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/bidList/delete/1"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/bidList/list"));
    verify(bidListService, times(1)).delete(1);
  }

  @DisplayName("GET /bidList/delete when BidList not found should return view with error message")
  @Test
  void deleteBidWhenNotFoundTest() throws Exception {
    // GIVEN
    doThrow(new ResourceNotFoundException("This bidList is not found")).when(bidListService).delete(anyInt());

    // WHEN
    mockMvc.perform(get("/bidList/delete/9"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/bidList/list"))
        .andExpect(flash().attributeExists("error"));
    verify(bidListService, times(1)).delete(9);
  }
}
