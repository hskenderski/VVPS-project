package com.example.train.hristiyan.dto;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TrainSearchCriteria
{
  @Length(max = 100)
  private String        townFrom;
  @Length(max = 100)
  private String        townTo;
  private LocalDateTime goingTimeFrom;
  private LocalDateTime goingTimeTo;
  private LocalDateTime arriveTimeFrom;
  private LocalDateTime arriveTimeTo;
  private BigDecimal    ticketPriceFrom;
  private BigDecimal    ticketPriceTo;

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

  public LocalDateTime getGoingTimeFrom()
  {
    return goingTimeFrom;
  }

  public void setGoingTimeFrom(LocalDateTime goingTimeFrom)
  {
    this.goingTimeFrom = goingTimeFrom;
  }

  public LocalDateTime getArriveTimeFrom()
  {
    return arriveTimeFrom;
  }

  public void setArriveTimeFrom(LocalDateTime arriveTimeFrom)
  {
    this.arriveTimeFrom = arriveTimeFrom;
  }

  public BigDecimal getTicketPriceFrom()
  {
    return ticketPriceFrom;
  }

  public void setTicketPriceFrom(BigDecimal ticketPriceFrom)
  {
    this.ticketPriceFrom = ticketPriceFrom;
  }

  public BigDecimal getTicketPriceTo()
  {
    return ticketPriceTo;
  }

  public void setTicketPriceTo(BigDecimal ticketPriceTo)
  {
    this.ticketPriceTo = ticketPriceTo;
  }

  public LocalDateTime getGoingTimeTo()
  {
    return goingTimeTo;
  }

  public void setGoingTimeTo(LocalDateTime goingTimeTo)
  {
    this.goingTimeTo = goingTimeTo;
  }

  public LocalDateTime getArriveTimeTo()
  {
    return arriveTimeTo;
  }

  public void setArriveTimeTo(LocalDateTime arriveTimeTo)
  {
    this.arriveTimeTo = arriveTimeTo;
  }
}
