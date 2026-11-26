package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.*;
import java.util.List;

abstract class GameOfLifeLine implements Cloneable, Serializable {

    @Serial
    private static final long serialVersionUID = -2906950546649641521L;
    private List<GameOfLifeCell> cells;

    public int countAliveCells() {
        int count = 0;
        for (GameOfLifeCell cell : cells) {
            if (cell.getCellValue()) {
                count++;
            }
        }
        return count;
    }

    public int countDeadCells() {
        int count = 0;
        for (GameOfLifeCell cell : cells) {
            if (!cell.getCellValue()) {
                count++;
            }
        }
        return count;
    }

    public GameOfLifeLine(List<GameOfLifeCell> cells) {
        this.cells = cells;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(31, 29)
                .append(cells)
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

        GameOfLifeLine temp = (GameOfLifeLine) obj;

        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(cells, temp.cells)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("cells", cells)
                .toString();
    }

    @Serial
    private void writeObject(ObjectOutputStream out)
            throws IOException {
        out.writeObject(cells);
    }

    @Serial
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        cells = (List<GameOfLifeCell>) in.readObject();
    }

    @Override
    public GameOfLifeLine clone() {
        try {
            //Serializacja obiektu
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(byteOutputStream);
            output.writeObject(this);

            //Deserializacja
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
            ObjectInputStream input = new ObjectInputStream(byteInputStream);
            return (GameOfLifeLine) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
