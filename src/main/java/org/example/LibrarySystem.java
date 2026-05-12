package org.example;

import lombok.Getter;
import org.example.items.Item;
import java.util.ArrayList;
import java.util.List;

public class LibrarySystem {
    @Getter
    private static List<Item> items = new ArrayList<>();

    private static List<Item> itemsByName;
    private static List<Item> itemsByResponsable;

    private static List<Item> borrowedItems = new ArrayList<>();
    private static List<Item> lostItems = new ArrayList<>();

    public static boolean addItem(Item item) {
        if (items.contains(item)) {
            return false;
        }

        items.add(item);
        return true;
    }

    public static boolean addBorrowedItem(Item item) {
        if (borrowedItems.contains(item)) {
            return false;
        }

        if (items.contains(item)) {
            items.remove(item);
            borrowedItems.add(item);
            item.setStatus(Item.Status.BORROWED);
            return true;
        }

        if (lostItems.contains(item)) {
            lostItems.remove(item);
            borrowedItems.add(item);
            item.setStatus(Item.Status.BORROWED);
            return true;
        }

        return false;
    }

    public static boolean addLostItem(Item item) {
        if (lostItems.contains(item)) {
            return false;
        }

        if (items.contains(item)) {
            items.remove(item);
            lostItems.add(item);
            item.setStatus(Item.Status.LOST);
            return true;
        }

        if (borrowedItems.contains(item)) {
            borrowedItems.remove(item);
            lostItems.add(item);
            item.setStatus(Item.Status.LOST);
            return true;
        }

        lostItems.add(item);
        item.setStatus(Item.Status.LOST);
        return true;
    }

    public static boolean returnItem(Item item) {
        if (items.contains(item)) {
            return false;
        }

        if (borrowedItems.contains(item)) {
            borrowedItems.remove(item);
            items.add(item);
            item.setStatus(Item.Status.INSTORE);
            return true;
        }

        if (lostItems.contains(item)) {
            lostItems.remove(item);
            items.add(item);
            item.setStatus(Item.Status.INSTORE);
            return true;
        }

        items.add(item);
        return true;
    }

    public static Item searchItemRecursive(String KeyWord) {
        return null;
    }

    public static Item searchItemStream(String KeyWord) {
        return null;
    }
}
