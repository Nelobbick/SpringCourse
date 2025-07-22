package Nelobbick.repo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import Nelobbick.models.Book;

import java.util.List;
import java.util.Optional;

public interface BooksRepository extends JpaRepository<Book,Integer> {

    List<Book> findBookByPersonId(int id);

    Optional<Book> findByTitleStartingWith(@NotEmpty(message = "Название книги не должно быть пустым") @Size(min = 2, max = 100, message = "Название книги должно быть от 2 до 100 символов длиной") String title);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.person WHERE b.person.id = :personId")
    List<Book> findBooksByPersonIdWithPerson(@Param("personId") int personId);

    List<Book> findBookById(int id);
}
