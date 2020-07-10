package epam.eremenko.restaurant.service.impl;

import epam.eremenko.restaurant.dao.Dao;
import epam.eremenko.restaurant.dao.impl.DaoFactory;
import epam.eremenko.restaurant.dao.exception.DaoException;
import epam.eremenko.restaurant.dao.table.UserTable;
import epam.eremenko.restaurant.dto.UserDto;
import epam.eremenko.restaurant.entity.BeanFactory;
import epam.eremenko.restaurant.entity.User;
import epam.eremenko.restaurant.service.UserService;
import epam.eremenko.restaurant.service.exception.ServiceException;
import epam.eremenko.restaurant.service.util.Encoder;
import epam.eremenko.restaurant.service.util.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final Dao<UserDto, UserTable> USER_DAO = DaoFactory.getInstance().getUserDao();
    private static final BeanFactory USER_FACTORY = BeanFactory.getInstance();

    public void add(UserDto userDto) throws ServiceException {
        validateDto(userDto);
        userDto.setPassword(encodePassword(userDto.getPassword()));
        saveUserToDatabase(userDto);
    }

    private void validateDto(UserDto userDto) throws ServiceException {
        new UserValidator().validate(userDto);
    }

    private String encodePassword(String password) {
        return Encoder.md5Apache(password);
    }

    private void saveUserToDatabase(UserDto userDto) throws ServiceException {
        try {
            USER_DAO.add(userDto);
        } catch (DaoException ex) {
            LOGGER.debug(ex.toString());
            throw new ServiceException("Username already exists");
        }
    }

    public User get(UserDto userDto) throws ServiceException {
        String password = encodePassword(userDto.getPassword());
        userDto = getUserFromDatabase(userDto);
        checkPassword(password, userDto);
        return USER_FACTORY.getUser(userDto);
    }

    private void checkPassword(String password, UserDto userDto) throws ServiceException {
        if (!password.equals(userDto.getPassword())) {
            throw new ServiceException("Invalid password");
        }
    }

    private UserDto getUserFromDatabase(UserDto userDto) throws ServiceException {
        try {
            return USER_DAO.get(userDto);
        } catch (DaoException ex) {
            LOGGER.debug(ex.getMessage());
            throw new ServiceException("Incorrect data or user doesn't exist.");
        }
    }
}
