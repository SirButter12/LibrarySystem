package org.example.users;

import org.example.LibrarySystem;

import java.io.File;

public class Admin extends User implements Reportable {
    public Admin(String name) {
        super(name);
    }

    @Override
    public File generateReport() {
        File directory = new File("src/main/resources/reports");
        return LibrarySystem.generateReport(directory);
    }
}
