package com.nnk.springboot.services;

import com.nnk.springboot.dto.UserDto;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service class implementation for User entity CRUD operations.
 */
@Service
public class UserServiceImpl implements UserService {

  @Override
  public List<UserDto> findAll() {
    return null;
  }

  @Override
  public UserDto findById(int id) {
    return null;
  }

  @Override
  public void add(UserDto userDto) {

  }

  @Override
  public void update(UserDto userDto) {

  }

  @Override
  public void delete(int id) {

  }

}
