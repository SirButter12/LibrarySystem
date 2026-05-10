package org.example.users;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Constants {
    public static final int BORROW_LIMIT_STUDENT = 5;
    public static final int BORROW_LIMIT_TEACHER = 10;
    public static final Set<String> BORROWABLE_STUDENT = new HashSet<String>(Arrays.asList("book"));
    public static final Set<String> BORROWABLE_TEACHER = new HashSet<String>(
            Arrays.asList("book", "DVD", "magazine"));

}
