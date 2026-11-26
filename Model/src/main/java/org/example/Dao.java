package org.example;

import org.example.exceptions.GameOfLifeDaoIoException;

public interface Dao<T> extends AutoCloseable {

    T read() throws GameOfLifeDaoIoException;

    void write(T obj) throws GameOfLifeDaoIoException;
}
