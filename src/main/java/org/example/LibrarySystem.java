package org.example;

import org.example.exceptions.AllCopiesBorrowedException;
import org.example.exceptions.LostItemSquaredException;
import org.example.exceptions.ReturnedAnInStoreItemException;
import org.example.items.Item;
import org.example.users.Constants;
import org.example.users.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Manages the library's item inventory and their lifecycle across different states.
 * Items are tracked in multiple lists simultaneously: a general list, status-based lists
 * (in-store, borrowed, lost), and two sorted lists for search purposes.
 *
 * <p>All methods are static, treating this class as a singleton-like system.</p>
 */
public class LibrarySystem {
    private static List<Item> inStoreItems = new ArrayList<>();
    private static List<Item> borrowedItems = new ArrayList<>();
    private static List<Item> lostItems = new ArrayList<>();

    private static List<Item> itemsByName = new ArrayList<>();
    private static List<Item> itemsByResponsable  = new ArrayList<>();

    private static List<User> users = new ArrayList<>();

    /**
     * Adds a new item to the library system.
     * The item is inserted into the master list, both sorted lists, and the
     * appropriate status list based on its current status.
     *
     * @param item the item to add
     * @return true if added successfully, false if the item already exists
     */
    public static boolean addItem(Item item) {
        if (itemsByName.contains(item)) {
            return false;
        }

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

    /**
     * Removes an item from the library system entirely.
     * The item is removed from all lists it belongs to.
     *
     * @param item the item to remove
     * @return true if removed successfully, false if the item does not exist
     */
    public static boolean removeItem(Item item) {
        if (!itemsByName.contains(item)) {
            return false;
        }

        itemsByName.remove(item);
        itemsByResponsable.remove(item);

        return switch (item.getStatus()) {
            case Item.Status.INSTORE -> inStoreItems.remove(item);
            case Item.Status.BORROWED -> borrowedItems.remove(item);
            case Item.Status.LOST -> lostItems.remove(item);
        };
    }

    /**
     * Marks an item as borrowed, moving it from its current status list to {@code borrowedItems}.
     * The item must currently be in-store or lost; if it is already borrowed,
     * an exception is thrown.
     *
     * @param item the item to borrow
     * @return true if the operation succeeded
     * @throws AllCopiesBorrowedException if the item is already borrowed
     */
    public static boolean addBorrowedItem(Item item) {
        Item.Status status = item.getStatus();
        item.setStatus(Item.Status.BORROWED);

        if (inStoreItems.contains(item)) {
            return successFullOperation(borrowedItems.add(item) && inStoreItems.remove(item), item, status);
        }

        if (lostItems.contains(item)) {
            return successFullOperation(borrowedItems.add(item) && lostItems.remove(item), item, status);
        }

        item.setStatus(status);
        throw new AllCopiesBorrowedException("There is no more available copies");
    }

    /**
     * Marks an item as lost, moving it from its current status list to {@code lostItems}.
     * The item must currently be in-store or borrowed; if it is already lost,
     * an exception is thrown.
     *
     * @param item the item to mark as lost
     * @return true if the operation succeeded
     * @throws LostItemSquaredException if the item is already lost
     */
    public static boolean addLostItem(Item item) {
        Item.Status status = item.getStatus();
        item.setStatus(Item.Status.LOST);

        if (inStoreItems.contains(item)) {
            return successFullOperation(lostItems.add(item) && inStoreItems.remove(item), item, status);
        }

        if (borrowedItems.contains(item)) {
            return successFullOperation(lostItems.add(item) && borrowedItems.remove(item), item, status);
        }

        item.setStatus(status);
        throw new LostItemSquaredException("How did you manage to lose an already lost item, you forgot about its existence or what?");
    }

    /**
     * Returns an item to the library, moving it from its current status list to {@code inStoreItems}.
     * The item must currently be borrowed or lost; if it is already in-store,
     * an exception is thrown.
     *
     * @param item the item being returned
     * @return true if the operation succeeded
     * @throws ReturnedAnInStoreItemException if the item is already in the library
     */
    public static boolean returnItem(Item item) {
        Item.Status status = item.getStatus();
        item.setStatus(Item.Status.INSTORE);

        if (borrowedItems.contains(item)) {
            return successFullOperation(inStoreItems.add(item) && borrowedItems.remove(item), item, status);
        }

        if (lostItems.contains(item)) {
            return successFullOperation(inStoreItems.add(item) && lostItems.remove(item), item, status);
        }

        item.setStatus(status);
        throw new ReturnedAnInStoreItemException("How are you doing this?");
    }

    /**
     * Rolls back an item's status if a list operation fails, keeping
     * the item's status field consistent with the list it actually belongs to.
     *
     * @param success whether the list operation succeeded
     * @param item    the item whose status may need to be rolled back
     * @param status  the previous status to restore on failure
     * @return the result of the operation
     */
    private static boolean successFullOperation(boolean success, Item item, Item.Status status) {
        if (!success) {
            item.setStatus(status);
            return success;
        }

        return success;
    }

    /**
     * Searches for an item by keyword using recursive binary search.
     * First searches by title in {@code itemsByName}; if not found,
     * searches by responsable in {@code itemsByResponsable}.
     *
     * @param keyword the exact title or responsable to search for (case-sensitive)
     * @return the matching item, or null if not found
     */
    public static Item searchItemRecursive(String keyword) {
        Item result = binarySearch(itemsByName, true, keyword, 0, itemsByName.size() - 1);
        if (result != null) {return result;}

        return binarySearch(itemsByResponsable, false ,keyword, 0, itemsByName.size() - 1);
    }

    /**
     * Recursive binary search over a sorted item list.
     * Compares either by title or responsable depending on {@code byTitle}.
     * Requires the list to be sorted by the corresponding field for correct results.
     *
     * @param list    the sorted list to search
     * @param byTitle if true, compares by title; if false, compares by responsable
     * @param keyword the exact value to search for (case-sensitive)
     * @param left    the left boundary of the current search range (inclusive)
     * @param right   the right boundary of the current search range (inclusive)
     * @return the matching item, or null if not found
     */
    private static Item binarySearch(List<Item> list, boolean byTitle, String keyword, int left, int right) {
        if (left > right) {
            return null;
        }

        int mid = left + (right - left) / 2;
        Item midItem = list.get(mid);
        int cmp = byTitle ? midItem.getTitle().compareToIgnoreCase(keyword) :
                midItem.getResponsable().compareToIgnoreCase(keyword);

        if (cmp == 0) { return midItem; };
        if (cmp > 0) {
            return binarySearch(list, byTitle ,keyword, left, mid - 1);
        } else {
            return binarySearch(list, byTitle ,keyword, mid + 1, right);
        }
    }

    /**
     * Searches for an item by keyword using streams.
     * First searches by title, then by responsable (case-insensitive).
     * Unlike {@link #searchItemRecursive}, the second stream is only
     * evaluated if the first yields no results.
     *
     * @param keyword the title or responsable to search for (case-insensitive)
     * @return the first matching item, or null if not found
     */
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

    public static File generateReport(File directory) {
        int n = 1;
        File reportFile;
        do {
            reportFile = new File(directory, "report_" + n + ".csv");
            n++;
        } while (reportFile.exists());

        try (FileWriter fw = new FileWriter(reportFile)) {
            fw.write("INSTORE,");
            for (Item item : inStoreItems) {
                fw.write(item.getId() + "|" + item.getTitle() + "|" + item.getType() + "|" + item.getResponsable() + ",");
            }
            fw.write("\n");

            fw.write("BORROWED,");
            for (Item item : borrowedItems) {
                fw.write(item.getId() + "|" + item.getTitle() + "|" + item.getType() + "|" + item.getResponsable() + ",");
            }
            fw.write("\n");

            fw.write("LOST,");
            for (Item item : lostItems) {
                fw.write(item.getId() + "|" + item.getTitle() + "|" + item.getType() + "|" + item.getResponsable() + ",");
            }
            fw.write("\n");

        } catch (IOException e) {
            System.out.println("Error generating report: " + e.getMessage());
            return null;
        }

        return reportFile;
    }
}
