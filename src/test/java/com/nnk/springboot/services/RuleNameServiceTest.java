package com.nnk.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
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
  
  @DisplayName("Find by id should return the corresponding RuleNameDto")
  @Test
  void findByIdTest() throws ResourceNotFoundException {
    // GIVEN
    when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(ruleNameTest));

    // WHEN
    RuleNameDto actualDto = ruleNameService.findById(1);

    // THEN
    assertThat(actualDto).usingRecursiveComparison().isEqualTo(ruleNameDtoTest);
    verify(ruleNameRepository, times(1)).findById(1);
  }

  @DisplayName("Find by id when the corresponding RuleName is not found should throw an exception")
  @Test
  void findByIdWhenNotFoundTest() {
    // GIVEN
    when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.empty());

    // WHEN
    assertThatThrownBy(() -> ruleNameService.findById(9))

        // THEN
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("This ruleName is not found");
    verify(ruleNameRepository, times(1)).findById(9);
  }

  @DisplayName("Add a new RuleName should persist it into database")
  @Test
  void addTest() {
    // GIVEN
    RuleNameDto newRuleName = new RuleNameDto(0, "Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
    RuleName expectedRuleName = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");

    // WHEN
    ruleNameService.add(newRuleName);

    // THEN
    verify(ruleNameRepository, times(1)).save(ruleNameArgumentCaptor.capture());
    assertThat(ruleNameArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedRuleName);
  }

  @DisplayName("Update a RuleName should persist it into database")
  @Test
  void updateTest() throws ResourceNotFoundException {
    // GIVEN
    RuleNameDto updateRuleName = new RuleNameDto(1, "Update Rule Name", "Update Description", "Update Json", "Update Template", "Update SQL", "Update SQL Part");
    RuleName expectedRuleName = new RuleName("Update Rule Name", "Update Description", "Update Json", "Update Template", "Update SQL", "Update SQL Part");
    expectedRuleName.setId(1);
    when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(ruleNameTest));

    // WHEN
    ruleNameService.update(updateRuleName);

    // THEN
    verify(ruleNameRepository, times(1)).findById(1);
    verify(ruleNameRepository, times(1)).save(ruleNameArgumentCaptor.capture());
    assertThat(ruleNameArgumentCaptor.getValue()).usingRecursiveComparison()
        .isEqualTo(expectedRuleName);
  }

  @DisplayName("Update a RuleName when it's not found should throw an exception")
  @Test
  void updateWhenNotFoundTest() {
    // GIVEN
    RuleNameDto updateRuleName = new RuleNameDto(9, "Update Rule Name", "Update Description", "Update Json", "Update Template", "Update SQL", "Update SQL Part");
    when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.empty());

    // WHEN
    assertThatThrownBy(() -> ruleNameService.update(updateRuleName))

        // THEN
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("This ruleName is not found");
    verify(ruleNameRepository, times(1)).findById(9);
    verify(ruleNameRepository, times(0)).save(any(RuleName.class));
  }

  @DisplayName("Delete a RuleName should delete it from database")
  @Test
  void deleteTest() throws ResourceNotFoundException {
    // GIVEN
    when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(ruleNameTest));

    // WHEN
    ruleNameService.delete(1);

    // THEN
    verify(ruleNameRepository, times(1)).findById(1);
    verify(ruleNameRepository, times(1)).delete(ruleNameArgumentCaptor.capture());
    assertThat(ruleNameArgumentCaptor.getValue()).isEqualTo(ruleNameTest);
  }

  @DisplayName("Delete a RuleName when it's not found should throw an exception")
  @Test
  void deleteWhenNotFoundTest() {
    // GIVEN
    when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.empty());

    // WHEN
    assertThatThrownBy(() -> ruleNameService.delete(9))

        // THEN
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("This ruleName is not found");
    verify(ruleNameRepository, times(1)).findById(9);
    verify(ruleNameRepository, times(0)).delete(any(RuleName.class));
  }

}
