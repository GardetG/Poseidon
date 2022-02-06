package com.nnk.springboot.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * CurvePoint entity with id, curveId, term and value.
 */
@Entity
@Table(name = "curvepoint")
public class CurvePoint {

  public CurvePoint() { }

  /**
   * Create an instance of CurvePoint with curveId, term and value.
   *
   * @param curveId of the CurvePoint
   * @param term    of the CurvePoint
   * @param value   of the CurvePoint
   */
  public CurvePoint(Integer curveId, Double term, Double value) {
    this.curveId = curveId;
    this.term = term;
    this.value = value;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "CurveId")
  private Integer curveId;
  @Column(name = "asofdate")
  private LocalDateTime asOfDate;
  @Column(name = "term")
  private Double term;
  @Column(name = "value")
  private Double value;
  @Column(name = "creationdate")
  private LocalDateTime creationDate;

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
