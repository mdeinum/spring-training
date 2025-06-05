package biz.deinum.library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
@Primary
class JdbcBookRepository implements BookRepository {

  private static final String FIND_ALL_SQL = "SELECT isbn, title, authors FROM book";
  private static final String SAVE_SQL = "INSERT INTO book (isbn, title, authors) VALUES (?, ?, ?)";
  private static final String FIND_BY_ISBN_SQL = "SELECT isbn, title, authors FROM book WHERE isbn = ?";

  private final DataSource dataSource;

  JdbcBookRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public List<Book> findAll() {

    try (var conn = dataSource.getConnection();
         var ps = conn.prepareStatement(FIND_ALL_SQL);
         var rs = ps.executeQuery()) {
      var books = new ArrayList<Book>();
      while (rs.next()) {
        books.add(mapToBook(rs));
      }
      return books;
    } catch (SQLException ex) {
      throw new DataRetrievalFailureException(ex.getMessage(), ex);
    }
  }

  @Override
  public Optional<Book> findByIsbn(String isbn) {
    try (var conn = dataSource.getConnection();
         var ps = conn.prepareStatement(FIND_BY_ISBN_SQL)) {
      ps.setString(1, isbn);

      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapToBook(rs));
        }
      }
    } catch (SQLException ex) {
      throw new DataRetrievalFailureException(ex.getMessage(), ex);
    }
    return Optional.empty();
  }

  @Override
  public Book save(Book book) {
    try (var conn = dataSource.getConnection();
         var ps = conn.prepareStatement(SAVE_SQL)) {
      ps.setString(1, book.isbn());
      ps.setString(2, book.title());
      ps.setObject(3, book.authors());
      ps.executeUpdate();
    } catch (SQLException ex) {
      throw new InvalidDataAccessApiUsageException(ex.getMessage(), ex);
    }
    return book;
  }

  private Book mapToBook(ResultSet rs) throws SQLException {
    var isbn = rs.getString("isbn");
    var title = rs.getString("title");
    var authors = rs.getObject("authors", String[].class);
    return new Book(isbn, title, authors);
  }
}
