package org.example.users;
import org.example.LibrarySystem;
import org.example.items.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class User {
    private String name;
    private String id;
    private static int nextId = 1;
    private static Set<Item.Type> borrowable;
    private List<Item> borrowedItems = new ArrayList<>();
    private static List<Item> itemsInLibrary = LibrarySystem.getItems();

    public User(String name) {
        this.name = name;
        this.id = String.format("%06d", nextId++);
    }

    public Item searchItem(String keyWord) {

    }

    public boolean borrowItem(Item item) {
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

    public boolean returnItem(Item item) {
        if (borrowedItems.contains(item)) {
            if(LibrarySystem.addItem(item)) {
                borrowedItems.remove(item);
                return true;
            }

            return false;
        }

        return false;
    }
}
