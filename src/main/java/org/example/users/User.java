package org.example.users;
import lombok.EqualsAndHashCode;
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
    protected static Set<Item.Type> borrowable;
    protected List<Item> borrowedItems = new ArrayList<>();
    protected int limit;

    public User(String name) {
        this.name = name;
        this.id = String.format("%06d", nextId++);
    }

    public Item searchItem(String keyWord) {
        return LibrarySystem.searchItem(keyWord);
    }

    public boolean borrowItem(Item item) {
        if (borrowedItems.size() < limit) {
            if (borrowable.contains(item.getType())) {
                if (borrowedItems.contains(item)) {
                    return false;
                }

                if (LibrarySystem.removeItem(item)) {
                    borrowedItems.add(item);
                    return true;
                }

                return false;
            }

            return false;
        }

        throw new BorrowedOverLimitsException("User borrowed items limit reached, please return items");
    }

    public boolean returnItem(Item item) {
        if (borrowedItems.contains(item)) {
            if (LibrarySystem.addItem(item)) {
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
