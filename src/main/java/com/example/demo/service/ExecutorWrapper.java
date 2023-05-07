package com.example.demo.service;

import com.example.demo.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class ExecutorWrapper {

  private static final Logger logger = LoggerFactory.getLogger(ExecutorWrapper.class);

  @Autowired
  PersonService personService;

  public void runPerson(List<Person> personList, UpdateFunction updateFunction, int threadCount) {
    if (threadCount == 0) {
      return;
    }

    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    for (int i = 0; i < threadCount; i++) {
      int startIndex = i * (personList.size() / threadCount);
      int endIndex;
      if (i == threadCount - 1) {
        endIndex = personList.size();
      } else {endIndex = (i + 1) * (personList.size() / threadCount);}

      List<Person> personSubList = personList.subList(startIndex, endIndex);
      executor.execute(() -> {
      long startTime = System.currentTimeMillis();
      logger.info("Starting task for index range {} - {}", startIndex, endIndex);
      updateFunction.update(personSubList);
      long endTime = System.currentTimeMillis();
      long duration = endTime - startTime;
      logger.info("Finished task for index range {} - {} in {} ms", startIndex, endIndex, duration);
      });
    }

    executor.shutdown();
    try {
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      logger.error("Task interrupted", e);
    }
  }

}
