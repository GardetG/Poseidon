package com.nnk.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
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
class BidListServiceTest {

  @Autowired
  private BidListService bidListService;

  @MockBean
  private BidListRepository bidListRepository;

  @Captor
  ArgumentCaptor<BidList> bidListArgumentCaptor;

  private BidList bidListTest;
  private BidListDto bidListDtoTest;

  @BeforeEach
  void setUp() {
    bidListTest = new BidList("Account Test", "Type Test", 10d);
    bidListTest.setBidListId(1);
    bidListDtoTest = new BidListDto(1,"Account Test", "Type Test", 10d);
  }

  @DisplayName("Find all should return a list of BidListDto")
  @Test
  void findAllTest() {
    // GIVEN
    when(bidListRepository.findAll()).thenReturn(Collections.singletonList(bidListTest));

    // WHEN
    List<BidListDto> actualDtos = bidListService.findAll();

    // THEN
    assertThat(actualDtos).usingRecursiveFieldByFieldElementComparator().containsExactly(bidListDtoTest);
    verify(bidListRepository, times(1)).findAll();
  }

  @DisplayName("Find all when no BidList in database should return an empty list")
  @Test
  void findAllWhenEmptyTest() {
    // GIVEN
    when(bidListRepository.findAll()).thenReturn(Collections.emptyList());

    // WHEN
    List<BidListDto> actualDtos = bidListService.findAll();

    // THEN
    assertThat(actualDtos).isEmpty();
    verify(bidListRepository, times(1)).findAll();
  }

  @DisplayName("Find by id should return the corresponding BidListDto")
  @Test
  void findByIdTest() throws ResourceNotFoundException {
    // GIVEN
    when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(bidListTest));

    // WHEN
    BidListDto actualDto = bidListService.findById(1);

    // THEN
    assertThat(actualDto).usingRecursiveComparison().isEqualTo(bidListDtoTest);
    verify(bidListRepository, times(1)).findById(1);
  }

  @DisplayName("Find by id when the corresponding BidList is not found should throw an exception")
  @Test
  void findByIdWhenNotFoundTest() {
    // GIVEN
    when(bidListRepository.findById(anyInt())).thenReturn(Optional.empty());

    // WHEN
    assertThatThrownBy(() -> bidListService.findById(9))

    // THEN
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("This bidList is not found");
    verify(bidListRepository, times(1)).findById(9);
  }

  @DisplayName("Add a new BidList should persist it in database")
  @Test
  void addTest() {
    // GIVEN
    BidListDto newBidList = new BidListDto(0,"new Account Test", "new Type Test", 10d);
    BidList expectedBidList = new BidList("new Account Test", "new Type Test", 10d);

    // WHEN
    bidListService.add(newBidList);

    // THEN
    verify(bidListRepository, times(1)).save(bidListArgumentCaptor.capture());
    assertThat(bidListArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedBidList);
  }

}