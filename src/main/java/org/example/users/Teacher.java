package org.example.users;

import lombok.EqualsAndHashCode;
import org.example.Constants;
import org.example.exceptions.ItemNotBorrowableException;
import org.example.items.Item;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a teacher user with an elevated borrow limit and access to all item types.
 *
 * <p>Teachers can borrow {@link Item.Type#BOOK}, {@link Item.Type#DVD},
 * and {@link Item.Type#MAGAZINE}.</p>
 *
 * @see Constants#BORROW_LIMIT_TEACHER
 */
@EqualsAndHashCode(callSuper = true)
public class Teacher extends User {
    /** Item types this user type is allowed to borrow. */
    private static final Set<Item.Type> borrowable = new HashSet<>(Arrays.asList(Item.Type.BOOK, Item.Type.DVD, Item.Type.MAGAZINE));

    /**
     * Creates a new Teacher with the given name and the teacher borrow limit.
     *
     * @param name the teacher's name
     */
    public Teacher(String name) {
        super(name);
        limit = Constants.BORROW_LIMIT_TEACHER;
    }

    public Teacher(String name, String id, List<Item> borrowedItems) {
        super(name, id, borrowedItems);
        limit = Constants.BORROW_LIMIT_TEACHER;
    }

    /**
     * Borrows an item if its type is allowed for teachers.
     *
     * @param item the item to borrow
     * @return true if borrowed successfully
     * @throws ItemNotBorrowableException if the item type is not in {@code borrowable}
     */
    @Override
    public boolean borrowItem(Item item) {
        if (borrowable.contains(item.getType())) {
            return super.borrowItem(item);
        }

        throw new ItemNotBorrowableException("Item not borrowable");
    }

    @Override
    public String toString() {
        return String.format( "Teacher { \n" +
                        super.toString() +
                        "borrowedItems: %s\n" +
                        "borrowableItems: %s\n" +
                        "borrowLimit: %s",
                borrowedItems.toString(),
                borrowable,
                limit
        );
    }
}
