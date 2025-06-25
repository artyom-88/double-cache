package com.ganev.doublecache.validation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class PutArgsValidationAspect {

  @Before("@annotation(com.ganev.doublecache.validation.ValidatePutArgs)")
  public void validatePutArgs(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    if (args[0] == null) throw new NullPointerException("Key cannot be null");
    if (args[1] == null) throw new NullPointerException("Value cannot be null");
  }
}
