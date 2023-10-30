package telran.java48.book.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookDto {
	String isbn;
    String title;
    List<AuthorDto> authors;
    String publisher;
}
