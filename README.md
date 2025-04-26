# Library-Management-System
# Library Management System

This is a simple **Library Management System** project in Java, built using **OOP concepts** like **abstraction**, **inheritance**, **interfaces**, **exception handling**, and the **Singleton design pattern**.  
It manages books and users (students and librarians), allowing functionalities such as **borrowing, returning, adding, removing**, and **searching for books**.

---

##  Features
-  **Book Management** (Add, Remove, Search, Display all books)
-  **Student Activities** (Borrow, Return, View Borrowed Books)
-  **Librarian Activities** (Manage library operations)
-  **Singleton Pattern** for managing a single instance of the library
-  **Data Persistence** (Save and Load Library Data to/from a file)
-  Exception Handling for smooth user experience
- 🮚 Clean and modular code structure

---

##  Main Components
| Class / Interface | Description |
| :--- | :--- |
| `LibraryOperations` | Interface defining book management functions |
| `UserActivities` | Interface defining student activities |
| `LibraryException` | Custom exception class for library-specific errors |
| `User` | Abstract class representing a general user (Student/Librarian) |
| `Student` | Subclass of User; can borrow and return books |
| `Librarian` | Subclass of User; can add, remove, search, and display books |
| `Book` | Class representing a book entity |
| `LibrarySystem` | Singleton class managing the entire library system |
| `LibraryManagementSystem` | `main()` method to test and run the system |

---

##  How To Run
1. Make sure you have **Java** installed.
2. Copy all the code into a file named `LibraryManagementSystem.java`.
3. Open the terminal and navigate to the directory where the file is saved.
4. Compile the code:
   ```bash
   javac LibraryManagementSystem.java
   ```
5. Run the program:
   ```bash
   java LibraryManagementSystem
   ```

---

##  Project Structure
```
LibraryManagementSystem.java
README.md
```

> **Note:**  
> The system uses file saving (`saveToFile`) and loading (`loadFromFile`) functionalities to persist data.

---

##  Future Enhancements
- Add GUI (Graphical User Interface) using JavaFX or Swing.
- Implement book reservation features.
- Add Admin roles with more functionalities.
- Password-protected login for users.
- Fine system for overdue books.

---

##  Acknowledgements
- Concepts inspired by real-world library management systems.
- Java OOP principles and design patterns.

---

#  Description
This **Library Management System** project offers a basic simulation of how libraries function, focusing on user roles (students and librarians) and book management operations.  
The project leverages **OOP principles** heavily, ensuring the code remains clean, organized, and extendable.  
It is an ideal mini-project for students learning **Java**, **OOP**, or practicing system design on a small scale.

