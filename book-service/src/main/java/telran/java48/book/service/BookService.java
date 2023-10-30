package telran.java48.book.service;

import telran.java48.book.dto.AuthorDto;
import telran.java48.book.dto.BookDto;

public interface BookService {
	boolean addBook(BookDto bookDto);
	
	BookDto findBookByIsbn(String isbn);
	
	BookDto remove(String isbn);
	
	BookDto updateBook(String isbn, String title);
	
	Iterable<BookDto> findBooksByAuthor(String author);
	
	Iterable<BookDto> findBooksByPublisher(String publisherName);
	
    Iterable<AuthorDto> findBooksAuthor(String isbn);
    
    Iterable<String> findPublishersByAuthor(String AuthorName);
    
    AuthorDto removeAuthor(String AuthorName);
	
	
	
}
