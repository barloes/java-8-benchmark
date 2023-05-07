package com.example.demo.repository;


import com.example.demo.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {
  // Add any custom query methods here
  List<Person> getPersonBySysDeleteAndIsHuman(
    boolean sysDelete,
    boolean isHuman
  );

  List<Person> getPersonByIsHuman(
          boolean isHuman
  );
}
