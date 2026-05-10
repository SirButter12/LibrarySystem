package org.example.users;
import java.util.Set;

import static org.example.users.Constants.BORROWABLE_STUDENT;
import static org.example.users.Constants.BORROW_LIMIT_STUDENT;

public class Student {
    private int limit = BORROW_LIMIT_STUDENT;
    private Set<String> borrowable = BORROWABLE_STUDENT;
}
