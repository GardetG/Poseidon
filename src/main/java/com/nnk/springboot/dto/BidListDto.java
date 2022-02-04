package com.nnk.springboot.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

/**
 * BidList DTO between view and model for CRUD operations.
 */
public class BidListDto {

  /**
   * Create an instance of the DTO.
   *
   * @param bidListId of the DTO
   * @param account of the DTO
   * @param type of the DTO
   * @param bidQuantity of the DTO
   */
  public BidListDto(Integer bidListId, String account, String type, double bidQuantity) {
    this.bidListId = bidListId;
    this.account = account;
    this.type = type;
    this.bidQuantity = bidQuantity;
  }

  private Integer bidListId;
  @NotBlank(message = "Account is mandatory")
  private String account;
  @NotBlank(message = "Type is mandatory")
  private String type;
  @Digits(integer = 20, fraction = 2)
  @DecimalMin(value = "0", message = "Quantity must be positive")
  private double bidQuantity;

  public Integer getBidListId() {
    return bidListId;
  }

  public String getAccount() {
    return account;
  }

  public String getType() {
    return type;
  }

  public double getBidQuantity() {
    return bidQuantity;
  }

}
