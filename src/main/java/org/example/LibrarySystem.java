package org.example;

import lombok.Getter;
import org.example.items.Item;
import java.util.ArrayList;
import java.util.List;

public class LibrarySystem {
    @Getter
    private static List<Item> items = new ArrayList<>();

    public static boolean addItem(Item item) {
        if (items.contains(item)) {
            return false;
        }

        items.add(item);
        return true;
    }

    public static boolean removeItem(Item item) {
        if (items.contains(item)) {
            items.remove(item);
            return true;
        }

        return false;
    }

}
