package com.example.train.exceptions;

public class InvalidException extends RuntimeException
{

  public InvalidException(String message)
  {
    super(message);
  }
}