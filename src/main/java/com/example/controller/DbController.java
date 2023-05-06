package com.example.controller;


import com.example.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DbController {

  private final Logger logger = LoggerFactory.getLogger(DbController.class);

  @Autowired
  private PersonService personService;

  @GetMapping("/test")
  public String test() {
    personService.test();
    return "2";
  }
}