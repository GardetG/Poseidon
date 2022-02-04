package com.nnk.springboot.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bidlist")
public class BidList {

    private BidList() {
        // Default constructor for hibernate
    }

    public BidList(String account, String type, double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bidlistid")
    private Integer bidListId;
    @Column(name = "account")
    private String account;
    @Column(name = "type")
    private String type;
    @Column(name = "bidquantity")
    private double bidQuantity;
    @Column(name = "askquantity")
    private double askQuantity;
    @Column(name = "bid")
    private double bid;
    @Column(name = "ask")
    private double ask;
    @Column(name = "benchmark")
    private String benchmark;
    @Column(name = "bidlistdate")
    private LocalDateTime bidListDate;
    @Column(name = "commentary")
    private String commentary;
    @Column(name = "security")
    private String security;
    @Column(name = "status")
    private String status;
    @Column(name = "trader")
    private String trader;
    @Column(name = "book")
    private String book;
    @Column(name = "creationname")
    private LocalDateTime creationName;
    @Column(name = "creationdate")
    private LocalDateTime creationDate;
    @Column(name = "revisionname")
    private LocalDateTime revisionName;
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

    public Integer getBidListId() {
        return bidListId;
    }

    public void setBidListId(Integer bidListId) {
        this.bidListId = bidListId;
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

    public double getBidQuantity() {
        return bidQuantity;
    }

    public void setBidQuantity(double bidQuantity) {
        this.bidQuantity = bidQuantity;
    }
}
