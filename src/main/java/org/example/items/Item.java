package org.example.items;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.util.Comparator;

/**
 * Base class for all library items.
 *
 * <p>Defines the common fields shared across all item types: a unique auto-generated id,
 * a title, a responsable (the person or organization accountable for the item — e.g. author,
 * publisher, or director), and a lifecycle status.</p>
 *
 * <p>Centralizing the comparators here avoids duplicating sorting logic across subclasses,
 * since all item types share the same sortable fields.</p>
 *
 * <p>Items are created with {@link Status#INSTORE} by default, reflecting the assumption
 * that a newly registered item is physically present in the library.</p>
 */
@EqualsAndHashCode
@Getter
public abstract class Item {
    @Setter
    private String title;

    @Setter
    /** The person or organization accountable for this item (author, publisher, director, etc). */
    private String responsable;

    private String id;
    private static int nextId = 1;
    @Setter
    private Status status = Status.INSTORE;
    protected Type type;

    /**
     * Creates a new item with the given title and responsable, assigning it an auto-generated id.
     * Status defaults to {@link Status#INSTORE}.
     *
     * @param title       the item's title
     * @param responsable the person or organization accountable for this item
     */
    public Item(String title, String responsable) {
        this.title = title;
        this.responsable = responsable;
        this.id = String.format("%06d", nextId++);
    }

    /**
     * Sorts items by title (case-insensitive), breaking ties by responsable,
     * then by id to guarantee a stable total ordering.
     */
    public static class TitleComparator implements Comparator<Item> {
        @Override
        public int compare(Item o1, Item o2) {
            int cmp = o1.getTitle().compareToIgnoreCase(o2.getTitle());
            if (cmp != 0) return cmp;

            int cmp2 = o1.getResponsable().compareToIgnoreCase(o2.getResponsable());
            if (cmp2 != 0) return cmp2;

            return o1.getId().compareTo(o2.getId());
        }
    }

    /**
     * Sorts items by responsable (case-insensitive), breaking ties by title,
     * then by id to guarantee a stable total ordering.
     */
    public static class ResponsableComparator implements Comparator<Item> {
        @Override
        public int compare(Item o1, Item o2) {
            int cmp2 = o1.getResponsable().compareToIgnoreCase(o2.getResponsable());
            if (cmp2 != 0) return cmp2;

            int cmp = o1.getTitle().compareToIgnoreCase(o2.getTitle());
            if (cmp != 0) return cmp;

            return o1.getId().compareTo(o2.getId());
        }
    }

    @Override
    public String toString() {
        return String.format("Id: %s\n" +
                "Title: %s\n", id, title);
    }

    /**
     * Represents the current physical state of an item in the library.
     *
     * <ul>
     *   <li>{@code INSTORE} — available for borrowing</li>
     *   <li>{@code BORROWED} — currently checked out by a user</li>
     *   <li>{@code LOST} — reported missing</li>
     * </ul>
     */
    public enum Status {
        BORROWED, INSTORE, LOST
    }

    /**
     * The media format of the item.
     */
    public enum Type {
        BOOK, DVD, MAGAZINE
    }
}
