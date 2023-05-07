package com.example.demo.service;


import com.example.demo.entity.Person;

import java.util.List;

@FunctionalInterface
public interface UpdateFunction {
  void update(List<Person> personList);
}