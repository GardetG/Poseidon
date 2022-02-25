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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "User")
class CurvePointIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @DisplayName("User access and fill add CurvePoint form to add new curvePoint")
  @Test
  void addCurvePointIntegrationTest() throws Exception {
    // GIVEN a user requesting the add CurvePoint form
    // WHEN
    mockMvc.perform(get("/curvePoint/add"))

    // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("curvePoint/add"))
        .andExpect(content().string(containsString("Add New Curve Point")));

    // GIVEN a user validating the form after filling it
    // WHEN
    mockMvc.perform(post("/curvePoint/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("curveId", String.valueOf(12))
            .param("term", String.valueOf(20d))
            .param("value", String.valueOf(40d))
            .with(csrf()))

    // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/curvePoint/list"));

    // GIVEN a user redirect to the curvePoint list
    // WHEN
    mockMvc.perform(get("/curvePoint/list"))

    // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("curvePoint/list"))
        .andExpect(content().string(containsString("<td>12</td>")))
        .andExpect(content().string(containsString("20.0")))
        .andExpect(content().string(containsString("40.0")));
  }

  @DisplayName("User access and fill update CurvePoint form to update existing curvePoint")
  @Test
  void updateCurvePointIntegrationTest() throws Exception {
    // GIVEN a user requesting the update CurvePoint form for CurvePoint 1
    // WHEN
    mockMvc.perform(get("/curvePoint/update/1"))

   // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("curvePoint/update"))
        .andExpect(content().string(containsString("Update CurvePoint")))
        .andExpect(content().string(containsString("value=\"10\"")))
        .andExpect(content().string(containsString("10.0")))
        .andExpect(content().string(containsString("20.0")));

    // GIVEN a user validating the form after filling it
    // WHEN
    mockMvc.perform(post("/curvePoint/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("curveId", String.valueOf(13))
            .param("term", String.valueOf(25d))
            .param("value", String.valueOf(50d))
            .with(csrf()))

    // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/curvePoint/list"));

    // GIVEN a user redirect to the curvePoint list
    // WHEN
    mockMvc.perform(get("/curvePoint/list"))

    // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("curvePoint/list"))
        .andExpect(content().string(containsString("<td>13</td>")))
        .andExpect(content().string(containsString("25.0")))
        .andExpect(content().string(containsString("50.0")));
  }

  @DisplayName("User request CurvePoint deletion")
  @Test
  void deleteCurvePointIntegrationTest() throws Exception {
    // GIVEN a user requesting deletion of CurvePoint 2
    // WHEN
    mockMvc.perform(get("/curvePoint/delete/2")
            .with(csrf()))

    // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/curvePoint/list"));

    // GIVEN a user redirect to the curvePoint list
    // WHEN
    mockMvc.perform(get("/curvePoint/list"))

    // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("curvePoint/list"))
        .andExpect(content().string(not(containsString("<td>11</td>"))))
        .andExpect(content().string(not(containsString("15.0"))))
        .andExpect(content().string(not(containsString("30.0"))));
  }

}
