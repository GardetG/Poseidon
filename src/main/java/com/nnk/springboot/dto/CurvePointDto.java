package com.nnk.springboot.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * CurvePoint DTO between view and model for CRUD operations.
 */
public class CurvePointDto {

  public CurvePointDto() { }

  /**
   * Create an instance of the DTO with required fields.
   *
   * @param id      of the DTO
   * @param curveId of the DTO
   * @param term    of the DTO
   * @param value   of the DTO
   */
  public CurvePointDto(Integer id, Integer curveId, Double term, Double value) {
    this.id = id;
    this.curveId = curveId;
    this.term = term;
    this.value = value;
  }

  private Integer id;
  @NotNull(message = "Id must not be null")
  @Min(value = 0, message = "Id must be positive")
  private Integer curveId;
  @NotNull(message = "Term is mandatory")
  @Digits(integer = 20, fraction = 2)
  private Double term;
  @NotNull(message = "Value is mandatory")
  @Digits(integer = 20, fraction = 2)
  private Double value;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getCurveId() {
    return curveId;
  }

  public void setCurveId(Integer curveId) {
    this.curveId = curveId;
  }

  public Double getTerm() {
    return term;
  }

  public void setTerm(Double term) {
    this.term = term;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

}
