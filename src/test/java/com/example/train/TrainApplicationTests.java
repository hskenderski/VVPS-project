package com.example.train;

import com.example.train.exceptions.InvalidException;
import com.example.train.hristiyan.TrainService;
import com.example.train.hristiyan.dto.Train;
import com.example.train.hristiyan.dto.TrainRequestDto;
import com.example.train.hristiyan.dto.TrainSearchCriteria;
import com.example.train.hristiyan.dto.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@Sql(scripts = "/test.sql")
class TrainApplicationTests
{

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  TrainService trainService;


  @Test
  @WithUserDetails("hris@abv.bg")
  public void testCreateTrain() throws Exception
  {

    Train train = new Train();
    train.setTownFrom("Sofia");
    train.setTownTo("Varna");
    train.setTicketPrice(new BigDecimal("10.99"));
    train.setGoingTime(LocalDateTime.now());
    train.setArriveTime(LocalDateTime.now().plusHours(5));
    train.setCapacity(100);

    mockMvc.perform(post("/svc/add-train")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(train)))
        .andExpect(status().isCreated())
        .andDo(print())
    ;
  }

  @Test
  public void testRegister() throws Exception
  {
    User user = new User();
    user.setFirstName("Hristiyan");
    user.setSecondName("Ivanov");
    user.setThirdName("Skenderski");
    user.setAge(23);
    user.setRole(User.RoleName.ADMIN);
    user.setCard(User.CardType.NO);
    user.setEmail("h.sskenderski@abv.bg");
    user.setPassword("1");

    mockMvc.perform(post("/svc/registration")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isCreated())
        .andDo(print())
    ;
  }

  @Test
  public void testGlobalExceptionHandler_1() throws Exception
  {
    User user = new User();
    user.setFirstName("Hristiyan");
    user.setThirdName("Skenderski");
    user.setAge(23);
    user.setRole(User.RoleName.ADMIN);
    user.setCard(User.CardType.NO);
    user.setEmail("h.sskenderski@abv.bg");
    user.setPassword("1");

    mockMvc.perform(post("/svc/registration")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isBadRequest())
        .andDo(print())
    ;
  }

  @WithUserDetails("hris@abv.bg")
  @Test
  public void testGlobalExceptionHandler_2() throws Exception
  {
    String str = "t";

    mockMvc.perform(get("/svc/user")
            .queryParam("email", str.repeat(101)))
        .andExpect(status().isBadRequest())
        .andDo(print())
    ;
  }

  @WithUserDetails("hris@abv.bg")
  @Test
  public void testGlobalExceptionHandler_3() throws Exception
  {
    mockMvc.perform(get("/svc/user")
            .queryParam("email", "INVALID"))
        .andExpect(status().isBadRequest())
        .andDo(print())
    ;
  }

  @WithUserDetails("hris@abv.bg")
  @Test
  public void testLogin() throws Exception
  {
    mockMvc.perform(post("/svc/login"))
        .andExpect(status().isOk())
        .andDo(print())
    ;
  }

  @WithUserDetails("hris@abv.bg")
  @Test
  public void testLoadUserByEmail() throws Exception
  {
    mockMvc.perform(get("/svc/user")
            .queryParam("email", "hris@abv.bg"))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.id").value(-999))
        .andExpect(jsonPath("$.firstName").value("Hristiyan"))
        .andExpect(jsonPath("$.secondName").value("Ivanov"))
        .andExpect(jsonPath("$.thirdName").value("Skenderski"))
        .andExpect(jsonPath("$.email").value("hris@abv.bg"))
        .andExpect(jsonPath("$.password").value("test"))
        .andExpect(jsonPath("$.role").value("ADMIN"))
        .andExpect(jsonPath("$.age").value(23))
        .andExpect(jsonPath("$.card").value("FAMILY"))
        .andExpect(jsonPath("$.authority").value("ROLE_ADMIN"))
    ;
  }

  @WithUserDetails("hris@abv.bg")
  @Test
  public void TestUpdateUserByEmail() throws Exception
  {

    User user = new User();
    user.setFirstName("Dancho");
    user.setSecondName("Petkov");
    user.setThirdName("Draganov");
    user.setEmail("dancho@abv.bg");
    user.setPassword("321");
    user.setAge(18);
    user.setRole(User.RoleName.ADMIN);
    user.setCard(User.CardType.FAMILY);

    mockMvc.perform(put("/svc/user")
            .queryParam("email", "ivan@abv.bg")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isNoContent())
        .andDo(print());

    mockMvc.perform(get("/svc/user")
            .queryParam("email", "dancho@abv.bg"))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.id").value(-998))
        .andExpect(jsonPath("$.firstName").value("Dancho"))
        .andExpect(jsonPath("$.secondName").value("Petkov"))
        .andExpect(jsonPath("$.thirdName").value("Draganov"))
        .andExpect(jsonPath("$.email").value("dancho@abv.bg"))
        .andExpect(jsonPath("$.password").value("321"))
        .andExpect(jsonPath("$.role").value("ADMIN"))
        .andExpect(jsonPath("$.age").value(18))
        .andExpect(jsonPath("$.card").value("FAMILY"))
        .andExpect(jsonPath("$.authority").value("ROLE_ADMIN"))
    ;


  }


  @WithUserDetails("hris@abv.bg")
  @Test
  public void testLoadTrains() throws Exception
  {

    TrainSearchCriteria criteria = new TrainSearchCriteria();
    criteria.setGoingTimeFrom(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    criteria.setGoingTimeTo(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    criteria.setArriveTimeFrom(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    criteria.setArriveTimeTo(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    criteria.setTownFrom("TestGrad1");
    criteria.setTownTo("TestGrad2");
    criteria.setTicketPriceFrom(new BigDecimal("89.99"));
    criteria.setTicketPriceTo(new BigDecimal("99.99"));

    mockMvc.perform(post("/svc/get-train")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(criteria)))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$.[0].id").value(-999))
        .andExpect(jsonPath("$.[0].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[0].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[0].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[0].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[0].capacity").value(99))
        .andExpect(jsonPath("$.[0].ticketPrice").value(99.99))
        .andExpect(jsonPath("$.[1].id").value(-998))
        .andExpect(jsonPath("$.[1].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[1].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[1].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[1].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[1].capacity").value(99))
        .andExpect(jsonPath("$.[1].ticketPrice").value(89.99))
    ;
  }

  @Test
  @WithUserDetails("hris@abv.bg")
  public void testAddTicketToCart() throws Exception
  {

    List<TrainRequestDto> trainRequestDtoList = new ArrayList<>();

    TrainRequestDto trainRequestDto = new TrainRequestDto();
    trainRequestDto.setId(-999);
    trainRequestDto.setTownFrom("TestGrad1");
    trainRequestDto.setTownTo("TestGrad2");
    trainRequestDto.setGoingTime(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto.setArriveTime(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto.setTicketPrice(new BigDecimal("99.99"));

    trainRequestDto.setTwoWay(true);
    trainRequestDto.setChild(false);

    trainRequestDto.setIdTwo(-99);
    trainRequestDto.setGoingTimeTwo(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto.setArriveTimeTwo(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto.setTicketPriceTwo(new BigDecimal("99.99"));

    TrainRequestDto trainRequestDto1 = new TrainRequestDto();
    trainRequestDto1.setId(-999);
    trainRequestDto1.setTownFrom("TestGrad1");
    trainRequestDto1.setTownTo("TestGrad2");
    trainRequestDto1.setGoingTime(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto1.setArriveTime(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto1.setTicketPrice(new BigDecimal("99.99"));

    trainRequestDto1.setTwoWay(true);
    trainRequestDto1.setChild(true);

    trainRequestDto1.setIdTwo(-99);
    trainRequestDto1.setGoingTimeTwo(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto1.setArriveTimeTwo(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto1.setTicketPriceTwo(new BigDecimal("99.99"));

    TrainRequestDto trainRequestDto2 = new TrainRequestDto();
    trainRequestDto2.setId(-999);
    trainRequestDto2.setTownFrom("TestGrad1");
    trainRequestDto2.setTownTo("TestGrad2");
    trainRequestDto2.setGoingTime(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto2.setArriveTime(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto2.setTicketPrice(new BigDecimal("99.99"));

    trainRequestDto2.setTwoWay(false);
    trainRequestDto2.setChild(true);

    trainRequestDtoList.add(trainRequestDto);
    trainRequestDtoList.add(trainRequestDto1);
    trainRequestDtoList.add(trainRequestDto2);


    mockMvc.perform(post("/svc/cart")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(trainRequestDtoList)))
        .andExpect(status().isNoContent())
        .andDo(print())
    ;

    mockMvc.perform(get("/svc/cart")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$.[0].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[0].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[0].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[0].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[0].townFrom2").value("TestGrad2"))
        .andExpect(jsonPath("$.[0].townTo2").value("TestGrad1"))
        .andExpect(jsonPath("$.[0].goingTime2").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[0].arriveTime2").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[0].price").value(90.24))
        .andExpect(jsonPath("$.[0].child").value(false))

        .andExpect(jsonPath("$.[1].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[1].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[1].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[1].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[1].townFrom2").value("TestGrad2"))
        .andExpect(jsonPath("$.[1].townTo2").value("TestGrad1"))
        .andExpect(jsonPath("$.[1].goingTime2").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[1].arriveTime2").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[1].price").value(90.24))
        .andExpect(jsonPath("$.[1].child").value(false))

        .andExpect(jsonPath("$.[2].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[2].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[2].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[2].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[2].townFrom2").isEmpty())
        .andExpect(jsonPath("$.[2].townTo2").isEmpty())
        .andExpect(jsonPath("$.[2].goingTime2").isEmpty())
        .andExpect(jsonPath("$.[2].arriveTime2").isEmpty())
        .andExpect(jsonPath("$.[2].price").value(47.5))
        .andExpect(jsonPath("$.[2].child").value(false))
    ;
  }


  //No card with childs
  @Test
  @WithUserDetails("ivan@abv.bg")
  public void testAddTicketToCart_2() throws Exception
  {

    List<TrainRequestDto> trainRequestDtoList = new ArrayList<>();

    TrainRequestDto trainRequestDto = new TrainRequestDto();
    trainRequestDto.setId(-999);
    trainRequestDto.setTownFrom("TestGrad1");
    trainRequestDto.setTownTo("TestGrad2");
    trainRequestDto.setGoingTime(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto.setArriveTime(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto.setTicketPrice(new BigDecimal("99.99"));

    trainRequestDto.setTwoWay(true);
    trainRequestDto.setChild(false);

    trainRequestDto.setIdTwo(-99);
    trainRequestDto.setGoingTimeTwo(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto.setArriveTimeTwo(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto.setTicketPriceTwo(new BigDecimal("99.99"));

    TrainRequestDto trainRequestDto1 = new TrainRequestDto();
    trainRequestDto1.setId(-999);
    trainRequestDto1.setTownFrom("TestGrad1");
    trainRequestDto1.setTownTo("TestGrad2");
    trainRequestDto1.setGoingTime(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto1.setArriveTime(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto1.setTicketPrice(new BigDecimal("99.99"));

    trainRequestDto1.setTwoWay(true);
    trainRequestDto1.setChild(true);

    trainRequestDto1.setIdTwo(-99);
    trainRequestDto1.setGoingTimeTwo(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto1.setArriveTimeTwo(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto1.setTicketPriceTwo(new BigDecimal("99.99"));

    TrainRequestDto trainRequestDto2 = new TrainRequestDto();
    trainRequestDto2.setId(-999);
    trainRequestDto2.setTownFrom("TestGrad1");
    trainRequestDto2.setTownTo("TestGrad2");
    trainRequestDto2.setGoingTime(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto2.setArriveTime(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto2.setTicketPrice(new BigDecimal("99.99"));

    trainRequestDto2.setTwoWay(false);
    trainRequestDto2.setChild(true);

    trainRequestDtoList.add(trainRequestDto);
    trainRequestDtoList.add(trainRequestDto1);
    trainRequestDtoList.add(trainRequestDto2);


    mockMvc.perform(post("/svc/cart")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(trainRequestDtoList)))
        .andExpect(status().isNoContent())
        .andDo(print())
    ;

    mockMvc.perform(get("/svc/cart")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$", hasSize(4)))
        .andExpect(jsonPath("$.[0].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[0].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[0].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[0].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[0].townFrom2").value("TestGrad2"))
        .andExpect(jsonPath("$.[0].townTo2").value("TestGrad1"))
        .andExpect(jsonPath("$.[0].goingTime2").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[0].arriveTime2").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[0].price").value(107.99))
        .andExpect(jsonPath("$.[0].child").value(true))

        .andExpect(jsonPath("$.[1].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[1].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[1].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[1].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[1].townFrom2").value("TestGrad2"))
        .andExpect(jsonPath("$.[1].townTo2").value("TestGrad1"))
        .andExpect(jsonPath("$.[1].goingTime2").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[1].arriveTime2").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[1].price").value(180.48))
        .andExpect(jsonPath("$.[1].child").value(false))

        .andExpect(jsonPath("$.[2].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[2].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[2].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[2].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[2].townFrom2").value("TestGrad2"))
        .andExpect(jsonPath("$.[2].townTo2").value("TestGrad1"))
        .andExpect(jsonPath("$.[2].goingTime2").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[2].arriveTime2").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[2].price").value(162.43))
        .andExpect(jsonPath("$.[2].child").value(false))

        .andExpect(jsonPath("$.[3].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[3].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[3].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[3].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[3].townFrom2").isEmpty())
        .andExpect(jsonPath("$.[3].townTo2").isEmpty())
        .andExpect(jsonPath("$.[3].goingTime2").isEmpty())
        .andExpect(jsonPath("$.[3].arriveTime2").isEmpty())
        .andExpect(jsonPath("$.[3].price").value(85.49))
        .andExpect(jsonPath("$.[3].child").value(false))
    ;
  }

  //With PENSIONER card discount
  @Test
  @WithUserDetails("h.skenderski@abv.bg")
  public void testAddTicketToCart_3() throws Exception
  {

    List<TrainRequestDto> trainRequestDtoList = new ArrayList<>();

    TrainRequestDto trainRequestDto = new TrainRequestDto();
    trainRequestDto.setId(-999);
    trainRequestDto.setTownFrom("TestGrad1");
    trainRequestDto.setTownTo("TestGrad2");
    trainRequestDto.setGoingTime(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto.setArriveTime(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto.setTicketPrice(new BigDecimal("99.99"));

    trainRequestDto.setTwoWay(true);
    trainRequestDto.setChild(false);

    trainRequestDto.setIdTwo(-99);
    trainRequestDto.setGoingTimeTwo(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto.setArriveTimeTwo(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto.setTicketPriceTwo(new BigDecimal("99.99"));

    TrainRequestDto trainRequestDto1 = new TrainRequestDto();
    trainRequestDto1.setId(-999);
    trainRequestDto1.setTownFrom("TestGrad1");
    trainRequestDto1.setTownTo("TestGrad2");
    trainRequestDto1.setGoingTime(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto1.setArriveTime(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto1.setTicketPrice(new BigDecimal("99.99"));

    trainRequestDto1.setTwoWay(true);
    trainRequestDto1.setChild(true);

    trainRequestDto1.setIdTwo(-99);
    trainRequestDto1.setGoingTimeTwo(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto1.setArriveTimeTwo(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto1.setTicketPriceTwo(new BigDecimal("99.99"));

    TrainRequestDto trainRequestDto2 = new TrainRequestDto();
    trainRequestDto2.setId(-999);
    trainRequestDto2.setTownFrom("TestGrad1");
    trainRequestDto2.setTownTo("TestGrad2");
    trainRequestDto2.setGoingTime(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    trainRequestDto2.setArriveTime(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    trainRequestDto2.setTicketPrice(new BigDecimal("99.99"));

    trainRequestDto2.setTwoWay(false);
    trainRequestDto2.setChild(true);

    trainRequestDtoList.add(trainRequestDto);
    trainRequestDtoList.add(trainRequestDto1);
    trainRequestDtoList.add(trainRequestDto2);


    mockMvc.perform(post("/svc/cart")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(trainRequestDtoList)))
        .andExpect(status().isNoContent())
        .andDo(print())
    ;

    mockMvc.perform(get("/svc/cart")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$.[0].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[0].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[0].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[0].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[0].townFrom2").value("TestGrad2"))
        .andExpect(jsonPath("$.[0].townTo2").value("TestGrad1"))
        .andExpect(jsonPath("$.[0].goingTime2").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[0].arriveTime2").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[0].price").value(119.12))
        .andExpect(jsonPath("$.[0].child").value(false))

        .andExpect(jsonPath("$.[1].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[1].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[1].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[1].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[1].townFrom2").value("TestGrad2"))
        .andExpect(jsonPath("$.[1].townTo2").value("TestGrad1"))
        .andExpect(jsonPath("$.[1].goingTime2").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[1].arriveTime2").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[1].price").value(119.12))
        .andExpect(jsonPath("$.[1].child").value(false))

        .andExpect(jsonPath("$.[2].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[2].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[2].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[2].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[2].townFrom2").isEmpty())
        .andExpect(jsonPath("$.[2].townTo2").isEmpty())
        .andExpect(jsonPath("$.[2].goingTime2").isEmpty())
        .andExpect(jsonPath("$.[2].arriveTime2").isEmpty())
        .andExpect(jsonPath("$.[2].price").value(62.69))
        .andExpect(jsonPath("$.[2].child").value(false))
    ;
  }

  @Test
  @WithUserDetails("ivan@abv.bg")
  public void testViewCart() throws Exception
  {
    mockMvc.perform(get("/svc/cart")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$.[0].ticketId").value(-11))
        .andExpect(jsonPath("$.[0].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[0].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[0].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[0].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[0].townFrom2").value("TestGrad2"))
        .andExpect(jsonPath("$.[0].townTo2").value("TestGrad1"))
        .andExpect(jsonPath("$.[0].goingTime2").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[0].arriveTime2").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[0].price").value(107.99))
        .andExpect(jsonPath("$.[0].child").value(true))
    ;
  }


  @Test
  @WithUserDetails("ivan@abv.bg")
  public void testRemoveItemFromCart() throws Exception
  {
    mockMvc.perform(delete("/svc/cart")
            .queryParam("ticketId", "-11"))
        .andExpect(status().isNoContent())
        .andDo(print())
    ;

    mockMvc.perform(get("/svc/cart")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$", hasSize(0)))
    ;
  }

  @Test
  @WithUserDetails("ivan@abv.bg")
  public void testOrderCart() throws Exception
  {
    mockMvc.perform(get("/svc/cart")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$.[0].ticketId").value(-11))
        .andExpect(jsonPath("$.[0].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[0].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[0].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[0].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[0].townFrom2").value("TestGrad2"))
        .andExpect(jsonPath("$.[0].townTo2").value("TestGrad1"))
        .andExpect(jsonPath("$.[0].goingTime2").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[0].arriveTime2").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[0].price").value(107.99))
        .andExpect(jsonPath("$.[0].child").value(true))
    ;

    mockMvc.perform(post("/svc/cart/order"))
        .andExpect(status().isCreated())
        .andDo(print())
    ;

    mockMvc.perform(get("/svc/cart")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$", hasSize(0)))
    ;

    mockMvc.perform(get("/svc/cart/order"))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$.[0].ticketId").value(-11))
        .andExpect(jsonPath("$.[0].townFrom").value("TestGrad1"))
        .andExpect(jsonPath("$.[0].townTo").value("TestGrad2"))
        .andExpect(jsonPath("$.[0].goingTime").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[0].arriveTime").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[0].townFrom2").value("TestGrad2"))
        .andExpect(jsonPath("$.[0].townTo2").value("TestGrad1"))
        .andExpect(jsonPath("$.[0].goingTime2").value("2023-01-01T12:00:00"))
        .andExpect(jsonPath("$.[0].arriveTime2").value("2023-01-01T14:00:00"))
        .andExpect(jsonPath("$.[0].price").value(107.99))
        .andExpect(jsonPath("$.[0].child").value(true))
    ;
  }

  @Test
  @WithUserDetails("ivan@abv.bg")
  public void testViewOrders() throws Exception
  {
    mockMvc.perform(get("/svc/cart/order"))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$", hasSize(0)))
    ;
  }


  @Test
  public void testDiscountTwoWay_1()
  {
    TrainRequestDto dto = new TrainRequestDto();
    dto.setTwoWay(true);
    dto.setIdTwo(3);
    dto.setArriveTimeTwo(LocalDateTime.now());
    dto.setGoingTimeTwo(LocalDateTime.now());
    dto.setTicketPriceTwo(new BigDecimal("20"));

    BigDecimal result = trainService.discountTwoWay(dto, new BigDecimal("18.77"), new BigDecimal("18.77"));
    Assertions.assertEquals(new BigDecimal("35.6630"), result);
  }

  @Test
  public void testDiscountTwoWay_2()
  {
    TrainRequestDto dto = new TrainRequestDto();
    dto.setTwoWay(true);
    dto.setTicketPriceTwo(new BigDecimal("20"));

    Assertions.assertThrows(InvalidException.class, () -> trainService.discountTwoWay(dto, new BigDecimal("18.77"), new BigDecimal("18.77")));
  }

  @Test
  public void testDiscountTwoWay_3()
  {
    TrainRequestDto dto = new TrainRequestDto();
    dto.setTwoWay(false);
    dto.setTicketPriceTwo(new BigDecimal("20"));

    BigDecimal result = trainService.discountTwoWay(dto, new BigDecimal("18.77"), null);
    Assertions.assertEquals(new BigDecimal("18.77"), result);
  }

  @Test
  public void testCalculateTicketDiscounts_1()
  {
    TrainRequestDto dto = new TrainRequestDto();
    dto.setTicketPrice(new BigDecimal("10.99"));
    dto.setTicketPriceTwo(new BigDecimal("10.99"));
    dto.setGoingTimeTwo(LocalDateTime.now());
    dto.setGoingTime(LocalDateTime.now());
    dto.setArriveTime(LocalDateTime.now());
    dto.setArriveTimeTwo(LocalDateTime.now());

    BigDecimal result = trainService.calculateTicketDiscounts(dto, "h.skenderski@abv.bg");
    Assertions.assertEquals(new BigDecimal("6.890730"), result);

  }

  @Test
  public void testCalculateTicketDiscounts_2()
  {
    TrainRequestDto dto = new TrainRequestDto();
    dto.setTicketPrice(new BigDecimal("10.99"));
    dto.setTicketPriceTwo(new BigDecimal("10.99"));
    dto.setGoingTime(LocalDateTime.now());
    dto.setArriveTime(LocalDateTime.now());
    dto.setArriveTimeTwo(LocalDateTime.now());

    BigDecimal result = trainService.calculateTicketDiscounts(dto, "h.skenderski@abv.bg");
    Assertions.assertEquals(new BigDecimal("6.890730"), result);

  }

  @Test
  public void addTicketToCart_1()
  {

    List<TrainRequestDto> list = new ArrayList<>();

    TrainRequestDto dto = new TrainRequestDto();
    dto.setId(-999);
    dto.setTownFrom("TestGrad1");
    dto.setTownTo("TestGrad2");
    dto.setGoingTime(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    dto.setArriveTime(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
    dto.setTicketPrice(new BigDecimal("99.99"));

    list.add(dto);

    trainService.addTicketToCart(list, "h.skenderski@abv.bg");

  }

  @Test
  public void addTicketToCart_2()
  {
    trainService.addTicketToCart(new ArrayList<>(), "h.skenderski@abv.bg");
  }

  @Test
  public void main()
  {
    TrainApplication.main(new String[]{});
  }


}
