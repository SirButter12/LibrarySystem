package org.example.users;

import org.example.LibrarySystem;
import org.example.items.Item;

import java.io.File;
import java.util.List;

/**
 * Represents a library administrator with elevated privileges.
 *
 * <p>Extends {@link User} with administrative operations: managing the item inventory,
 * managing users, generating reports, and persisting the library state to disk.</p>
 *
 * <p>All operations delegate directly to {@link LibrarySystem}, keeping business logic
 * centralized in the system layer.</p>
 */
public class Admin extends User implements Reportable {
    /**
     * Creates a new Admin with the given name and an auto-generated id.
     *
     * @param name the administrator's name
     */
    public Admin(String name) {
        super(name);
        limit = Integer.MAX_VALUE;
    }

    /**
     * Reconstructs an Admin from persisted data (e.g. loaded from CSV).
     * Unlike the standard constructor, this does not auto-generate an id —
     * it is restored exactly as provided.
     *
     * @param name          the administrator's name
     * @param id            the exact id to restore
     * @param borrowedItems the list of items currently borrowed by this user
     */
    public Admin(String name, String id, List<Item> borrowedItems) {
        super(name, id, borrowedItems);
        limit = Integer.MAX_VALUE;
    }

    /**
     * Generates a CSV inventory report grouped by item status (in-store, borrowed, lost).
     * Reports are saved in {@code src/main/resources/reports} and named sequentially
     * (report_1.csv, report_2.csv, etc.) to avoid overwriting existing files.
     *
     * @return the generated report file, or null if an error occurred
     */
    @Override
    public File generateReport() {
        File directory = new File("src/main/resources/reports");
        return LibrarySystem.generateReport(directory);
    }

    /**
     * Removes an item from the library system entirely.
     *
     * @param item the item to remove
     * @return true if removed successfully, false if the item does not exist
     */
    public boolean removeItem(Item item) {
        return LibrarySystem.removeItem(item);
    }

    /**
     * Adds a new item to the library system.
     *
     * @param item the item to add
     * @return true if added successfully, false if the item already exists
     */
    public boolean addItem(Item item) {
        return LibrarySystem.addItem(item);
    }

    /**
     * Marks an item as lost in the library system.
     *
     * @param item the item to mark as lost
     * @return true if the operation succeeded
     */
    public boolean addLostItem(Item item) {
        return LibrarySystem.addLostItem(item);
    }

    /**
     * Searches for a user by name in the library system.
     *
     * @param name the name to search for
     * @return the matching user, or null if not found
     */
    public User searchUser(String name) {
        return LibrarySystem.searchUser(name);
    }

    /**
     * Registers a new user in the library system.
     *
     * @param user the user to add
     * @return true if added successfully, false if the user already exists
     */
    public boolean addUser(User user) {
        return LibrarySystem.addUser(user);
    }

    /**
     * Removes a user from the library system.
     *
     * @param user the user to remove
     * @return true if removed successfully, false if the user does not exist
     */
    public boolean removeUser(User user) {
        return LibrarySystem.removeUser(user);
    }

    /**
     * Loads all items from {@code src/main/resources/items.csv} into the library system.
     * Restores the id counter from the last line of the file to prevent duplicate ids.
     */
    public void loadItems() {
        LibrarySystem.loadItems();
    }

    /**
     * Persists all current items to {@code src/main/resources/items.csv}, overwriting
     * the file entirely. The last line written is the current id counter value,
     * used by {@link #loadItems()} to restore state on the next run.
     */
    public void saveItems() {
        LibrarySystem.saveItems();
    }

    @Override
    public String toString() {
        return String.format ("Admin {\n" +
                super.toString() +
                "}\n"
        );
    }
}
