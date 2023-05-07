package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "personHistory")
public class PersonHistory {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private boolean sysDelete;
    private boolean isHuman;
}
