package org.example.users;

import org.example.items.Item;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Constants {
    public static final int BORROW_LIMIT_STUDENT = 5;
    public static final int BORROW_LIMIT_TEACHER = 10;
    public static final Comparator<Item> titleComparator = new Item.titleComparator();
    public static final Comparator<Item> responsableComparator = new Item.ResponsableComparator();
}
