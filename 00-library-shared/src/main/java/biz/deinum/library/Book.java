package biz.deinum.library;

import java.util.Objects;
import java.util.Set;
import org.springframework.data.annotation.Id;

public record Book(@Id Long id, String isbn, String title, String... authors) {

  public Book(String isbn, String title, String... authors) {
    this(null, isbn, title, authors);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Book other) {
      return this.isbn != null && Objects.equals(this.isbn, other.isbn);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.isbn);
  }
}
