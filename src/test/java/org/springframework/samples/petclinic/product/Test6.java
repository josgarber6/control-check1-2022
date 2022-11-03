package org.springframework.samples.petclinic.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.feeding.Feeding;
import org.springframework.samples.petclinic.feeding.FeedingController;
import org.springframework.samples.petclinic.feeding.FeedingService;
import org.springframework.samples.petclinic.feeding.FeedingType;
import org.springframework.samples.petclinic.feeding.FeedingTypeFormatter;
import org.springframework.samples.petclinic.feeding.UnfeasibleFeedingException;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetFormatter;
import org.springframework.samples.petclinic.pet.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = FeedingController.class,
		includeFilters = {@ComponentScan.Filter(value = FeedingTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
            @ComponentScan.Filter(value = PetFormatter.class, type = FilterType.ASSIGNABLE_TYPE)},
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class Test6 {
    @MockBean
    FeedingService feedingService;
    @MockBean
	PetService petService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    FeedingTypeFormatter feedingTypeFormatter;

    private static final String DOG_FEEDING_TYPE_NAME = "High Protein Puppy Food";
    private static final String A_DOG_NAME = "Rosy";
    private static final String A_CAT_NAME = "Leo";

    @BeforeEach
    public void configureMock() throws UnfeasibleFeedingException, ParseException{
        FeedingType feedingType=new FeedingType();
        feedingType.setName(DOG_FEEDING_TYPE_NAME);
        Pet dogPet = new Pet();
        dogPet.setName(A_DOG_NAME);
        Pet catPet = new Pet();
        catPet.setName(A_CAT_NAME);
        List<FeedingType> feedingTypes=new ArrayList<FeedingType>();
        feedingTypes.add(feedingType);

        Mockito.when(feedingService.save(any(Feeding.class))).thenReturn(null);
        Mockito.when(feedingService.getFeedingType(DOG_FEEDING_TYPE_NAME)).thenReturn(feedingType);
        Mockito.when(feedingService.getAllFeedingTypes()).thenReturn(feedingTypes);
        Mockito.when(petService.getPetByName(A_CAT_NAME)).thenReturn(catPet);
        Mockito.when(petService.getPetByName(A_DOG_NAME)).thenReturn(dogPet);
        Mockito.when(feedingTypeFormatter.parse(eq(DOG_FEEDING_TYPE_NAME), any())).thenReturn(feedingType);
    }    

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void test6()  throws Exception {
        testFeedingCreationControllerOK();                
    }

	void testFeedingCreationControllerOK() throws Exception {
        mockMvc.perform(post("/feeding/create")
                            .with(csrf())
                            .param("pet", A_DOG_NAME)
                            .param("feedingType", DOG_FEEDING_TYPE_NAME)
                            .param("startDate", "2021/12/31")
                            .param("weeksDuration", "2.0"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/welcome"));
    }	
}


