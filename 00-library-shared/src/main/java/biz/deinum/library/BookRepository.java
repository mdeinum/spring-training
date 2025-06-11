package biz.deinum.library;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;

public interface BookRepository {

  List<Book> findAll();
  @Query
  Optional<Book> findByIsbn(String isbn);
  Book save(Book book);
}
