package org.example.users;
import lombok.EqualsAndHashCode;
import org.example.exceptions.ItemNotBorrowableException;
import org.example.items.Item;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a student user with a restricted borrow limit and access to books only.
 *
 * <p>Students can only borrow {@link Item.Type#BOOK}.</p>
 *
 * @see Constants#BORROW_LIMIT_STUDENT
 */
@EqualsAndHashCode(callSuper = true)
public class Student extends User {
    /** Item types this user type is allowed to borrow. */
    private static final Set<Item.Type> borrowable = new HashSet<>(Arrays.asList(Item.Type.BOOK));

    /**
     * Creates a new Student with the given name and the student borrow limit.
     *
     * @param name the student's name
     */
    public Student(String name) {
        super(name);
        limit = Constants.BORROW_LIMIT_STUDENT;
    }

    /**
     * Borrows an item if its type is allowed for students.
     *
     * @param item the item to borrow
     * @return true if borrowed successfully
     * @throws ItemNotBorrowableException if the item type is not {@link Item.Type#BOOK}
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
        return String.format( "Student { \n" +
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
