package org.example;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.*;
import java.util.List;

public class GameOfLifeRow extends GameOfLifeLine implements Cloneable, Serializable {
    @Serial
    private static final long serialVersionUID = -3332997159680887604L;

    public GameOfLifeRow(List<GameOfLifeCell> cells) {
        super(cells);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 29)
                .appendSuper(super.hashCode())
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public GameOfLifeRow clone() {
        return (GameOfLifeRow) super.clone();
    }
}
