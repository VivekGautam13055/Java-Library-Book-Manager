import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private final List<Book> books = new ArrayList<>();
    private final Path dataFile;

    public LibraryManager() {
        this(Path.of("books.txt"));
    }

    public LibraryManager(Path dataFile) {
        this.dataFile = dataFile;
        loadBooks();
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public void addBook(String title, String author) throws IOException {
        books.add(new Book(title, author));
        saveBooks();
    }

    public void updateBook(Book book, String newTitle, String newAuthor) throws IOException {
        if (!books.contains(book)) {
            throw new IllegalArgumentException("The selected book no longer exists.");
        }
        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        saveBooks();
    }

    public void deleteBook(Book book) throws IOException {
        if (books.remove(book)) {
            saveBooks();
        }
    }

    public List<Book> searchBooks(String query) {
        List<Book> matches = new ArrayList<>();
        String searchText = query == null ? "" : query.trim().toLowerCase();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchText)
                    || book.getAuthor().toLowerCase().contains(searchText)) {
                matches.add(book);
            }
        }
        return matches;
    }

    public void issueBook(Book book) throws IOException {
        if (book == null || !books.contains(book)) {
            throw new IllegalArgumentException("Please select a valid book.");
        }
        if (!book.isAvailable()) {
            throw new IllegalStateException("This book is already issued.");
        }
        book.setAvailable(false);
        saveBooks();
    }

    public void returnBook(Book book) throws IOException {
        if (book == null || !books.contains(book)) {
            throw new IllegalArgumentException("Please select a valid book.");
        }
        if (book.isAvailable()) {
            throw new IllegalStateException("This book is already available.");
        }
        book.setAvailable(true);
        saveBooks();
    }

    private void saveBooks() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                dataFile, StandardCharsets.UTF_8)) {
            for (Book book : books) {
                writer.write(book.getTitle() + "|" + book.getAuthor()
                        + "|" + book.isAvailable());
                writer.newLine();
            }
        }
    }

    private void loadBooks() {
        if (!Files.exists(dataFile)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                dataFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split into exactly three fields: title, author and availability.
                String[] parts = line.split("\\|", 3);
                if (parts.length == 3) {
                    boolean available = Boolean.parseBoolean(parts[2]);
                    books.add(new Book(parts[0], parts[1], available));
                }
            }
        } catch (IOException | IllegalArgumentException exception) {
            System.err.println("Could not load book records: " + exception.getMessage());
        }
    }
}
