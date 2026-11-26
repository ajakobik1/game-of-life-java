package org.example;

import java.io.IOException;

public interface DaoFactory<T> {
    Dao<T> createFile(String fileName) throws IOException;
}
