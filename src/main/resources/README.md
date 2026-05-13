# Library Management System

**Course:** Programming 2 — Data Structures & Algorithms
**Language:** Java 17+ | **Build:** Maven

---

## Overview

A console-based library management system that allows users to borrow, return, and search for library items. The project demonstrates core OOP principles alongside data structures and algorithms covered in the course.

---

## Features

- **User roles** — Student, Teacher, and Admin, each with different borrowing permissions and limits
- **Item types** — Books, DVDs, and Magazines, each with type-specific attributes
- **Borrowing & returning** — enforced limits and status tracking per item
- **Search** — recursive binary search and stream-based search by title, author/director/publisher, or ID
- **Sorting** — multiple comparator strategies for items and users
- **Reports** — Admin can generate CSV inventory reports grouped by item status
- **Data persistence** — load and save items and users to CSV files on startup and exit
- **Interactive console menu** — role-based menus for all user types

---

## Project Structure

```
LibraryManagementSystem/
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── items/
│   │   │   │   ├── Item.java           # Abstract base class for all items
│   │   │   │   ├── Book.java           # Extends Item; adds genre and USBN
│   │   │   │   ├── DVD.java            # Extends Item; adds duration
│   │   │   │   └── Magazine.java       # Extends Item; adds issueNumber
│   │   │   ├── users/
│   │   │   │   ├── User.java           # Abstract base class for all users
│   │   │   │   ├── Student.java        # Can borrow books only; limit 5
│   │   │   │   ├── Teacher.java        # Can borrow all types; limit 10
│   │   │   │   ├── Admin.java          # Full system access; implements Reportable
│   │   │   │   ├── Reportable.java     # Interface for report generation
│   │   │   │   └── Constants.java      # Borrow limits and comparators
│   │   │   ├── exceptions/
│   │   │   │   ├── AllCopiesBorrowedException.java
│   │   │   │   ├── BorrowedOverLimitsException.java
│   │   │   │   ├── InexistentItemException.java
│   │   │   │   ├── ItemNotBorrowableException.java
│   │   │   │   ├── LostItemSquaredException.java
│   │   │   │   └── ReturnedAnInStoreItemException.java
│   │   │   ├── LibrarySystem.java      # Core system — all item/user state and operations
│   │   │   └── Main.java               # Entry point; interactive console menu
│   │   └── resources/
│   │       ├── items.csv               # Persisted item inventory
│   │       ├── users.csv               # Persisted user registry
│   │       └── reports/                # Generated inventory reports
│   └── test/
│       └── java/org/example/
│           └── ...                     # JUnit test classes
└── pom.xml
```

---

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+

### Run

```bash
mvn compile
mvn exec:java -Dexec.mainClass="org.example.Main"
```

Or run `Main.java` directly from IntelliJ.

---

## CSV Format

### items.csv

Each row represents one item. The last line is the current ID counter.

```
BOOK,000001,INSTORE,Cien años de soledad,García Márquez,Ficción,123456
DVD,000002,BORROWED,Inception,Nolan,148
MAGAZINE,000003,LOST,Nature,Springer,301
4
```

Columns: `type, id, status, title, responsable, [type-specific fields...]`

Type-specific fields:
- **BOOK** → `genre, usbn`
- **DVD** → `duration (minutes)`
- **MAGAZINE** → `issueNumber`

### users.csv

Each row represents one user. Borrowed item IDs are dot-separated. The last line is the current ID counter.

```
student,000001,Juan García,000002.000005
teacher,000002,María López,000001
admin,000003,Carlos Ruiz,
4
```

Columns: `type, id, name, borrowedItemIds`

> **Important:** `items.csv` must be loaded before `users.csv` since user records reference item IDs.

---

## Console Menu

### Login screen
```
Welcome to the library system!

1. Enter existing user ID
2. Create new user
0. Exit
```

### User menu (Student / Teacher)
```
1. Search item
2. Borrow item
3. Return item
4. View my borrowed items
5. View all items
0. Logout
```

### Admin menu
```
1.  Search item
2.  Borrow item
3.  Return item
4.  View my borrowed items
5.  Add item
6.  Remove item
7.  Mark item as lost
8.  Add user
9.  Remove user
10. Generate report
11. View all items
12. View all users
0.  Logout
```

---

## Data Structures & Algorithms

| Concept | Where used |
|---|---|
| Abstract classes & inheritance | `Item`, `User` and all subclasses |
| Interfaces | `Reportable` implemented by `Admin` |
| ArrayList | All item and user lists in `LibrarySystem` |
| Parallel sorted lists | `itemsByName`, `itemsByResponsable`, `items` (by ID), `usersByName` |
| Recursive binary search | `searchItemRecursive`, `searchUser` — searches by ID, title, and responsable |
| Stream search | `searchItemStream` — filter + findFirst |
| Comparators | `TitleComparator`, `ResponsableComparator`, `IdComparator`, `NameComparator` |
| Custom exceptions | 6 domain-specific exceptions for invalid state transitions |
| File I/O (TextIO) | `loadItems`, `saveItems`, `loadUsers`, `saveUsers`, `generateReport` |
| Enum | `Item.Status` (INSTORE, BORROWED, LOST), `Item.Type` (BOOK, DVD, MAGAZINE) |

---

## Borrowing Rules

| Role | Allowed types | Limit |
|---|---|---|
| Student | BOOK only | 5 |
| Teacher | BOOK, DVD, MAGAZINE | 10 |
| Admin | BOOK, DVD, MAGAZINE | Unlimited |

---

## Exception Handling

| Exception | Thrown when |
|---|---|
| `AllCopiesBorrowedException` | Borrowing an item that is already borrowed |
| `BorrowedOverLimitsException` | User has reached their borrow limit |
| `ItemNotBorrowableException` | User type cannot borrow this item type |
| `LostItemSquaredException` | Marking an already-lost item as lost |
| `ReturnedAnInStoreItemException` | Returning an item that is already in store |
| `InexistentItemException` | Searching for an item or user that does not exist |

---

## Design Decisions

**Multiple parallel lists in `LibrarySystem`**
Items are maintained in three sorted lists simultaneously (`items` by ID, `itemsByName` by title, `itemsByResponsable` by responsable) to enable O(log n) binary search on any field. This is a deliberate space-for-time trade-off.

**Static singleton pattern**
`LibrarySystem` uses static state to act as a global singleton. This keeps the design simple for the scope of the project while centralizing all business logic in one place.

**Semantic aliases on subclasses**
`Book` exposes `getAuthor()`/`setAuthor()`, `DVD` exposes `getDirector()`/`setDirector()`, and `Magazine` exposes `getPublisher()`/`setPublisher()` — all delegating to the inherited `responsable` field for semantic clarity without duplicating data.

**Status rollback on failed operations**
`LibrarySystem` sets an item's status field before moving it between lists. If the list operation fails, `successFullOperation` restores the previous status to keep the object consistent with its actual list membership.
