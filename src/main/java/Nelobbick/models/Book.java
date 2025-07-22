package Nelobbick.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;


@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "Название книги не должно быть пустым")
    @Size(min = 2, max = 100, message = "Название книги должно быть от 2 до 100 символов длиной")
    private String title;

    @Column(name = "author")
    @NotEmpty(message = "Автор не должен быть пустым")
    @Size(min = 2, max = 100, message = "Имя автора должно быть от 2 до 100 символов длиной")
    private String author;

    @Column(name = "year")
    @Min(value = 1500, message = "Год должен быть больше, чем 1500")
    private int year;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "date_of_collection")
    private LocalDateTime date_of_collection;

    @Column(name = "isexpired")
    private boolean isExpired;

    public Book() {

    }

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }
    public boolean getIsExpired(){
        return isExpired;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDateTime getDate_of_collection() {
        return date_of_collection;
    }

    public void setDate_of_collection(LocalDateTime date_of_collection) {
        this.date_of_collection = date_of_collection;
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
