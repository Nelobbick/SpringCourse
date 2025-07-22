package Nelobbick.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Nelobbick.models.Book;
import Nelobbick.models.Person;
import Nelobbick.repo.BooksRepository;
import Nelobbick.repo.PeopleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    private static final int EXPIRATION_DAYS = 10;

    @Autowired
    public BooksService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Book> getAllBooks() {
        return booksRepository.findAll();
    }

    public List<Book> getSortedBooks() {
        return booksRepository.findAll(Sort.by("year").ascending());
    }

    public Page<Book> getBooks(int page, int size, boolean sort) {
        Pageable pageable = sort
                ? PageRequest.of(page, size, Sort.by("year").ascending())
                : PageRequest.of(page, size);
        return booksRepository.findAll(pageable);
    }


    public Optional<Book> findBookByTitle(String title) {
        return booksRepository.findByTitleStartingWith(title);
    }


    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        if (foundBook.isPresent()) {
            Book book = foundBook.get();
            isExpired(book);
            return book;
        }
        return null;
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public Optional<Person> getBookOwner(int id) {
        return booksRepository.findById(id).map(Book::getPerson);
    }
    @Transactional
    public void isExpired(Book book) {
        if (book.getPerson() != null && book.getDate_of_collection() != null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime takenDate = book.getDate_of_collection();

            // Вычисляем разницу в днях между текущей датой и датой взятия
            long diffInDays = ChronoUnit.DAYS.between(takenDate, now);

            book.setExpired(diffInDays > EXPIRATION_DAYS);
        } else {
            book.setExpired(false); // Сбрасываем статус, если книга свободна
        }
    }
    public List<Book> getBooksByPersonId(int id) {
        List<Book> books = booksRepository.findBookByPersonId(id);
        books.forEach(book -> {
            if (book.getPerson() != null) {
                // Проверка просрочки
                long days = ChronoUnit.DAYS.between(
                        book.getDate_of_collection().toLocalDate(),
                        LocalDate.now()
                );
                book.setExpired(days > 10);
            }
        });
        return books;
    }

    @Transactional
    public void release(int id) {
        booksRepository.findById(id).ifPresent(book -> {
            book.setPerson(null);
            book.setDate_of_collection(null);
            booksRepository.save(book);
        });
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        booksRepository.findById(id).ifPresent(book -> {
            book.setPerson(selectedPerson);
            book.setDate_of_collection(LocalDateTime.now());
            booksRepository.save(book);
        });
    }


}
