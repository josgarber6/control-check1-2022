package org.springframework.samples.petclinic.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.feeding.FeedingController;
import org.springframework.samples.petclinic.feeding.FeedingRepository;
import org.springframework.samples.petclinic.feeding.FeedingService;
import org.springframework.samples.petclinic.feeding.FeedingTypeFormatter;
import org.springframework.samples.petclinic.pet.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = FeedingController.class,
		includeFilters = @ComponentScan.Filter(value = FeedingTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class Test5 {
    @MockBean
	FeedingService feedingService;
    @MockBean
	PetService petService;
	@MockBean
	FeedingRepository feedingRepository;
    @Autowired
	private MockMvc mockMvc;

    @WithMockUser(value = "spring")
    @Test
	void test5() throws Exception {
		mockMvc.perform(get("/feeding/create")).andExpect(status().isOk())
				.andExpect(view().name("feedings/createOrUpdateFeedingForm"))
                .andExpect(model().attributeExists("feeding"));
	}
}
