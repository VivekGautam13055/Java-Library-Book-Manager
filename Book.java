public class Book {
    private String title;
    private String author;
    private boolean available;

    public Book(String title, String author) {
        this(title, author, true);
    }

    public Book(String title, String author, boolean available) {
        setTitle(title);
        setAuthor(author);
        this.available = available;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Book title cannot be empty.");
        }
        if (title.contains("|")) {
            throw new IllegalArgumentException("Book title cannot contain |.");
        }
        this.title = title.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author name cannot be empty.");
        }
        if (author.contains("|")) {
            throw new IllegalArgumentException("Author name cannot contain |.");
        }
        this.author = author.trim();
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        String status = available ? "Available" : "Issued";
        return title + " | " + author + " | " + status;
    }
}
