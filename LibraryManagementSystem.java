import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

interface LibraryOperations {
    void addBook(Book book) throws LibraryException;
    void removeBook(String bookId) throws LibraryException;
    Book searchBook(String query) throws LibraryException;
    void displayAllBooks();
}

interface UserActivities {
    void borrowBook(String bookId) throws LibraryException;
    void returnBook(String bookId) throws LibraryException;
    void displayBorrowedBooks();
}

class LibraryException extends Exception {
    public LibraryException(String message) {
        super(message);
    }
}

abstract class User implements Serializable {
    protected final String userId;
    protected String name;
    protected String email;
    protected List<String> borrowedBooks;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
    }

    public final String getUserId() {
        return userId;
    }

    public abstract void displayRole();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId.equals(user.userId) && email.equals(user.email);
    }

    @Override
    public String toString() {
        return "User ID: " + userId + ", Name: " + name + ", Email: " + email;
    }

    
}

class Book implements Serializable {
    private final String bookId;
    private String title;
    private String author;
    private boolean isAvailable;
    private int numberOfCopies;

    public Book(String bookId, String title, String author, int numberOfCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.numberOfCopies = numberOfCopies;
        this.isAvailable = numberOfCopies > 0;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
        this.isAvailable = numberOfCopies > 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return bookId.equals(book.bookId);
    }

    @Override
    public String toString() {
        return "Book ID: " + bookId +
               ", Title: " + title +
               ", Author: " + author +
               ", Available: " + isAvailable +
               ", Copies: " + numberOfCopies;
    }
}

class Student extends User implements UserActivities {
    private String department;

    public Student(String userId, String name, String email, String department) {
        super(userId, name, email);
        this.department = department;
    }

    @Override
    public void displayRole() {
        System.out.println("Student: " + name + " from " + department);
    }

    @Override
    public void borrowBook(String bookId) throws LibraryException {
        try {
            LibrarySystem library = LibrarySystem.getInstance();
            Book book = library.searchBook(bookId);

            if (!book.isAvailable()) {
                throw new LibraryException("Book " + book.getTitle() + " is not available");
            }

            if (borrowedBooks.size() >= 5) {
                throw new LibraryException("You have reached the maximum borrowing limit (5 books)");
            }

            borrowedBooks.add(bookId);
            book.setNumberOfCopies(book.getNumberOfCopies() - 1);
            System.out.println("Book " + book.getTitle() + " borrowed successfully");
        } catch (LibraryException e) {
            throw e;
        } catch (Exception e) {
            throw new LibraryException("Error borrowing book: " + e.getMessage());
        }
    }

    @Override
    public void returnBook(String bookId) throws LibraryException {
        try {
            LibrarySystem library = LibrarySystem.getInstance();
            Book book = library.searchBook(bookId);

            if (!borrowedBooks.contains(bookId)) {
                throw new LibraryException("You haven't borrowed this book");
            }

            borrowedBooks.remove(bookId);
            book.setNumberOfCopies(book.getNumberOfCopies() + 1);
            System.out.println("Book " + book.getTitle() + " returned successfully");
        } catch (LibraryException e) {
            throw e;
        } catch (Exception e) {
            throw new LibraryException("Error returning book: " + e.getMessage());
        }
    }

    @Override
    public void displayBorrowedBooks() {
        try {
            LibrarySystem library = LibrarySystem.getInstance();
            System.out.println("Books borrowed by " + name + ":");

            if (borrowedBooks.isEmpty()) {
                System.out.println("No books borrowed");
                return;
            }

            for (String bookId : borrowedBooks) {
                Book book = library.searchBook(bookId);
                System.out.println(book);
            }
        } catch (LibraryException e) {
            System.out.println("Error displaying borrowed books: " + e.getMessage());
        }
    }
}

class Librarian extends User implements LibraryOperations {
    public Librarian(String userId, String name, String email) {
        super(userId, name, email); //basically calls its superior class and passes the common parameters to be assigned 
    }

    @Override
    public void displayRole() {
        System.out.println("Librarian: " + name);
    }

    @Override
    public void addBook(Book book) throws LibraryException {
        try {
            LibrarySystem library = LibrarySystem.getInstance();
            library.addBookInternal(book); //adds book to the lib
            System.out.println("Book added successfully: " + book.getTitle());
        } catch (Exception e) {
            throw new LibraryException("Error adding book: " + e.getMessage());
        }
    }

    @Override
    public void removeBook(String bookId) throws LibraryException {
        try {
            LibrarySystem library = LibrarySystem.getInstance();
            library.removeBookInternal(bookId);
            System.out.println("Book removed successfully");
        } catch (Exception e) {
            throw new LibraryException("Error removing book: " + e.getMessage());
        }
    }

    @Override
    public Book searchBook(String query) throws LibraryException {
        try {
            LibrarySystem library = LibrarySystem.getInstance();
            return library.searchBookInternal(query);
        } catch (Exception e) {
            throw new LibraryException("Error searching book: " + e.getMessage());
        }
    }

    @Override
    public void displayAllBooks() {
        try {
            LibrarySystem library = LibrarySystem.getInstance();
            library.displayAllBooksInternal();
        } catch (Exception e) {
            System.out.println("Error displaying books: " + e.getMessage());
        }
    }
}

class LibrarySystem implements LibraryOperations, Serializable {
    private static LibrarySystem instance;
    private Map<String, Book> books;
    private Map<String, User> users;

    private LibrarySystem() {
        books = new HashMap<>();
        users = new HashMap<>();
    }

    public static synchronized LibrarySystem getInstance() {
        if (instance == null) {
            instance = new LibrarySystem();
        }
        return instance;
    }

    public void addUser(User user) throws LibraryException {
        if (users.containsKey(user.getUserId())) {
            throw new LibraryException("User ID already exists");
        }
        users.put(user.getUserId(), user);
    }

    public void addUser(Student student) throws LibraryException {
        addUser((User)student);
    }

    public void addUser(Librarian librarian) throws LibraryException {
        addUser((User)librarian);
    }

    public User getUser(String userId) throws LibraryException {
        User user = users.get(userId);
        if (user == null) {
            throw new LibraryException("User not found");
        }
        return user;
    }

    void addBookInternal(Book book) throws LibraryException {
        if (books.containsKey(book.getBookId())) {
            throw new LibraryException("Book ID already exists");
        }
        books.put(book.getBookId(), book);
    }

    void removeBookInternal(String bookId) throws LibraryException {
        if (!books.containsKey(bookId)) {
            throw new LibraryException("Book not found");
        }
        books.remove(bookId);
    }

    Book searchBookInternal(String query) throws LibraryException {
        if (books.containsKey(query)) {
            return books.get(query);
        }

        List<Book> matchedBooks = books.values().stream()
            .filter(book -> book.getTitle().equalsIgnoreCase(query) ||
                            book.getAuthor().equalsIgnoreCase(query))
            .collect(Collectors.toList());

        if (matchedBooks.isEmpty()) {
            throw new LibraryException("No books found matching: " + query);
        }

        if (matchedBooks.size() > 1) {
            System.out.println("Multiple books found. Returning first match.");
        }

        return matchedBooks.get(0);
    }

    void displayAllBooksInternal() {
        if (books.isEmpty()) {
            System.out.println("No books in the library");
            return;
        }

        System.out.println("All Books in Library:");
        books.values().forEach(System.out::println);
    }

    @Override
    public void addBook(Book book) throws LibraryException {
        addBookInternal(book);
    }

    @Override
    public void removeBook(String bookId) throws LibraryException {
        removeBookInternal(bookId);
    }

    @Override
    public Book searchBook(String query) throws LibraryException {
        return searchBookInternal(query);
    }

    @Override
    public void displayAllBooks() {
        displayAllBooksInternal();
    }

    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
            System.out.println("Library data saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving library data: " + e.getMessage());
        }
    }

    public static LibrarySystem loadFromFile(String filename) throws LibraryException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject();
            if (obj instanceof LibrarySystem) {
                instance = (LibrarySystem) obj;
                System.out.println("Library data loaded successfully");
                return instance;
            }
            throw new LibraryException("Invalid data in file");
        } catch (IOException | ClassNotFoundException e) {
            throw new LibraryException("Error loading library data: " + e.getMessage());
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        try {
            LibrarySystem library = LibrarySystem.getInstance();

            //getInstance() returns a bool value checking if an instance of that particular 
            //class is created or no
            //Synchronized keyword ensures only 1 thread is allowed - only 1 instance is created at a time

            Librarian librarian = new Librarian("lib001", "John Doe", "john@library.com");
            Student student1 = new Student("stu001", "Alice Smith", "alice@uni.edu", "Computer Science");
            Student student2 = new Student("stu002", "Bob Johnson", "bob@uni.edu", "Mathematics");

            library.addUser(librarian);
            library.addUser(student1);
            library.addUser(student2);

            Book book1 = new Book("B001", "Java Programming", "James Gosling", 5);
            Book book2 = new Book("B002", "Effective Java", "Joshua Bloch", 3);
            Book book3 = new Book("B003", "Clean Code", "Robert Martin", 2);

            librarian.addBook(book1);
            librarian.addBook(book2);
            librarian.addBook(book3);

            librarian.displayAllBooks();

            System.out.println("\nStudent activities:");
            student1.borrowBook("B001");
            student1.borrowBook("B002");
            
            //code to check if re borrowing is possible or no
            try {
                student1.borrowBook("B001");
            } catch (LibraryException e) {
                System.out.println("Error: " + e.getMessage());
            }

            student1.displayBorrowedBooks();

            student1.returnBook("B001");
            student1.displayBorrowedBooks();

            System.out.println("\nSearch results:");
            //to search for a particular book
            Book foundBook = librarian.searchBook("Clean Code");
            System.out.println("Found: " + foundBook);

            //checks for non existing book
            try {
                librarian.searchBook("Non-existent Book");
            } catch (LibraryException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (LibraryException e) {
            System.out.println("Library Error: " + e.getMessage());
        }
    }
}
