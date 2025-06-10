package biz.deinum.library;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class JdbcBookRepositoryTest {

  @Autowired
  private JdbcBookRepository repository;

  @BeforeEach
  public void setUp() {
    if (this.repository.findAll().isEmpty()) {
      repository.saveAll(BookGenerator.all());
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
    ((BookRepository) this.repository).save(newBook);
    var cntAfter = repository.findAll().size();
    Assertions.assertThat(cntAfter).isEqualTo(cntBefore + 1);
  }


}