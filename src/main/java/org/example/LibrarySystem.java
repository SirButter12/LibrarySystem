package org.example;

import lombok.Getter;
import org.example.items.Item;
import java.util.ArrayList;
import java.util.List;

public class LibrarySystem {
    @Getter
    private static List<Item> items = new ArrayList<>();

}
