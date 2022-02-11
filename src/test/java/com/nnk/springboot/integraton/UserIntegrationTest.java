package com.nnk.springboot.integraton;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @DisplayName("User access and fill add User form to add new user")
  @Test
  void addUserIntegrationTest() throws Exception {
    // GIVEN a user requesting the add User form
    // WHEN
    mockMvc.perform(get("/user/add"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("user/add"))
        .andExpect(content().string(containsString("Add New User")));

    // GIVEN a user validating the form after filling it
    // WHEN
    mockMvc.perform(post("/user/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "New Username")
            .param("password", "NewPasswdA1=")
            .param("fullName", "New FullName")
            .param("role", "USER")
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/user/list"));

    // GIVEN a user redirect to the user list
    // WHEN
    mockMvc.perform(get("/user/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("user/list"))
        .andExpect(content().string(containsString("<td>New Username</td>")))
        .andExpect(content().string(containsString("<td>New FullName</td>")))
        .andExpect(content().string(containsString("<td style=\"width: 10%\">3</td>")));
  }

  @DisplayName("User access and fill update User form to update existing user")
  @Test
  void updateUserIntegrationTest() throws Exception {
    // GIVEN a user requesting the update User form for User 1
    // WHEN
    mockMvc.perform(get("/user/update/1"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("user/update"))
        .andExpect(content().string(containsString("admin")))
        .andExpect(content().string(containsString("Administrator")))
        .andExpect(content().string(containsString("ADMIN")));

    // GIVEN a user validating the form after filling it
    // WHEN
    mockMvc.perform(post("/user/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "Update Username")
            .param("password", "UpdatePasswdA1=")
            .param("fullName", "Update FullName")
            .param("role", "USER")
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/user/list"));

    // GIVEN a user redirect to the user list
    // WHEN
    mockMvc.perform(get("/user/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("user/list"))
        .andExpect(content().string(containsString("<td>Update Username</td>")))
        .andExpect(content().string(containsString("<td>Update FullName</td>")))
        .andExpect(content().string(containsString("<td style=\"width: 10%\">1</td>")));
  }

  @DisplayName("User request User deletion")
  @Test
  void deleteUserIntegrationTest() throws Exception {
    // GIVEN a user requesting deletion of User 2
    // WHEN
    mockMvc.perform(get("/user/delete/2")
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/user/list"));

    // GIVEN a user redirect to the user list
    // WHEN
    mockMvc.perform(get("/user/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("user/list"))
        .andExpect(content().string(not(containsString("<td>User</td>"))))
        .andExpect(content().string(not(containsString("<td>user</td>"))))
        .andExpect(content().string(not(containsString("<td style=\"width: 10%\">2</td>"))));
  }

}
