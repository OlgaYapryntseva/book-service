package telran.java48.book.dao;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.java48.book.model.Book;

public interface BookRepositoty extends JpaRepository<Book, String> {
     
	Stream<Book> findByAuthorsName(String authorName);

}
