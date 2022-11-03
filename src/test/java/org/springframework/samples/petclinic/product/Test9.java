package org.springframework.samples.petclinic.product;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
public class Test9 {

    @Autowired
    FeedingService fs;
    @Autowired
    PetService ps;

    @Test
    public void test9(){
        testSaveFeedingSuccessfull();
        testSaveUnfeasibleFeeding();
        testTransactionalRollback();
    }

    @Transactional
    private void testSaveFeedingSuccessfull()
    {
        Pet pet=ps.findPetById(1); // This pet is Leo, a cat
        FeedingType ft=fs.getFeedingType("Adult Weight Management"); // This feeding type is intended for cats
        Feeding f = new Feeding();
        setPet(f, pet);
        f.setStartDate(LocalDate.now());
        f.setWeeksDuration(3);
        setFeedingType(f, ft);
        try {
            fs.save(f);
        } catch (UnfeasibleFeedingException e) {
            fail("The excepcion should not be thrown, the feeding type is feasible!");
        }
    }

    @Transactional
    private void testSaveUnfeasibleFeeding()   {
        Pet pet=ps.findPetById(1); // This pet is Leo, a cat
        FeedingType ft=fs.getFeedingType("High Protein Puppy Food"); // The feeding type is intended for dogs
        Feeding f = new Feeding();
		setPet(f, pet);
        f.setStartDate(LocalDate.now());
        f.setWeeksDuration(3);
        setFeedingType(f, ft);
        // Thus, the save should throw an exception:
        assertThrows(UnfeasibleFeedingException.class,
            ()-> fs.save(f));
    }

    private void testTransactionalRollback() {
        Method save=null;
        try {
            save = FeedingService.class.getDeclaredMethod("save", Feeding.class);
        } catch (NoSuchMethodException e) {
           fail("FeedingService does not have a save method");
        } catch (SecurityException e) {
            fail("save method is not accessible in FeedingService");
        }
        Transactional transactionalAnnotation=save.getAnnotation(Transactional.class);
        assertNotNull(transactionalAnnotation,"The method save is not annotated as transactional");
        List<Class<? extends Throwable>> exceptionsWithRollbackFor=Arrays.asList(transactionalAnnotation.rollbackFor());
        assertTrue(exceptionsWithRollbackFor.contains(UnfeasibleFeedingException.class));
    }

    private void setFeedingType(Feeding f, FeedingType ft) {
        try {
            Field feedingType = Feeding.class.getDeclaredField("feedingType");
            feedingType.setAccessible(true);
            feedingType.set(f, ft);
        } catch (NoSuchFieldException e) {
            fail("The Feeding class should have an attribute called feedingType that is not present: ", e);
        } catch (IllegalArgumentException e) {
            fail("The Feeding class should have an attribute called feedingType that is not present: ", e);
        } catch (IllegalAccessException e) {
            fail("The Feeding class should have an attribute called feedingType that is not present: ", e);
        }        
    }

    private void setPet(Feeding f, Pet p) {
        try {
            Field pet = Feeding.class.getDeclaredField("pet");
            pet.setAccessible(true);
            pet.set(f, p);
        } catch (NoSuchFieldException e) {
            fail("The Feeding class should have an attribute called pet that is not present: ", e);
        } catch (IllegalArgumentException e) {
            fail("The Feeding class should have an attribute called pet that is not present: ", e);
        } catch (IllegalAccessException e) {
            fail("The Feeding class should have an attribute called pet that is not present: ", e);
        }        
    }

}

