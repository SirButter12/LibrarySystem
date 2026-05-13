package org.example;

import org.example.exceptions.AllCopiesBorrowedException;
import org.example.exceptions.InexistentItemException;
import org.example.exceptions.LostItemSquaredException;
import org.example.exceptions.ReturnedAnInStoreItemException;
import org.example.items.Book;
import org.example.items.DVD;
import org.example.items.Item;
import org.example.items.Magazine;
import org.example.users.Admin;
import org.example.users.Student;
import org.example.users.Teacher;
import org.example.users.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
    private static List<Item> items = new ArrayList<>();

    private static List<User> users = new ArrayList<>();
    private static List<User> usersByName = new ArrayList<>();

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

        items.add(item);
        items.sort(Constants.itemIdComparator);

        itemsByName.add(item);
        itemsByName.sort(Constants.itemTitleComparator);

        itemsByResponsable.add(item);
        itemsByResponsable.sort(Constants.itemResponsableComparator);

        return switch (item.getStatus()) {
            case Item.Status.INSTORE -> inStoreItems.add(item);
            case Item.Status.BORROWED -> borrowedItems.add(item);
            case Item.Status.LOST -> lostItems.add(item);
        };
    }

    public static boolean addUser(User user) {
        if (users.contains(user)) {
            return false;
        }

        users.add(user);
        users.sort(Constants.userIdComparator);

        usersByName.add(user);
        usersByName.sort(Constants.userNameComparator);

        return true;
    }

    public static boolean removeUser(User user) {
        if(!users.contains(user)) {
            return false;
        }

        users.remove(user);
        usersByName.remove(user);

        return true;
    }

    /**
     * Removes an item from the library system entirely.
     * The item is removed from all lists it belongs to.
     *
     * @param item the item to remove
     * @return true if removed successfully, false if the item does not exist
     */
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
     * @param keyString the exact title, responsable or id search for (case-sensitive)
     * @return the matching item, or null if not found
     */
    public static Item searchItemRecursive(String keyString) {
        Item result = binarySearchItem(0 ,keyString, 0, items.size() - 1);
        if (result != null) {return result;}

        result = binarySearchItem(1, keyString, 0, items.size() - 1);
        if (result != null) {return result;}

        result = binarySearchItem(2 ,keyString, 0, items.size() - 1);
        if (result != null) {return result;}

        throw new InexistentItemException("This item does not exist in the system");
    }

    public static User searchUser(String keyString) {
        User result = binarySearchUser(0, keyString, 0, users.size() - 1);
        if (result != null) {return result;}

        result = binarySearchUser(1, keyString, 0, users.size() - 1);
        if (result != null) {return result;}

        throw new InexistentItemException("This user does not exist in the system");
    }

    /**
     * Recursive binary search over a sorted item list.
     * Compares either by title or responsable depending on {@code byTitle}.
     * Requires the list to be sorted by the corresponding field for correct results.
     *
     *
     * @param mode 0 searches by Id, 1 by title, and 2 by responsible
     * @param keyString the exact value to search for (case-sensitive)
     * @param left    the left boundary of the current search range (inclusive)
     * @param right   the right boundary of the current search range (inclusive)
     * @return the matching item, or null if not found
     */
    private static Item binarySearchItem(int mode, String keyString, int left, int right) {
        if (left > right) {
            return null;
        }

        int mid = left + (right - left) / 2;
        Item midItem = switch (mode) {
            case 0 -> items.get(mid);
            case 1 -> itemsByName.get(mid);
            case 2 -> itemsByResponsable.get(mid);
            default -> throw new IllegalArgumentException("Invalid mode");
        };
        int cmp = switch (mode) {
            case 0 -> midItem.getId().compareToIgnoreCase(keyString);
            case 1 -> midItem.getTitle().compareToIgnoreCase(keyString);
            case 2 -> midItem.getResponsable().compareToIgnoreCase(keyString);
            default -> throw new IllegalArgumentException("Invalid mode");
        };

        if (cmp == 0) { return midItem; };
        if (cmp > 0) {
            return binarySearchItem(mode ,keyString, left, mid - 1);
        } else {
            return binarySearchItem(mode ,keyString, mid + 1, right);
        }
    }

    private static User binarySearchUser(int mode, String name, int left, int right) {
        if (left > right) {
            return null;
        }

        int mid = left + (right - left) / 2;
        User midUser = switch (mode) {
            case 0 -> users.get(mid);
            case 1 -> usersByName.get(mid);
            default -> throw new IllegalArgumentException("Invalid mode");
        };

        int cmp = switch (mode) {
            case 0 -> midUser.getId().compareToIgnoreCase(name);
            case 1 -> midUser.getName().compareToIgnoreCase(name);
            default -> throw new IllegalArgumentException("Invalid mode");
        };

        if (cmp == 0) { return midUser; };
        if (cmp > 0) {
            return binarySearchUser(mode, name, left, mid - 1);
        } else {
            return binarySearchUser(mode, name, mid + 1, right);
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

    /**
     * Loads items from src/main/resources/items.csv and registers them in the system.
     * The last line of the file must be a single integer representing the last nextId processed,
     * used to restore the id counter and avoid duplicates.
     */
    public static void loadItems() {
        File file = new File("src/main/resources/items.csv");

        try (Scanner input = new Scanner(file)) {
            while (input.hasNextLine()) {
                String row = input.nextLine().trim();
                if (row.isEmpty()) continue;

                try {
                    int savedNextId = Integer.parseInt(row);
                    Item.setNextId(savedNextId);
                    break;
                } catch (NumberFormatException ignored) {
                    //keep goin bud
                }

                String[] data = row.split(",");
                Item.Type type = Item.Type.valueOf(data[0]);
                String id = data[1];
                Item.Status status = Item.Status.valueOf(data[2]);
                String title = data[3];
                String responsable = data[4];

                Item item = switch (type) {
                    case BOOK -> new Book(title, responsable, id, status, Item.Type.BOOK, data[5], data[6]);
                    case DVD -> new DVD(title, responsable, id, status, Item.Type.DVD, Integer.parseInt(data[5]));
                    case MAGAZINE -> new Magazine(title, responsable, id, status, Item.Type.MAGAZINE, Integer.parseInt(data[5]));
                };

                addItem(item);
            }
        } catch (IOException e) {
            System.out.println("Error loading items: " + e.getMessage());
        }
    }

    /**
     * Saves all items to src/main/resources/items.csv in load order.
     * Overwrites the file entirely. The last line is the current nextId counter.
     */
    public static void saveItems() {
        File file = new File("src/main/resources/items.csv");

        try (FileWriter fw = new FileWriter(file)) {
            for (Item item : itemsByName) {
                fw.write(item.getType() + ",");
                fw.write(item.getId() + ",");
                fw.write(item.getStatus() + ",");
                fw.write(item.getTitle() + ",");
                fw.write(item.getResponsable() + ",");

                switch (item.getType()) {
                    case BOOK -> {
                        Book book = (Book) item;
                        fw.write(book.getGenre() + ",");
                        fw.write(book.getUSBN());
                    }
                    case DVD -> {
                        DVD dvd = (DVD) item;
                        fw.write(String.valueOf(dvd.getDuration()));
                    }
                    case MAGAZINE -> {
                        Magazine magazine = (Magazine) item;
                        fw.write(String.valueOf(magazine.getIssueNumber()));
                    }
                }

                fw.write("\n");
            }

            // Last line: current nextId counter
            fw.write(String.valueOf(Item.getNextId()));

        } catch (IOException e) {
            System.out.println("Error saving items: " + e.getMessage());
        }
    }

    /**
     * Loads users from src/main/resources/users.csv and registers them in the system.
     * Borrowed items are resolved by id against the items list.
     * The last line must be a single integer representing the last nextId processed.
     */
    public static void loadUsers() {
        File file = new File("src/main/resources/users.csv");

        try (Scanner input = new Scanner(file)) {
            while (input.hasNextLine()) {
                String row = input.nextLine().trim();
                if (row.isEmpty()) continue;

                try {
                    int savedNextId = Integer.parseInt(row);
                    User.setNextId(savedNextId);
                    break;
                } catch (NumberFormatException ignored) {
                    // keep goin bud
                }

                String[] data = row.split(",");
                String type = data[0];
                String id = data[1];
                String name = data[2];

                List<Item> borrowedItems = new ArrayList<>();
                if (data.length > 3 && !data[3].isBlank()) {
                    String[] itemIds = data[3].split("\\.");
                    for (String itemId : itemIds) {
                        Item found = binarySearchItem(0, itemId, 0, items.size() - 1);
                        if (found != null) borrowedItems.add(found);
                    }
                }

                User user = switch (type) {
                    case "student" -> new Student(name, id, borrowedItems);
                    case "teacher" -> new Teacher(name, id, borrowedItems);
                    case "admin"   -> new Admin(name, id, borrowedItems);
                    default -> throw new IllegalArgumentException("Unknown user type: " + type);
                };

                addUser(user);
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    /**
     * Saves all users to src/main/resources/users.csv, overwriting the file entirely.
     * Borrowed items are stored as dot-separated ids. The last line is the current nextId counter.
     */
    public static void saveUsers() {
        File file = new File("src/main/resources/users.csv");

        try (FileWriter fw = new FileWriter(file)) {
            for (User user : users) {
                String type;
                if (user instanceof Admin)        type = "admin";
                else if (user instanceof Teacher) type = "teacher";
                else                              type = "student";

                fw.write(type + ",");
                fw.write(user.getId() + ",");
                fw.write(user.getName() + ",");

                // Borrowed items as dot-separated ids
                List<Item> borrowed = user.getBorrowedItems();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < borrowed.size(); i++) {
                    sb.append(borrowed.get(i).getId());
                    if (i < borrowed.size() - 1) sb.append(".");
                }
                fw.write(sb.toString());
                fw.write("\n");
            }

            // Last line: current nextId counter
            fw.write(String.valueOf(User.getNextId()));

        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
}

//overengineering is my passion
