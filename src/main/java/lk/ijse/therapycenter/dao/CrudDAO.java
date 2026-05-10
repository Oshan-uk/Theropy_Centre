package lk.ijse.therapycenter.dao;

import java.util.List;

public interface CrudDAO<T> {

    boolean save(T dto);

    boolean update(T dto);

    boolean delete(String id);

    T search(String id);

    List<T> getAll();
}