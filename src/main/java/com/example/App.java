package com.example;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.entity.Person;
import com.example.repository.PersonRepository;

@SpringBootApplication
public class App implements CommandLineRunner{
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Autowired
    private PersonRepository personRepository;
    

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) {
        List<Person> personList = personRepository.getPersonBySysDeleteAndIsHuman(false, true);

        logger.info("Person count {}", personList.size());
    }
}
