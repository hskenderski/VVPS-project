package com.example.train.hristiyan;

import com.example.train.hristiyan.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainDao
{
  private final NamedParameterJdbcOperations template;

  @Autowired
  public TrainDao(NamedParameterJdbcOperations namedParameterJdbcOperations)
  {
    this.template = namedParameterJdbcOperations;
  }

  public void createTrain(Train train)
  {
    final String sql =
        ""
            + "INSERT INTO train "
            + " (                "
            + "  town_from       " //1
            + " ,town_to         " //2
            + " ,going_time      " //3
            + " ,arrive_time     " //4
            + " ,capacity        " //5
            + " ,ticket_price    " //6
            + " )VALUES          "
            + " (                "
            + "  :townFrom       " //1
            + " ,:townTo         " //2
            + " ,:goingTime      " //3
            + " ,:arriveTime     " //4
            + " ,:capacity       " //5
            + " ,:ticketPrice    " //6
            + " )                ";

    MapSqlParameterSource sp = new MapSqlParameterSource();
    sp.addValue("townFrom", train.getTownFrom());
    sp.addValue("townTo", train.getTownTo());
    sp.addValue("goingTime", train.getGoingTime());
    sp.addValue("arriveTime", train.getArriveTime());
    sp.addValue("capacity", train.getCapacity());
    sp.addValue("ticketPrice", train.getTicketPrice());

    template.update(sql, sp);

  }

  public void register(User user)
  {
    final String sql =
        ""
            + "INSERT INTO user  "
            + " (                "
            + "  first_name      " //1
            + " ,second_name     " //2
            + " ,third_name      " //3
            + " ,email           " //4
            + " ,password        " //5
            + " ,role            " //6
            + " ,age             " //7
            + " ,card            " //8
            + " )VALUES          "
            + " (                "
            + "  :firstName      " //1
            + " ,:secondName     " //2
            + " ,:thirdName      " //3
            + " ,:email          " //4
            + " ,:password       " //5
            + " ,:role           " //6
            + " ,:age            " //7
            + " ,:card           " //8
            + " )                ";

    MapSqlParameterSource sp = new MapSqlParameterSource();
    sp.addValue("firstName", user.getFirstName());
    sp.addValue("secondName", user.getSecondName());
    sp.addValue("thirdName", user.getThirdName());
    sp.addValue("email", user.getEmail());
    sp.addValue("password", user.getPassword());
    sp.addValue("role", user.getRole().toString());
    sp.addValue("age", user.getAge());
    sp.addValue("card", user.getCard().toString());

    template.update(sql, sp);

  }

  public Optional<User> getUserByEmail(String email)
  {
    String sql =
        "SELECT id,            "
            + "       first_name,    "
            + "       second_name,   "
            + "       third_name,    "
            + "       email,         "
            + "       password,      "
            + "       role,          "
            + "       age,           "
            + "       card           "
            + "  FROM user           "
            + " WHERE email = :email ";

    MapSqlParameterSource sp = new MapSqlParameterSource()
        .addValue("email", email);

    try {
      return template.queryForObject(sql, sp, (rs, rowNum) ->
          Optional.of(
              new User(
                  rs.getInt("id"),
                  rs.getString("first_name"),
                  rs.getString("second_name"),
                  rs.getString("third_name"),
                  rs.getString("email"),
                  rs.getString("password"),
                  User.RoleName.valueOf(rs.getString("role")),
                  rs.getInt("age"),
                  User.CardType.valueOf(rs.getString("card")))));
    }
    catch (EmptyResultDataAccessException ex) {
      return Optional.empty();
    }
  }

  public List<Train> loadTrains(TrainSearchCriteria criteria)
  {
    String sql =
        ""
            + "SELECT id,          "
            + "       town_from,   "
            + "       town_to,     "
            + "       going_time,  "
            + "       arrive_time, "
            + "       capacity,    "
            + "       ticket_price "
            + "FROM train          "
            + "WHERE 1=1           ";
    if (null != criteria.getGoingTimeFrom()) {
      sql += " AND going_time >= :goingTimeFrom ";
    }
    if (null != criteria.getGoingTimeTo()) {
      sql += " AND going_time <= :goingTimeTo ";
    }

    if (null != criteria.getArriveTimeFrom()) {
      sql += " AND arrive_time >= :arriveTimeFrom ";
    }

    if (null != criteria.getArriveTimeTo()) {
      sql += "AND arrive_time <= :arriveTimeTo ";
    }

    if (null != criteria.getTownFrom()) {
      sql += "AND town_from like :townFrom ";
    }

    if (null != criteria.getTownTo()) {
      sql += "AND town_to like :townTo ";
    }

    if (null != criteria.getTicketPriceFrom()) {
      sql += "AND ticket_price >= :ticketPriceFrom ";
    }

    if (null != criteria.getTicketPriceTo()) {
      sql += "AND ticket_price <= :ticketPriceTo ";
    }

    sql += "ORDER BY id ";

    MapSqlParameterSource sp = new MapSqlParameterSource()
        .addValue("goingTimeFrom", criteria.getGoingTimeFrom())
        .addValue("goingTimeTo", criteria.getGoingTimeTo())
        .addValue("arriveTimeFrom", criteria.getArriveTimeFrom())
        .addValue("arriveTimeTo", criteria.getArriveTimeTo())
        .addValue("townFrom", criteria.getTownFrom())
        .addValue("townFrom", criteria.getTownFrom())
        .addValue("townTo", criteria.getTownTo())
        .addValue("ticketPriceFrom", criteria.getTicketPriceFrom())
        .addValue("ticketPriceTo", criteria.getTicketPriceTo());

    return template.query(sql, sp, (rs, rowNum) ->
        new Train(
            rs.getInt("id"),
            rs.getString("town_from"),
            rs.getString("town_to"),
            rs.getObject("going_time", LocalDateTime.class),
            rs.getObject("arrive_time", LocalDateTime.class),
            rs.getInt("capacity"),
            rs.getBigDecimal("ticket_price")
        ));

  }

  public void addTicketToCart(TrainRequestDto requestDto, String username, BigDecimal priceWithDiscount)
  {
    final String sql =
        ""
            + "INSERT INTO user_train   "
            + "(                        "
            + " user_id,                "
            + " going_train_id,         "
            + " back_train_id,          "
            + " historized,             "
            + " cart,                   "
            + " add_cart_date,          "
            + " price_with_discount,    "
            + " child)                  "
            + "VALUES(                  "
            + " (SELECT id              "
            + "  FROM user              "
            + "  WHERE email = :email), "
            + " :goingTrainId,          "
            + " :backTrainId,           "
            + " 'N',                    "
            + " 'Y',                    "
            + " SYSDATE(),              "
            + " :priceWithDiscount,     "
            + " :isChild)               ";


    MapSqlParameterSource sp = new MapSqlParameterSource()
        .addValue("email", username)
        .addValue("goingTrainId", requestDto.getId())
        .addValue("backTrainId", requestDto.getIdTwo())
        .addValue("priceWithDiscount", priceWithDiscount)
        .addValue("isChild", requestDto.isChild());

    template.update(sql, sp);
  }


  public void clearCart(String email)
  {
    final String sql =
        ""
            + "DELETE FROM user_train                                   "
            + "WHERE user_id = (SELECT id                               "
            + "                 FROM user                               "
            + "                 WHERE email = :email)                   "
            + "AND  DATE_ADD(add_cart_date,INTERVAL +7 DAY) < SYSDATE() ";

    template.update(sql, new MapSqlParameterSource("email", email));

  }

  public List<TicketInCart> viewCart(String username, boolean isCart)
  {
    String sql =
        ""
            + " SELECT t.town_from,                       "
            + "        t.town_to,                         "
            + "        t.going_time,                      "
            + "        t.arrive_time,                     "
            + "        t2.town_from,                      "
            + "        t2.town_to,                        "
            + "        t2.going_time,                     "
            + "        t2.arrive_time,                    "
            + "        ut.id,                             "
            + "        ut.child,                          "
            + "        ut.price_with_discount             "
            + " FROM user_train ut                        "
            + "     LEFT JOIN train t2                    "
            + "      ON t2.id = ut.back_train_id          "
            + "     LEFT JOIN train t                     "
            + "      ON t.id = ut.going_train_id          "
            + " WHERE ut.user_id = (SELECT id             "
            + "                     FROM user             "
            + "                     WHERE email = :email) "
            + " AND ut.historized = 'N'                   ";

    if (isCart) {
      sql += " AND ut.cart = 'Y'                   ";
    }
    else {
      sql += " AND ut.cart = 'N'                   ";
    }


    return template.query(sql, new MapSqlParameterSource("email", username), (rs, rowNum) ->
        new TicketInCart(
            rs.getInt("ut.id"),
            rs.getString("t.town_from"),
            rs.getString("t.town_to"),
            rs.getObject("t.going_time", LocalDateTime.class),
            rs.getObject("t.arrive_time", LocalDateTime.class),
            rs.getString("t2.town_from"),
            rs.getString("t2.town_to"),
            rs.getObject("t2.going_time", LocalDateTime.class),
            rs.getObject("t2.arrive_time", LocalDateTime.class),
            rs.getBigDecimal("ut.price_with_discount"),
            TrainDao.toBoolean((rs.getString("ut.child")))
        ));
  }

  public void removeItemFromCart(String email, Integer ticketId)
  {
    final String sql =
        ""
            + "DELETE FROM user_train                 "
            + "WHERE user_id = (SELECT id             "
            + "                 FROM user             "
            + "                 WHERE email = :email) "
            + "AND id = :ticketId                     "
            + "AND cart = 'Y'                         ";

    MapSqlParameterSource sp = new MapSqlParameterSource()
        .addValue("email", email)
        .addValue("ticketId", ticketId);

    template.update(sql, sp);
  }

  public void updateUserByEmail(String email, User user)
  {
    final String sql =
        ""
            + " UPDATE user                    "
            + " SET first_name  = :firstName,  "
            + "     second_name = :secondName, "
            + "     third_name  = :thirdName,  "
            + "     email       = :newEmail,   "
            + "     password    = :password,   "
            + "     role        = :role,       "
            + "     age         = :age,        "
            + "     card        = :card        "
            + " WHERE email = :email           ";

    MapSqlParameterSource sp = new MapSqlParameterSource()
        .addValue("firstName", user.getFirstName())
        .addValue("secondName", user.getSecondName())
        .addValue("thirdName", user.getThirdName())
        .addValue("newEmail", user.getEmail())
        .addValue("password", user.getPassword())
        .addValue("role", user.getRole().toString())
        .addValue("age", user.getAge())
        .addValue("card", user.getCard().toString())
        .addValue("email", email);

    template.update(sql, sp);
  }

  public void orderCart(String email)
  {
    final String sql =
        ""
            + " UPDATE user_train                      "
            + " SET cart = 'N'                         "
            + " WHERE user_id = (SELECT id             "
            + "                  FROM user             "
            + "                  WHERE email = :email) "
            + " AND cart = 'Y'                         ";

    template.update(sql, new MapSqlParameterSource("email", email));
  }

  public static boolean toBoolean(String str)
  {
    return "Y".equals(str);
  }

}
