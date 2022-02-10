package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
  public UserDto findById(int id) throws ResourceNotFoundException {
    return userRepository.findById(id)
        .map(user -> new UserDto(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            user.getRole()
        ))
        .orElseThrow(() -> new ResourceNotFoundException("This user is not found"));
  }

  @Override
  public void add(UserDto userDto) {
    User userToAdd = new User(
        userDto.getUsername(),
        userDto.getPassword(),
        userDto.getFullName(),
        userDto.getRole()
    );
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    userToAdd.setPassword(encoder.encode(userToAdd.getPassword()));
    userRepository.save(userToAdd);
  }

  @Override
  public void update(UserDto userDto) throws ResourceNotFoundException {
    User userToUpdate = userRepository.findById(userDto.getId())
        .orElseThrow(() -> new ResourceNotFoundException("This user is not found"));
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    userToUpdate.setUsername(userDto.getUsername());
    userToUpdate.setPassword(encoder.encode(userDto.getPassword()));
    userToUpdate.setFullName(userDto.getFullName());
    userToUpdate.setRole(userDto.getRole());
    userRepository.save(userToUpdate);
  }

  @Override
  public void delete(int id) {

  }

}
