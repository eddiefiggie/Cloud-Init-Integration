package edu.csudh.figueroa.project;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> get(long id);

    List<T> getAll();

    void save(T t, String path);

    void update(T t, String[] params);

    void delete(T t, String path);
}