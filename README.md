# Library Book Manager

A beginner-friendly desktop application for maintaining a small library's book records, built with Java Swing.

## Features
- Add books with a title and author
- View all books and their availability
- Search by book title or author
- Update book details
- Issue and return books
- Delete books with confirmation
- Save records automatically in `books.txt`

## Project structure

```text
LibraryBookManager/
├── src/
│   ├── Main.java             # Starts the application
│   ├── Book.java             # Represents one book
│   ├── LibraryManager.java   # Handles book operations and file storage
│   └── LibraryFrame.java     # Builds and controls the Swing interface
└── README.md
```

## Java concepts used
- Classes, objects and encapsulation
- ArrayList / List collections
- Constructors, getters and setters
- Methods, loops and conditional statements
- File handling with BufferedReader and BufferedWriter
- Exception handling
- Java Swing components and event handling

## Requirements
- JDK 17 or later

## Compile and run
Open a terminal in the `LibraryBookManager` folder and run:

```bash
javac -d out src/*.java
java -cp out Main
```

The application creates `books.txt` in the current working directory when records are saved. Keep this file if you want to retain your book records between runs.

## Data format
Each line in `books.txt` stores one book as:

```text
Title|Author|Availability
```

The vertical bar (`|`) is used as a separator, so it is not accepted inside a title or author name.
