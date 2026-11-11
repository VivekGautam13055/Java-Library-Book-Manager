import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class LibraryFrame extends JFrame {
    private final LibraryManager library = new LibraryManager();
    private final DefaultListModel<Book> listModel = new DefaultListModel<>();
    private final JList<Book> bookList = new JList<>(listModel);
    private final JTextField searchField = new JTextField(18);

    public LibraryFrame() {
        setTitle("Library Book Manager");
        setSize(760, 500);
        setMinimumSize(new Dimension(620, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        JLabel heading = new JLabel("Library Book Manager", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setBorder(new EmptyBorder(18, 10, 8, 10));
        add(heading, BorderLayout.NORTH);

        bookList.setFont(new Font("Arial", Font.PLAIN, 15));
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookList.setFixedCellHeight(30);
        add(new JScrollPane(bookList), BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridLayout(5, 1, 8, 8));
        actions.setBorder(new EmptyBorder(8, 8, 8, 15));
        JButton addButton = new JButton("Add Book");
        JButton updateButton = new JButton("Update Book");
        JButton issueButton = new JButton("Issue Book");
        JButton returnButton = new JButton("Return Book");
        JButton deleteButton = new JButton("Delete Book");
        actions.add(addButton);
        actions.add(updateButton);
        actions.add(issueButton);
        actions.add(returnButton);
        actions.add(deleteButton);
        add(actions, BorderLayout.EAST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBorder(new EmptyBorder(0, 8, 12, 8));
        searchPanel.add(new JLabel("Search title or author:"));
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);
        add(searchPanel, BorderLayout.SOUTH);

        addButton.addActionListener(event -> addBook());
        updateButton.addActionListener(event -> updateBook());
        issueButton.addActionListener(event -> changeBookStatus(true));
        returnButton.addActionListener(event -> changeBookStatus(false));
        deleteButton.addActionListener(event -> deleteBook());
        searchButton.addActionListener(event -> searchBooks());
        showAllButton.addActionListener(event -> refreshList(library.getAllBooks()));

        refreshList(library.getAllBooks());
    }

    private void addBook() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        Object[] fields = {"Book title:", titleField, "Author:", authorField};
        int result = JOptionPane.showConfirmDialog(this, fields,
                "Add a Book", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            library.addBook(titleField.getText(), authorField.getText());
            refreshList(library.getAllBooks());
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        } catch (IOException exception) {
            showError("Could not save the book: " + exception.getMessage());
        }
    }

    private void updateBook() {
        Book selectedBook = bookList.getSelectedValue();
        if (selectedBook == null) {
            showError("Please select a book first.");
            return;
        }

        JTextField titleField = new JTextField(selectedBook.getTitle());
        JTextField authorField = new JTextField(selectedBook.getAuthor());
        Object[] fields = {"Book title:", titleField, "Author:", authorField};
        int result = JOptionPane.showConfirmDialog(this, fields,
                "Update Book", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            library.updateBook(selectedBook, titleField.getText(), authorField.getText());
            refreshList(library.getAllBooks());
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        } catch (IOException exception) {
            showError("Could not update the book: " + exception.getMessage());
        }
    }

    private void changeBookStatus(boolean issue) {
        Book selectedBook = bookList.getSelectedValue();
        if (selectedBook == null) {
            showError("Please select a book first.");
            return;
        }

        try {
            if (issue) {
                library.issueBook(selectedBook);
            } else {
                library.returnBook(selectedBook);
            }
            refreshList(library.getAllBooks());
        } catch (IllegalStateException | IllegalArgumentException exception) {
            showError(exception.getMessage());
        } catch (IOException exception) {
            showError("Could not save the change: " + exception.getMessage());
        }
    }

    private void deleteBook() {
        Book selectedBook = bookList.getSelectedValue();
        if (selectedBook == null) {
            showError("Please select a book first.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Delete '" + selectedBook.getTitle() + "'?",
                "Confirm deletion", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                library.deleteBook(selectedBook);
                refreshList(library.getAllBooks());
            } catch (IOException exception) {
                showError("Could not delete the book: " + exception.getMessage());
            }
        }
    }

    private void searchBooks() {
        List<Book> matches = library.searchBooks(searchField.getText());
        refreshList(matches);
        if (matches.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No matching books found.");
        }
    }

    private void refreshList(List<Book> books) {
        listModel.clear();
        for (Book book : books) {
            listModel.addElement(book);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Library Book Manager", JOptionPane.WARNING_MESSAGE);
    }
}
