package com.nnk.springboot.utils;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;

/**
 * Mapper utility class to map User DTO and entity.
 */
public class UserMapper {

  private UserMapper() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Map a User into DTO.
   *
   * @param user to map
   * @return corresponding UserDto
   */
  public static UserDto toDto(User user) {
    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getFullName(),
        user.getRole()
    );
  }

  /**
   * Map a UserDto into entity.
   *
   * @param user    to map into
   * @param userDto to map from
   */
  public static void toEntity(User user, UserDto userDto) {
    user.setUsername(userDto.getUsername());
    user.setPassword(userDto.getPassword());
    user.setFullName(userDto.getFullName());
    user.setRole(userDto.getRole());
  }

}