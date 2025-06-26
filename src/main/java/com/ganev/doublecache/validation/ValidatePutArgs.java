package com.ganev.doublecache.validation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatePutArgs {}
