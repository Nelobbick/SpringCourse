package Nelobbick.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Nelobbick.models.Book;
import Nelobbick.models.Person;
import Nelobbick.repo.BooksRepository;
import Nelobbick.repo.PeopleRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person undatedPerson) {
        undatedPerson.setId(id);
        peopleRepository.save(undatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> getPersonByFullName(String fullname) {
        return peopleRepository.findPersonByFullName(fullname);
    }
    public List<Book> getBooksByPersonId(int id) {
        List<Book> books = booksRepository.findBookByPersonId(id);
        books.forEach(book -> {
            if (book.getDate_of_collection() != null) {
                long days = ChronoUnit.DAYS.between(
                        book.getDate_of_collection().toLocalDate(),
                        LocalDate.now()
                );
                book.setExpired(days > 10); // 10 дней срок возврата
            }
        });
        return books;
    }



}
