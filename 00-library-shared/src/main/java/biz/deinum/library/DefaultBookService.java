package biz.deinum.library;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class DefaultBookService implements BookService {

  private final BookRepository books;

  DefaultBookService(BookRepository books) {
    this.books = books;
  }

  @Override
  public List<Book> findAll() {
    return books.findAll();
  }

  @Override
  public Book create(Book book) {
    return books.save(book);
  }

  @Override
  public Optional<Book> findByIsbn(String isbn) {
    return books.findByIsbn(isbn);
  }
}