package com.nnk.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class UserServiceTest {

  @Autowired
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @Captor
  private ArgumentCaptor<User> userArgumentCaptor;

  private User userTest;
  private UserDto userDtoTest;

  @BeforeEach
  void setUp() {
    userTest = new User("Username", "Password", "User", "USER");
    userTest.setId(1);
    userDtoTest = new UserDto(1, "Username", "User", "USER");
  }

  @DisplayName("Find all should return a list of UserDto")
  @Test
  void findAllTest() {
    // GIVEN
    when(userRepository.findAll()).thenReturn(Collections.singletonList(userTest));

    // WHEN
    List<UserDto> actualDtoList = userService.findAll();

    // THEN
    assertThat(actualDtoList).usingRecursiveFieldByFieldElementComparator()
        .containsExactly(userDtoTest);
    verify(userRepository, times(1)).findAll();
  }

  @DisplayName("Find all when no User in database should return an empty list")
  @Test
  void findAllWhenEmptyTest() {
    // GIVEN
    when(userRepository.findAll()).thenReturn(Collections.emptyList());

    // WHEN
    List<UserDto> actualDtoList = userService.findAll();

    // THEN
    assertThat(actualDtoList).isEmpty();
    verify(userRepository, times(1)).findAll();
  }

  @DisplayName("Find by id should return the corresponding UserDto")
  @Test
  void findByIdTest() throws ResourceNotFoundException {
    // GIVEN
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(userTest));

    // WHEN
    UserDto actualDto = userService.findById(1);

    // THEN
    assertThat(actualDto).usingRecursiveComparison().isEqualTo(userDtoTest);
    verify(userRepository, times(1)).findById(1);
  }

  @DisplayName("Find by id when the corresponding User is not found should throw an exception")
  @Test
  void findByIdWhenNotFoundTest() {
    // GIVEN
    when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

    // WHEN
    assertThatThrownBy(() -> userService.findById(9))

        // THEN
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("This user is not found");
    verify(userRepository, times(1)).findById(9);
  }

  @DisplayName("Add a new User should persist it into database with hashed password")
  @Test
  void addTest() {
    // GIVEN
    UserDto newUser = new UserDto(0, "Username", "User", "USER");
    newUser.setPassword("Password");
    User expectedUser = new User("Username", "Password", "User", "USER");

    // WHEN
    userService.add(newUser);

    // THEN
    verify(userRepository, times(1)).save(userArgumentCaptor.capture());
    assertThat(userArgumentCaptor.getValue()).usingRecursiveComparison().ignoringFields("password").isEqualTo(expectedUser);
    assertThat(userArgumentCaptor.getValue().getPassword()).isNotBlank();
    assertThat(userArgumentCaptor.getValue().getPassword()).isNotEqualTo(newUser.getPassword());
  }
  
  @DisplayName("Update a User should persist it into database")
  @Test
  void updateTest() throws ResourceNotFoundException {
    // GIVEN
    UserDto updateUser = new UserDto(1, "Update Username", "Update User", "ADMIN");
    updateUser.setPassword("UpdatePassword");
    User expectedUser = new User("Update Username", "UpdatePassword", "Update User", "ADMIN");
    expectedUser.setId(1);
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(userTest));

    // WHEN
    userService.update(updateUser);

    // THEN
    verify(userRepository, times(1)).findById(1);
    verify(userRepository, times(1)).save(userArgumentCaptor.capture());
    assertThat(userArgumentCaptor.getValue()).usingRecursiveComparison().ignoringFields("password").isEqualTo(expectedUser);
    assertThat(userArgumentCaptor.getValue().getPassword()).isNotBlank();
    assertThat(userArgumentCaptor.getValue().getPassword()).isNotEqualTo(expectedUser.getPassword());
  }

  @DisplayName("Update a User when it's not found should throw an exception")
  @Test
  void updateWhenNotFoundTest() {
    // GIVEN
    UserDto updateUser = new UserDto(9, "Update Username", "Update User", "ADMIN");
    when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

    // WHEN
    assertThatThrownBy(() -> userService.update(updateUser))

        // THEN
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("This user is not found");
    verify(userRepository, times(1)).findById(9);
    verify(userRepository, times(0)).save(any(User.class));
  }
  
  @DisplayName("Delete a User should delete it from database")
  @Test
  void deleteTest() throws ResourceNotFoundException {
    // GIVEN
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(userTest));

    // WHEN
    userService.delete(1);

    // THEN
    verify(userRepository, times(1)).findById(1);
    verify(userRepository, times(1)).delete(userArgumentCaptor.capture());
    assertThat(userArgumentCaptor.getValue()).isEqualTo(userTest);
  }

  @DisplayName("Delete a User when it's not found should throw an exception")
  @Test
  void deleteWhenNotFoundTest() {
    // GIVEN
    when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

    // WHEN
    assertThatThrownBy(() -> userService.delete(9))

        // THEN
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("This user is not found");
    verify(userRepository, times(1)).findById(9);
    verify(userRepository, times(0)).delete(any(User.class));
  }

}
