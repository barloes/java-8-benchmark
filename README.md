# java8-benchmark
Java starter project to benchmark sql commands

```agsl
docker-compose up -d

mvn spring-boot:run
```

```agsl
batch optimisation refers to changing id to UUID
No significant difference after changing id to UUID when using saveAll, save and manual.

before batch optimisation:
insert 10000 records - 30 seconds (saveAll)
insert 10000 records - 15 seconds (native query)
after batch optimisation:
inserting 10000 records - 12 seconds (saveAll)
inserting 10000 records - 4 seconds (saveAll with Multi Threding)

before batch optimisation:
update 10000 records - 15 seconds (saveAll)

after batch optimisation:
update 10000 records - 12 seconds (saveAll) after batch optimisation
update 10000 records - 8 seconds (saveAll with Multi Threading) after batch optimisation
```

# Optimisation tips
```agsl
- use UUID instead as Hibernate disallow batch operation for GenerationType.IDENTITY
- detach entity in Transaction if they are not used as any changes are also carried out
- multithreading works with UUID Id but not GenerationType.IDENTITY id
```

```
- async operation for adding history 
- enable hibernate statistics
```

# Useful resource
- https://stackoverflow.com/questions/50772230/how-to-do-bulk-multi-row-inserts-with-jparepository
- https://github.com/Cepr0/sb-jpa-batch-insert-demo