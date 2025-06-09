Spring (Boot) JDBC Support
===

We are going to add database to our project and for now we are going to use the [H2 Database](https://www.h2database.com/html/main.html). We are going to add some beans to configure the infrastructure (`DataSource`, `JdbcTemplate`) and using those we are going to improve the JDBC based repository implementation.


Getting Started
---
First take a look at the `pom.xml` as well as the `biz.deinum.library.JdbcBookRepository` class. This is a plain JDBC implementation of the `BookRepository` interface. It uses the `DataSource` to execute queries and map the `ResultSet` (if needed) using the `mapToBook` method. 

To make this work we first need to have an actual `DataSource`, for this we are going to use the H2 database.

**NOTE:** The Project also contains a test case for the `JdbcRepository` ideally after each step you make that green as well, adapting it to the new and improved `JdbcRepository`!

Spring and JDBC
---

1. Add the following dependencies to the `pom.xml`
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

**NOTE:** Notice the omission of the `version` element in the XML declaration, this is due to Spring Boot managing the version for us!

2. Open the `LibraryApplication` class and add a bean for the datasource

```java
@Bean
public DataSource dataSource() {
   return new EmbeddedDatabaseBuilder()
           .setType(EmbeddedDatabaseType.H2)
           .setName("books")
           .addDefaultScripts()
           .build();
}
```

**TIP:** The `EmbeddedDatabaseBuilder` is a nice helper class from the Spring Framework to make it easier to setup an embedded database. However Spring Boot will make it even easier :). 

3. With the bean added run the `LibraryApplication`. If all goes well it should print the content of the database to the terminal. 

Spring and `JdbcTemplate`
---
The `JdbcBookRepository` contains a few methods to retrieve or save a `Book` to the database. There is a lot of boilerplate in those methods, like opening a connection, making sure it is being closed etc. Spring has a `JdbcTemplate` to make it easier to work with JDBC.

1. Add the following bean to the `LibraryApplication`

```java
import org.springframework.context.annotation.Bean;

@Bean
public JdbcTemplate jdbcTemplate(DataSource dataSource) {
  return new JdbcTemplate(dataSource);
}
```

2. Open the `JdbcBookRepository` and modify the implementation to use a `JdbcTemplate`.
   1. Change the constructor to take a `JdbcTemplate` and assign it to a field replacing the `DataSource` field.
   2. Replace the `findAll`, `findById` and `save` methods with the following
    ```java
      @Override
      public List<Book> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, (rs, row) -> mapToBook(rs));
      }
    
      @Override
      public Optional<Book> findById(String isbn) {
        try {
          var dbResult = jdbcTemplate.queryForObject(FIND_BY_ISBN_SQL, (rs, row) -> mapToBook(rs), isbn);
          return Optional.ofNullable(dbResult);
        } catch (IncorrectResultSizeDataAccessException ex) {
          return Optional.empty();
        }
      }
    
      @Override
      public Book save(Book book) {
        jdbcTemplate.update(SAVE_SQL, book.isbn(), book.title(), book.authors().toArray(new String[0]));
        return book;
      }
    ```
3. Now re-run the `LibraryApplication` and it should still work as it did before. However with a lot less boilerplate code.
4. Try to rewrite the `JdbcBookRepository` into using the `JdbcClient` instead of the `JdbcTemplate`. 
   
Spring Boot and Auto-Configuration
---
1. Open the `LibraryApplication` and remove the added `@Bean` methods
2. Now re-run the `LibraryApplication` again and it should still work as it did before. However with even less boilerplate code.
    * Notice that the output for starting the datasource is slightly different as it now uses a proper connection pool (Hikari by default)

Spring Boot and Jdbc Testing
---
To test JDBC based application Spring Boot has the nice `@JdbcTest` annotation. This will setup a minimal application context with the needed JDBC infrastructure like a datasource, jdbc template and transaction management. 

1. Create a test for the `JdbcBookRepository` and add the `@JdbcTest` annotation
2. Add a field for the `DataSource` and put `@Autowired` on top of it. 
3. Add a field for the `JdbcBookRepository`
4. Create a method to create the `JdbcBookRepository` with the `DataSource` and to initialize the data.
   ```java
   @BeforeEach
   public void setUp() {
    this.repository = new JdbcBookRepository(dataSource);
    if (this.repository.findAll().isEmpty()) {
      BookGenerator.all().forEach(repository::save);
    }
   }
   ```
   * This will create the repository and initialize the data before each that is being run. 
   * **TIP:** You could also add `@Import(JdbcBookRepository.class)` to the test to have the repository automatically created and dependency injected. 
5. Lets create some methods to actually test the repository
   ```java
   @Test
   void shouldRetrieveAllBooks() {
    var books = repository.findAll();
    assertEquals(24, books.size());
   }
   
   @Test
   void shouldFindBookByIsbn() {
    var isbn = "9780764558313";
    var books = repository.findByIsbn(isbn);
    Assertions.assertThat(books)
        .isNotEmpty()
        .hasValueSatisfying( (book) -> Assertions.assertThat(book.isbn()).isEqualTo(isbn));
   }
   
   @Test
   void shouldSaveBook() {
    var newBook = new Book("123456789test", "Test Book", "Test Author");
    var cntBefore = repository.findAll().size();
    this.repository.save(newBook);
    var cntAfter = repository.findAll().size();
    Assertions.assertThat(cntAfter).isEqualTo(cntBefore + 1);
   }
   ```
   
Spring Boot and Spring Data JDBC
---
1. Replace the `spring-boot-starter-jdbc` dependency with `spring-boot-starter-data-jdbc`
2. Delete the `JdbcBookRepository` class
3. Add a new interface named `JdbcBookReposity` with the following declaration

```java
import biz.deinum.library.BookRepository;

public interface JdbcBookRepository extends ListCrudRepository<Book, Long>, BookRepository {
}
```
4. Finally re-run the application and if all done right it should still work. This is due to Spring Data "generating" the implementation at runtime (it actually uses proxies to accomplish this)
   * The `BookRepository` contains methods with the same signature, or ones understood by Spring Data to create queries based on the method names.
5. For the test you should `@DataJdbcTest` and autowire the `JdbcBookRepository` into the test case instead of a `DataSource` or `JdbcTemplate`. 
   * The `@DataJdbcTest`, as the name implies, also bootstraps the needed Spring Data JDBC components needed to create the proper runtime repositories. 