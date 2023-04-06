package com.example.train.hristiyan.dto;

import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotNull;

public class User implements GrantedAuthority
{
  private Integer  id;
  @NotNull
  private String   firstName;
  @NotNull
  private String   secondName;
  @NotNull
  private String   thirdName;
  @NotNull
  private String   email;
  @NotNull
  private String   password;
  @NotNull
  private RoleName role;
  @NotNull
  private Integer  age;
  @NotNull
  private CardType card;

  public User()
  {
  }

  public User(Integer id, String firstName, String secondName, String thirdName, String email, String password, RoleName role, Integer age, CardType card)
  {
    this.id = id;
    this.firstName = firstName;
    this.secondName = secondName;
    this.thirdName = thirdName;
    this.email = email;
    this.password = password;
    this.role = role;
    this.age = age;
    this.card = card;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public String getSecondName()
  {
    return secondName;
  }

  public void setSecondName(String secondName)
  {
    this.secondName = secondName;
  }

  public String getThirdName()
  {
    return thirdName;
  }

  public void setThirdName(String thirdName)
  {
    this.thirdName = thirdName;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public RoleName getRole()
  {
    return role;
  }

  public void setRole(RoleName role)
  {
    this.role = role;
  }

  public Integer getAge()
  {
    return age;
  }

  public void setAge(Integer age)
  {
    this.age = age;
  }

  public CardType getCard()
  {
    return card;
  }

  public void setCard(CardType card)
  {
    this.card = card;
  }

  public enum RoleName
  {
    USER,
    ADMIN
  }

  public enum CardType
  {
    PENSIONER,
    FAMILY,
    NO
  }

  @Transient
  @Override
  public String getAuthority()
  {
    return "ROLE_" + role.toString();
  }

}

