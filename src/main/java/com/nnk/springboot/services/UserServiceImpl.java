package com.nnk.springboot.services;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.repositories.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class implementation for User entity CRUD operations.
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(user -> new UserDto(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            user.getRole()
        ))
        .collect(Collectors.toList());
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
