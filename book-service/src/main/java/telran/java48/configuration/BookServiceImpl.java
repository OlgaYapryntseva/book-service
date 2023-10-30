package telran.java48.configuration;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import telran.java48.book.dao.AuthorRepository;
import telran.java48.book.dao.BookRepositoty;
import telran.java48.book.dao.PublisherRepository;
import telran.java48.book.dto.AuthorDto;
import telran.java48.book.dto.BookDto;
import telran.java48.book.exeption.EntityNotFoundException;
import telran.java48.book.model.Author;
import telran.java48.book.model.Book;
import telran.java48.book.model.Publisher;
import telran.java48.book.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	final BookRepositoty bookRepositoty;
	final AuthorRepository authorRepository;
	final PublisherRepository publisherRepository;
	final ModelMapper modelMapper;

	@Override
	@Transactional
	public boolean addBook(BookDto bookDto) {
		if (bookRepositoty.existsById(bookDto.getIsbn())) {
			return false;
		}
		Publisher publisher = publisherRepository.findById(bookDto.getPublisher())
				.orElse(publisherRepository.save(new Publisher(bookDto.getPublisher())));

		Set<Author> authors = bookDto.getAuthors().stream()
				.map(a -> authorRepository.findById(a.getName())
						.orElse(authorRepository.save(new Author(a.getName(), a.getBirthDate()))))
				.collect(Collectors.toSet());
		Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);
		bookRepositoty.save(book);
		return true;
	}

	@Override
	public BookDto findBookByIsbn(String isbn) {
		Book book = bookRepositoty.findById(isbn).orElseThrow(EntityNotFoundException::new);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	public BookDto remove(String isbn) {
		Book book = bookRepositoty.findById(isbn).orElseThrow(EntityNotFoundException::new);
		bookRepositoty.deleteById(isbn);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto updateBook(String isbn, String title) {
		Book book = bookRepositoty.findById(isbn).orElseThrow(EntityNotFoundException::new);
		book.setTitle(title);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<BookDto> findBooksByAuthor(String authorName) {
		return bookRepositoty.findByAuthorsName(authorName)
				.map(b -> modelMapper.map(b, BookDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<BookDto> findBooksByPublisher(String publisherName) {
		return bookRepositoty.findAll().stream()
				.filter(b -> b.getPublisher().getPublisherName().equals(publisherName))
				.map(b -> modelMapper.map(b, BookDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<AuthorDto> findBooksAuthor(String isbn) {
		return bookRepositoty.findById(isbn).orElseThrow(EntityNotFoundException::new).getAuthors().stream()
				.map(a -> modelMapper.map(a, AuthorDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Iterable<String> findPublishersByAuthor(String authorName) {
		return bookRepositoty.findByAuthorsName(authorName)
				.map(p -> p.getPublisher().getPublisherName())
				.collect(Collectors.toSet());
	}

	@Override
	@Transactional
	public AuthorDto removeAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(EntityNotFoundException::new);
		List<Book> books = bookRepositoty.findByAuthorsName(authorName).collect(Collectors.toList());
		books.stream().forEach(b -> {
		       if(b.getAuthors().size() > 1) {
		    	   b.getAuthors().remove(author);
		       } else {
		    	   bookRepositoty.delete(b);
		       }
		});
		authorRepository.delete(author);
		return modelMapper.map(author, AuthorDto.class);
	}
}
