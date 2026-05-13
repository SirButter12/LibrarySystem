package org.example.users;
import lombok.EqualsAndHashCode;
import org.example.exceptions.ItemNotBorrowableException;
import org.example.items.Item;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class Student extends User {
    protected static final Set<Item.Type> borrowable = new HashSet<>(Arrays.asList(Item.Type.BOOK));

    public Student(String name) {
        super(name);
        limit = Constants.BORROW_LIMIT_STUDENT;
    }

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
