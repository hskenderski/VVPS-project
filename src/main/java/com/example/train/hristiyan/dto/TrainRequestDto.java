package com.example.train.hristiyan.dto;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TrainRequestDto
{

  private Integer       id;
  @Length(max = 100)
  private String        townFrom;
  @Length(max = 100)
  private String        townTo;
  private LocalDateTime goingTime;
  private LocalDateTime arriveTime;
  private BigDecimal    ticketPrice;

  private boolean       isTwoWay;
  private boolean       isChild;
  //For the 2 train (to return)
  private Integer       idTwo;
  private LocalDateTime goingTimeTwo;
  private LocalDateTime arriveTimeTwo;
  private BigDecimal    ticketPriceTwo;


  public boolean isTwoWay()
  {
    return isTwoWay;
  }

  public void setTwoWay(boolean twoWay)
  {
    isTwoWay = twoWay;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public String getTownFrom()
  {
    return townFrom;
  }

  public void setTownFrom(String townFrom)
  {
    this.townFrom = townFrom;
  }

  public String getTownTo()
  {
    return townTo;
  }

  public void setTownTo(String townTo)
  {
    this.townTo = townTo;
  }

  public LocalDateTime getGoingTime()
  {
    return goingTime;
  }

  public void setGoingTime(LocalDateTime goingTime)
  {
    this.goingTime = goingTime;
  }

  public LocalDateTime getArriveTime()
  {
    return arriveTime;
  }

  public void setArriveTime(LocalDateTime arriveTime)
  {
    this.arriveTime = arriveTime;
  }

  public BigDecimal getTicketPrice()
  {
    return ticketPrice;
  }

  public void setTicketPrice(BigDecimal ticketPrice)
  {
    this.ticketPrice = ticketPrice;
  }

  public Integer getIdTwo()
  {
    return idTwo;
  }

  public void setIdTwo(Integer idTwo)
  {
    this.idTwo = idTwo;
  }

  public LocalDateTime getGoingTimeTwo()
  {
    return goingTimeTwo;
  }

  public void setGoingTimeTwo(LocalDateTime goingTimeTwo)
  {
    this.goingTimeTwo = goingTimeTwo;
  }

  public LocalDateTime getArriveTimeTwo()
  {
    return arriveTimeTwo;
  }

  public void setArriveTimeTwo(LocalDateTime arriveTimeTwo)
  {
    this.arriveTimeTwo = arriveTimeTwo;
  }

  public BigDecimal getTicketPriceTwo()
  {
    return ticketPriceTwo;
  }

  public void setTicketPriceTwo(BigDecimal ticketPriceTwo)
  {
    this.ticketPriceTwo = ticketPriceTwo;
  }

  public boolean isChild()
  {
    return isChild;
  }

  public void setChild(boolean child)
  {
    isChild = child;
  }

}
