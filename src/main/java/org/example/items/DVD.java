package org.example.items;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a DVD item in the library.
 *
 * <p>Extends {@link Item} with a {@code duration} field measured in minutes.
 * The {@code responsable} field inherited from {@link Item} maps to the DVD's director,
 * exposed through {@link #getDirector()} and {@link #setDirector(String)} for semantic clarity.</p>
 */
@EqualsAndHashCode (callSuper = true)
public class DVD extends Item {
    @Getter @Setter
    /** Runtime of the DVD in minutes. */
    private int duration;

    /**
     * Creates a new DVD with the given title, director, and duration.
     *
     * @param title    the DVD's title
     * @param director the DVD's director
     * @param duration the runtime in minutes
     */
    public DVD(String title, String director, int duration) {
        super(title, director, Type.DVD);
        this.duration = duration;
    }

    public DVD(String title, String responsable, String id, Status status, Type type, int duration) {
        super(title, responsable, id, status, type);
        this.duration = duration;
    }

    /**
     * Returns the director of this DVD.
     * Alias for {@link Item#getResponsable()}.
     *
     * @return the director's name
     */
    public String getDirector() {
        return super.getResponsable();
    }

    /**
     * Sets the director of this DVD.
     * Alias for {@link Item#setResponsable(String)}.
     *
     * @param director the director's name
     */
    public void setDirector(String director) {
        super.setResponsable(director);
    }

    @Override
    public String toString() {
        return String.format("DVD {\n" +
                super.toString() +
                "Director: %s\n" +
                "Duration: %dm\n"+
                "} \n",
                this.getDirector(), duration);
    }
}
