package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.*;

public class GameOfLifeCell implements Serializable, Cloneable, Comparable<GameOfLifeCell> {
    @Serial
    private static final long serialVersionUID = -4584061924511485567L;
    private boolean value;
    private int column;
    private int row;
    private GameOfLifeNeighbors neighbors;

    //WARNING
    //uzywanie tego konstruktora POZA boardem jest bardzo niewskazane, przez neighbors moze dojsc do pewnych
    // nieporzadanych zachowan
    public GameOfLifeCell(boolean value, int x, int y, GameOfLifeNeighbors neighbors) {
        this.value = value;
        this.neighbors = neighbors;
        this.column = x;
        this.row = y;
    }

    public boolean getCellValue() {
        return value;
    }

    public void updateState(boolean newState) {
        this.value = newState;
    }

    public boolean nextState() {
        int aliveNeighbors = neighbors.countAliveNeighbors(column, row);

        if (value) {
            // Żywa komórka żyje dalej, jeśli ma 2 lub 3 żywych sąsiadów
            return aliveNeighbors == 2 || aliveNeighbors == 3;
        } else {
            // Martwa komórka odradza się, jeśli ma dokładnie 3 żywych sąsiadów
            return aliveNeighbors == 3;
        }

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("value", value)
                .append("column", column)
                .append("row", row)
                .append("neighbors", neighbors)
                .toString();
    }

    public int hashCode() {
        return new HashCodeBuilder(13, 23)
                .append(value)
                .append(column)
                .append(row)
                .append(neighbors)
                .toHashCode();
    }

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

        GameOfLifeCell temp = (GameOfLifeCell) obj;

        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(value, temp.value)
                .append(column, temp.column)
                .append(row, temp.row)
                .append(neighbors, temp.neighbors)
                .isEquals();
    }

    @Serial
    private void writeObject(ObjectOutputStream out)
            throws IOException {
        out.writeInt(column);
        out.writeInt(row);
        out.writeBoolean(value);
        out.writeObject(neighbors);
    }

    @Serial
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        column = in.readInt();
        row = in.readInt();
        value = in.readBoolean();
        neighbors = (GameOfLifeNeighbors) in.readObject();
    }

    @Override
    public GameOfLifeCell clone() {
        try {
            //Serializacja obiektu
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(byteOutputStream);
            output.writeObject(this);

            //Deserializacja
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
            ObjectInputStream input = new ObjectInputStream(byteInputStream);
            return (GameOfLifeCell) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int compareTo(GameOfLifeCell obj) {
        if (obj == null) {
            throw new NullPointerException("Specified object is null");
        }
        if (obj == this) {
            return 0;
        }

        //Jezeli jest wczesniej na boardzie to jest "wiekszy", true > false
        if (this.row < obj.row) {
            return 1;
        } else if (this.row > obj.row) {
            return -1;
        } else {
            //jezeli ten cell jest true, to jezeli obj jest true to sa rowne
            //jezeli nie, to jest wiekszy (true > false)
            //w innym wypadku (jezeli ten cell jest false)
            // jezeli obj jest true to jest wiekszy, jezeli obj jest false to sa rowne
            if (this.value) {
                return obj.value ? 0 : 1;
            } else {
                return obj.value ? -1 : 0;
            }
        }
    }
}
