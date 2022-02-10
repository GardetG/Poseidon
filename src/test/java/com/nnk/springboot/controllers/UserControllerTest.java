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
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
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
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;
  @MockBean
  private UserRepository userRepository;

  @Captor
  private ArgumentCaptor<UserDto> dtoCaptor;

  @DisplayName("GET /user/list should return view with list of User as attribute")
  @Test
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

  @DisplayName("GET /user/list with no User in database should return view with empty list as attribute")
  @Test
  void homeWhenEmptyTest() throws Exception {
    // GIVEN
    when(userService.findAll()).thenReturn(Collections.emptyList());

    // WHEN
    mockMvc.perform(get("/user/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("user/list"))
        .andExpect(model().attributeExists("users"))
        .andExpect(model().attribute("users", Collections.emptyList()));
  }

  @DisplayName("GET /user/add should return view")
  @Test
  void addUserFormTest() throws Exception {
    // WHEN
    mockMvc.perform(get("/user/add"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("user/add"));
  }

  @DisplayName("POST valid DTO on /user/validate should persist User then return view")
  @Test
  void validateTest() throws Exception {
    // GIVEN
    UserDto expectedDto = new UserDto(null, "Username 1", "User 1", "USER");
    expectedDto.setPassword("Password");

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

  @DisplayName("POST invalid DTO on /user/validate should return form view")
  @Test
  void validateWhenInvalidTest() throws Exception {
    // WHEN
    mockMvc.perform(post("/user/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username","This user's username exceed the 125 characters limit " +
                "to test the validation of the Dto when the add User form is submit by the user")
            .param("password", "")
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
  
}
