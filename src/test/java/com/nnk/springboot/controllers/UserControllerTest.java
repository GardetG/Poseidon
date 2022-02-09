package com.nnk.springboot.controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;
  @MockBean
  private UserRepository userRepository;

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
