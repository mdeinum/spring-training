package biz.deinum.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LibraryConfig {

  private final Logger log = LoggerFactory.getLogger(getClass());

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
