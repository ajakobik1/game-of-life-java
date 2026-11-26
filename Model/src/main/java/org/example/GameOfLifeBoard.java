package org.example;

// przyjmujemy, ze jesli mamy wartosc true, to komorka jest zywa, a false oznacza komorke martwa
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class GameOfLifeBoard implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 3084819851427068607L;
    //board wyglada mniej wiecej tak
    //0 1 2
    //3 4 5
    //6 7 8
    //gdzie te liczby to ich indeks na ArrayList
    //wiec zeby dostac sie do np. pola (0, 2) musimy uzyc wzoru
    //i + j * szerokosc
    private List<GameOfLifeCell> board;
    private GameOfLifeSimulator simulator;
    private List<GameOfLifeRow> rows;
    private List<GameOfLifeColumn> columns;
    

    //zwraca jedno pole w tablicy
    public boolean get(int x, int y) {
        int col = calculateColumn(x);
        int row = calculateRow(y);
        return board.get(col + row * getWidth()).getCellValue();
    }

    //ustawia jedno pole w tablicy na wybrana wartosc
    public void set(int x, int y, boolean value) {
        int col = calculateColumn(x);
        int row = calculateRow(y);

        board.get(col + row * getWidth()).updateState(value);
    }

    //zwraca nastepna wartosc
    public boolean getNext(int x, int y) {
        int col = calculateColumn(x);
        int row = calculateRow(y);

        return board.get(col + row * getWidth()).nextState();
    }


    public int getHeight() {
        return rows.size();
    }

    public int getWidth() {
        return columns.size();
    }

    public GameOfLifeRow getRow(int y) {
        y = calculateRow(y);
        return rows.get(y);
    }

    public GameOfLifeColumn getColumn(int x) {
        x = calculateColumn(x);
        return columns.get(x);
    }

    private int calculateColumn(int x) {
        int col = x % getWidth();
        if (col < 0) {
            col += getWidth();
        }
        return col;
    }

    private int calculateRow(int y) {
        int row = y % getHeight();
        if (row < 0) {
            row += getHeight();
        }
        return row;
    }

    // n = wysokosc, m = dlugosc
    // konstruktor iniciuje nowa planasze m x n, wypelniana jest losowo dzieki Random

    public GameOfLifeBoard(int n, int m, GameOfLifeSimulator simulator) {
        //injection
        this.simulator = simulator;

        GameOfLifeCell[] boardArrayConv = new GameOfLifeCell[n * m];
        GameOfLifeRow[] rowArrayConv = new GameOfLifeRow[n];
        GameOfLifeColumn[] columnArrayConv = new GameOfLifeColumn[m];
        GameOfLifeNeighbors neighbors = new GameOfLifeNeighbors(this);

        //inicjacja klasy random
        Random ran = new Random();

        //tworzenie boarda i wierszy
        for (int i = 0; i < n; i++) {
            GameOfLifeCell[] rowArray = new GameOfLifeCell[m];
            for (int j = 0; j < m; j++) {
                GameOfLifeCell cell = new GameOfLifeCell(ran.nextBoolean(), j, i, neighbors);
                boardArrayConv[j + i * m] = cell;
                rowArray[j] = cell;
            }
            GameOfLifeRow row = new GameOfLifeRow(Arrays.asList(rowArray));
            rowArrayConv[i] = row;
        }

        //tworzenie kolumn
        for (int i = 0; i < m; i++) {
            GameOfLifeCell[] columnArray = new GameOfLifeCell[n];
            for (int j = 0; j < n; j++) {
                columnArray[j] = boardArrayConv[i + m * j];
            }
            GameOfLifeColumn column = new GameOfLifeColumn(Arrays.asList(columnArray));
            columnArrayConv[i] = column;
        }

        //przekazywanie do atrybutow przez liste, aby nie dalo sie jej modyfikowac
        board = Arrays.asList(boardArrayConv);
        columns = Arrays.asList(columnArrayConv);
        rows = Arrays.asList(rowArrayConv);
    }

    public void doSimulationStep() {
        simulator.doStep(this);
    }
    // przechodzi do nastepnego kroku symulacji, liczac ile kazda komorka ma zywych komorek w sasiedztwie i decydujac
    // co sie z nia dalej stanie

    public int hashCode() {
        return new HashCodeBuilder(17, 23)
                .append(board)
                .append(simulator)
                .append(rows)
                .append(columns)
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

        GameOfLifeBoard temp = (GameOfLifeBoard) obj;

        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(board, temp.board)
                .append(columns, temp.columns)
                .append(simulator, temp.simulator)
                .append(rows, temp.rows)
                .isEquals();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("board", board)
                .append("simulator", simulator)
                .append("rows", rows)
                .append("columns", columns)
                .toString();
    }

    @Serial
    private void writeObject(ObjectOutputStream out)
            throws IOException {
        out.writeObject(board);
        out.writeInt(columns.size());
        out.writeInt(rows.size());
        out.writeObject(simulator);
    }

    @Serial
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        board = (List<GameOfLifeCell>) in.readObject();

        int columnsSize = in.readInt();
        int rowsSize = in.readInt();
        GameOfLifeColumn[] columnArrayConv = new GameOfLifeColumn[columnsSize];
        GameOfLifeRow[] rowArrayConv = new GameOfLifeRow[rowsSize];

        //tworzenie boarda i wierszy
        for (int i = 0; i < rowsSize; i++) {
            GameOfLifeCell[] rowArray = new GameOfLifeCell[columnsSize];
            for (int j = 0; j < columnsSize; j++) {
                rowArray[j] = board.get(j + i * columnsSize);
            }
            GameOfLifeRow row = new GameOfLifeRow(Arrays.asList(rowArray));
            rowArrayConv[i] = row;
        }

        //tworzenie kolumn
        for (int i = 0; i < columnsSize; i++) {
            GameOfLifeCell[] columnArray = new GameOfLifeCell[rowsSize];
            for (int j = 0; j < rowsSize; j++) {
                columnArray[j] = board.get(i + j * columnsSize);
            }
            GameOfLifeColumn column = new GameOfLifeColumn(Arrays.asList(columnArray));
            columnArrayConv[i] = column;
        }
        rows = Arrays.asList(rowArrayConv);
        columns = Arrays.asList(columnArrayConv);
        simulator = (GameOfLifeSimulator) in.readObject();
    }

    @Override
    public GameOfLifeBoard clone() {
        try {
            //Serializacja obiektu
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(byteOutputStream);
            output.writeObject(this);

            //Deserializacja
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
            ObjectInputStream input = new ObjectInputStream(byteInputStream);
            return (GameOfLifeBoard) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

