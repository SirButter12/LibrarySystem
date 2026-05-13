package org.example;

import org.example.items.Book;
import org.example.items.DVD;
import org.example.items.Magazine;
import org.example.items.Item;
import org.example.users.*;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        LibrarySystem.loadItems();
        LibrarySystem.loadUsers();

        System.out.println("Welcome to the library system!");

        User currentUser = loginMenu();
        if (currentUser == null) {
            System.out.println("Goodbye!");
            return;
        }

        if (currentUser instanceof Admin) {
            adminMenu((Admin) currentUser);
        } else {
            userMenu(currentUser);
        }

        LibrarySystem.saveItems();
        LibrarySystem.saveUsers();
        System.out.println("Goodbye!");
    }

    private static User loginMenu() {
        while (true) {
            System.out.println("\n1. Enter existing user ID");
            System.out.println("2. Create new user");
            System.out.println("0. Exit");
            System.out.print("> ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    User user = enterExistingUser();
                    if (user != null) return user;
                    break;
                case "2":
                    return createNewUser();
                case "0":
                    return null;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    private static User enterExistingUser() {
        System.out.print("Enter your user ID: ");
        String id = scanner.nextLine().trim();
        try {
            User user = LibrarySystem.searchUser(id);
            System.out.println("Welcome back, " + user.getName() + "!");
            return user;
        } catch (Exception e) {
            System.out.println("User not found.");
            return null;
        }
    }

    private static User createNewUser() {
        System.out.print("Create username: ");
        String name = scanner.nextLine().trim();

        System.out.println("Select type:");
        System.out.println("  1. Teacher");
        System.out.println("  2. Student");
        System.out.println("  3. Admin");
        System.out.print("> ");
        String typeChoice = scanner.nextLine().trim();

        User user = null;
        switch (typeChoice) {
            case "1": user = new Teacher(name); break;
            case "2": user = new Student(name); break;
            case "3": user = new Admin(name);   break;
            default:
                System.out.println("Invalid type.");
                return null;
        }

        LibrarySystem.addUser(user);
        System.out.println("User created! Your ID is: " + user.getId());
        return user;
    }

    private static void userMenu(User user) {
        while (true) {
            System.out.println("\n── " + user.getName() + " ──");
            System.out.println("1. Search item");
            System.out.println("2. Borrow item");
            System.out.println("3. Return item");
            System.out.println("4. View my borrowed items");
            System.out.println("5. View all items");
            System.out.println("0. Logout");
            System.out.print("> ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": searchItem(user);        break;
                case "2": borrowItem(user);        break;
                case "3": returnItem(user);        break;
                case "4": viewBorrowedItems(user); break;
                case "5": viewAllItems();          break;
                case "0": return;
                default:  System.out.println("Invalid option, try again.");
            }
        }
    }

    private static void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\n── Admin: " + admin.getName() + " ──");
            System.out.println("1. Search item");
            System.out.println("2. Borrow item");
            System.out.println("3. Return item");
            System.out.println("4. View my borrowed items");
            System.out.println("5. Add item");
            System.out.println("6. Remove item");
            System.out.println("7. Mark item as lost");
            System.out.println("8. Add user");
            System.out.println("9. Remove user");
            System.out.println("10. Generate report");
            System.out.println("11. View all items");
            System.out.println("12. View all users");
            System.out.println("0. Logout");
            System.out.print("> ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":  searchItem(admin);        break;
                case "2":  borrowItem(admin);        break;
                case "3":  returnItem(admin);        break;
                case "4":  viewBorrowedItems(admin); break;
                case "5":  addItem(admin);           break;
                case "6":  removeItem(admin);        break;
                case "7":  markLost(admin);          break;
                case "8":  addUser();                break;
                case "9":  removeUser(admin);        break;
                case "10": generateReport(admin);    break;
                case "11": viewAllItems();           break;
                case "12": viewAllUsers();           break;
                case "0":  return;
                default:   System.out.println("Invalid option, try again.");
            }
        }
    }

    private static void searchItem(User user) {
        System.out.print("Enter title, author/director/publisher, or ID: ");
        String keyword = scanner.nextLine().trim();
        try {
            System.out.println(user.searchItemRecursive(keyword));
        } catch (Exception e) {
            System.out.println("Item not found.");
        }
    }

    private static void borrowItem(User user) {
        System.out.print("Enter item ID to borrow: ");
        String id = scanner.nextLine().trim();
        try {
            Item item = user.searchItemRecursive(id);
            if (user.borrowItem(item)) {
                System.out.println("Borrowed successfully: " + item.getTitle());
            } else {
                System.out.println("Could not borrow item.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void returnItem(User user) {
        System.out.print("Enter item ID to return: ");
        String id = scanner.nextLine().trim();
        try {
            Item item = user.searchItemRecursive(id);
            if (user.returnItem(item)) {
                System.out.println("Returned successfully: " + item.getTitle());
            } else {
                System.out.println("You don't have that item borrowed.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewBorrowedItems(User user) {
        List<Item> borrowed = user.getBorrowedItems();
        if (borrowed.isEmpty()) {
            System.out.println("No borrowed items.");
            return;
        }
        System.out.println("Borrowed items:");
        for (Item item : borrowed) {
            System.out.println("  " + item.getId() + " - " + item.getTitle());
        }
    }

    // ─── Admin-only Actions ───────────────────────────────────────────────────

    private static void addItem(Admin admin) {
        System.out.println("Select type: (1) Book  (2) DVD  (3) Magazine");
        System.out.print("> ");
        String typeChoice = scanner.nextLine().trim();

        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Author/Director/Publisher: ");
        String responsable = scanner.nextLine().trim();

        Item item = null;
        switch (typeChoice) {
            case "1":
                System.out.print("Genre: ");
                String genre = scanner.nextLine().trim();
                item = new Book(title, responsable, genre);
                break;
            case "2":
                System.out.print("Duration (minutes): ");
                int duration = Integer.parseInt(scanner.nextLine().trim());
                item = new DVD(title, responsable, duration);
                break;
            case "3":
                System.out.print("Issue number: ");
                int issue = Integer.parseInt(scanner.nextLine().trim());
                item = new Magazine(title, responsable, issue);
                break;
            default:
                System.out.println("Invalid type.");
                return;
        }

        if (admin.addItem(item)) {
            System.out.println("Item added with ID: " + item.getId());
        } else {
            System.out.println("Item already exists.");
        }
    }

    private static void removeItem(Admin admin) {
        System.out.print("Enter item ID to remove: ");
        String id = scanner.nextLine().trim();
        try {
            Item item = LibrarySystem.searchItemRecursive(id);
            if (admin.removeItem(item)) {
                System.out.println("Item removed.");
            } else {
                System.out.println("Could not remove item.");
            }
        } catch (Exception e) {
            System.out.println("Item not found.");
        }
    }

    private static void markLost(Admin admin) {
        System.out.print("Enter item ID to mark as lost: ");
        String id = scanner.nextLine().trim();
        try {
            Item item = LibrarySystem.searchItemRecursive(id);
            if (admin.addLostItem(item)) {
                System.out.println("Item marked as lost.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addUser() {
        createNewUser();
    }

    private static void removeUser(Admin admin) {
        System.out.print("Enter user ID to remove: ");
        String id = scanner.nextLine().trim();
        try {
            User user = LibrarySystem.searchUser(id);
            if (admin.removeUser(user)) {
                System.out.println("User removed.");
            } else {
                System.out.println("Could not remove user.");
            }
        } catch (Exception e) {
            System.out.println("User not found.");
        }
    }

    private static void generateReport(Admin admin) {
        File file = admin.generateReport();
        if (file != null) {
            System.out.println("Report generated: " + file.getAbsolutePath());
        } else {
            System.out.println("Error generating report.");
        }
    }

    private static void viewAllItems() {
        List<Item> allItems = LibrarySystem.getItems();
        if (allItems.isEmpty()) {
            System.out.println("No items in the system.");
            return;
        }
        System.out.println("All items:");
        for (Item item : allItems) {
            System.out.println(item);
        }
    }

    private static void viewAllUsers() {
        List<User> allUsers = LibrarySystem.getUsers();
        if (allUsers.isEmpty()) {
            System.out.println("No users in the system.");
            return;
        }
        System.out.println("All users:");
        for (User user : allUsers) {
            System.out.println(user);
        }
    }
}