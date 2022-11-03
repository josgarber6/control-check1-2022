package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.feeding.Feeding;
import org.springframework.samples.petclinic.feeding.FeedingService;
import org.springframework.samples.petclinic.feeding.FeedingType;
import org.springframework.samples.petclinic.feeding.UnfeasibleFeedingException;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test4 {
    @Autowired(required = false)
    FeedingService fs;

    @Autowired
    PetService ps;

    @Autowired
    EntityManager em;

    @Test public void test4() throws UnfeasibleFeedingException{
        feedingServiceIsInjected();
        feedingServiceCanGetFeedings();
        feedingServiceCanGetFeedingTypes();
        feedingServiceCanSaveFeedings();
        checkTransactional("save", Feeding.class);
        checkTransactional("getAllFeedingTypes");
        checkTransactional("getAll");
    }
    
    public void feedingServiceIsInjected() {
        assertNotNull(fs,"FeedingService was not injected by spring");       
    }
    
    public void feedingServiceCanGetFeedings(){
        assertNotNull(fs,"FeedingService was not injected by spring");
        List<Feeding> feedings=fs.getAll();
        assertNotNull(feedings,"The list of feedings found by the FeedingService was null");
        assertFalse(feedings.isEmpty(),"The list of feedings found by the FeedingService was empty");       
    }

    private void feedingServiceCanGetFeedingTypes() {
        assertNotNull(fs,"FeedingService was not injected by spring");
        List<FeedingType> feedingTypes=fs.getAllFeedingTypes();
        assertNotNull(feedingTypes,"The list of feedings types found by the FeedingService was null");
        assertFalse(feedingTypes.isEmpty(),"The list of feedings types found by the FeedingService was empty");       
    }

    private void feedingServiceCanSaveFeedings() throws UnfeasibleFeedingException {        
        Feeding f4=new Feeding();
        f4.setId(7);
        f4.setStartDate(LocalDate.of(2021, 12, 31));
        f4.setWeeksDuration(3.0);

        try{
            Field feedingTypeField = Feeding.class.getDeclaredField("feedingType");
            feedingTypeField.setAccessible(true);
            Field petField = Feeding.class.getDeclaredField("pet");
            petField.setAccessible(true);
            FeedingType ft = em.find(FeedingType.class, 2);
            Pet p = ps.getPetByName("Leo");
            feedingTypeField.set(f4, ft);
            petField.set(f4, p);
        }catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex){
            
        }        

        Feeding f4Saved = fs.save(f4);
        assertNotNull(f4Saved, "Feedings cannot be saved by FeedingService");
    }

    private void checkTransactional(String methodName, Class<?>... parameterTypes) {
        Method method=null;
        try {
            method = FeedingService.class.getDeclaredMethod(methodName, parameterTypes);
            Transactional transactionalAnnotation=method.getAnnotation(Transactional.class);
            assertNotNull(transactionalAnnotation,"The method "+methodName+" is not annotated as transactional or it does not use the right transactional annotation, which is org.springframework.transaction.annotation.Transactional");
        } catch (NoSuchMethodException e) {
            fail("FeedingService does not have a "+methodName+" method");
        } catch (SecurityException e) {
            fail(methodName+" method is not accessible in FeedingService");
        }
    }    



}
