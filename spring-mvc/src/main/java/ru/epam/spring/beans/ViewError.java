package ru.epam.spring.beans;

import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ViewError {

    String value();

    HttpStatus status() default HttpStatus.INTERNAL_SERVER_ERROR;
}
