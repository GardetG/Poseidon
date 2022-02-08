package com.nnk.springboot.utils;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;

/**
 * Mapper utility class to map Trade DTO and entity.
 */
public class TradeMapper {

  private TradeMapper() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Map a Trade into DTO.
   *
   * @param trade to map
   * @return corresponding TradeDto
   */
  public static TradeDto toDto(Trade trade) {
    return new TradeDto(
        trade.getTradeId(),
        trade.getAccount(),
        trade.getType(),
        trade.getBuyQuantity()
    );
  }

  /**
   * Map a TradeDto into entity.
   *
   * @param trade to map into
   * @param tradeDto to map from
   */
  public static void toEntity(Trade trade, TradeDto tradeDto) {
    trade.setAccount(tradeDto.getAccount());
    trade.setType(tradeDto.getType());
    trade.setBuyQuantity(tradeDto.getBuyQuantity());
  }

}
