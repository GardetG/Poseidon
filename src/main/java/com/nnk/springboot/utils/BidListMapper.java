package com.nnk.springboot.utils;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;

/**
 * Mapper utility class to map BidList DTO and entity.
 */
public class BidListMapper {

  private BidListMapper() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Map a BidList into DTO.
   *
   * @param bidList to map
   * @return corresponding BidListDto
   */
  public static BidListDto toDto(BidList bidList) {
    return new BidListDto(
        bidList.getBidListId(),
        bidList.getAccount(),
        bidList.getType(),
        bidList.getBidQuantity()
    );
  }

}
