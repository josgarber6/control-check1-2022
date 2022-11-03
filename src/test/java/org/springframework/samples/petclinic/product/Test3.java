package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.feeding.Feeding;
import org.springframework.samples.petclinic.feeding.FeedingRepository;
import org.springframework.samples.petclinic.feeding.FeedingType;
import org.springframework.stereotype.Service;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test3 {
    @Autowired
    FeedingRepository fr;
    
    @Test
    public void test3(){
        testInitialFeeding();
        testInitialFeedingTypes();
    }
    
    public void testInitialFeeding(){
        List<Feeding> products=fr.findAll();
        assertTrue(products.size()==2, "Exactly two feedings should be present in the DB");

        Optional<Feeding> p1=fr.findById(1);
        assertTrue(p1.isPresent(),"There should exist a feeding with id:1");
        assertEquals(p1.get().getStartDate(),LocalDate.of(2022, 01, 05), "The start date of the feeding with id:1 should be 05/01/2022");
        assertEquals(p1.get().getWeeksDuration(),7.5, "The weeks duration of the feeding with id:1 should be 7.5");

        Optional<Feeding> p2=fr.findById(2);
        assertTrue(p2.isPresent(),"There should exist a feeding with id:2");
        assertEquals(p2.get().getStartDate(),LocalDate.of(2022,1,4),"The date of the feeding with id:2 should be 04/01/2022");
        assertEquals(p2.get().getWeeksDuration(),6, "The weeks duration of the feeding with id:1 should be 6");
    }

    public void testInitialFeedingTypes()
    {
        List<FeedingType> feedingTypes = new ArrayList<FeedingType>();
        try {
            Method findAllFeedingTypes = FeedingRepository.class.getDeclaredMethod("findAllFeedingTypes");
            if(fr!=null){
                feedingTypes = (List<FeedingType>) findAllFeedingTypes.invoke(fr);
            }else
                fail("The repository was not injected into the tests, its autowired value was null");
        } catch(NoSuchMethodException e) {
            fail("There is no method findAllFindingTypes in FeedingRepository", e);
        } catch (IllegalAccessException e) {
            fail("There is no public method findAllFindingTypes in FeedingRepository", e);
        } catch (IllegalArgumentException e) {
            fail("There is no method findAllFindingTypes() in FeedingRepository", e);
        } catch (InvocationTargetException e) {
            fail("There is no method findAllFindingTypes() in FeedingRepository", e);
        }

        assertTrue(feedingTypes.size()==2,"Exactly two feeding types should be present in the DB");

        for(FeedingType v:feedingTypes) {
            if(v.getId()==1){
                assertEquals(v.getName(),"High Protein Puppy Food","The name of the feeding type with id:1 should be 'High Protein Puppy Food'");
                assertEquals(v.getDescription(),"Using a standard 8 oz/250 ml measuring cup which contains approximately 112 g of food: For a body weight of 3 - 12, feed with 1/2 to 2/3 cups until 3 months.","The description of the feeding type with id:1 is not correct");
            }else if(v.getId()==2){
                assertEquals(v.getName(),"Adult Weight Management","The name of the feeding type with id:2 should be 'Adult Weight Management'");
                assertEquals(v.getDescription(),"Chicken and Rice Formula Dry Cat Food. Feed 1 can per 2-1/2 lbs of body weight daily. Adjust as needed. Divide into 2 or more meals.","The description of the feeding type with id:1 is not correct");
            }else {
                fail("The id of the feeding types should be either 1 or 2");
            }
        }
    }
}
