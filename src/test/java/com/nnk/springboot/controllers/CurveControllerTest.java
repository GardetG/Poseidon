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

import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.CurvePointService;
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
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(CurveController.class)
class CurveControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CurvePointService curvePointService;
  @MockBean
  private UserDetailsService userDetailsService;
  @MockBean
  private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

  @Captor
  ArgumentCaptor<CurvePointDto> dtoCaptor;


  @DisplayName("GET /curvePoint/list when not authenticate should redirect to login")
  @Test
  @WithAnonymousUser
  void homeWhenNotAuthenticateTest() throws Exception {
    // WHEN
    ResultActions response = mockMvc.perform(get("/curvePoint/list"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("http://localhost/login"));
  }
  
  @DisplayName("GET /curvePoint/list should return view with list of CurvePoint as attribute")
  @Test
  @WithMockUser(username="user") 
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

  @DisplayName("GET /curvePoint/add should return view")
  @Test
  @WithMockUser(username="user") 
  void addCurveFormTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/curvePoint/add"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("curvePoint/add"));
  }

  @DisplayName("POST valid DTO on /curvePoint/validate should persist CurvePoint then return view")
  @Test
  @WithMockUser(username="user")
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
  @WithMockUser(username="user") 
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

  @DisplayName("GET /curvePoint/update should return view")
  @Test
  @WithMockUser(username="user") 
  void showUpdateFormTest() throws Exception {
    // GIVEN
    CurvePointDto curvePointDto = new CurvePointDto(1,10,10d,30d);
    when(curvePointService.findById(anyInt())).thenReturn(curvePointDto);

    // WHEN
    mockMvc.perform(get("/curvePoint/update/1"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("curvePoint/update"))
        .andExpect(model().attributeExists("curvePointDto"))
        .andExpect(model().attribute("curvePointDto", curvePointDto));
    verify(curvePointService, times(1)).findById(1);
  }

  @DisplayName("GET /curvePoint/update when curvePoint not found should return view with error message")
  @Test
  @WithMockUser(username="user") 
  void showUpdateFormWhenNotFoundTest() throws Exception {
    // GIVEN
    doThrow(new ResourceNotFoundException("This curvePoint is not found")).when(curvePointService).findById(anyInt());

    // WHEN
    mockMvc.perform(get("/curvePoint/update/9"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/curvePoint/list"))
        .andExpect(flash().attributeExists("error"));
    verify(curvePointService, times(1)).findById(9);
  }

  @DisplayName("POST valid DTO on /curvePoint/update should persist curvePoint then return view")
  @Test
  @WithMockUser(username="user") 
  void updateCurveTest() throws Exception {
    // GIVEN
    CurvePointDto expectedDto = new CurvePointDto(1,20,30d,60d);

    // WHEN
    mockMvc.perform(post("/curvePoint/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("curveId", String.valueOf(expectedDto.getCurveId()))
            .param("term", String.valueOf(expectedDto.getTerm()))
            .param("value", String.valueOf(expectedDto.getValue()))
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/curvePoint/list"));
    verify(curvePointService, times(1)).update(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST invalid DTO on /curvePoint/update should return from view")
  @Test
  @WithMockUser(username="user") 
  void updateCurveWhenInvalidTest() throws Exception {
    // WHEN
    mockMvc.perform(post("/curvePoint/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("curveId","")
            .param("term", String.valueOf(30d))
            .param("value", String.valueOf(60d))
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("curvePoint/update"))
        .andExpect(model().attributeHasFieldErrors("curvePointDto", "curveId"));
    verify(curvePointService, times(0)).update(any(CurvePointDto.class));
  }

  @DisplayName("POST DTO on /curvePoint/update when curvePoint not found should return view with error message")
  @Test
  @WithMockUser(username="user") 
  void updateCurveWhenNotFoundTest() throws Exception {
    // GIVEN
    CurvePointDto expectedDto = new CurvePointDto(9,20,30d,60d);
    doThrow(new ResourceNotFoundException("This curvePoint is not found")).when(curvePointService).update(any(CurvePointDto.class));

    // WHEN
    mockMvc.perform(post("/curvePoint/update/9")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("curveId", String.valueOf(expectedDto.getCurveId()))
            .param("term", String.valueOf(expectedDto.getTerm()))
            .param("value", String.valueOf(expectedDto.getValue()))
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/curvePoint/list"))
        .andExpect(flash().attributeExists("error"));
    verify(curvePointService, times(1)).update(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("GET /curvePoint/delete should delete CurvePoint then return view")
  @Test
  @WithMockUser(username="user") 
  void deleteCurveTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/curvePoint/delete/1"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/curvePoint/list"));
    verify(curvePointService, times(1)).delete(1);
  }

  @DisplayName("GET /CurvePoint/delete when CurvePoint not found should return view with error message")
  @Test
  @WithMockUser(username="user") 
  void deleteCurveWhenNotFoundTest() throws Exception {
    // GIVEN
    doThrow(new ResourceNotFoundException("This CurvePoint is not found")).when(curvePointService).delete(anyInt());

    // WHEN
    mockMvc.perform(get("/curvePoint/delete/9"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/curvePoint/list"))
        .andExpect(flash().attributeExists("error"));
    verify(curvePointService, times(1)).delete(9);
  }

}
