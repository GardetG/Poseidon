package com.nnk.springboot.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nnk.springboot.dto.RuleNameDto;
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
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RuleNameController.class)
class RuleNameControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RuleNameService ruleNameService;

  @Captor
  private ArgumentCaptor<RuleNameDto> dtoCaptor;

  @DisplayName("GET /ruleName/list should return view with list of RuleName as attribute")
  @Test
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

  @DisplayName("GET /ruleName/list with no RuleName in database should return view with empty list as attribute")
  @Test
  void homeWhenEmptyTest() throws Exception {
    // GIVEN
    when(ruleNameService.findAll()).thenReturn(Collections.emptyList());

    // WHEN
    mockMvc.perform(get("/ruleName/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("ruleName/list"))
        .andExpect(model().attributeExists("ruleNames"))
        .andExpect(model().attribute("ruleNames", Collections.emptyList()));
  }
}
