package org.example.users;
import org.example.items.Item;


public abstract class User {
    private String name;
    private String id;
    private static int nextId = 1;

    public User(String name) {
        this.name = name;
        this.id = String.format("%06d", nextId++);
    }

    public Item searchItem(String keyWord) {

    }
}
