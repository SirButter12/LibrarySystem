package org.example.items;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * This is a subclass that describes a magazine it is just a basic item with an extra issue number parameter
 * Responsable is externally called publisher
 */
@EqualsAndHashCode(callSuper = true)
public class Magazine extends Item {
    @Getter @Setter
    private int issueNumber;

    public Magazine(String title, String publisher, int issueNumber) {
        super(title, publisher);
        this.issueNumber = issueNumber;
    }

    public String getPublisher() {
        return super.getResponsable();
    }

    public void setPublisher(String director) {
        super.setResponsable(director);
    }

    @Override
    public String toString() {
        return String.format("Magazine {\n" +
                        super.toString() +
                        "Publisher: %s\n" +
                        "IssueNumber: %dm\n"+
                        "} \n",
                this.getPublisher(), issueNumber);
    }
}
