package epam.eremenko.restaurant.service;

import epam.eremenko.restaurant.dto.UserDto;
import epam.eremenko.restaurant.entity.User;
import epam.eremenko.restaurant.service.exception.ServiceException;

public interface UserService {
    void add(UserDto userDto) throws ServiceException;

    User get(UserDto userDto) throws ServiceException;
}
