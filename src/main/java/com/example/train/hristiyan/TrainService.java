package com.example.train.hristiyan;

import com.example.train.exceptions.InvalidException;
import com.example.train.hristiyan.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class TrainService
{
  private final TrainDao        trainDao;
  private final TrainValidator  trainValidator;
  private final PasswordEncoder passwordEncoder;


  @Autowired
  public TrainService(TrainDao trainDao, TrainValidator trainValidator)
  {
    this.trainDao = trainDao;
    this.trainValidator = trainValidator;
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  public void createTrain(Train train)
  {
    trainDao.createTrain(train);
  }

  public void register(User user)
  {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    trainDao.register(user);
  }

  public User loadUserByEmail(String email)
  {
    return trainDao.getUserByEmail(email)
        .orElseThrow(() -> new InvalidException("No such user!"));
  }

  public void updateUserByEmail(String email, User user)
  {
    trainDao.updateUserByEmail(email, user);
  }

  public List<Train> loadTrains(TrainSearchCriteria criteria)
  {
    return trainDao.loadTrains(criteria);
  }

  public void addTicketToCart(List<TrainRequestDto> requestDto, String username)
  {
    for (TrainRequestDto tr : requestDto) {
      BigDecimal ticketPriceWithDiscount = calculateTicketDiscounts(tr, username);

      trainDao.addTicketToCart(tr, username, ticketPriceWithDiscount);
    }
  }

  public List<TicketInCart> viewCart(String username)
  {
    //remove those items that are added before 7 days or more
    trainDao.clearCart(username);
    return trainDao.viewCart(username, true);
  }

  public void removeItemFromCart(String username, Integer ticketId)
  {
    trainDao.removeItemFromCart(username, ticketId);
  }

  public void orderCart(String username)
  {
    trainDao.orderCart(username);
  }

  public List<TicketInCart> viewOrders(String username)
  {
    return trainDao.viewCart(username, false);
  }

  public BigDecimal calculateTicketDiscounts(TrainRequestDto tr, String username)
  {
    BigDecimal ticketPriceWithDiscount1 = tr.getTicketPrice();
    BigDecimal ticketPriceWithDiscount2 = tr.getTicketPriceTwo();

    //Между 09:30 и 16:00ч и след 19:30 има отстъпка 5%
      ticketPriceWithDiscount1 = discountTimeDiapason(ticketPriceWithDiscount1, tr.getGoingTime());

    //Ако билета е двупосочен
    if (null != tr.getGoingTimeTwo()) {
      //Между 09:30 и 16:00ч и след 19:30 има отстъпка 5% за 2 посока
      ticketPriceWithDiscount2 = discountTimeDiapason(ticketPriceWithDiscount2, tr.getGoingTimeTwo());
    }

    BigDecimal ticketPriceWithDiscount = discountTwoWay(tr, ticketPriceWithDiscount1, ticketPriceWithDiscount2);

    User user = trainDao.getUserByEmail(username).orElseThrow(() -> new InvalidException("No such user!"));

    return discountCard(tr, ticketPriceWithDiscount, user);
  }

  public BigDecimal discountCard(TrainRequestDto tr, BigDecimal ticketPriceWithDiscount, User user)
  {
    if ("PENSIONER".equals(user.getCard().name())) {
      ticketPriceWithDiscount = ticketPriceWithDiscount.multiply(new BigDecimal("0.66")); //34% discount
    }
    else if ("FAMILY".equals(user.getCard().name())) {
      ticketPriceWithDiscount = ticketPriceWithDiscount.multiply(new BigDecimal("0.5")); // 50% discount
    }
    else {
      if (tr.isChild()) {
        ticketPriceWithDiscount = ticketPriceWithDiscount.multiply(new BigDecimal("0.9")); // 10% discount
      }
    }
    return ticketPriceWithDiscount;
  }

  public BigDecimal discountTwoWay(TrainRequestDto tr, @NotNull BigDecimal ticketPriceWithDiscount1, BigDecimal ticketPriceWithDiscount2)
  {
    BigDecimal ticketPriceWithDiscount;
    if (tr.isTwoWay()) {
      ticketPriceWithDiscount = ticketPriceWithDiscount1.add(ticketPriceWithDiscount2);
      ticketPriceWithDiscount = ticketPriceWithDiscount.multiply(new BigDecimal("0.95")); //5% discount
      if (null == tr.getIdTwo() || null == tr.getArriveTimeTwo() || null == tr.getGoingTimeTwo() || null == tr.getTicketPriceTwo()) {
        throw new InvalidException("You must chose two ticket for two way option!");
      }
    }
    else {
      ticketPriceWithDiscount = ticketPriceWithDiscount1;
    }
    return ticketPriceWithDiscount;
  }

  public BigDecimal discountTimeDiapason(BigDecimal ticketPriceWithDiscount1, LocalDateTime goingTime)
  {
    if (goingTime.toLocalTime().isAfter(LocalTime.of(9, 30, 0))
        && goingTime.toLocalTime().isBefore(LocalTime.of(16, 0, 0))) {
      ticketPriceWithDiscount1 = ticketPriceWithDiscount1.multiply(new BigDecimal("0.95")); //5% discount
    }
    else if (goingTime.toLocalTime().isAfter(LocalTime.of(19, 30, 0))) {
      ticketPriceWithDiscount1 = ticketPriceWithDiscount1.multiply(new BigDecimal("0.95")); //5% discount
    }
    return ticketPriceWithDiscount1;
  }


}
