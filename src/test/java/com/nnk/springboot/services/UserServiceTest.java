package com.nnk.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceAlreadyExistsException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class UserServiceTest {

  @Autowired
  private UserServiceImpl userService;

  @MockBean
  private UserRepository userRepository;
  @MockBean
  private PasswordEncoder encoder;

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
  void addTest() throws ResourceAlreadyExistsException {
    // GIVEN
    UserDto newUser = new UserDto(0, "Username", "User", "USER");
    newUser.setPassword("Password");
    User expectedUser = new User("Username", "EncodedPassword", "User", "USER");
    when(encoder.encode(anyString())).thenReturn("EncodedPassword");

    // WHEN
    userService.add(newUser);

    // THEN
    verify(userRepository, times(1)).save(userArgumentCaptor.capture());
    verify(encoder, times(1)).encode("Password");
    assertThat(userArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedUser);
  }

  @DisplayName("Add a new User when username already exists should throw an exception")
  @Test
  void addWhenUsernameAlreadyExistsTest() {
    // GIVEN
    UserDto newUser = new UserDto(0, "ExistingUsername", "User", "USER");
    when(userRepository.existsByUsername(anyString())).thenReturn(true);

    // WHEN
    assertThatThrownBy(() -> userService.add(newUser))

        // THEN
        .isInstanceOf(ResourceAlreadyExistsException.class)
        .hasMessage("This username is already used");
    verify(userRepository, times(0)).save(any(User.class));
    verify(userRepository, times(1)).existsByUsername("ExistingUsername");
  }
  
  @DisplayName("Update a User should persist it into database")
  @Test
  void updateTest() throws ResourceNotFoundException, ResourceAlreadyExistsException {
    // GIVEN
    UserDto updateUser = new UserDto(1, "Update Username", "Update User", "ADMIN");
    updateUser.setPassword("UpdatePassword");
    User expectedUser = new User("Update Username", "EncodedPassword", "Update User", "ADMIN");
    expectedUser.setId(1);
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(userTest));
    when(encoder.encode(anyString())).thenReturn("EncodedPassword");

    // WHEN
    userService.update(updateUser);

    // THEN
    verify(userRepository, times(1)).findById(1);
    verify(encoder, times(1)).encode("UpdatePassword");
    verify(userRepository, times(1)).save(userArgumentCaptor.capture());
    assertThat(userArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedUser);
  }

  @DisplayName("Update a User should persist it into database")
  @Test
  void updateSameUsernameTest() throws ResourceNotFoundException, ResourceAlreadyExistsException {
    // GIVEN
    UserDto updateUser = new UserDto(1, "Username", "Update User", "ADMIN");
    updateUser.setPassword("UpdatePassword");
    User expectedUser = new User("Username", "EncodedPassword", "Update User", "ADMIN");
    expectedUser.setId(1);
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(userTest));
    when(encoder.encode(anyString())).thenReturn("EncodedPassword");

    // WHEN
    userService.update(updateUser);

    // THEN
    verify(userRepository, times(1)).findById(1);
    verify(encoder, times(1)).encode("UpdatePassword");
    verify(userRepository, times(1)).save(userArgumentCaptor.capture());
    assertThat(userArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedUser);
  }

  @DisplayName("Update a User when username already exists should throw an exception")
  @Test
  void updateWhenNewUsernameAlreadyExistsTest() {
    // GIVEN
    UserDto updateUser = new UserDto(1, "Update ExistingUsername", "Update User", "ADMIN");
    updateUser.setPassword("UpdatePassword");
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(userTest));
    when(userRepository.existsByUsername(anyString())).thenReturn(true);

    // WHEN
    assertThatThrownBy(() -> userService.update(updateUser))

        // THEN
        .isInstanceOf(ResourceAlreadyExistsException.class)
        .hasMessage("This username is already used");
    verify(userRepository, times(1)).findById(1);
    verify(userRepository, times(1)).existsByUsername("Update ExistingUsername");
    verify(userRepository, times(0)).save(any(User.class));
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
