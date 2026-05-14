package org.example.users;

import java.io.File;

/**
 * Marks a user type as capable of generating inventory reports.
 *
 * <p>Implementors are expected to produce a file summarizing the current
 * state of the library's inventory, grouped by item status.</p>
 *
 * @see Admin
 */
public interface Reportable {
    /**
     * Generates an inventory report and saves it to disk.
     *
     * @return the generated report file, or null if an error occurred
     */
    File generateReport();
}
