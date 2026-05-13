package org.example;

import org.example.items.Item;
import org.example.users.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LibrarySystem {
    private static List<Item> items = new ArrayList<>();

    private static List<Item> inStoreItems = new ArrayList<>();
    private static List<Item> borrowedItems = new ArrayList<>();
    private static List<Item> lostItems = new ArrayList<>();

    private static List<Item> itemsByName = new ArrayList<>();
    private static List<Item> itemsByResponsable  = new ArrayList<>();

    public static boolean addItem(Item item) {
        if (items.contains(item)) {
            return false;
        }
        items.add(item);

        itemsByName.add(item);
        itemsByName.sort(Constants.titleComparator);

        itemsByResponsable.add(item);
        itemsByResponsable.sort(Constants.responsableComparator);

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

    public static Item searchItemRecursive(String keyword) {
        Item result = binarySearch(itemsByName, keyword, 0, items.size() - 1);
        if (result != null) {return result;}

        return binarySearch(itemsByResponsable, keyword, 0, items.size() - 1);
    }

    private static Item binarySearch(List<Item> list, String keyword, int left, int right) {
        if (left > right) {
            return null;
        }

        int mid = left + (right - left) / 2;
        Item midItem = list.get(mid);
        int cmp = midItem.getTitle().compareTo(keyword);

        if (cmp == 0) { return midItem; };
        if (cmp > 0) {
            return binarySearch(list, keyword, left, mid - 1);
        } else {
            return binarySearch(list, keyword, mid + 1, right);
        }
    }

    public static Item searchItemStream(String keyword) {
        return Stream.concat(
                        itemsByName.stream()
                                .filter(item -> item.getTitle().equalsIgnoreCase(keyword)),
                        itemsByResponsable.stream()
                                .filter(item -> item.getResponsable().equalsIgnoreCase(keyword))
                )
                .findFirst()
                .orElse(null);
    }
}
