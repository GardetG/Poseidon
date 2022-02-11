package com.nnk.springboot.services;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceAlreadyExistsException;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service interface for User entity CRUD operations.
 */
@Service
public interface UserService {

  /**
   * Find all persisted User and return the corresponding list of DTO.
   *
   * @return List of UserDto
   */
  List<UserDto> findAll();

  /**
   * Find a persisted User by Id and return the corresponding DTO.
   *
   * @param id of the User
   * @return UserDto
   * @throws ResourceNotFoundException when User not found
   */
  UserDto findById(int id) throws ResourceNotFoundException;

  /**
   * Add a new User from values of the DTO and persist it.
   *
   * @param userDto to create
   */
  void add(UserDto userDto) throws ResourceAlreadyExistsException;

  /**
   * Update a User with value from the DTO and persist it.
   *
   * @param userDto to update
   * @throws ResourceNotFoundException when User not found
   */
  void update(UserDto userDto) throws ResourceNotFoundException, ResourceAlreadyExistsException;

  /**
   * Delete a persisted User by Id.
   *
   * @param id of the User
   * @throws ResourceNotFoundException when User not found
   */
  void delete(int id) throws ResourceNotFoundException;

}
