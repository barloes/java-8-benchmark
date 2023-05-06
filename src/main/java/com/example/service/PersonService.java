package com.example.service;

import com.example.entity.Person;
import com.example.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

  Logger logger = LoggerFactory.getLogger(PersonService.class);

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private EntityManager entityManager;

  @Transactional
  public void savePersons(List<Person> newPersonList) {
    personRepository.saveAll(newPersonList);
  }

  @Transactional
  public void test() {
    List<Person> personList = personRepository.getPersonBySysDeleteAndIsHuman(true, true);
    logger.info("personList size: {}", personList.size());
    List<Person> personListToDelete = personList.stream().map(person ->
      {
      person.setSysDelete(true);
      return person;
      }).collect(Collectors.toList());

    insertPersonHistoryDefault(personListToDelete);


  }

  // insert 10 000 using multi threading
  //  @Transactional
  //  public void insertPersonHistory(Person person, ExecutorService executor) {
  //    executor.execute(() ->
  //      {
  //      String sql = "INSERT into `personHistory` (`name`, `sysDelete`, `isHuman`) VALUES (?, ?, ?);";
  //      try {
  //        entityManager.createNativeQuery(sql).setParameter(1, person.getName()).setParameter(2, person.isSysDelete()).setParameter(3, person.isHuman())
  //                .executeUpdate();
  //      } catch (Exception e) {
  //        e.printStackTrace();
  //      }
  //      });
  //  }

  // insert 10 000 not using multi threading
  @Transactional
  public void insertPersonHistoryDefault(List<Person> personList) {
    Long starttime = System.currentTimeMillis();

    StringBuilder sql = new StringBuilder("INSERT INTO `personHistory` (`name`, `sysDelete`, `isHuman`) VALUES (?, ?, ?)");
    // Append placeholders for the values
    for (int i = 0; i < personList.size() - 1; i++) {
      sql.append(", (?, ?, ?)");
    }
    logger.info("SQL: {}", sql.toString());

    int index = 1;
    Query query = entityManager.createNativeQuery(sql.toString());
    for (Person person : personList) {
      query.setParameter(index++, person.getName());
      query.setParameter(index++, person.isSysDelete());
      query.setParameter(index++, person.isHuman());
    }

    query.executeUpdate();

    Long timeTaken = System.currentTimeMillis() - starttime;
    logger.info("Time taken to insert {} records default, {} ms", personList.size(), timeTaken);
  }
}
