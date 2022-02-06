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
class BidListIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @DisplayName("User access and fill add BidList form to add new BidList")
  @Test
  void addBidListIntegrationTest() throws Exception {
    // GIVEN a user requesting the add BidList form
    // WHEN
    mockMvc.perform(get("/bidList/add"))

    // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("bidList/add"))
        .andExpect(content().string(containsString("Add New Bid")));

    // GIVEN a user validating the form after filling it
    // WHEN
    mockMvc.perform(post("/bidList/validate")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("account", "New Account Test")
            .param("type", "New Type Test")
            .param("bidQuantity", String.valueOf(10d))
            .with(csrf()))

    // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/bidList/list"));

    // GIVEN a user redirect to the BidList list
    // WHEN
    mockMvc.perform(get("/bidList/list"))

    // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("bidList/list"))
        .andExpect(content().string(containsString("New Account Test")))
        .andExpect(content().string(containsString("New Type Test")))
        .andExpect(content().string(containsString("10.0")));
  }

  @DisplayName("User access and fill update BidList form to update existing BidList")
  @Test
  void updateBidListIntegrationTest() throws Exception {
    // GIVEN a user requesting the update BidList form for BidList 1
    // WHEN
    mockMvc.perform(get("/bidList/update/1"))

    // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("bidList/update"))
        .andExpect(content().string(containsString("Update Bid")))
        .andExpect(content().string(containsString("Account Test")))
        .andExpect(content().string(containsString("Type Test")))
        .andExpect(content().string(containsString("10.0")));

    // GIVEN a user validating the form after filling it
    // WHEN
    mockMvc.perform(post("/bidList/update/1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("account", "Update Account Test")
            .param("type", "Update Type Test")
            .param("bidQuantity", String.valueOf(50d))
            .with(csrf()))

    // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/bidList/list"));

    // GIVEN a user redirect to the BidList list
    // WHEN
    mockMvc.perform(get("/bidList/list"))

    // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("bidList/list"))
        .andExpect(content().string(containsString("Update Account Test")))
        .andExpect(content().string(containsString("Update Type Test")))
        .andExpect(content().string(containsString("50.0")));
  }

  @DisplayName("User request BidList deletion")
  @Test
  void deleteBidListIntegrationTest() throws Exception {
    // GIVEN a user requesting deletion of BidList 2
    // WHEN
    mockMvc.perform(get("/bidList/delete/2")
            .with(csrf()))

    // THEN
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/bidList/list"));

    // GIVEN a user redirect to the BidList list
    // WHEN
    mockMvc.perform(get("/bidList/list"))

    // THEN
        .andExpect(status().isOk())
        .andExpect(view().name("bidList/list"))
        .andExpect(content().string(not(containsString("Account to delete Test"))))
        .andExpect(content().string(not(containsString("Type to delete Test"))))
        .andExpect(content().string(not(containsString("20.0"))));
  }

}
