package com.studentportal.hibernate;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, Id extends Serializable> {

    public T findById(Id id);

    public List<T> findAll();

    public void save(T entity);

    public void update(T entity);

    public void delete(T entity);

    public void deleteAll();
}
