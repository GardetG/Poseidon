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
   * Map a BidList entity into DTO.
   *
   * @param bidList to map
   * @return corresponding BidListDto mapped
   */
  public static BidListDto toDto(BidList bidList) {
    return new BidListDto(
        bidList.getBidListId(),
        bidList.getAccount(),
        bidList.getType(),
        bidList.getBidQuantity()
    );
  }

  /**
   * Map a BidListDto into entity.
   *
   * @param bidList to map into
   * @param bidListDto to map from
   */
  public static void toEntity(BidList bidList, BidListDto bidListDto) {
    bidList.setAccount(bidListDto.getAccount());
    bidList.setType(bidListDto.getType());
    bidList.setBidQuantity(bidListDto.getBidQuantity());
  }

}
