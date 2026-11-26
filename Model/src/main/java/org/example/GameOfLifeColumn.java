package org.example;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.*;
import java.util.List;

public class GameOfLifeColumn extends GameOfLifeLine implements Cloneable, Serializable {
    @Serial
    private static final long serialVersionUID = -5046693636855869224L;

    public GameOfLifeColumn(List<GameOfLifeCell> cells) {
        super(cells);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 29)
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
    public GameOfLifeColumn clone() {
        return (GameOfLifeColumn) super.clone();
    }
}
