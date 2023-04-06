package com.example.train.hristiyan.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Train
{
  private Integer       id;
  @NotNull
  private String        townFrom;
  @NotNull
  private String        townTo;
  @NotNull
  private LocalDateTime goingTime;
  @NotNull
  private LocalDateTime arriveTime;
  @NotNull
  private Integer       capacity;
  @NotNull
  private BigDecimal    ticketPrice;

  public Train(Integer id, String townFrom, String townTo, LocalDateTime goingTime, LocalDateTime arriveTime, Integer capacity, BigDecimal ticketPrice)
  {
    this.id = id;
    this.townFrom = townFrom;
    this.townTo = townTo;
    this.goingTime = goingTime;
    this.arriveTime = arriveTime;
    this.capacity = capacity;
    this.ticketPrice = ticketPrice;
  }

  public Train()
  {
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

  public Integer getCapacity()
  {
    return capacity;
  }

  public void setCapacity(Integer capacity)
  {
    this.capacity = capacity;
  }

  public BigDecimal getTicketPrice()
  {
    return ticketPrice;
  }

  public void setTicketPrice(BigDecimal ticketPrice)
  {
    this.ticketPrice = ticketPrice;
  }
}
