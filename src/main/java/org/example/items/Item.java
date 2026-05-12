package org.example.items;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.util.Comparator;

/**
 * This abstract class is the base to all Items, it sets 3 base parameters that all items share
 * Id, title, responsable, and status. Responsable is just a person/organization responsable for this item.
 * This is here because all classes have its own responsable analogue, whether it is the author, the publisher or the
 * director.
 * Implementing this here makes the code easer to maintain since the comparator has oly to be done in one place rather than
 * in the n different subclasses.
 * Status is initially in-store just because it makes sense to me that all items start in the store and then their
 * status is updated as the time goes. This is only to simplify adding a new Item to the library.
 */
@EqualsAndHashCode
public abstract class Item {
    @Getter @Setter
    private String title;
    @Getter @Setter
    private String responsable;
    @Getter
    private String id;
    @Getter
    private static int nextId = 1;
    @Getter @Setter
    private Status status = Status.INSTORE;
    @Getter
    protected Type type;

    public Item(String title, String responsable) {
        this.title = title;
        this.responsable = responsable;
        this.id = String.format("%06d", nextId++);
    }

    public class titleComparator implements Comparator<Item> {
        @Override
        public int compare(Item o1, Item o2) {
            int cmp = o1.getTitle().compareToIgnoreCase(o2.getTitle());
            if (cmp != 0) return cmp;

            int cmp2 = o1.getResponsable().compareToIgnoreCase(o2.getResponsable());
            if (cmp2 != 0) return cmp2;

            return o1.getId().compareTo(o2.getId());
        }
    }

    public class ResponsableComparator implements Comparator<Item> {
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

    public enum Status {
        BORROWED, INSTORE, LOST
    }

    public enum Type {
        BOOK, DVD, MAGAZINE
    }
}
