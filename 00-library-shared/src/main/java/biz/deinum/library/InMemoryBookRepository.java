package biz.deinum.library;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

class InMemoryBookRepository implements BookRepository {

  private final Map<String, Book> books = new ConcurrentHashMap<>();

  @Override
  public List<Book> findAll() {
    return List.copyOf(books.values());
  }

  @Override
  public Optional<Book> findByIsbn(String isbn) {
    return Optional.ofNullable(books.get(isbn));
  }

  @Override
  public Book save(Book book) {
    books.put(book.isbn(), book);
    return book;
  }
}
