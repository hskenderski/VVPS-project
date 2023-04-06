package com.example.train.security;


import com.example.train.exceptions.InvalidException;
import com.example.train.hristiyan.TrainDao;
import com.example.train.hristiyan.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService
{

  private final TrainDao trainDao;

  @Autowired
  public UserDetailServiceImpl(TrainDao trainDao)
  {
    this.trainDao = trainDao;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
  {
    User user = trainDao.getUserByEmail(username).orElseThrow(() -> new InvalidException("No such user!"));

    return org.springframework.security.core.userdetails.User
        .withUsername(user.getEmail())
        .password(user.getPassword())
        .roles(user.getRole().name())
        .build();
  }
}