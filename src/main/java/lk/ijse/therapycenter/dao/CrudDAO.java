package lk.ijse.therapycenter.dao;

import java.util.List;


public interface CrudDAO<T, ID> {
    boolean save(T entity);
    boolean update(T entity);
    boolean delete(ID id);
    T findById(ID id);
    List<T> findAll();
}