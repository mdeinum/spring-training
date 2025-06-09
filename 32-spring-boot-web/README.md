Spring Boot - Web
=

In this excercise we will be using Spring Boot to build a web front-end for our library. We will be using Thymeleaf for the web pages and annotated controllers to handle the retrieval of the data and selection of the view to render. 

Getting Started
-
1. Add the following dependencies to the `pom.xml`
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

This will add the needed dependencies, Tomcat, Spring Web MVC and Thymeleaf.

2. Next add a class to the project `biz.deinum.library.web.LibraryController`
   * Add the `org.springframework.stereotype.Controller` to the class
   * Create a `private` `final` field to hold the `BookService` and create the appropriate constructor for it

3. Next we need a method to react to a GET request on `/books` which prepares the model with a list of books so that we can render them on the webpage named `library/list`.
   * Add the following method to the `LibraryController`
      ```java
      @GetMapping()
      public String list(Model model) {
        model.addAttribute("books", books.findAll());
        return "library/list";
      }
     ```
4. Now you can try and run the `LibraryApplication` when started navigate to `http://localhost:8080/books` and it should render a table with titles and ISBN numbers. 
   * Take a look at the `src/main/resources/templates/library/list.html` this contains the template to render the list page.
   * Try to click a link (this should yield an error as we have no controller method for it.)

Using path variables
-
1. Now lets add a method to the `LibraryController` to find the book and display a form with the necessary information on it. 
    * Add the following method to the `LibraryController`
   ```java
   @GetMapping("/{isbn}")
   public String show(@PathVariable("isbn") String isbn, Model model) {
     var book = books.findByIsbn(isbn).ifPresent((b) -> model.addAttribute("book", b));
     return "library/book";
   }
   ```
   * The `{isbn}` in the url is a path-variable and it will be bound to the `@PathVariable` annotated method argument, we can use it to retrieve the book, when found we add it to the model and select the `library/book` page to display the information.
   * Re-run the `LibraryApplication` and try to click a link, it should now display the information.
   * Now try to modify to URL manually with an ISBN that doesn't exist
   * Finally take a look at the `library/book.html` page.

Accepting data
-
To modify or create a new book we need to have some additional methods on the controller. 1 method for preparing the model and another for persisting the incoming data. 

1. Add a `@GetMapping("/")` on the controller that will put an empty `Book` into the `Model` and renders the `library/book` page.
   ```java
   @GetMapping("/")
   public String newBook(Model model) {
      model.addAttribute("book", new Book(null, null));
      return "library/book";
   }
   ```
2. Next we need a method to receive a `Book` and persist it. For this add a method annotated with `@PostMapping`. The method takes a `Book` as the parameter and that parameter is annotated with `@ModelAttribute`. After persisting the new (or updated) book it will redirect to the `/books/{isbn}` url.
   ```java
   @PostMapping
   public String create(@ModelAttribute Book book) {
      var dbBook = books.create(book);
      return "redirect:/books/" + dbBook.isbn();
   }   
   ```
3. Re-run the application and see if you can add a new book or edit an existing one.

Exception Handling
-
1. Lets improve on the exception handling and lets introduce an `EntityNotFoundException` and handling for it when we don't find the book. 
    * Modify the `show` method to throw the `EntityNotFoundException` when the book isn't found. 
   ```java
   @GetMapping("/{isbn}")
   public String show(@PathVariable("isbn") String isbn, Model model) {
     var book = books.findByIsbn(isbn).orElseThrow(EntityNotFoundException::new);
     model.addAttribute("book", book);
     return "library/book";
   }
   ```
   * Add the the following method to handle the `EntityNotFoundException`, this method will be invoked when this controller throws an `EntityNotFoundException` and will render the `error/404.html` page. 
   ```java
    @ExceptionHandler(EntityNotFoundException.class)
    public String notFoundException() {
      return "error/404";
    }
   ```
   * Re-run the application and again try to find a book that doesn't exist, that should now render the 404 page. 
7. Now lets improve even more, remove the `notFoundException` method and annotate the `EntityNotFoundException` with `@ResponseStatus(HttpStatus.NOT_FOUND)`, when having done this restart the application. It should still render the 404 when it cannot find the book. But now try to get to an non-existing page, it will also render the 404 page. 

Testing
-
Until now we relied only on running the application and we haven't written any test for our controller. Spring Boot has the `@WebMvcTest` that we can utilize to write a test for the controller and we can use `@MockitoBean` to create a mock for the dependencies to be injected. 

For testing we need the `spring-boot-starter-test` dependency which would pull in the necessary testing libraries. However that is already defined as a dependency in the **parent project** so no need to add that in here. 

1. Add a class `biz.deinum.library.web.LibraryControllerTest` in `src/test/java` and put an `@WebMvcTest` annotation on it (`@WebMvcTest(LibraryController.class)` to specify which controller to test),
2. Add a field of type `MockMvc` to the test and put `@Autowired` on top of it
3. Add a field of type `BookService` to the test and put `@MockitoBean` on top it.
4. Now add the following test methods.
   ```java
   @Test
   void shouldRenderListPageEmpty() throws Exception {
     mockMvc
     .perform(MockMvcRequestBuilders.get("/books"))
     .andExpect(MockMvcResultMatchers.status().isOk())
     .andExpect(MockMvcResultMatchers.view().name("library/list"))
     .andExpect(MockMvcResultMatchers.model().attributeExists("books"))
     .andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.empty()))
     .andDo(MockMvcResultHandlers.print());
   }
   
   @Test
   void shouldRenderListPageWithContent() throws Exception {
     Mockito.when(bookService.findAll()).thenReturn(List.of(
       new Book("123456789", "Title 1", "Author 1"),
       new Book("987654321", "Title 2", "Author 2")));
     mockMvc
       .perform(MockMvcRequestBuilders.get("/books"))
       .andExpect(MockMvcResultMatchers.status().isOk())
       .andExpect(MockMvcResultMatchers.view().name("library/list"))
       .andExpect(MockMvcResultMatchers.model().attributeExists("books"))
       .andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.hasSize(2)))
       .andDo(MockMvcResultHandlers.print());
     }
   ```
   * The `MockMvc` is a mocked web environment setup by Spring and Spring Boot to make it easier to test web related parts. You can fire off requests to your controller and verify the result.
   * With the `@MockitoBean` (formerly `@MockBean`) we can automatically create a mock of a service that will be managed by Spring. In the `shouldRenderListPageWithContent` test we actually use it to have some content to be rendered. 
   * The `andExpect` are assertions on the result and can be varied
   * The `andDo` can contain additional actions, here we use it to print the request and response information to the console, including the rendered HTML.
5. Run the tests and check the output
6. Try to add 2 tests for the `/books/{isbn}` url, remember to register some behavior on the `BookService.findByIsbn` method if you really want to get a result. The default is to return an empty `Optional`. 

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
3. When trying to access `http://localhost:8080/books` you will now be prompted by the default Spring Security login page.
4. Login with the `user` and the generated password, after doing so you should be greeted with the list of books again
5. Relying on a generated password is probably not such a good idea, lets create an explicit security configuration. Create a `LibrarySecurityConfig` class and annotate it with `@Configuration` and `@EnableWebSecurity`.
6. Add the following methods
```java
  @Bean
  public UserDetailsService userDetailsService() {
    var user1 = User.withDefaultPasswordEncoder().username("user").password("secret").build();
    var user2 = User.withDefaultPasswordEncoder().username("admin").password("top-secret").build();
    return new InMemoryUserDetailsManager(user1, user2);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .logout(Customizer.withDefaults())
        .formLogin(Customizer.withDefaults());
    return http.build();
  }
```
   * The `userDetailsService` configures the repository to use to lookup the users. Here we use an in-memory one but one for JDBC is also provided.
   * The `securityFilterChain` creates the Servlet API based Filter Chain to secure the application. the `loginForm` enables login through a login form and by default one will be generated for you. The `logout` enables the possibility to logout again and finally the `authorizeHttpRequests` secures our application, it allows to write complex security rules if needed using SpEL. 
7. Now re-run the `LibraryApplication` and try to login with one of the users. 
8. Finally try to re-run the tests they should now fail with a 401 exception meaning that you don't have access.
9. Add the `spring-security-test` dependency as a `test` scoped dependency (and refresh the project)
10. Put the `@WithMockUser` annotation on the `LibraryControllerTest` and re-run the tests, they should now succeed. 
    * The `@WithMockUser` annotatation will register a user for use with Spring Security. It will act as if you just authenticated through the login page and thus you are now able to access the pages. 


   