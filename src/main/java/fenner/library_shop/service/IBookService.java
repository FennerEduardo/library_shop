package fenner.library_shop.service;

import fenner.library_shop.model.Book;

import java.util.List;

public interface IBookService {
    public List<Book> getAll();

    public Book getById(Integer id);

    public void store(Book book);

    public void delete(Book book);
}
