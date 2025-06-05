package biz.deinum.library;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@SpringBootApplication
public class LibraryApplication {

  private static final Logger log = LoggerFactory.getLogger(LibraryApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(LibraryApplication.class, args);
  }

  @Bean
  public ApplicationRunner initBooks(BookService books) {
    return args -> {
      if (books.findAll().isEmpty()) {
        BookGenerator.all().forEach(books::create);
      }

      for (var book : books.findAll()) {
        log.info("Book: {}", book);
      }
    };
  }


}