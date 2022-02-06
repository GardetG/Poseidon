package com.nnk.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CurvePointServiceTest {

  @Autowired
  private CurvePointService curvePointService;

  @MockBean
  private CurvePointRepository curvePointRepository;

  private CurvePoint curvePointTest;
  private CurvePointDto curvePointDtoTest;

  @BeforeEach
  void setUp() {
    curvePointTest = new CurvePoint(10, 10d, 30d);
    curvePointTest.setId(1);
    curvePointDtoTest = new CurvePointDto(1,10, 10d, 30d);
  }

  @DisplayName("Find all should return a list of CurvePointDto")
  @Test
  void findAllTest() {
    // GIVEN
    when(curvePointRepository.findAll()).thenReturn(Collections.singletonList(curvePointTest));

    // WHEN
    List<CurvePointDto> actualDtos = curvePointService.findAll();

    // THEN
    assertThat(actualDtos).usingRecursiveFieldByFieldElementComparator().containsExactly(curvePointDtoTest);
    verify(curvePointRepository, times(1)).findAll();
  }

  @DisplayName("Find all when no CurvePoint in database should return an empty list")
  @Test
  void findAllWhenEmptyTest() {
    // GIVEN
    when(curvePointRepository.findAll()).thenReturn(Collections.emptyList());

    // WHEN
    List<CurvePointDto> actualDtos = curvePointService.findAll();

    // THEN
    assertThat(actualDtos).isEmpty();
    verify(curvePointRepository, times(1)).findAll();
  }

  @DisplayName("Find by id should return the corresponding curvePointDto")
  @Test
  void findByIdTest() throws ResourceNotFoundException {
    // GIVEN
    when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(curvePointTest));

    // WHEN
    CurvePointDto actualDto = curvePointService.findById(1);

    // THEN
    assertThat(actualDto).usingRecursiveComparison().isEqualTo(curvePointDtoTest);
    verify(curvePointRepository, times(1)).findById(1);
  }

  @DisplayName("Find by id when the corresponding curvePoint is not found should throw an exception")
  @Test
  void findByIdWhenNotFoundTest() {
    // GIVEN
    when(curvePointRepository.findById(anyInt())).thenReturn(Optional.empty());

    // WHEN
    assertThatThrownBy(() -> curvePointService.findById(9))

    // THEN
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("This curvePoint is not found");
    verify(curvePointRepository, times(1)).findById(9);
  }

}
