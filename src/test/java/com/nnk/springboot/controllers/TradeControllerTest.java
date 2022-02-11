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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.TradeService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TradeController.class)
class TradeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TradeService tradeService;
  @MockBean
  private UserDetailsService userDetailsService;

  @Captor
  private ArgumentCaptor<TradeDto> dtoCaptor;

  @DisplayName("GET /trade/list when not authenticate should redirect to login")
  @Test
  @WithAnonymousUser
  void homeWhenNotAuthenticateTest() throws Exception {
    // WHEN
    ResultActions response = mockMvc.perform(get("/trade/list"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("http://localhost/login"));
  }

  @DisplayName("GET /trade/list should return view with list of Trade as attribute")
  @Test
  @WithMockUser(username="user")
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

  @DisplayName("GET /trade/add should return view")
  @Test
  @WithMockUser(username="user") 
  void addTradeFormTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/trade/add"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("trade/add"));
  }

  @DisplayName("POST valid DTO on /trade/validate should persist Trade then return view")
  @Test
  @WithMockUser(username="user") 
  void validateTest() throws Exception {
    // GIVEN
    TradeDto
        expectedDto = new TradeDto(null, "Trade Account", "Type", 10d);

    // WHEN
    mockMvc.perform(post("/trade/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("account", expectedDto.getAccount())
            .param("type", expectedDto.getType())
            .param("buyQuantity", String.valueOf(expectedDto.getBuyQuantity()))
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/trade/list"));
    verify(tradeService, times(1)).add(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST invalid DTO on /trade/validate should return form view")
  @Test
  @WithMockUser(username="user") 
  void validateWhenInvalidTest() throws Exception {
    // WHEN
    mockMvc.perform(post("/trade/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("account","Account Test exceed 30 characters")
            .param("type", "")
            .param("buyQuantity", String.valueOf(-10d))
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("trade/add"))
        .andExpect(model().attributeHasFieldErrors("tradeDto", "account"))
        .andExpect(model().attributeHasFieldErrors("tradeDto", "type"))
        .andExpect(model().attributeHasFieldErrors("tradeDto", "buyQuantity"));
    verify(tradeService, times(0)).add(any(TradeDto.class));
  }

  @DisplayName("GET /trade/update should return view")
  @Test
  @WithMockUser(username="user") 
  void showUpdateFormTest() throws Exception {
    // GIVEN
    TradeDto tradeDto = new TradeDto(1, "Trade Account", "Type", 10d);
    when(tradeService.findById(anyInt())).thenReturn(tradeDto);

    // WHEN
    mockMvc.perform(get("/trade/update/1"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("trade/update"))
        .andExpect(model().attributeExists("tradeDto"))
        .andExpect(model().attribute("tradeDto", tradeDto));
    verify(tradeService, times(1)).findById(1);
  }

  @DisplayName("GET /trade/update when trade not found should return view with error message")
  @Test
  @WithMockUser(username="user") 
  void showUpdateFormWhenNotFoundTest() throws Exception {
    // GIVEN
    doThrow(new ResourceNotFoundException("This trade is not found")).when(tradeService).findById(anyInt());

    // WHEN
    mockMvc.perform(get("/trade/update/9"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/trade/list"))
        .andExpect(flash().attributeExists("error"));
    verify(tradeService, times(1)).findById(9);
  }

  @DisplayName("POST valid DTO on /trade/update should persist trade then return view")
  @Test
  @WithMockUser(username="user") 
  void updateTradeTest() throws Exception {
    // GIVEN
    TradeDto expectedDto = new TradeDto(1, "Update Trade Account", "Update Type", 20d);

    // WHEN
    mockMvc.perform(post("/trade/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("account", expectedDto.getAccount())
            .param("type", expectedDto.getType())
            .param("buyQuantity", String.valueOf(expectedDto.getBuyQuantity()))
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/trade/list"));
    verify(tradeService, times(1)).update(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST invalid DTO on /trade/update should return from view")
  @Test
  @WithMockUser(username="user") 
  void updateTradeWhenInvalidTest() throws Exception {
    // WHEN
    mockMvc.perform(post("/trade/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("account","Account Test exceed 30 characters")
            .param("type", "")
            .param("buyQuantity", String.valueOf(-10d))
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("trade/update"))
        .andExpect(model().attributeHasFieldErrors("tradeDto", "account"))
        .andExpect(model().attributeHasFieldErrors("tradeDto", "type"))
        .andExpect(model().attributeHasFieldErrors("tradeDto", "buyQuantity"));
    verify(tradeService, times(0)).update(any(TradeDto.class));
  }

  @DisplayName("POST DTO on /trade/update when trade not found should return view with error message")
  @Test
  @WithMockUser(username="user") 
  void updateTradeWhenNotFoundTest() throws Exception {
    // GIVEN
    TradeDto expectedDto = new TradeDto(9, "Update Trade Account", "Update Type", 20d);
    doThrow(new ResourceNotFoundException("This trade is not found")).when(tradeService).update(any(TradeDto.class));

    // WHEN
    mockMvc.perform(post("/trade/update/9")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("account", expectedDto.getAccount())
            .param("type", expectedDto.getType())
            .param("buyQuantity", String.valueOf(expectedDto.getBuyQuantity()))
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/trade/list"))
        .andExpect(flash().attributeExists("error"));
    verify(tradeService, times(1)).update(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("GET /trade/delete should delete Trade then return view")
  @Test
  @WithMockUser(username="user") 
  void deleteTradeTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/trade/delete/1"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/trade/list"));
    verify(tradeService, times(1)).delete(1);
  }

  @DisplayName("GET /Trade/delete when Trade not found should return view with error message")
  @Test
  @WithMockUser(username="user") 
  void deleteTradeWhenNotFoundTest() throws Exception {
    // GIVEN
    doThrow(new ResourceNotFoundException("This Trade is not found")).when(tradeService).delete(anyInt());

    // WHEN
    mockMvc.perform(get("/trade/delete/9"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/trade/list"))
        .andExpect(flash().attributeExists("error"));
    verify(tradeService, times(1)).delete(9);
  }
  
}
