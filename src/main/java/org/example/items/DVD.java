package org.example.items;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * This is a subclass that describes a magazine it is just a basic item with an extra duration parameter
 * Responsable is externally called director
 */
@EqualsAndHashCode (callSuper = true)
public class DVD extends Item {
    @Getter @Setter
    private int duration;

    public DVD(String title, String director, int duration) {
        super(title, director);
        this.duration = duration;
        super.type = Type.DVD;
    }

    public String getDirector() {
        return super.getResponsable();
    }

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
