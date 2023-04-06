package com.example.train.hristiyan.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketInCart
{
  private Integer       ticketId;
  private String        townFrom;
  private String        townTo;
  private LocalDateTime goingTime;
  private LocalDateTime arriveTime;

  private String        townFrom2;
  private String        townTo2;
  private LocalDateTime goingTime2;
  private LocalDateTime arriveTime2;


  private BigDecimal price;
  private boolean    isChild;


  public TicketInCart(Integer ticketId, String townFrom, String townTo, LocalDateTime goingTime, LocalDateTime arriveTime, String townFrom2, String townTo2, LocalDateTime goingTime2, LocalDateTime arriveTime2, BigDecimal price, boolean isChild)
  {
    setTicketId(ticketId);
    setTownFrom(townFrom);
    setTownTo(townTo);
    setGoingTime(goingTime);
    setArriveTime(arriveTime);
    setTownFrom2(townFrom2);
    setTownTo2(townTo2);
    setGoingTime2(goingTime2);
    setArriveTime2(arriveTime2);
    setPrice(price);
    setChild(isChild);
  }

  public Integer getTicketId()
  {
    return ticketId;
  }

  public void setTicketId(Integer ticketId)
  {
    this.ticketId = ticketId;
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

  public String getTownFrom2()
  {
    return townFrom2;
  }

  public void setTownFrom2(String townFrom2)
  {
    this.townFrom2 = townFrom2;
  }

  public String getTownTo2()
  {
    return townTo2;
  }

  public void setTownTo2(String townTo2)
  {
    this.townTo2 = townTo2;
  }

  public LocalDateTime getGoingTime2()
  {
    return goingTime2;
  }

  public void setGoingTime2(LocalDateTime goingTime2)
  {
    this.goingTime2 = goingTime2;
  }

  public LocalDateTime getArriveTime2()
  {
    return arriveTime2;
  }

  public void setArriveTime2(LocalDateTime arriveTime2)
  {
    this.arriveTime2 = arriveTime2;
  }

  public BigDecimal getPrice()
  {
    return price;
  }

  public void setPrice(BigDecimal price)
  {
    this.price = price;
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
