package org.example.items;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a magazine item in the library.
 *
 * <p>Extends {@link Item} with an {@code issueNumber} field to identify the specific edition.
 * The {@code responsable} field inherited from {@link Item} maps to the magazine's publisher,
 * exposed through {@link #getPublisher()} and {@link #setPublisher(String)} for semantic clarity.</p>
 */
@EqualsAndHashCode(callSuper = true)
public class Magazine extends Item {
    @Getter @Setter
    private int issueNumber;

    /**
     * Creates a new Magazine with the given title, publisher, and issue number.
     *
     * @param title       the magazine's title
     * @param publisher   the magazine's publisher
     * @param issueNumber the edition number
     */
    public Magazine(String title, String publisher, int issueNumber) {
        super(title, publisher, Type.MAGAZINE);
        this.issueNumber = issueNumber;
    }

    /**
     * Reconstructs a Magazine from persisted data (e.g. loaded from CSV).
     * Unlike the standard constructor, this does not auto-generate an id —
     * it is restored exactly as provided.
     *
     * @param title       the magazine's title
     * @param responsable the magazine's publisher
     * @param id          the exact id to restore
     * @param status      the item's persisted status
     * @param type        the item type (should always be {@link Type#MAGAZINE})
     * @param issueNumber the edition number
     */
    public Magazine(String title, String responsable, String id, Status status, Type type, int issueNumber) {
        super(title, responsable, id, status, type);
        this.issueNumber = issueNumber;
    }

    /**
     * Returns the publisher of this magazine.
     * Alias for {@link Item#getResponsable()}.
     *
     * @return the publisher's name
     */
    public String getPublisher() {
        return super.getResponsable();
    }

    /**
     * Sets the publisher of this magazine.
     * Alias for {@link Item#setResponsable(String)}.
     *
     * @param publisher the publisher's name
     */
    public void setPublisher(String publisher) {
        super.setResponsable(publisher);
    }

    @Override
    public String toString() {
        return String.format("Magazine {\n" +
                        super.toString() +
                        "Publisher: %s\n" +
                        "IssueNumber: %d\n"+
                        "} \n",
                this.getPublisher(), issueNumber);
    }
}
