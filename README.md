# java8-benchmark
Java starter project to benchmark sql commands

```agsl
docker-compose up -d

mvn spring-boot:run
```

```agsl
batch optimisation refers to changing id to UUID
No significant difference after changing id to UUID when using saveAll, save and manual.

insert 10000 records - 30 seconds (saveAll)
insert 10000 records - 15 seconds (native query)
inserting 10000 records - 4 seconds (saveAll with Multi Threding)

update 10000 records - 15 seconds (saveAll)
update 10000 records - 12 seconds (saveAll) after batch optimisation
update 10000 records - 8 seconds (saveAll with Multi Threading) after batch optimisation
```

# Optimisation tips
```agsl
mysql do not allow use of anything other then @GeneratedValue(strategy = GenerationType.IDENTITY)
- use UUID instead for Id generation as we are using mySql -> batch operation
- detach entity if they are not used.
- after using UUID, multithreading started to work
- async operation for adding history 
- enable hibernate statistics
```


# Useful resource
- https://stackoverflow.com/questions/50772230/how-to-do-bulk-multi-row-inserts-with-jparepository
- https://github.com/Cepr0/sb-jpa-batch-insert-demo