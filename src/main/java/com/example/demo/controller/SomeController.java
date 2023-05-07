package com.example.demo.controller;

import com.example.demo.entity.Person;
import com.example.demo.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SomeController {

  private final Logger logger = LoggerFactory.getLogger(SomeController.class);

  @Autowired
  private PersonService personService;

  @GetMapping("/insert/{threadCount}")
  String one(@PathVariable int threadCount) {
    Long startTime = System.currentTimeMillis();

    personService.insertPersonHistoryMultithread(threadCount);

    Long endTime = System.currentTimeMillis();
    logger.info("insert took " + (endTime - startTime) + " ms");
    return "insert multi threading";
  }

  @GetMapping("/update/{threadCount}")
  String two(@PathVariable int threadCount) {
    Long startTime = System.currentTimeMillis();

    personService.updatePersonHistoryMultithread(threadCount);

    Long endTime = System.currentTimeMillis();
    logger.info("insert took " + (endTime - startTime) + " ms");
    return "insert multi threading";
  }

  @GetMapping("/insertDefault")
  public String insertDefault() {
    Long startTime = System.currentTimeMillis();

    List<Person> personList = personService.getPeopleWithOppositeSysDelete();
    personService.insertPersonHistoryDefault(personList);

    Long endTime = System.currentTimeMillis();
    logger.info("insert took " + (endTime - startTime) + " ms");
    return "insert";
  }

  @GetMapping("/insertManual")
  public String insertManual() {
    Long startTime = System.currentTimeMillis();

    List<Person> personList = personService.getPeopleWithOppositeSysDelete();
    personService.insertPersonHistoryManual(personList);

    Long endTime = System.currentTimeMillis();
    logger.info("insert took " + (endTime - startTime) + " ms");
    return "insert";
  }


  @GetMapping("/insertSave")
  public String insertSave() {
    Long startTime = System.currentTimeMillis();

    List<Person> personList = personService.getPeopleWithOppositeSysDelete();
    personService.insertPersonHistoryUsingSaveAll(personList);

    Long endTime = System.currentTimeMillis();
    logger.info("insert true took " + (endTime - startTime) + " ms");
    return "insert";
  }

  @GetMapping("/delSingle")
  public String deleteSingle() {
    Long startTime = System.currentTimeMillis();

    logger.info("change all sysDelete to true");
    personService.deletePeopleSingle();

    Long endTime = System.currentTimeMillis();
    logger.info("delete single took " + (endTime - startTime) + " ms");
    return "del";
  }

  @GetMapping("/del")
  public String delete() {
    Long startTime = System.currentTimeMillis();

    logger.info("change all sysDelete to true");
    personService.deletePeople();

    Long endTime = System.currentTimeMillis();
    logger.info("delete took " + (endTime - startTime) + " ms");
    return "del";
  }

  @GetMapping("/add")
  public String add() {
    Long startTime = System.currentTimeMillis();

    logger.info("change all sysDelete to false");
    personService.undeletePeople();

    Long endTime = System.currentTimeMillis();
    logger.info("add took " + (endTime - startTime) + " ms");
    return "add";
  }

  @GetMapping("/addSingle")
  public String addSingle() {
    Long startTime = System.currentTimeMillis();

    logger.info("change all sysDelete to false");
    personService.undeletePeopleSingle();

    Long endTime = System.currentTimeMillis();
    logger.info("delete single took " + (endTime - startTime) + " ms");
    return "del";
  }

  @GetMapping("/op/add")
  public String addOp() {
    Long startTime = System.currentTimeMillis();

    logger.info("change all sysDelete to false");
    personService.undeletePeopleOptimised();

    Long endTime = System.currentTimeMillis();
    logger.info("delete single took " + (endTime - startTime) + " ms");
    return "del";
  }
}