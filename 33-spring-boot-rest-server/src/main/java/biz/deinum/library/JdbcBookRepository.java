package biz.deinum.library;

import org.springframework.data.repository.ListCrudRepository;

interface JdbcBookRepository extends ListCrudRepository<Book, Long>, BookRepository { }
