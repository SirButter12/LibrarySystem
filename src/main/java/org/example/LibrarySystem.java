package org.example;

import lombok.Getter;
import org.example.items.Item;
import java.util.ArrayList;
import java.util.List;

public class LibrarySystem {
    @Getter
    private static List<Item> items = new ArrayList<>();

    private static List<Item> inStoreItems = new ArrayList<>();
    private static List<Item> borrowedItems = new ArrayList<>();
    private static List<Item> lostItems = new ArrayList<>();

    private static List<Item> itemsByName;
    private static List<Item> itemsByResponsable;



    public static boolean addItem(Item item) {
        if (items.contains(item)) {
            return false;
        }
        items.add(item);

        return switch (item.getStatus()) {
            case Item.Status.INSTORE -> inStoreItems.add(item);
            case Item.Status.BORROWED -> borrowedItems.add(item);
            case Item.Status.LOST -> lostItems.add(item);
        };
    }

    public static boolean removeItem(Item item) {
        if (!items.contains(item)) {
            return false;
        }
        items.remove(item);

        return switch (item.getStatus()) {
            case Item.Status.INSTORE -> inStoreItems.remove(item);
            case Item.Status.BORROWED -> borrowedItems.remove(item);
            case Item.Status.LOST -> lostItems.remove(item);
        };
    }

    public static boolean addBorrowedItem(Item item) {
        if (borrowedItems.contains(item)) {
            return false;
        }

        if (inStoreItems.contains(item)) {
            inStoreItems.remove(item);
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

        if (inStoreItems.contains(item)) {
            inStoreItems.remove(item);
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

        return false;
    }

    public static boolean returnItem(Item item) {
        if (inStoreItems.contains(item)) {
            return false;
        }

        if (borrowedItems.contains(item)) {
            borrowedItems.remove(item);
            inStoreItems.add(item);
            item.setStatus(Item.Status.INSTORE);
            return true;
        }

        if (lostItems.contains(item)) {
            lostItems.remove(item);
            inStoreItems.add(item);
            item.setStatus(Item.Status.INSTORE);
            return true;
        }

        return false;
    }

    public static Item searchItemRecursive(String KeyWord) {
        return null;
    }

    public static Item searchItemStream(String KeyWord) {
        return null;
    }
}
