package org.example.users;

import java.io.File;

public class Admin extends User implements Reportable {
    public Admin(String name) {
        super(name);
    }

    @Override
    public File generateReport() {
    return null;
    }

}
