package com.nnk.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CustomOAuth2UserServiceTest {

  @Autowired
  private CustomOAuth2UserService OAuth2UserService;

  @MockBean
  private UserRepository userRepository;

  @Captor
  private ArgumentCaptor<User> userCaptor;

  private OAuth2User oAuth2User;

  @BeforeEach
  void setUp() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("login", "Username");
    attributes.put("name", "User FullName");
    oAuth2User = new DefaultOAuth2User(authorities, attributes, "login");
  }

  @DisplayName("Load a User should return User with OAuth2User attributes and persisted data")
  @Test
  void loadUserFromRepositoryTest() {
    // GIVEN
    User userTest = new User("Username", "Password", "User", "USER");
    userTest.setId(1);
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userTest));

    // WHEN
    OAuth2User actualUser = OAuth2UserService.loadUserFromRepository(oAuth2User);

    // THEN
    assertThat(actualUser).isEqualTo(userTest);
    assertThat(actualUser.getAttributes()).isEqualTo(oAuth2User.getAttributes());
    verify(userRepository, times(1)).findByUsername("Username");
    verify(userRepository, times(0)).save(any(User.class));
  }

  @DisplayName("Load a User when not persisted should register it and return User with OAuth2User attributes")
  @Test
  void loadUserFromRepositoryWhenNotPersistedTest() {
    // GIVEN
    User expectedUser = new User("Username", null, "User FullName", "USER");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    when(userRepository.save(any(User.class))).thenReturn(expectedUser);

    // WHEN
    OAuth2User actualUser = OAuth2UserService.loadUserFromRepository(oAuth2User);

    // THEN
    assertThat(actualUser).isEqualTo(expectedUser);
    assertThat(actualUser.getAttributes()).isEqualTo(oAuth2User.getAttributes());
    verify(userRepository, times(1)).findByUsername("Username");
    verify(userRepository, times(1)).save(userCaptor.capture());
    assertThat(userCaptor.getValue()).usingRecursiveComparison().ignoringFields("attributes").isEqualTo(expectedUser);
  }

}
