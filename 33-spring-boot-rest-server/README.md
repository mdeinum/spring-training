Spring Boot - Rest Server
=

In this excercise we will be using Spring Boot to build a REST API for our library. We will be using Jackson as a JSON library and annotated controllers to handle the retrieval of the data. 

Getting Started
-
1. Add the following dependencies to the `pom.xml`
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

This will add the needed dependencies, Tomcat, Spring Web MVC and Jackson.

2. Next add a class to the project `biz.deinum.library.rest.LibraryApiController`
   * Add the `org.springframework.stereotype.Controller` to the class
   * Create a `private` `final` field to hold the `BookService` and create the appropriate constructor for it

3. Next we need a method to react to a GET request on `/api/books` which returns a collection of books. 
   * Add `@RequestMapping("/api/books")` to the class
   * Add the following method to the `LibraryApiController`
      ```java
      @GetMapping()
      @ResponseBody
      public List<Book> list() {
        return books.findAll();
      }
     ```
4. Now you can try and run the `LibraryApplication` when started navigate to `http://localhost:8080/api/books` and it should display a JSON array with books.
   * You can also use `curl` or `httpie` or another CLI tool to access the URL.
   
Using path variables
-
1. Now lets add a method to the `LibraryApiController` to find the book by ISBN and return the book
    * Add the following method to the `LibraryApiController`
   ```java
   @GetMapping("/{isbn}")
   public Optional<Book> details(@PathVariable("isbn") String isbn) {
     return books.findByIsbn(isbn);
   }
   ```
   * The `{isbn}` in the url is a path-variable and it will be bound to the `@PathVariable` annotated method argument, we can use it to retrieve the book
   * Re-run the `LibraryApplication` and access `/books/{isbn}` with an existing book, next try a non existing one. 
   
3. From the previous recipe we covered exception handling, re-create the `EntityNotFoundException` that was annotated with `@ResponseStatus` and throw that if the book isnt' found. 
   * Re-run the application and try to find a non existing book. It should now return 404 instead of 200 with `null`.d 
 
Accepting data
-
To modify or create a new book we need to have an additional method on the controller. As we aren't using a form we don't have a model object, but rather we want to use the request body. For this use the `@RequestBody` annotation instead of the `@ModelAttribute`. 

2. Add method to receive a `Book` and persist it. For this add a method annotated with `@PostMapping`. The method takes a `Book` as the parameter and that parameter is annotated with `@RequestBody`. After persisting the new (or updated) book it will be returned
   ```java
   @PostMapping
   @ResponseBody
   public Book create(@RequestBody Book book) {
      return books.create(book);
   }   
   ```
3. Re-run the application
   ```shell
   http -v POST http://localhost:8080/api/books isbn=987654321 title="Some Title" authors:='["Me"]'
   ```
4. Instead of directly returning the object we can wrap it in a `ResponseEntity` object, to allow more control over the response. Like adding headers, change code etc. 
5. Lets modify the create method to return a HTTP Status code of created (201) instead of OK (200).
   ```java
   @PostMapping
   @ResponseBody
   public ResponseEntity<Book> create(@RequestBody Book book) {
     var dbBook = this.books.create(book);
     return ResponseEntity
       .created(URI.create("/api/books/" + dbBook.isbn()))
       .body(dbBook);
   }
   ```
6. Re-run and check the result when creating a book. 

Validation and Exception Handling
-
1. Lets modify the `@PostMapping` method to also do validation:
   * Add the `spring-boot-starter-validation` dependency
   * Add `@Valid` next to the `@RequestBody` annotation
2. Re-run the application and try to post an invalid book (like missing an ISBN etc.) and see the results.
   * If done well you should get an http response with code 400 (Bad Request)
   * Although it tells you the request is wrong it doesn't explain **what** is wrong. 
3. When building REST APIs we can utilize the [Problem Details for HTTP APIs](https://www.rfc-editor.org/rfc/rfc9457.html) standard (RFC-9457), for which Spring has build in support. 
4. If not already there add an `application.properties` in `src/main/resources` and add `spring.mvc.problemdetails.enabled=true`
   * If you now re-run the application the response has changed but it still isn't really useful. 
5. Add a new class `biz.deinum.library.rest.LibraryExceptionHandler` and use the following content
   ```java
   @ControllerAdvice
   public class LibraryExceptionHandler extends ResponseEntityExceptionHandler {
   
     @Override
     protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
       HttpHeaders headers, HttpStatusCode status, WebRequest request) {
       var errors = resolveMessage(ex);
       ex.getBody().setProperty("errors", errors);
       return super.handleExceptionInternal(ex, null, headers, status, request);
     }
     
     private Map<String, List<String>> resolveMessage(MethodArgumentNotValidException ex) {
       return  ex.getAllErrors().stream()
        .collect(
            Collectors.groupingBy(this::getKey,
                Collectors.mapping(this::resolveMessage, Collectors.toList())));
     }

     private String getKey(ObjectError error) {
       return (error instanceof FieldError fe) ? fe.getField() : error.getObjectName();
     }
   
     private String resolveMessage(ObjectError error) {
       return getMessageSource() != null ? getMessageSource().getMessage(error, LocaleContextHolder.getLocale()) : 
       error.getDefaultMessage();
     }
   }
   ```
   * This will extend the default `ProblemDetail` object with an `errors` property which contains specific information on the fields that have an error.
6. Add a `messages.properties` to the `src/main/resources` directory with the following content:
   ```
   NotBlank.book.title=Title is required
   NotBlank.book.isbn=ISBN is required
   Size.book.isbn=ISBN should be between 9 and 13 long
   NotEmpty.book.authors=Book should have at least 1 author
   ``` 
   * This will create messages for the `@NotBlank` on the `title` field of the `Book` class (and more).
   * You can also add a `messages_nl.properties` and provide Dutch translations (or another ISO_639 language code to provide translations for that specific language.
7. Re-run the application and check the response, it should now give detailed information about the fields that are wrong, using the translations we have put in the `messages.properties`.   

Testing
-
Until now we relied only on running the application and we haven't written any test for our controller. Spring Boot has the `@WebMvcTest` that we can utilize to write a test for the controller and we can use `@MockitoBean` to create a mock for the dependencies to be injected. 

For testing we need the `spring-boot-starter-test` dependency which would pull in the necessary testing libraries. However that is already defined as a dependency in the **parent project** so no need to add that in here. 

1. Add a class `biz.deinum.library.rest.LibraryApiControllerTest` in `src/test/java` and put an `@WebMvcTest` annotation on it (`@WebMvcTest(LibraryApiController.class)` to specify which controller to test),
2. Add a field of type `MockMvc` to the test and put `@Autowired` on top of it
3. Add a field of type `BookService` to the test and put `@MockitoBean` on top it.
4. Now add the following test methods.
   ```java
   @Test
   void shouldReturnEmptyJsonArray() throws Exception {
     mockMvc
       .perform(MockMvcRequestBuilders.get("/api/books"))
       .andExpect(MockMvcResultMatchers.status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
       .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty())
       .andDo(MockMvcResultHandlers.print());
   }

   @Test
   void shouldReturnNonEmptyJsonArray() throws Exception {
     Mockito.when(bookService.findAll()).thenReturn(List.of(
       new Book("123456789", "Title 1", "Author 1"),
       new Book("987654321", "Title 2", "Author 2")));
     mockMvc
       .perform(MockMvcRequestBuilders.get("/api/books"))
       .andExpect(MockMvcResultMatchers.status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
       .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasSize(2)))
       .andDo(MockMvcResultHandlers.print());
   }
   ```
   * The `MockMvc` is a mocked web environment setup by Spring and Spring Boot to make it easier to test web related parts. You can fire off requests to your controller and verify the result.
   * With the `@MockitoBean` (formerly `@MockBean`) we can automatically create a mock of a service that will be managed by Spring. In the `shouldRenderListPageWithContent` test we actually use it to have some content to be rendered. 
   * The `andExpect` are assertions on the result and can be varied
   * The `andDo` can contain additional actions, here we use it to print the request and response information to the console, including the rendered HTML.
5. Run the tests and check the output
6. Try to add 2 tests for the `/api/books/{isbn}` url, remember to register some behavior on the `BookService.findByIsbn` method if you really want to get a result. The default is to return an empty `Optional`. 

Security
-
The application at the moment is open for everyone to access, ideally we would limit access to only those wo should have access. Lets add Spring Security into the mix. 

1. Add the `spring-boot-starter-security` dependency
2. Re-run the `LibraryApplication` and notice a message like the following
```
2025-06-07T14:32:29.452+02:00  WARN 27995 --- [           main] .s.s.UserDetailsServiceAutoConfiguration : 

Using generated security password: 41142a31-fc33-4b87-bbcb-4b40f613c267

This generated password is for development use only. Your security configuration must be updated before running your application in production.

2025-06-07T14:32:29.455+02:00  INFO 27995 --- [           main] r$InitializeUserDetailsManagerConfigurer : Global AuthenticationManager configured with UserDetailsService bean with name inMemoryUserDetailsManager
```
This contains the password you can use to login with a user named `user`.
3. When trying to access `http://localhost:8080/api/books` you will now get a 401 response (Unauthorized)
4. To login we would need to provide the username and password. For a REST api we can use HTTP basic authentication (although in real world applications you probably use a JWT or some other authenticationt token)
   ```shell
   http -a user:<generated-pwd> :8080/api/books/
   ```
   
5. Relying on a generated password is probably not such a good idea, lets create an explicit security configuration. Create a `LibrarySecurityConfig` class and annotate it with `@Configuration` and `@EnableWebSecurity`.
6. Add the following methods
```java
  @Bean
  public UserDetailsService userDetailsService() {
    var user1 = User.withDefaultPasswordEncoder().username("user").roles("USER").password("secret").build();
    var user2 = User.withDefaultPasswordEncoder().username("admin").roles("ADMIN", "USER").password("top-secret").build();
    return new InMemoryUserDetailsManager(user1, user2);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }
```
   * The `userDetailsService` configures the repository to use to lookup the users. Here we use an in-memory one but one for JDBC is also provided.
   * The `securityFilterChain` creates the Servlet API based Filter Chain to secure the application. the `loginForm` enables login through a login form and by default one will be generated for you. The `logout` enables the possibility to logout again and finally the `authorizeHttpRequests` secures our application, it allows to write complex security rules if needed using SpEL. 
7. Now re-run the `LibraryApplication` and try to login with one of the users. 
8. Finally try to re-run the tests they should now fail with a 401 exception meaning that you don't have access.
9. Add the `spring-security-test` dependency as a `test` scoped dependency (and refresh the project)
10. Put the `@WithMockUser` annotation on the `LibraryApiControllerTest` and re-run the tests, they should now succeed. 
    * The `@WithMockUser` annotatation will register a user for use with Spring Security. It will act as if you just authenticated through the login page and thus you are now able to access the pages. 



   