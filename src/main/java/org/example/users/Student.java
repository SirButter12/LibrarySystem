package org.example.users;
import lombok.EqualsAndHashCode;
import org.example.items.Item;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class Student extends User {
    protected static Set<Item.Type> borrowable;

    public Student(String name) {
        super(name);
        limit = Constants.BORROW_LIMIT_STUDENT;
        borrowable = new HashSet<>(Arrays.asList(Item.Type.BOOK));
    }

    @Override
    public String toString() {
        return String.format( "Student { \n" +
                super.toString() +
                "borrowedItems: %s\n" +
                "borrowableItems: %s\n" +
                "borrowLimit: %s",
                borrowedItems.toString(),
                borrowable.toString(),
                limit
        );
    }
}
