package com.nnk.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.repositories.UserRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class UserServiceTest {

  @Autowired
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  private User userTest;
  private UserDto userDtoTest;

  @BeforeEach
  void setUp() {
    userTest = new User("Username", "Password","User" , "USER");
    userTest.setId(1);
    userDtoTest = new UserDto(1, "Username", "Password","User" , "USER");
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
  
}
