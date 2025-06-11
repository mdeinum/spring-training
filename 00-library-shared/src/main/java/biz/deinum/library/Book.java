package biz.deinum.library;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import org.springframework.data.annotation.Id;

public record Book(@Id Long id, @NotBlank @Size(min=9, max=13) String isbn,
                   @NotBlank String title, @NotEmpty String... authors) {

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
