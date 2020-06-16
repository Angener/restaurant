package epam.eremenko.restaurant.dao;

import epam.eremenko.restaurant.dao.exception.DaoException;
import epam.eremenko.restaurant.dto.Dto;

public interface Dao<T extends Dto, E extends Enum<E>> {
    void add(T dto) throws DaoException;

    void update(E field, T dto) throws DaoException;

    T get(T dto) throws DaoException;

    void delete(T dto) throws DaoException;
}
