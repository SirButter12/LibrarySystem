package org.example;

import org.example.items.Item;
import org.example.users.User;

import java.util.Comparator;

/**
 * Shared constants for the user subsystem.
 *
 * <p>Centralizes borrow limits and item comparators to avoid
 * magic numbers and redundant instantiation across the codebase.</p>
 */
public class Constants {
    /** Maximum number of items a student can borrow simultaneously. */
    public static final int BORROW_LIMIT_STUDENT = 5;

    /** Maximum number of items a teacher can borrow simultaneously. */
    public static final int BORROW_LIMIT_TEACHER = 10;

    /** Comparator that sorts items by title, then responsable, then id. */
    public static final Comparator<Item> itemTitleComparator = new Item.TitleComparator();

    /** Comparator that sorts items by responsable, then title, then id. */
    public static final Comparator<Item> itemResponsableComparator = new Item.ResponsableComparator();

    /** Comparator that sorts items by id. */
    public static final Comparator<Item> itemIdComparator = new Item.IdComparator();

    /** Comparator that compares users by name */
    public static final Comparator<User> userNameComparator = new User.NameComparator();

    /** Comparator that sorts users by id. */
    public static final Comparator<User> userIdComparator = new User.IdComparator();
}
