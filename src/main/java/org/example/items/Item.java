package org.example.items;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.util.Comparator;

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

    public Item(String title, String author) {
        this.title = title;
        this.responsable = author;
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
}
