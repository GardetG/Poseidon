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
class TradeIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("User access and fill add Trade form to add new trade")
	@Test
	void addTradeIntegrationTest() throws Exception {
		// GIVEN a user requesting the add Trade form
		// WHEN
		mockMvc.perform(get("/trade/add"))

				// THEN
				.andExpect(status().isOk())
				.andExpect(view().name("trade/add"))
				.andExpect(content().string(containsString("Add New Trade")));

		// GIVEN a user validating the form after filling it
		// WHEN
		mockMvc.perform(post("/trade/validate")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("account", "New Account")
						.param("type", "New Type")
						.param("buyQuantity", String.valueOf(30d))
						.with(csrf()))

				// THEN
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/trade/list"));

		// GIVEN a user redirect to the trade list
		// WHEN
		mockMvc.perform(get("/trade/list"))

				// THEN
				.andExpect(status().isOk())
				.andExpect(view().name("trade/list"))
				.andExpect(content().string(containsString("New Account")))
				.andExpect(content().string(containsString("New Type")))
				.andExpect(content().string(containsString("30.0")));
	}

	@DisplayName("User access and fill update Trade form to update existing trade")
	@Test
	void updateTradeIntegrationTest() throws Exception {
		// GIVEN a user requesting the update Trade form for Trade 1
		// WHEN
		mockMvc.perform(get("/trade/update/1"))

				// THEN
				.andExpect(status().isOk())
				.andExpect(view().name("trade/update"))
				.andExpect(content().string(containsString("Update Trade")))
				.andExpect(content().string(containsString("Account Test")))
				.andExpect(content().string(containsString("Type Test")))
				.andExpect(content().string(containsString("10.0")));

		// GIVEN a user validating the form after filling it
		// WHEN
		mockMvc.perform(post("/trade/update/1")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("account", "Update Account")
						.param("type", "Update Type")
						.param("buyQuantity", String.valueOf(40d))
						.with(csrf()))

				// THEN
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/trade/list"));

		// GIVEN a user redirect to the trade list
		// WHEN
		mockMvc.perform(get("/trade/list"))

				// THEN
				.andExpect(status().isOk())
				.andExpect(view().name("trade/list"))
				.andExpect(content().string(containsString("Update Account")))
				.andExpect(content().string(containsString("Update Type")))
				.andExpect(content().string(containsString("40.0")));
	}

	@DisplayName("User request Trade deletion")
	@Test
	void deleteTradeIntegrationTest() throws Exception {
		// GIVEN a user requesting deletion of Trade 2
		// WHEN
		mockMvc.perform(get("/trade/delete/2")
						.with(csrf()))

				// THEN
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/trade/list"));

		// GIVEN a user redirect to the trade list
		// WHEN
		mockMvc.perform(get("/trade/list"))

				// THEN
				.andExpect(status().isOk())
				.andExpect(view().name("trade/list"))
				.andExpect(content().string(not(containsString("Account to delete"))))
				.andExpect(content().string(not(containsString("Type to delete"))))
				.andExpect(content().string(not(containsString("20.0"))));
	}

}
