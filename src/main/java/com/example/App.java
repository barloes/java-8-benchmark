package com.example;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.entity.Person;
import com.example.repository.PersonRepository;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
@EnableJpaRepositories(basePackages = {"com.example.repository"})
public class App implements CommandLineRunner{
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Autowired
    private PersonRepository personRepository;
    

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) {
        Long startTime = System.currentTimeMillis();
        List<Person> personList = personRepository.getPersonBySysDeleteAndIsHuman(false, true);
        List<Person> newPersonList = new ArrayList<>();

        Long count = 0L;
        for (Person person : personList) {
            person.setSysDelete(true);
            newPersonList.add(person);
            count += 1;
        }

        personRepository.saveAll(newPersonList);

        Long timeTaken = System.currentTimeMillis() - startTime;
        logger.info("Time taken to update {} records: {} ms", count, timeTaken);
    }
}
