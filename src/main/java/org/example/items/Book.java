package org.example.items;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a book item in the library.
 *
 * <p>Extends {@link Item} with a {@code genre} field and a {@code USBN} (Universal Serial Book Number),
 * a unique identifier derived from the book's hash code. The USBN is automatically recalculated
 * whenever the title or author changes, since those fields affect the hash.</p>
 *
 * <p>The {@code responsable} field inherited from {@link Item} maps to the book's author,
 * exposed through {@link #getAuthor()} and {@link #setAuthor(String)} for semantic clarity.</p>
 */
@EqualsAndHashCode(callSuper = true)
public class Book extends Item {
    @Getter @Setter
    private String genre;

    @Getter
    /** Unique identifier derived from this book's hash code. Recalculated on title or author change. */
    private String USBN;

    /**
     * Creates a new Book with the given title, author, and genre.
     * The USBN is generated from the object's hash code at construction time.
     *
     * @param title  the book's title
     * @param author the book's author
     * @param genre  the book's genre
     */
    public Book(String title, String author, String genre) {
        super(title, author, Type.BOOK);
        this.genre = genre;

        USBN = String.format("%d", hashCode());
    }

    public Book(String title, String responsable, String id, Status status, Type type, String genre, String USBN) {
        super(title, responsable, id, status, type);
        this.genre = genre;
        this.USBN = USBN;
    }

    /**
     * Sets the title and recalculates the USBN since the title affects the hash.
     *
     * @param title the new title
     */
    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        USBN = String.format("%d", hashCode());
    }

    /**
     * Sets the author and recalculates the USBN since the author affects the hash.
     * Alias for {@link Item#setResponsable(String)}.
     *
     * @param author the author's name
     */
    public void setAuthor(String author) {
        super.setResponsable(author);
        USBN = String.format("%d", hashCode());
    }

    public String getAuthor() {
        return super.getResponsable();
    }

    /**
     * Returns the author of this book.
     * Alias for {@link Item#getResponsable()}.
     *
     * @return the author's name
     */

    @Override
    public String toString() {
        return String.format("Book {\n" +
                        super.toString() +
                        "Author: %s\n" +
                        "USBN: %s\n"+
                        "} \n",
                this.getAuthor(), USBN);
    }
}
