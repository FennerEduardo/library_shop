package fenner.library_shop.view;

import fenner.library_shop.model.Book;
import fenner.library_shop.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class BookForm extends JFrame {
    BookService bookService;

    private JPanel panel;
    private JTable table;
    private JTextField id;
    private JTextField name;
    private JTextField author;
    private JTextField price;
    private JTextField stock;
    private  JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private DefaultTableModel tableModelBooks;

    @Autowired
    public BookForm(BookService bookService){
        this.bookService = bookService;
        startForm();
        addButton.addActionListener(e -> addBook());
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                loadSelectedBook();
            }
        });
        modifyButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());
    }

    private void startForm(){
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900,700);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth() / 2);
        int y = (screenSize.height - getHeight() / 2);
        setLocation(x, y);
    }
    private void showMessage(String message){
        JOptionPane.showMessageDialog(this, message);
    }

    private void cleanForm(){
        name.setText("");
        author.setText("");
        price.setText("");
        stock.setText("");
    }

    private void createUIComponents() {
        id = new JTextField("");
        id.setVisible(false);
        this.tableModelBooks = new DefaultTableModel(0, 5){
            @Override
            public boolean isCellEditable(int row, int column){return false;}
        };

        String[] headings = {"Id", "Libro", "Autor", "Precio", "Existencias"};
        this.tableModelBooks.setColumnIdentifiers(headings);

        this.table = new JTable(tableModelBooks);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listBooks();
    }

    private void listBooks(){
        tableModelBooks.setNumRows(0);
        var books = bookService.getAll();
        books.forEach((book) -> {
            Object[] rowBook = {
                    book.getId(),
                    book.getName(),
                    book.getAuthor(),
                    book.getPrice(),
                    book.getStock()
            };
            this.tableModelBooks.addRow(rowBook);
        });
    }

    private void addBook(){
        if(name.getText().equals("")){
            showMessage("The book name is required");
            name.requestFocusInWindow();
            return;
        }

        var nameBook = name.getText();
        var authorBook = author.getText();
        var priceBook = Double.parseDouble(price.getText());
        var stockBook = Integer.parseInt(stock.getText());
        var newBook = new Book(null, nameBook, authorBook,priceBook, stockBook);

        this.bookService.store(newBook);
        showMessage("New book added");
        cleanForm();
        listBooks();
    }

    private void loadSelectedBook(){
        var row = table.getSelectedRow();
        if(row != -1){
            String idRow = table.getModel().getValueAt(row, 0).toString();
            id.setText(idRow);
            String nameRow = table.getModel().getValueAt(row, 1).toString();
            name.setText(nameRow);
            String authorRow = table.getModel().getValueAt(row, 2).toString();
            author.setText(authorRow);
            String priceRow = table.getModel().getValueAt(row, 3).toString();
            price.setText(priceRow);
            String stockRow = table.getModel().getValueAt(row, 4).toString();
            stock.setText(stockRow);
        }
    }

    private void updateBook(){
        if(this.id.getText().equals("")){
            showMessage("You should select at least a book");
        }else{
            if(name.getText().equals("")){
                showMessage("The book name is required");
                name.requestFocusInWindow();
                return;
            }
            int idBook = Integer.parseInt(id.getText());
            var nameBook = name.getText();
            var authorBook = author.getText();
            var priceBook = Double.parseDouble(price.getText());
            var stockBook = Integer.parseInt(stock.getText());
            var newBook = new Book(idBook, nameBook, authorBook,priceBook, stockBook);

            this.bookService.store(newBook);
            showMessage("Book updated successfully");
            cleanForm();
            listBooks();
        }
    }
    private void deleteBook(){
        var row = table.getSelectedRow();
        if(row != -1){
            String idRow =
                    table.getModel().getValueAt(row, 0).toString();
            var book = new Book();
            book.setId(Integer.parseInt(idRow));
            bookService.delete(book);
            showMessage("Book " + idRow + " deleted.");
            cleanForm();
            listBooks();
        }
        else{
            showMessage("Select at least a book to delete");
        }
    }
}
