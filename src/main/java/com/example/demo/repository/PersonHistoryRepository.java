package com.example.demo.repository;

import com.example.demo.entity.PersonHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonHistoryRepository extends JpaRepository<PersonHistory, UUID> {
    // Add any custom query methods here
    
}
