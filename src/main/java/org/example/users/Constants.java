package org.example.users;

import org.example.items.Item;
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
    public static final Comparator<Item> titleComparator = new Item.TitleComparator();

    /** Comparator that sorts items by responsable, then title, then id. */
    public static final Comparator<Item> responsableComparator = new Item.ResponsableComparator();
}
