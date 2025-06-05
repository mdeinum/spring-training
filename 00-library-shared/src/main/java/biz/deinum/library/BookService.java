package biz.deinum.library;


import java.util.List;
import java.util.Optional;

public interface BookService {

  List<Book> findAll();
  Book create(Book book);
  Optional<Book> findByIsbn(String isbn);
}
