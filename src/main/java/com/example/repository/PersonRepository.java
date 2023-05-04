package com.example.repository;

import com.example.entity.Person;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
  // Add any custom query methods here
  List<Person> getPersonBySysDeleteAndIsHuman(
    boolean sysDelete,
    boolean isHuman
  );
}
