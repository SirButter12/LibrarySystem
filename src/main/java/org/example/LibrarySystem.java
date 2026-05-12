package org.example;

import lombok.Getter;
import org.example.items.Item;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LibrarySystem {
    @Getter
    private static List<Item> items = new ArrayList<>();

    private static List<Item> booksByTitle;
    private static List<Item> booksByAuthor;

    private static List<Item> DVDsByTitle;
    private static List<Item> DVDsByDirector;

    private static List<Item> magazinesByTitle;
    private static List<Item> magazinesByPublisher;

    private static Comparator<Item> titleComparator = new Item.titleComparator();
    private static Comparator<Item> responsableComparator = new Item.ResponsableComparator();

    public static void updateBooks() {
        booksByTitle = items.stream().filter(item -> item.getType().equals(Item.Type.BOOK)).toList();
        booksByAuthor = items.stream().filter(item -> item.getType().equals(Item.Type.BOOK)).toList();

        booksByTitle.sort(titleComparator);
        booksByAuthor.sort(responsableComparator);
    }

    public static void updateDVDs() {
        DVDsByDirector = items.stream().filter(item -> item.getType().equals(Item.Type.DVD)).toList();
        DVDsByTitle = items.stream().filter(item -> item.getType().equals(Item.Type.DVD)).toList();

        DVDsByTitle.sort(titleComparator);
        DVDsByDirector.sort(responsableComparator);
    }

    public static void updateMagazines() {
        magazinesByTitle = items.stream().filter(item -> item.getType().equals(Item.Type.MAGAZINE)).toList();
        magazinesByPublisher = items.stream().filter(item -> item.getType().equals(Item.Type.MAGAZINE)).toList();

        booksByTitle.sort(titleComparator);
        magazinesByPublisher.sort(responsableComparator);
    }

    private void books() {
    }


    public static boolean addItem(Item item) {
        if (items.contains(item)) {
            return false;
        }

        items.add(item);

        switch (item.getType()) {
            case BOOK -> updateBooks();
            case DVD -> updateDVDs();
            case MAGAZINE -> updateMagazines();
        }

        return true;
    }

    public static boolean removeItem(Item item) {
        if (items.contains(item)) {
            items.remove(item);
            return true;
        }

        return false;
    }

}
