package com.nnk.springboot.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * Rating DTO between view and model for CRUD operations.
 */
public class RatingDto {

  public RatingDto() { }

  /**
   * Create an instance of the DTO with required fields.
   *
   * @param id           of the DTO
   * @param moodysRating of the DTO
   * @param sandpRating  of the DTO
   * @param fitchRating  of the DTO
   * @param orderNumber  of the DTO
   */
  public RatingDto(Integer id, String moodysRating, String sandpRating, String fitchRating,
                   Integer orderNumber) {
    this.id = id;
    this.moodysRating = moodysRating;
    this.sandpRating = sandpRating;
    this.fitchRating = fitchRating;
    this.orderNumber = orderNumber;
  }

  private Integer id;
  @NotBlank(message = "Moody's Rating is mandatory")
  @Length(max = 125, message = "Moody's Rating cannot exceed 125 characters")
  private String moodysRating;
  @NotBlank(message = "S&P Rating is mandatory")
  @Length(max = 125, message = "S&P Rating cannot exceed 125 characters")
  private String sandpRating;
  @NotBlank(message = "Fitch Rating is mandatory")
  @Length(max = 125, message = "Fitch Rating cannot exceed 125 characters")
  private String fitchRating;
  @NotNull(message = "Order Number is mandatory")
  @Min(value = 0, message = "Order Number must be positive")
  private Integer orderNumber;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getMoodysRating() {
    return moodysRating;
  }

  public void setMoodysRating(String moodysRating) {
    this.moodysRating = moodysRating;
  }

  public String getSandpRating() {
    return sandpRating;
  }

  public void setSandpRating(String sandpRating) {
    this.sandpRating = sandpRating;
  }

  public String getFitchRating() {
    return fitchRating;
  }

  public void setFitchRating(String fitchRating) {
    this.fitchRating = fitchRating;
  }

  public Integer getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(Integer orderNumber) {
    this.orderNumber = orderNumber;
  }

}
