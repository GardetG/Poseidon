package com.nnk.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
class CustomUserDetailsServiceTest {

  @Autowired
  private UserDetailsService userService;

  @MockBean
  private UserRepository userRepository;

  @DisplayName("Load a User by Username should return it from database")
  @Test
  void loadUserByUsernameTest() {
    // GIVEN
    User userTest = new User("Username", "Password", "User", "USER");
    userTest.setId(1);
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userTest));

    // WHEN
    UserDetails actualUser = userService.loadUserByUsername("Username");

    // THEN
    verify(userRepository, times(1)).findByUsername("Username");
    assertThat(actualUser).isEqualTo(userTest);
  }

  @DisplayName("Load a User by Username when it's not found should throw an exception")
  @Test
  void loadUserByUsernameWhenNotFoundTest() {
    // GIVEN
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    // WHEN
    assertThatThrownBy(() -> userService.loadUserByUsername("NonExistentUser"))

        // THEN
        .isInstanceOf(UsernameNotFoundException.class);
    verify(userRepository, times(1)).findByUsername("NonExistentUser");
  }

}
