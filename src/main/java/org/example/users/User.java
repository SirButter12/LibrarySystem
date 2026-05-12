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
    private Set<Item.Type> borrowable = new HashSet<>();
    private List<Item> BorowedItems = new ArrayList<>();
    private static List<Item> itemsInLibrary = LibrarySystem.getItems();

    public User(String name) {
        this.name = name;
        this.id = String.format("%06d", nextId++);
    }

    public Item searchItem(String keyWord) {

    }
}
