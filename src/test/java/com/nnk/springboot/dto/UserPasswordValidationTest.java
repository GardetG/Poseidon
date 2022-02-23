package com.nnk.springboot.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Stream;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserPasswordValidationTest {

  @Autowired
  Validator validator;

  private UserDto userDto;

  // au moins une lettre majuscule, au moins 8 caractères, au moins un chiffre et un symbole) ;
  public static Stream<String> invalidPasswordProvider() {
    return Stream.of(
        "",             // Empty
        "A1=",          // Less than 8 characters
        "        ",     // Blank
        "A1=     ",     // Contain whitespace
        "passwdA1",     // No special characters
        "passwdA=",     // No digit
        "passwd1=",     // No uppercase
        "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$_The_user_password_exceed_125_characters_$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"
    );
  }

  @BeforeEach
  void setUp() {
    userDto = new UserDto();
  }

  @DisplayName("Validate a UserDto with invalid password return a constraint violation")
  @ParameterizedTest(name = "{index} - Password \"{0}\" is invalid")
  @MethodSource("invalidPasswordProvider")
  void invalidUserPasswordTest(String password) {
    userDto.setPassword(password);

    Set<ConstraintViolation<UserDto>> violations = validator.validateProperty(userDto, "password") ;
    assertThat(violations.size()).isNotZero();
  }

  @DisplayName("Validate a UserDto with valid password return no constraint violation")
  @Test
  void validUserPasswordTest() {
    userDto.setPassword("passwordA1=");

    Set<ConstraintViolation<UserDto>> violations = validator.validateProperty(userDto, "password");
    assertThat(violations).isEmpty();
  }

}
