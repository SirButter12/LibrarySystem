package org.example.users;
import lombok.Getter;
import lombok.Setter;
import org.example.LibrarySystem;
import org.example.exceptions.BorrowedOverLimitsException;
import org.example.items.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public abstract class User {
    @Setter
    private String name;
    private String id;
    @Setter
    private static int nextId = 1;
    protected List<Item> borrowedItems = new ArrayList<>();
    protected int limit;

    public User(String name) {
        this.name = name;
        this.id = String.format("%06d", nextId++);
    }

    public Item searchItemRecursive(String keyWord) {
        return LibrarySystem.searchItemRecursive(keyWord);
    }

    public Item searchItemStream(String keyWord) {
        return LibrarySystem.searchItemStream(keyWord);
    }

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
}
