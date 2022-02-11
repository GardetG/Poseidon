package com.nnk.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceAlreadyExistsException;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.UserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;
  @MockBean
  private UserDetailsService userDetailsService;

  @Captor
  private ArgumentCaptor<UserDto> dtoCaptor;

  @DisplayName("GET /user/list when not authenticate should allow access")
  @Test
  @WithAnonymousUser
  void homeWhenNotAuthenticateTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/user/list"))

        // THEN
        .andExpect(status().isOk());
  }

  @DisplayName("GET /user/list should return view with list of User as attribute")
  @Test
  @WithMockUser(username="user")
  void homeTest() throws Exception {
    // GIVEN
    List<UserDto> DtoList = new ArrayList<>();
    DtoList.add(new UserDto(1, "Username 1", "User 1", "USER"));
    DtoList.add(new UserDto(2, "Username 2", "User 2", "USER"));
    when(userService.findAll()).thenReturn(DtoList);

    // WHEN
    mockMvc.perform(get("/user/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("user/list"))
        .andExpect(model().attributeExists("users"))
        .andExpect(model().attribute("users", DtoList));
    assertThat(DtoList).extracting("password").containsOnlyNulls();
  }

  @DisplayName("GET /user/add should return view")
  @Test
  @WithMockUser(username="user") 
  void addUserFormTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/user/add"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("user/add"));
  }

  @DisplayName("POST valid DTO on /user/validate should persist User then return view")
  @Test
  @WithMockUser(username="user") 
  void validateTest() throws Exception {
    // GIVEN
    UserDto expectedDto = new UserDto(null, "Username 1", "User 1", "USER");
    expectedDto.setPassword("PasswdA1=");

    // WHEN
    mockMvc.perform(post("/user/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", expectedDto.getUsername())
            .param("password", expectedDto.getPassword())
            .param("fullName", expectedDto.getFullName())
            .param("role", expectedDto.getRole())
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/user/list"));
    verify(userService, times(1)).add(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST valid DTO on /user/validate when username already existed should return from view with error message")
  @Test
  @WithMockUser(username="user")
  void validateWhenUsernameAlreadyExistsTest() throws Exception {
    // GIVEN
    UserDto expectedDto = new UserDto(null, "ExistingUsername", "User 1", "USER");
    expectedDto.setPassword("PasswdA1=");
    doThrow(new ResourceAlreadyExistsException("dfdf")).when(userService).add(any(UserDto.class));

    // WHEN
    mockMvc.perform(post("/user/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", expectedDto.getUsername())
            .param("password", expectedDto.getPassword())
            .param("fullName", expectedDto.getFullName())
            .param("role", expectedDto.getRole())
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("user/add"))
        .andExpect(model().attributeExists("error"));
    verify(userService, times(1)).add(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST invalid DTO on /user/validate should return form view")
  @Test
  @WithMockUser(username="user") 
  void validateWhenInvalidTest() throws Exception {
    // WHEN
    mockMvc.perform(post("/user/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username","This user's username exceed the 125 characters limit " +
                "to test the validation of the Dto when the add User form is submit by the user")
            .param("password", "password")
            .param("fullName", "")
            .param("role", "")
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("user/add"))
        .andExpect(model().attributeHasFieldErrors("userDto", "username"))
        .andExpect(model().attributeHasFieldErrors("userDto", "password"))
        .andExpect(model().attributeHasFieldErrors("userDto", "fullName"))
        .andExpect(model().attributeHasFieldErrors("userDto", "role"));
    verify(userService, times(0)).add(any(UserDto.class));
  }

  @DisplayName("GET /user/update should return view")
  @Test
  @WithMockUser(username="user") 
  void showUpdateFormTest() throws Exception {
    // GIVEN
    UserDto userDto = new UserDto(1, "Username 1", "User 1", "USER");
    when(userService.findById(anyInt())).thenReturn(userDto);

    // WHEN
    mockMvc.perform(get("/user/update/1"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("user/update"))
        .andExpect(model().attributeExists("userDto"))
        .andExpect(model().attribute("userDto", userDto));
    verify(userService, times(1)).findById(1);
    assertThat(userDto.getPassword()).isNull();
  }

  @DisplayName("GET /user/update when user not found should return view with error message")
  @Test
  @WithMockUser(username="user") 
  void showUpdateFormWhenNotFoundTest() throws Exception {
    // GIVEN
    doThrow(new ResourceNotFoundException("This user is not found")).when(userService).findById(anyInt());

    // WHEN
    mockMvc.perform(get("/user/update/9"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/user/list"))
        .andExpect(flash().attributeExists("error"));
    verify(userService, times(1)).findById(9);
  }

  @DisplayName("POST valid DTO on /user/update should persist user then return view")
  @Test
  @WithMockUser(username="user") 
  void updateUserTest() throws Exception {
    // GIVEN
    UserDto expectedDto = new UserDto(1, "Update Username", "Update User", "ADMIN");
    expectedDto.setPassword("UpdatePasswdA1=");

    // WHEN
    mockMvc.perform(post("/user/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", expectedDto.getUsername())
            .param("password", expectedDto.getPassword())
            .param("fullName", expectedDto.getFullName())
            .param("role", expectedDto.getRole())
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/user/list"));
    verify(userService, times(1)).update(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST valid DTO on /user/update when username already existed should return from view with error message")
  @Test
  @WithMockUser(username="user")
  void updateWhenUsernameAlreadyExistsTest() throws Exception {
    // GIVEN
    UserDto expectedDto = new UserDto(1, "Update ExistingUsername", "Update User", "ADMIN");
    expectedDto.setPassword("UpdatePasswdA1=");
    doThrow(new ResourceAlreadyExistsException("dfdf")).when(userService).update(any(UserDto.class));

    // WHEN
    mockMvc.perform(post("/user/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", expectedDto.getUsername())
            .param("password", expectedDto.getPassword())
            .param("fullName", expectedDto.getFullName())
            .param("role", expectedDto.getRole())
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("user/update"))
        .andExpect(model().attributeExists("error"));
    verify(userService, times(1)).update(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("POST invalid DTO on /user/update should return from view")
  @Test
  @WithMockUser(username="user") 
  void updateUserWhenInvalidTest() throws Exception {
    // WHEN
    mockMvc.perform(post("/user/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username","This user's username exceed the 125 characters limit " +
                "to test the validation of the Dto when the add User form is submit by the user")
            .param("password", "")
            .param("fullName", "")
            .param("role", "")
            .with(csrf()))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("user/update"))
        .andExpect(model().attributeHasFieldErrors("userDto", "username"))
        .andExpect(model().attributeHasFieldErrors("userDto", "password"))
        .andExpect(model().attributeHasFieldErrors("userDto", "fullName"))
        .andExpect(model().attributeHasFieldErrors("userDto", "role"));
    verify(userService, times(0)).update(any(UserDto.class));
  }

  @DisplayName("POST DTO on /user/update when user not found should return view with error message")
  @Test
  @WithMockUser(username="user") 
  void updateUserWhenNotFoundTest() throws Exception {
    // GIVEN
    UserDto expectedDto = new UserDto(9, "Update Username", "Update User", "ADMIN");
    expectedDto.setPassword("UpdatePasswdA1=");
    doThrow(new ResourceNotFoundException("This user is not found")).when(userService).update(any(UserDto.class));

    // WHEN
    mockMvc.perform(post("/user/update/9")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", expectedDto.getUsername())
            .param("password", expectedDto.getPassword())
            .param("fullName", expectedDto.getFullName())
            .param("role", expectedDto.getRole())
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/user/list"))
        .andExpect(flash().attributeExists("error"));
    verify(userService, times(1)).update(dtoCaptor.capture());
    assertThat(dtoCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDto);
  }

  @DisplayName("GET /user/delete should delete User then return view")
  @Test
  @WithMockUser(username="user") 
  void deleteUserTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/user/delete/1"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/user/list"));
    verify(userService, times(1)).delete(1);
  }

  @DisplayName("GET /user/delete when User not found should return view with error message")
  @Test
  @WithMockUser(username="user") 
  void deleteUserWhenNotFoundTest() throws Exception {
    // GIVEN
    doThrow(new ResourceNotFoundException("This User is not found")).when(userService).delete(anyInt());

    // WHEN
    mockMvc.perform(get("/user/delete/9"))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/user/list"))
        .andExpect(flash().attributeExists("error"));
    verify(userService, times(1)).delete(9);
  }

}
