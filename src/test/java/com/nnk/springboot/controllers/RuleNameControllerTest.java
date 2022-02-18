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

import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.RuleNameService;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(RuleNameController.class)
class RuleNameControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RuleNameService ruleNameService;
  @MockBean
  private UserDetailsService userDetailsService;
  @MockBean
  private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

  @Captor
  private ArgumentCaptor<RuleNameDto> dtoCaptor;

  @DisplayName("GET /ruleName/list when not authenticate should redirect to login")
  @Test
  @WithAnonymousUser
  void homeWhenNotAuthenticateTest() throws Exception {
    // WHEN
    ResultActions response = mockMvc.perform(get("/ruleName/list"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("http://localhost/login"));
  }
  
  @DisplayName("GET /ruleName/list should return view with list of RuleName as attribute")
  @Test
  @WithMockUser(username="user")
  void homeTest() throws Exception {
    // GIVEN
    List<RuleNameDto> DtoList = new ArrayList<>();
    DtoList.add(new RuleNameDto(1,"Rule Name 1", "Description 1", "Json 1", "Template 1", "SQL 1", "SQL Part 1"));
    DtoList.add(new RuleNameDto(2, "Rule Name 2", "Description 2", "Json 2", "Template 2", "SQL 2", "SQL Part 2"));
    when(ruleNameService.findAll()).thenReturn(DtoList);

    // WHEN
    mockMvc.perform(get("/ruleName/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("ruleName/list"))
        .andExpect(model().attributeExists("ruleNames"))
        .andExpect(model().attribute("ruleNames", DtoList));
  }

  @DisplayName("GET /ruleName/add should return view")
  @Test
  @WithMockUser(username="user") 
  void addRuleNameFormTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/ruleName/add"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("ruleName/add"));
  }

  @DisplayName("POST valid DTO on /ruleName/validate should persist RuleName then return view")
  @Test
  @WithMockUser(username="user") 
  void validateTest() throws Exception {
    // GIVEN
    RuleNameDto
        expectedDto = new RuleNameDto(null, "Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");

    // WHEN
    mockMvc.perform(post("/ruleName/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", expectedDto.getName())
            .param("description", expectedDto.getDescription())
            .param("json", expectedDto.getJson())
            .param("template", expectedDto.getTemplate())
            .param("sqlStr", expectedDto.getSqlStr())
            .param("sqlPart", expectedDto.getSqlPart())
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/ruleName/list"));
    verify(ruleNameService, times(1)).add(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST invalid DTO on /ruleName/validate should return form view")
  @Test
  @WithMockUser(username="user") 
  void validateWhenInvalidTest() throws Exception {
    // WHEN
    mockMvc.perform(post("/ruleName/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "This ruleName's name exceed the 125 characters limit to test the validation of the Dto " +
                "when the add RuleName form is submit by the user")
            .param("description", "")
            .param("json", "")
            .param("template", "")
            .param("sqlStr", "")
            .param("sqlPart", "")
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("ruleName/add"))
        .andExpect(model().attributeHasFieldErrors("ruleNameDto", "name"))
        .andExpect(model().attributeHasFieldErrors("ruleNameDto", "description"))
        .andExpect(model().attributeHasFieldErrors("ruleNameDto", "json"))
        .andExpect(model().attributeHasFieldErrors("ruleNameDto", "template"))
        .andExpect(model().attributeHasFieldErrors("ruleNameDto", "sqlStr"))
        .andExpect(model().attributeHasFieldErrors("ruleNameDto", "sqlPart"));
    verify(ruleNameService, times(0)).add(any(RuleNameDto.class));
  }

  @DisplayName("GET /ruleName/update should return view")
  @Test
  @WithMockUser(username="user") 
  void showUpdateFormTest() throws Exception {
    // GIVEN
    RuleNameDto ruleNameDto = new RuleNameDto(1, "Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
    when(ruleNameService.findById(anyInt())).thenReturn(ruleNameDto);

    // WHEN
    mockMvc.perform(get("/ruleName/update/1"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("ruleName/update"))
        .andExpect(model().attributeExists("ruleNameDto"))
        .andExpect(model().attribute("ruleNameDto", ruleNameDto));
    verify(ruleNameService, times(1)).findById(1);
  }

  @DisplayName("GET /ruleName/update when ruleName not found should return view with error message")
  @Test
  @WithMockUser(username="user") 
  void showUpdateFormWhenNotFoundTest() throws Exception {
    // GIVEN
    doThrow(new ResourceNotFoundException("This ruleName is not found")).when(ruleNameService).findById(anyInt());

    // WHEN
    mockMvc.perform(get("/ruleName/update/9"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/ruleName/list"))
        .andExpect(flash().attributeExists("error"));
    verify(ruleNameService, times(1)).findById(9);
  }

  @DisplayName("POST valid DTO on /ruleName/update should persist ruleName then return view")
  @Test
  @WithMockUser(username="user") 
  void updateRuleNameTest() throws Exception {
    // GIVEN
    RuleNameDto expectedDto = new RuleNameDto(1, "Update Rule Name", "Update Description", "Update Json", "Update Template", "Update SQL", "Update SQL Part");

    // WHEN
    mockMvc.perform(post("/ruleName/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", expectedDto.getName())
            .param("description", expectedDto.getDescription())
            .param("json", expectedDto.getJson())
            .param("template", expectedDto.getTemplate())
            .param("sqlStr", expectedDto.getSqlStr())
            .param("sqlPart", expectedDto.getSqlPart())
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/ruleName/list"));
    verify(ruleNameService, times(1)).update(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST invalid DTO on /ruleName/update should return from view")
  @Test
  @WithMockUser(username="user") 
  void updateRuleNameWhenInvalidTest() throws Exception {
    // WHEN
    mockMvc.perform(post("/ruleName/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "This ruleName's name exceed the 125 characters limit to test the validation of the Dto " +
                "when the add RuleName form is submit by the user")
            .param("description", "")
            .param("json", "")
            .param("template", "")
            .param("sqlStr", "")
            .param("sqlPart", "")
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("ruleName/update"))
        .andExpect(model().attributeHasFieldErrors("ruleNameDto", "name"))
        .andExpect(model().attributeHasFieldErrors("ruleNameDto", "description"))
        .andExpect(model().attributeHasFieldErrors("ruleNameDto", "json"))
        .andExpect(model().attributeHasFieldErrors("ruleNameDto", "template"))
        .andExpect(model().attributeHasFieldErrors("ruleNameDto", "sqlStr"))
        .andExpect(model().attributeHasFieldErrors("ruleNameDto", "sqlPart"));
    verify(ruleNameService, times(0)).update(any(RuleNameDto.class));
  }

  @DisplayName("POST DTO on /ruleName/update when ruleName not found should return view with error message")
  @Test
  @WithMockUser(username="user") 
  void updateRuleNameWhenNotFoundTest() throws Exception {
    // GIVEN
    RuleNameDto expectedDto = new RuleNameDto(9, "Update Rule Name", "Update Description", "Update Json", "Update Template", "Update SQL", "Update SQL Part");
    doThrow(new ResourceNotFoundException("This ruleName is not found")).when(ruleNameService).update(any(RuleNameDto.class));

    // WHEN
    mockMvc.perform(post("/ruleName/update/9")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", expectedDto.getName())
            .param("description", expectedDto.getDescription())
            .param("json", expectedDto.getJson())
            .param("template", expectedDto.getTemplate())
            .param("sqlStr", expectedDto.getSqlStr())
            .param("sqlPart", expectedDto.getSqlPart())
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/ruleName/list"))
        .andExpect(flash().attributeExists("error"));
    verify(ruleNameService, times(1)).update(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("GET /ruleName/delete should delete RuleName then return view")
  @Test
  @WithMockUser(username="user") 
  void deleteRuleNameTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/ruleName/delete/1"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/ruleName/list"));
    verify(ruleNameService, times(1)).delete(1);
  }

  @DisplayName("GET /RuleName/delete when RuleName not found should return view with error message")
  @Test
  @WithMockUser(username="user") 
  void deleteRuleNameWhenNotFoundTest() throws Exception {
    // GIVEN
    doThrow(new ResourceNotFoundException("This RuleName is not found")).when(ruleNameService).delete(anyInt());

    // WHEN
    mockMvc.perform(get("/ruleName/delete/9"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/ruleName/list"))
        .andExpect(flash().attributeExists("error"));
    verify(ruleNameService, times(1)).delete(9);
  }

}
