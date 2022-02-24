package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceAlreadyExistsException;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import com.nnk.springboot.utils.UserMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class implementation for User entity CRUD operations.
 */
@Service
public class UserServiceImpl implements UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(UserMapper::toDto)
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserDto findById(int id) throws ResourceNotFoundException {
    return UserMapper.toDto(getOrThrowException(id));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add(UserDto userDto) throws ResourceAlreadyExistsException {
    checkUsernameUnique(userDto.getUsername());
    User userToAdd = new User();
    UserMapper.toEntity(userToAdd, userDto);
    userToAdd.setPassword(encoder.encode(userToAdd.getPassword()));
    userRepository.save(userToAdd);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(UserDto userDto)
      throws ResourceNotFoundException, ResourceAlreadyExistsException {
    User userToUpdate = getOrThrowException(userDto.getId());
    if (!userDto.getUsername().equals(userToUpdate.getUsername())) {
      checkUsernameUnique(userDto.getUsername());
    }
    UserMapper.toEntity(userToUpdate, userDto);
    userToUpdate.setPassword(encoder.encode(userToUpdate.getPassword()));
    userRepository.save(userToUpdate);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(int id) throws ResourceNotFoundException {
    User userToDelete = getOrThrowException(id);
    userRepository.delete(userToDelete);
  }

  private User getOrThrowException(int id) throws ResourceNotFoundException {
    return userRepository.findById(id)
        .orElseThrow(() -> {
          LOGGER.error("The User with id {} is not found", id);
          return new ResourceNotFoundException("This user is not found");
        });
  }

  private void checkUsernameUnique(String username) throws ResourceAlreadyExistsException {
    if (userRepository.existsByUsername(username)) {
      LOGGER.error("The username {} is already used", username);
      throw new ResourceAlreadyExistsException("This username is already used");
    }
  }

}
