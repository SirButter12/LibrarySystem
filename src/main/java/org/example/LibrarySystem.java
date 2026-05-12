package org.example;

import org.example.items.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LibrarySystem {
    private static List<Item> items = new ArrayList<>();

    private static List<Item> inStoreItems = new ArrayList<>();
    private static List<Item> borrowedItems = new ArrayList<>();
    private static List<Item> lostItems = new ArrayList<>();

    private static List<Item> itemsByName = new ArrayList<>();
    private static List<Item> itemsByResponsable  = new ArrayList<>();


    /**
     * adds a brand new item to the item list
     * @param item the item to be added
     * @return true if succesful, false otherwise
     */
    public static boolean addItem(Item item) {
        if (items.contains(item)) {
            return false;
        }
        items.add(item);

        itemsByName.add(item);
        itemsByName.sort(new Item.titleComparator());

        itemsByResponsable.add(item);
        itemsByResponsable.sort(new Item.ResponsableComparator());

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
        itemsByName.remove(item);
        itemsByResponsable.remove(item);

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
