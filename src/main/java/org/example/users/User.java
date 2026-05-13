package org.example.users;
import lombok.Getter;
import lombok.Setter;
import org.example.LibrarySystem;
import org.example.exceptions.BorrowedOverLimitsException;
import org.example.items.Item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a library user capable of borrowing and returning items.
 * Each user has a borrow limit defined by their subclass, and maintains
 * their own list of currently borrowed items in sync with {@link LibrarySystem}.
 *
 * <p>Concrete subclasses must define {@code limit} to enforce borrowing restrictions.</p>
 */
@Getter
public abstract class User {
    @Setter
    private String name;
    private String id;
    private static int nextId = 1;
    protected List<Item> borrowedItems = new ArrayList<>();
    protected int limit;

    /**
     * Creates a new user with the given name and an auto-generated id.
     *
     * @param name the user's name
     */
    public User(String name) {
        this.name = name;
        this.id = String.format("%06d", nextId++);
    }

    /**
     * Delegates a recursive binary search to {@link LibrarySystem}.
     * Searches by title first, then by responsable if not found.
     *
     * @param keyWord the exact title or responsable to search for
     * @return the matching item, or null if not found
     */
    public Item searchItemRecursive(String keyWord) {
        return LibrarySystem.searchItemRecursive(keyWord);
    }

    /**
     * Delegates a stream-based search to {@link LibrarySystem}.
     * Searches by title first, then by responsable if not found (case-insensitive).
     *
     * @param keyWord the title or responsable to search for
     * @return the first matching item, or null if not found
     */
    public Item searchItemStream(String keyWord) {
        return LibrarySystem.searchItemStream(keyWord);
    }

    /**
     * Borrows an item from the library, adding it to this user's borrowed list.
     * The item must not already be borrowed by this user, and the user must
     * not have reached their borrow limit.
     *
     * @param item the item to borrow
     * @return true if borrowed successfully, false if the user already has this item
     *         or if the library could not process the request
     * @throws BorrowedOverLimitsException if the user has reached their borrow limit
     */
    public boolean borrowItem(Item item) {
        if (borrowedItems.size() < limit) {
            if (borrowedItems.contains(item)) {
                return false;
            }

            if (LibrarySystem.addBorrowedItem(item)) {
                return borrowedItems.add(item);
            }
            return false;
        }

        throw new BorrowedOverLimitsException("User borrowed items limit reached, please return items");
    }

    /**
     * Returns a borrowed item to the library, removing it from this user's borrowed list.
     * The item must currently be in this user's borrowed list.
     *
     * @param item the item to return
     * @return true if returned successfully, false if the user does not have this item
     *         or if the library could not process the return
     */
    public boolean returnItem(Item item) {
        if (borrowedItems.contains(item)) {
            if (LibrarySystem.returnItem(item)) {
                borrowedItems.remove(item);
                return true;
            }

            return false;
        }

        return false;
    }


    @Override
    public String toString() {
        return String.format("name: %s\n" +
                "id: %s\n",
                this.name, this.id);
    }

    public static class NameComparator implements Comparator<User> {
        @Override
        public int compare(User o1, User o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
