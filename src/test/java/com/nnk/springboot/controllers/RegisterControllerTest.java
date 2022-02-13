package com.nnk.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceAlreadyExistsException;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RegisterController.class)
class RegisterControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;
  @MockBean
  private UserDetailsService userDetailsService;

  @Captor
  private ArgumentCaptor<UserDto> dtoCaptor;

  @DisplayName("GET /register should return view")
  @Test
  @WithAnonymousUser
  void addUserFormTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/register"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("userDto"));
  }

  @DisplayName("POST valid DTO on /register/validate should persist User then return view")
  @Test
  @WithAnonymousUser
  void validateTest() throws Exception {
    // GIVEN
    UserDto expectedDto = new UserDto(null, "Username 1", "User 1", "USER");
    expectedDto.setPassword("PasswdA1=");

    // WHEN
    mockMvc.perform(post("/register/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", expectedDto.getUsername())
            .param("password", expectedDto.getPassword())
            .param("fullName", expectedDto.getFullName())
            .param("role", expectedDto.getRole())
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/"));
    verify(userService, times(1)).add(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST valid DTO on /register/validate when username already existed should return from view with error message")
  @Test
  @WithAnonymousUser
  void validateWhenUsernameAlreadyExistsTest() throws Exception {
    // GIVEN
    UserDto expectedDto = new UserDto(null, "ExistingUsername", "User 1", "USER");
    expectedDto.setPassword("PasswdA1=");
    doThrow(new ResourceAlreadyExistsException("This username is already used")).when(userService).add(any(UserDto.class));

    // WHEN
    mockMvc.perform(post("/register/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", expectedDto.getUsername())
            .param("password", expectedDto.getPassword())
            .param("fullName", expectedDto.getFullName())
            .param("role", expectedDto.getRole())
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("error"));
    verify(userService, times(1)).add(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST invalid DTO on /register/validate should return form view")
  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void validateWhenInvalidTest() throws Exception {
    // WHEN
    mockMvc.perform(post("/register/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "This user's username exceed the 125 characters limit " +
                "to test the validation of the Dto when the add User form is submit by the user")
            .param("password", "password")
            .param("fullName", "")
            .param("role", "USER")
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeHasFieldErrors("userDto", "username"))
        .andExpect(model().attributeHasFieldErrors("userDto", "password"))
        .andExpect(model().attributeHasFieldErrors("userDto", "fullName"));
    verify(userService, times(0)).add(any(UserDto.class));
  }

}
