package com.nnk.springboot.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * Trade DTO between view and model for CRUD operations.
 */
public class TradeDto {

  public TradeDto() { }

  /**
   * Create an instance of the DTO with required fields.
   *
   * @param tradeId     of the DTO
   * @param account     of the DTO
   * @param type        of the DTO
   * @param buyQuantity of the DTO
   */
  public TradeDto(Integer tradeId, String account, String type, Double buyQuantity) {
    this.tradeId = tradeId;
    this.account = account;
    this.type = type;
    this.buyQuantity = buyQuantity;
  }

  private Integer tradeId;
  @NotBlank(message = "Account is mandatory")
  @Length(max = 30, message = "Account cannot exceed 30 characters")
  private String account;
  @NotBlank(message = "Type is mandatory")
  @Length(max = 30, message = "Type cannot exceed 30 characters")
  private String type;
  @NotNull(message = "Quantity is mandatory")
  @Digits(integer = 20, fraction = 2)
  @DecimalMin(value = "0", message = "Quantity must be positive")
  private Double buyQuantity;

  public Integer getTradeId() {
    return tradeId;
  }

  public void setTradeId(Integer tradeId) {
    this.tradeId = tradeId;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Double getBuyQuantity() {
    return buyQuantity;
  }

  public void setBuyQuantity(Double buyQuantity) {
    this.buyQuantity = buyQuantity;
  }

}
