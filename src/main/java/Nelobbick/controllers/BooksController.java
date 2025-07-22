package Nelobbick.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import Nelobbick.models.Book;
import Nelobbick.models.Person;

import jakarta.validation.Valid;
import Nelobbick.services.BooksService;
import Nelobbick.services.PeopleService;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/books")
public class BooksController {

    private final PeopleService peopleService;
    private final BooksService booksService;


    @Autowired
    public BooksController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping
    public String index(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "false") Boolean sort,
            Model model) {

        boolean paginationEnabled = true;
        boolean sortingEnabled = Boolean.TRUE.equals(sort);

        if (size > 0) {
            Page<Book> pageResult = booksService.getBooks(page, size, sortingEnabled);

            pageResult.getContent().forEach(booksService::isExpired);

            model.addAttribute("books", pageResult.getContent());
            model.addAttribute("totalPages", pageResult.getTotalPages());
        } else {
            List<Book> books = sortingEnabled
                    ? booksService.getSortedBooks()
                    : booksService.getAllBooks();

            books.forEach(booksService::isExpired);
            model.addAttribute("books", books);
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortingEnabled", sortingEnabled);
        model.addAttribute("paginationEnabled", paginationEnabled);


        return "books/index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "search", required = false) String search,
                         Model model) {
        Optional<Book> findBookByTitle;
        if (search != null && !search.isEmpty()) {
            findBookByTitle = booksService.findBookByTitle(search);
            if (findBookByTitle.isPresent()) {
                model.addAttribute("findBookByTitle", findBookByTitle.get());
            } else {
                model.addAttribute("errorMessage", "Книга с данным названием не найдена.");
            }
        } else {
            model.addAttribute("infoMessage", "Введите название книги для поиска");
        }

        return "books/search";

    }


    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(id));

        Optional<Person> bookOwner = booksService.getBookOwner(id);

        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book Book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book Book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        booksService.save(Book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        booksService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        booksService.assign(id, selectedPerson);

        return "redirect:/books/" + id;
    }
}
