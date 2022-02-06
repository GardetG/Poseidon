package com.nnk.springboot.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Rating entity with moodysRating, sandPRating, fitchRating and orderNumber.
 */
@Entity
@Table(name = "rating")
public class Rating {

  public Rating() {
  }

  /**
   * Create an instance of Rating with moodysRating, sandPRating, fitchRating and orderNumber.
   *
   * @param moodysRating of Rating
   * @param sandpRating  of Rating
   * @param fitchRating  of Rating
   * @param orderNumber  of Rating
   */
  public Rating(String moodysRating, String sandpRating, String fitchRating,
                Integer orderNumber) {
    this.moodysRating = moodysRating;
    this.sandpRating = sandpRating;
    this.fitchRating = fitchRating;
    this.orderNumber = orderNumber;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "moodysrating")
  private String moodysRating;
  @Column(name = "sandprating")
  private String sandpRating;
  @Column(name = "fitchrating")
  private String fitchRating;
  @Column(name = "ordernumber")
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
