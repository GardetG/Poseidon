package com.nnk.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
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
class TradeServiceTest {

  @Autowired
  private TradeService tradeService;

  @MockBean
  private TradeRepository tradeRepository;

  @Captor
  private ArgumentCaptor<Trade> tradeArgumentCaptor;

  private Trade tradeTest;
  private TradeDto tradeDtoTest;

  @BeforeEach
  void setUp() {
    tradeTest = new Trade("Trade Account", "Type", 10d);
    tradeTest.setTradeId(1);
    tradeDtoTest = new TradeDto(1, "Trade Account", "Type", 10d);
  }

  @DisplayName("Find all should return a list of TradeDto")
  @Test
  void findAllTest() {
    // GIVEN
    when(tradeRepository.findAll()).thenReturn(Collections.singletonList(tradeTest));

    // WHEN
    List<TradeDto> actualDtoList = tradeService.findAll();

    // THEN
    assertThat(actualDtoList).usingRecursiveFieldByFieldElementComparator()
        .containsExactly(tradeDtoTest);
    verify(tradeRepository, times(1)).findAll();
  }

  @DisplayName("Find all when no Trade in database should return an empty list")
  @Test
  void findAllWhenEmptyTest() {
    // GIVEN
    when(tradeRepository.findAll()).thenReturn(Collections.emptyList());

    // WHEN
    List<TradeDto> actualDtoList = tradeService.findAll();

    // THEN
    assertThat(actualDtoList).isEmpty();
    verify(tradeRepository, times(1)).findAll();
  }
  
  @DisplayName("Find by id should return the corresponding TradeDto")
  @Test
  void findByIdTest() throws ResourceNotFoundException {
    // GIVEN
    when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(tradeTest));

    // WHEN
    TradeDto actualDto = tradeService.findById(1);

    // THEN
    assertThat(actualDto).usingRecursiveComparison().isEqualTo(tradeDtoTest);
    verify(tradeRepository, times(1)).findById(1);
  }

  @DisplayName("Find by id when the corresponding Trade is not found should throw an exception")
  @Test
  void findByIdWhenNotFoundTest() {
    // GIVEN
    when(tradeRepository.findById(anyInt())).thenReturn(Optional.empty());

    // WHEN
    assertThatThrownBy(() -> tradeService.findById(9))

        // THEN
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("This trade is not found");
    verify(tradeRepository, times(1)).findById(9);
  }
  
}
