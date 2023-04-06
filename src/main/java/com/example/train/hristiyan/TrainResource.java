package com.example.train.hristiyan;

import com.example.train.hristiyan.dto.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class TrainResource
{
  private final TrainService trainService;

  @Autowired
  public TrainResource(TrainService trainService)
  {
    this.trainService = trainService;
  }


  @PostMapping("/svc/add-train")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('ADMIN')")
  public void createTrain(@RequestBody @Valid Train train,
                          @CurrentSecurityContext(expression = "authentication") Authentication authentication)
  {
    trainService.createTrain(train);
  }

  @PostMapping("/svc/registration")
  @ResponseStatus(HttpStatus.CREATED)
  public void register(@RequestBody @Valid User user)
  {
    trainService.register(user);
  }

  @PostMapping("/svc/login")
  @ResponseStatus(HttpStatus.OK)
  public String login()
  {
    return "login successful";
  }


  @GetMapping("/svc/user")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ADMIN')")
  public User loadUserByEmail(@RequestParam @Length(max = 100) String email)
  {
    return trainService.loadUserByEmail(email);
  }


  @PutMapping("/svc/user")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAnyRole('ADMIN')")
  public void updateUserByEmail(@RequestParam String email,
                                @RequestBody @Valid User user
  )
  {
    trainService.updateUserByEmail(email, user);
  }


  @PostMapping("/svc/get-train")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  public List<Train> loadTrains(@RequestBody @Valid TrainSearchCriteria criteria)
  {
    return trainService.loadTrains(criteria);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PostMapping("/svc/cart")
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  public void addTicketToCart(@RequestBody @Valid List<TrainRequestDto> requestDto,
                              @CurrentSecurityContext(expression = "authentication") Authentication authentication)
  {
    String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
    trainService.addTicketToCart(requestDto, username);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/svc/cart")
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  public List<TicketInCart> viewCart(@CurrentSecurityContext(expression = "authentication") Authentication authentication)
  {
    String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
    return trainService.viewCart(username);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/svc/cart")
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  public void removeItemFromCart(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                 @RequestParam Integer ticketId)
  {
    String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
    trainService.removeItemFromCart(username, ticketId);
  }


  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/svc/cart/order")
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  public void orderCart(@CurrentSecurityContext(expression = "authentication") Authentication authentication)
  {
    String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
    trainService.orderCart(username);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/svc/cart/order")
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  public List<TicketInCart> viewOrders(@CurrentSecurityContext(expression = "authentication") Authentication authentication)
  {
    String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
    return trainService.viewOrders(username);
  }

}
