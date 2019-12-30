# Azure Cosmos DB

## What is Azure Cosmos DB?

Azure Cosmos DB is Microsoft's globally distributed, multi-model database service. With a click of a button, Cosmos DB enables you to elastically and independently scale throughput and storage across any number of Azure regions worldwide. You can elastically scale throughput and storage, and take advantage of fast, single-digit-millisecond data access using your favorite API including **SQL**, **MongoDB**, **Cassandra**, **Tables**, or **Gremlin**. Cosmos DB provides comprehensive service level agreements (SLAs) for throughput, latency, availability, and consistency guarantees, something no other database service offers.

## How to create a database

Please follow the section *Create a database account* under [this Quickstart tutorial](https://docs.microsoft.com/en-us/azure/cosmos-db/create-sql-api-java#create-a-database-account)

## Cosmos DB and Spring

There way several ways of starting integrating Cosmos DB and Java projects. Specific targeting Spring web applications, [Spring Data](https://spring.io/projects/spring-data) provides a consistent programming model for data access in Cosmos through repository pattern.

This can be easily done using one of the following APIs:

* [Spring Data for Cosmos DB (SQL API)](https://github.com/microsoft/spring-data-cosmosdb)
* [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb)
* [Spring Data for Apache Cassandra](https://spring.io/projects/spring-data-cassandra)
* [Spring Data for Gremlin](https://github.com/microsoft/spring-data-gremlin)

We choose to use Spring Data for Cosmos (SQL) in the current Helium project.

### Create and consume repositories

Usually you start defining an entity:

```java
@Getter
@AllArgsConstructor
public class Genre {
    private String id;
    private String genre;
}
```

Then, just create an interface that extends a Spring data repository. In our case, `DocumentDbRepository`.

```java
@Repository
public interface GenresRepository extends DocumentDbRepository<Genre, String> {
}
```

> The repository interface `DocumentDbRepository<TEntity, TKey>` accepts `TEntity` as the main entity type, and `TKey` as the key type.

After that, you can use methods from an autowired repository, such as:

```java
@Service
public class GenresService {

    @Autowired
    private GenresRepository repository;

    public void doSomething(){
        String id = UUID.randomUUID().toString();
        Genre genre = new Genre(id, "Sci-Fi");
        repository.save(genre);

        boolean exists = repository.existsById(id);

        Optional<Genre> genreFind = repository.findById(id);

        repository.delete(genre);

        long count = repository.count();

        Iterable<Genre> genres = repository.findAll();

        repository.deleteAll();
    }
}
```

## Custom Queries

Spring Data support custom query creation from methods names. As stated in [the official documentation](https://docs.spring.io/spring-data/commons/docs/current/reference/html/#repositories.query-methods.details), here are some examples for a Person repository:

```java
interface PersonRepository extends Repository<User, Long> {

  List<Person> findByEmailAddressAndLastname(EmailAddress emailAddress, String lastname);

  // Enables the distinct flag for the query
  List<Person> findDistinctPeopleByLastnameOrFirstname(String lastname, String firstname);
  List<Person> findPeopleDistinctByLastnameOrFirstname(String lastname, String firstname);

  // Enabling ignoring case for an individual property
  List<Person> findByLastnameIgnoreCase(String lastname);
  // Enabling ignoring case for all suitable properties
  List<Person> findByLastnameAndFirstnameAllIgnoreCase(String lastname, String firstname);

  // Enabling static ORDER BY for a query
  List<Person> findByLastnameOrderByFirstnameAsc(String lastname);
  List<Person> findByLastnameOrderByFirstnameDesc(String lastname);
}
```

## Cross-partition queries

If you have partitioned your data, there are some limitations when querying data.
Please see instructions at [How to Query Partitioned Azure Cosmos DB Collection](https://github.com/microsoft/spring-data-cosmosdb/blob/master/QueryPartitionedCollection.md)
