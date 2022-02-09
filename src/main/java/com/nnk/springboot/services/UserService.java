package com.nnk.springboot.services;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service interface for User entity CRUD operations.
 */
@Service
public interface UserService {

  List<UserDto> findAll();

  UserDto findById(int id) throws ResourceNotFoundException;

  void add(UserDto userDto);

  void update(UserDto userDto);

  void delete(int id);

}
