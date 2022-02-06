package com.nnk.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CurvePointServiceTest {

  @Autowired
  private CurvePointService curvePointService;

  @MockBean
  private CurvePointRepository curvePointRepository;

  @Captor
  private ArgumentCaptor<CurvePoint> curvePointArgumentCaptor;

  private CurvePoint curvePointTest;
  private CurvePointDto curvePointDtoTest;

  @BeforeEach
  void setUp() {
    curvePointTest = new CurvePoint(10, 10d, 30d);
    curvePointTest.setId(1);
    curvePointDtoTest = new CurvePointDto(1, 10, 10d, 30d);
  }

  @DisplayName("Find all should return a list of CurvePointDto")
  @Test
  void findAllTest() {
    // GIVEN
    when(curvePointRepository.findAll()).thenReturn(Collections.singletonList(curvePointTest));

    // WHEN
    List<CurvePointDto> actualDtoList = curvePointService.findAll();

    // THEN
    assertThat(actualDtoList).usingRecursiveFieldByFieldElementComparator()
        .containsExactly(curvePointDtoTest);
    verify(curvePointRepository, times(1)).findAll();
  }

  @DisplayName("Find all when no CurvePoint in database should return an empty list")
  @Test
  void findAllWhenEmptyTest() {
    // GIVEN
    when(curvePointRepository.findAll()).thenReturn(Collections.emptyList());

    // WHEN
    List<CurvePointDto> actualDtoList = curvePointService.findAll();

    // THEN
    assertThat(actualDtoList).isEmpty();
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

  @DisplayName("Add a new CurvePoint should persist it into database")
  @Test
  void addTest() {
    // GIVEN
    CurvePointDto newCurvePoint = new CurvePointDto(0, 10, 10d, 30d);
    CurvePoint expectedCurvePoint = new CurvePoint(10, 10d, 30d);

    // WHEN
    curvePointService.add(newCurvePoint);

    // THEN
    verify(curvePointRepository, times(1)).save(curvePointArgumentCaptor.capture());
    assertThat(curvePointArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedCurvePoint);
  }

  @DisplayName("Update a CurvePoint should persist it into database")
  @Test
  void updateTest() throws ResourceNotFoundException {
    // GIVEN
    CurvePointDto updateCurvePoint = new CurvePointDto(1, 10, 20d, 60d);
    CurvePoint expectedCurvePoint = new CurvePoint(10, 20d, 60d);
    expectedCurvePoint.setId(1);
    when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(curvePointTest));

    // WHEN
    curvePointService.update(updateCurvePoint);

    // THEN
    verify(curvePointRepository, times(1)).findById(1);
    verify(curvePointRepository, times(1)).save(curvePointArgumentCaptor.capture());
    assertThat(curvePointArgumentCaptor.getValue()).usingRecursiveComparison()
        .isEqualTo(expectedCurvePoint);
  }

  @DisplayName("Update a CurvePoint when it's not found should throw an exception")
  @Test
  void updateWhenNotFoundTest() {
    // GIVEN
    CurvePointDto updateCurvePoint = new CurvePointDto(9, 10, 20d, 60d);
    when(curvePointRepository.findById(anyInt())).thenReturn(Optional.empty());

    // WHEN
    assertThatThrownBy(() -> curvePointService.update(updateCurvePoint))

        // THEN
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("This curvePoint is not found");
    verify(curvePointRepository, times(1)).findById(9);
    verify(curvePointRepository, times(0)).save(any(CurvePoint.class));
  }

  @DisplayName("Delete a CurvePoint should delete it from database")
  @Test
  void deleteTest() throws ResourceNotFoundException {
    // GIVEN
    when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(curvePointTest));

    // WHEN
    curvePointService.delete(1);

    // THEN
    verify(curvePointRepository, times(1)).findById(1);
    verify(curvePointRepository, times(1)).delete(curvePointArgumentCaptor.capture());
    assertThat(curvePointArgumentCaptor.getValue()).isEqualTo(curvePointTest);
  }

  @DisplayName("Delete a CurvePoint when it's not found should throw an exception")
  @Test
  void deleteWhenNotFoundTest() {
    // GIVEN
    when(curvePointRepository.findById(anyInt())).thenReturn(Optional.empty());

    // WHEN
    assertThatThrownBy(() -> curvePointService.delete(9))

        // THEN
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("This curvePoint is not found");
    verify(curvePointRepository, times(1)).findById(9);
    verify(curvePointRepository, times(0)).delete(any(CurvePoint.class));
  }

}
