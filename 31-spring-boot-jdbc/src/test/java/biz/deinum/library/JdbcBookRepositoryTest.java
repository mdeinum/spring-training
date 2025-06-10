package biz.deinum.library;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class JdbcBookRepositoryTest {

  @Autowired
  private DataSource dataSource;

  private JdbcBookRepository repository;

  @BeforeEach
  public void setUp() {
    this.repository = new JdbcBookRepository(dataSource);
    if (this.repository.findAll().isEmpty()) {
      BookGenerator.all().forEach(repository::save);
    }
  }

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


}