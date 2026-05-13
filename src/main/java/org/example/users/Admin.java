package org.example.users;

import org.example.LibrarySystem;
import org.example.items.Item;

import java.io.File;

public class Admin extends User implements Reportable {
    public Admin(String name) {
        super(name);
    }

    @Override
    public File generateReport() {
        File directory = new File("src/main/resources/reports");
        return LibrarySystem.generateReport(directory);
    }

    public boolean removeItem(Item item) {
        return LibrarySystem.removeItem(item);
    }

    public boolean addItem(Item item) {
        return LibrarySystem.addItem(item);
    }

    public boolean addLostItem(Item item) {
        return LibrarySystem.addLostItem(item);
    }

    public User searchUser(String name) {
        return LibrarySystem.searchUser(name);
    }

    public boolean addUser(User user) {
        return LibrarySystem.addUser(user);
    }

    public boolean removeUser(User user) {
        return LibrarySystem.removeUser(user);
    }

    public void loadItems() {
        LibrarySystem.loadItems();
    }

    public void saveItems() {
        LibrarySystem.saveItems();
    }
}
