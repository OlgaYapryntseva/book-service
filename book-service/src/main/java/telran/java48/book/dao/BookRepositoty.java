package telran.java48.book.dao;

import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import telran.java48.book.model.Book;
import telran.java48.book.model.Publisher;


public interface BookRepositoty extends JpaRepository<Book, String> {
     
	Stream<Book> findByAuthorsName(String authorName);
	
	Stream<Book> findByPublisher(Publisher publisherName);

}
