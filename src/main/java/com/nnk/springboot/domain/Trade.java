package com.nnk.springboot.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Trade entity with account, type, and buy quantity.
 */
@Entity
@Table(name = "trade")
public class Trade {

  public Trade() { }

  /**
   * Create an instance of RuleName with account, type, and buy quantity.
   *
   * @param account     of Trade
   * @param type        of Trade
   * @param buyQuantity of Trade
   */
  public Trade(String account, String type, Double buyQuantity) {
    this.account = account;
    this.type = type;
    this.buyQuantity = buyQuantity;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tradeid")
  private Integer tradeId;
  @Column(name = "account")
  private String account;
  @Column(name = "type")
  private String type;
  @Column(name = "buyquantity")
  private Double buyQuantity;
  @Column(name = "sellquantity")
  private Double sellQuantity;
  @Column(name = "buyprice")
  private Double buyPrice;
  @Column(name = "sellprice")
  private Double sellPrice;
  @Column(name = "tradedate")
  private LocalDateTime tradeDate;
  @Column(name = "security")
  private String security;
  @Column(name = "status")
  private String status;
  @Column(name = "trader")
  private String trader;
  @Column(name = "benchmark")
  private String benchmark;
  @Column(name = "book")
  private String book;
  @Column(name = "creationname")
  private String creationName;
  @Column(name = "creationdate")
  private LocalDateTime creationDate;
  @Column(name = "revisionname")
  private String revisionName;
  @Column(name = "revisiondate")
  private LocalDateTime revisionDate;
  @Column(name = "dealname")
  private String dealName;
  @Column(name = "dealtype")
  private String dealType;
  @Column(name = "sourcelistid")
  private String sourceListId;
  @Column(name = "side")
  private String side;

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
