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
class RatingIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @DisplayName("User access and fill add Rating form to add new rating")
  @Test
  void addRatingIntegrationTest() throws Exception {
    // GIVEN a user requesting the add Rating form
    // WHEN
    mockMvc.perform(get("/rating/add"))

   // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("rating/add"))
        .andExpect(content().string(containsString("Add New Rating")));

    // GIVEN a user validating the form after filling it
    // WHEN
    mockMvc.perform(post("/rating/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("moodysRating", "New Moody Rating")
            .param("sandpRating", "New S&P Rating")
            .param("fitchRating", "New Fitch Rating")
            .param("orderNumber", String.valueOf(12))
            .with(csrf()))

    // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/rating/list"));

    // GIVEN a user redirect to the rating list
    // WHEN
    mockMvc.perform(get("/rating/list"))

    // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("rating/list"))
        .andExpect(content().string(containsString("New Moody Rating")))
        .andExpect(content().string(containsString("New S&amp;P Rating")))
        .andExpect(content().string(containsString("New Fitch Rating")))
        .andExpect(content().string(containsString("12")));
  }

  @DisplayName("User access and fill update Rating form to update existing rating")
  @Test
  void updateRatingIntegrationTest() throws Exception {
    // GIVEN a user requesting the update Rating form for Rating 1
    // WHEN
    mockMvc.perform(get("/rating/update/1"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("rating/update"))
        .andExpect(content().string(containsString("Update Rating")))
        .andExpect(content().string(containsString("Moody Rating Test")))
        .andExpect(content().string(containsString("S&amp;P Rating Test")))
        .andExpect(content().string(containsString("Fitch Rating Test")))
        .andExpect(content().string(containsString("10")));

    // GIVEN a user validating the form after filling it
    // WHEN
    mockMvc.perform(post("/rating/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("moodysRating", "Update Moody Rating")
            .param("sandpRating", "Update S&P Rating")
            .param("fitchRating", "Update Fitch Rating")
            .param("orderNumber", String.valueOf(13))
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/rating/list"));

    // GIVEN a user redirect to the rating list
    // WHEN
    mockMvc.perform(get("/rating/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("rating/list"))
        .andExpect(content().string(containsString("Update Moody Rating")))
        .andExpect(content().string(containsString("Update S&amp;P Rating")))
        .andExpect(content().string(containsString("Update Fitch Rating")))
        .andExpect(content().string(containsString("13")));
  }

  @DisplayName("User request Rating deletion")
  @Test
  void deleteRatingIntegrationTest() throws Exception {
    // GIVEN a user requesting deletion of Rating 2
    // WHEN
    mockMvc.perform(get("/rating/delete/2")
            .with(csrf()))

        // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/rating/list"));

    // GIVEN a user redirect to the rating list
    // WHEN
    mockMvc.perform(get("/rating/list"))

        // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("rating/list"))
        .andExpect(content().string(not(containsString("Moody Rating to delete Test"))))
        .andExpect(content().string(not(containsString("S&amp;P Rating to delete Test"))))
        .andExpect(content().string(not(containsString("Fitch Rating to delete Test"))))
        .andExpect(content().string(not(containsString("11"))));
  }

}
