package com.nnk.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.repositories.RuleNameRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class RuleNameServiceTest {

  @Autowired
  private RuleNameService ruleNameService;

  @MockBean
  private RuleNameRepository ruleNameRepository;

  @Captor
  private ArgumentCaptor<RuleName> ruleNameArgumentCaptor;

  private RuleName ruleNameTest;
  private RuleNameDto ruleNameDtoTest;

  @BeforeEach
  void setUp() {
    ruleNameTest =  new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
    ruleNameTest.setId(1);
    ruleNameDtoTest = new RuleNameDto(1, "Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
  }

  @DisplayName("Find all should return a list of RuleNameDto")
  @Test
  void findAllTest() {
    // GIVEN
    when(ruleNameRepository.findAll()).thenReturn(Collections.singletonList(ruleNameTest));

    // WHEN
    List<RuleNameDto> actualDtoList = ruleNameService.findAll();

    // THEN
    assertThat(actualDtoList).usingRecursiveFieldByFieldElementComparator()
        .containsExactly(ruleNameDtoTest);
    verify(ruleNameRepository, times(1)).findAll();
  }

  @DisplayName("Find all when no RuleName in database should return an empty list")
  @Test
  void findAllWhenEmptyTest() {
    // GIVEN
    when(ruleNameRepository.findAll()).thenReturn(Collections.emptyList());

    // WHEN
    List<RuleNameDto> actualDtoList = ruleNameService.findAll();

    // THEN
    assertThat(actualDtoList).isEmpty();
    verify(ruleNameRepository, times(1)).findAll();
  }

}
