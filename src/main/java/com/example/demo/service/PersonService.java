package com.example.demo.service;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Person;
import com.example.demo.entity.PersonHistory;
import com.example.demo.repository.PersonHistoryRepository;
import com.example.demo.repository.PersonRepository;

@Service
public class PersonService {

	Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private PersonHistoryRepository personHistoryRepository;

	@Autowired
	private EntityManager mainEntityManager;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private ExecutorWrapper executorWrapper;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Transactional
	public void savePersons(List<Person> newPersonList) {
		personRepository.saveAll(newPersonList);
	}

	// insert 10 000 using multi threading
	@Transactional
	public void insertPersonHistoryMultithread(int threadCount) {
		List<Person> personList = getPeopleWithOppositeSysDelete();
		executorWrapper.runPerson(personList, this::insertPersonHistoryUsingSaveAll, threadCount);
	}

	@Transactional
	public void updatePersonHistoryMultithread(int threadCount) {
		List<Person> personList = getPeopleWithOppositeSysDelete();
		executorWrapper.runPerson(personList, this::persistPeopleSaveAll, threadCount);
	}

	// insert 10 000 not using multi threading
	@Transactional()
	public void insertPersonHistoryDefault(List<Person> personList) {
		if (personList.isEmpty()) {
			return;
		}
		logger.info("processing size: {}", personList.size());


		StringBuilder sql = new StringBuilder("INSERT INTO `personHistory` (`name`, `sysDelete`, `isHuman`) VALUES (?, ?, ?)");
		for (int i = 0; i < personList.size() - 1; i++) {
			sql.append(", (?, ?, ?)");
		}

		int index = 1;
		Query query = mainEntityManager.createNativeQuery(sql.toString());
		for (Person person : personList) {
			query.setParameter(index++, person.getName());
			query.setParameter(index++, person.isSysDelete());
			query.setParameter(index++, person.isHuman());
		}

		query.executeUpdate();
	}

	@Transactional
	public void insertPersonHistoryManual(List<Person> personList) {
		if (personList.isEmpty()) {
			return;
		}
		List<PersonHistory> personHistoryList = personList.stream().map(person ->
			{
			PersonHistory personHistory = new PersonHistory();
			personHistory.setName(person.getName());
			personHistory.setSysDelete(person.isSysDelete());
			personHistory.setHuman(person.isHuman());
			return personHistory;
			}).collect(Collectors.toList());

		persistHistory(personHistoryList, 1000);
	}

	public void insertPersonHistoryTest(List<Person> personList) {
		if (personList.isEmpty()) {
			return;
		}
		logger.info("processing size: {}", personList.size());

		persistHistory(personList.stream().map(person ->
			{
			PersonHistory personHistory = new PersonHistory();
			personHistory.setName(person.getName());
			personHistory.setSysDelete(person.isSysDelete());
			personHistory.setHuman(person.isHuman());
			return personHistory;
			}).collect(Collectors.toList()), 50);
	}

	public void persistHistory(List<PersonHistory> personHistoryList, int batchSize) {
		EntityManager subEntityManager = entityManagerFactory.createEntityManager();
		EntityTransaction tx = subEntityManager.getTransaction();
		Iterator<PersonHistory> iterator = personHistoryList.iterator();
		tx.begin();
		int cont = 0;
		while (iterator.hasNext()) {
			subEntityManager.persist(iterator.next());
			cont++;
			if (cont % batchSize == 0) {
				subEntityManager.flush();
				subEntityManager.clear();
				tx.commit();
				tx.begin();
			}
		}
		tx.commit();

		subEntityManager.close();
	}

	public void persistPeopleSaveAll(List<Person> personList) {
		if (personList.isEmpty()) {
			return;
		}
		personRepository.saveAll(personList);
	}

	public void persistPeople(List<Person> personList) {
		if (personList.isEmpty()) {
			return;
		}
		EntityManager subEntityManager = entityManagerFactory.createEntityManager();
		EntityTransaction tx = subEntityManager.getTransaction();
		Iterator<Person> iterator = personList.iterator();
		tx.begin();
		int cont = 0;
		while (iterator.hasNext()) {
			subEntityManager.merge(iterator.next());
			cont++;
			if (cont % 100 == 0) {
				subEntityManager.flush();
				subEntityManager.clear();
			}
		}
		tx.commit();
		subEntityManager.close();
	}

	@Transactional
	public void deletePeople() {
		List<Person> personListToDelete = personRepository.getPersonBySysDeleteAndIsHuman(false, true).stream().map(person ->
			{
			person.setSysDelete(true);
			return person;
			}).collect(Collectors.toList());

		updatePersonUsingSaveAll(personListToDelete);
	}

	@Transactional
	public void undeletePeople() {
		List<Person> personListToUndelete = personRepository.getPersonBySysDeleteAndIsHuman(true, true).stream().map(person ->
			{
			person.setSysDelete(false);
			return person;
			}).collect(Collectors.toList());

		updatePersonUsingSaveAll(personListToUndelete);
	}

	public void undeletePeopleOptimised() {
		List<Person> personListToUndelete = personRepository.getPersonBySysDeleteAndIsHuman(true, true).stream().map(person ->
			{
			person.setSysDelete(false);
			return person;
			}).collect(Collectors.toList());

		persistPeople(personListToUndelete);
	}

	@Transactional
	public void deletePeopleSingle() {
		List<Person> personListToDelete = personRepository.getPersonBySysDeleteAndIsHuman(false, true).stream().map(person ->
			{
			person.setSysDelete(true);
			return person;
			}).collect(Collectors.toList());

		updatePersonUsingSave(personListToDelete);
	}

	@Transactional
	public void undeletePeopleSingle() {
		List<Person> personListToUndelete = personRepository.getPersonBySysDeleteAndIsHuman(true, true).stream().map(person ->
			{
			person.setSysDelete(false);
			return person;
			}).collect(Collectors.toList());

		updatePersonUsingSave(personListToUndelete);
	}

	@Transactional
	public void insertPersonHistoryUsingSaveAll(List<Person> personList) {
		// Detach all Person entities in the personList
		for (Person person : personList) {
			mainEntityManager.detach(person);
		}

		List<PersonHistory> personHistoryList = personList.stream().map(person ->
			{
			PersonHistory personHistory = new PersonHistory();
			personHistory.setName(person.getName());
			personHistory.setSysDelete(person.isSysDelete());
			personHistory.setHuman(person.isHuman());
			return personHistory;
			}).collect(Collectors.toList());

		personHistoryRepository.saveAll(personHistoryList);
	}

	@Transactional
	public void updatePersonUsingSaveAll(List<Person> personList) {
		if (personList.isEmpty()) {
			return;
		}
		personRepository.saveAll(personList);
	}

	@Transactional
	public void updatePersonUsingSave(List<Person> personList) {
		if (personList.isEmpty()) {
			return;
		}
		for (Person person : personList) {
			personRepository.save(person);
		}
	}

	public List<Person> getPeopleWithOppositeSysDelete() {
		List<Person> personList = personRepository.getPersonByIsHuman(true);
		for (Person person : personList) {
			mainEntityManager.detach(person);
		}
		return personList.stream().map(person ->
			{
			person.setSysDelete(!person.isSysDelete());
			return person;
			}).collect(Collectors.toList());
	}
}
