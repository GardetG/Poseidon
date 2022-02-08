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
class RuleNameIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("User access and fill add RuleName form to add new ruleName")
	@Test
	void addRuleNameIntegrationTest() throws Exception {
		// GIVEN a user requesting the add RuleName form
		// WHEN
		mockMvc.perform(get("/ruleName/add"))

				// THEN
				.andExpect(status().isOk())
				.andExpect(view().name("ruleName/add"))
				.andExpect(content().string(containsString("Add New Rule")));

		// GIVEN a user validating the form after filling it
		// WHEN
		mockMvc.perform(post("/ruleName/validate")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("name", "New Name")
						.param("description", "New Description")
						.param("json", "New Json")
						.param("template", "New Template")
						.param("sqlStr", "New SQL")
						.param("sqlPart", "New SQLPart")
						.with(csrf()))

				// THEN
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/ruleName/list"));

		// GIVEN a user redirect to the ruleName list
		// WHEN
		mockMvc.perform(get("/ruleName/list"))

				// THEN
				.andExpect(status().isOk())
				.andExpect(view().name("ruleName/list"))
				.andExpect(content().string(containsString("New Name")))
				.andExpect(content().string(containsString("New Description")))
				.andExpect(content().string(containsString("New Json")))
				.andExpect(content().string(containsString("New Template")))
				.andExpect(content().string(containsString("New SQL")))
				.andExpect(content().string(containsString("New SQLPart")));
	}

	@DisplayName("User access and fill update RuleName form to update existing ruleName")
	@Test
	void updateRuleNameIntegrationTest() throws Exception {
		// GIVEN a user requesting the update RuleName form for RuleName 1
		// WHEN
		mockMvc.perform(get("/ruleName/update/1"))

				// THEN
				.andExpect(status().isOk())
				.andExpect(view().name("ruleName/update"))
				.andExpect(content().string(containsString("Update Rule")))
				.andExpect(content().string(containsString("Name Test")))
				.andExpect(content().string(containsString("Description Test")))
				.andExpect(content().string(containsString("Json Test")))
				.andExpect(content().string(containsString("Template Test")))
				.andExpect(content().string(containsString("SQL Test")))
				.andExpect(content().string(containsString("SQLPart Test")));

		// GIVEN a user validating the form after filling it
		// WHEN
		mockMvc.perform(post("/ruleName/update/1")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("name", "Update Name")
						.param("description", "Update Description")
						.param("json", "Update Json")
						.param("template", "Update Template")
						.param("sqlStr", "Update SQL")
						.param("sqlPart", "Update SQLPart")
						.with(csrf()))

				// THEN
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/ruleName/list"));

		// GIVEN a user redirect to the ruleName list
		// WHEN
		mockMvc.perform(get("/ruleName/list"))

				// THEN
				.andExpect(status().isOk())
				.andExpect(view().name("ruleName/list"))
				.andExpect(content().string(containsString("Update Name")))
				.andExpect(content().string(containsString("Update Description")))
				.andExpect(content().string(containsString("Update Json")))
				.andExpect(content().string(containsString("Update Template")))
				.andExpect(content().string(containsString("Update SQL")))
				.andExpect(content().string(containsString("Update SQLPart")));
	}

	@DisplayName("User request RuleName deletion")
	@Test
	void deleteRuleNameIntegrationTest() throws Exception {
		// GIVEN a user requesting deletion of RuleName 2
		// WHEN
		mockMvc.perform(get("/ruleName/delete/2")
						.with(csrf()))

				// THEN
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/ruleName/list"));

		// GIVEN a user redirect to the ruleName list
		// WHEN
		mockMvc.perform(get("/ruleName/list"))

				// THEN
				.andExpect(status().isOk())
				.andExpect(view().name("ruleName/list"))
				.andExpect(content().string(not(containsString("Name to delete"))))
				.andExpect(content().string(not(containsString("Description to delete"))))
				.andExpect(content().string(not(containsString("Json to delete"))))
				.andExpect(content().string(not(containsString("Template to delete"))))
				.andExpect(content().string(not(containsString("SQL to delete"))))
				.andExpect(content().string(not(containsString("SQLPart to delete"))));
	}

}
