package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.feeding.FeedingRepository;
import org.springframework.samples.petclinic.feeding.FeedingType;
import org.springframework.samples.petclinic.pet.PetRepository;
import org.springframework.samples.petclinic.pet.PetType;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
public class Test2 {
    @Autowired(required = false)
    FeedingRepository fr;
    @Autowired
    PetRepository pr;
    @Autowired(required = false)
    EntityManager em;
    
    @Test
    public void test2(){
        entityExists();
        repositoryContainsMethod();
        testConstraints();
        testAnnotations();
    }

   
    public void entityExists() {
        FeedingType p=new FeedingType();
        p.setId(7);
        p.setName("High Protein Puppy Food");        
        p.setDescription("Using a standard 8 oz/250 ml measuring cup which contains approximately 112 g of food: For a body weight of 3 - 12, feed with 1/2 to 2/3 cups until 3 months.");
    }  
    
    public void repositoryContainsMethod() {
        try {
            Method findAllFeedingTypes = FeedingRepository.class.getDeclaredMethod("findAllFeedingTypes");
            if(fr!=null){
                List<FeedingType> pts= (List<FeedingType>) findAllFeedingTypes.invoke(fr);
                assertNotNull(pts,"We can not query all the feeding types through the repository");
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
    }

    @Transactional
    public void testConstraints(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        PetType pt = pr.findPetTypes().get(0);

        Field petTypeField = null;
        try{
            petTypeField = FeedingType.class.getDeclaredField("petType");
            petTypeField.setAccessible(true);
        }catch(NoSuchFieldException ex){
            petTypeField = null;
        }

        
        FeedingType feedingType=new FeedingType();       
        feedingType.setName("ja");
        feedingType.setDescription("blabla");
        if (petTypeField != null) {
            try {
                petTypeField.set(feedingType, pt);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                
            }
        }
        assertFalse(validator.validate(feedingType).isEmpty(), "It should not allow names shorter than 5");

        feedingType=new FeedingType();       
        feedingType.setName("En un lugar de la mancha, de cuyo nombre no quiero acordarme, no ha mucho tiempo que vivía un hidalgo de los de lanza en astillero, adarga antigua, rocín flaco y galgo corredor.");
        feedingType.setDescription("blabla");
        if (petTypeField != null) {
            try {
                petTypeField.set(feedingType, pt);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                
            }
        }
        assertFalse(validator.validate(feedingType).isEmpty(), "It should not allow names longer than 30");

        feedingType=new FeedingType();       
        feedingType.setDescription("blabla");
        if (petTypeField != null) {
            try {
                petTypeField.set(feedingType, pt);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                
            }
        }
        assertFalse(validator.validate(feedingType).isEmpty(), "It should not allow empty names");

        feedingType=new FeedingType();       
        feedingType.setName("High Protein Puppy Food");
        if (petTypeField != null) {
            try {
                petTypeField.set(feedingType, pt);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                
            }
        }
        assertFalse(validator.validate(feedingType).isEmpty(), "It should not allow empty descriptions");

        feedingType=new FeedingType();
        feedingType.setName("Happy Puppy Food");        
        feedingType.setDescription("Using a standard 8 oz/250 ml measuring cup which contains approximately 112 g of food: For a body weight of 3 - 12, feed with 1/2 to 2/3 cups until 3 months.");
        if (petTypeField != null) {
            try {
                petTypeField.set(feedingType, pt);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                
            }
        }
        // em.persist(feedingType);
        
        // FeedingType ft2=new FeedingType();       
        // ft2.setName("Happy Puppy Food");        
        // ft2.setDescription("This is another description.");
        // if (petTypeField != null) {
        //     try {
        //         petTypeField.set(feedingType, ft2);
        //     } catch (IllegalArgumentException | IllegalAccessException e) {
                
        //     }
        // }
        // assertThrows(Exception.class,()->em.persist(ft2),"It should not allow two feeding types with the same name");        
    }

    void testAnnotations(){
        // try{
        //     Field nameField = FeedingType.class.getDeclaredField("name");
        //     Column annotationColumn = nameField.getAnnotation(Column.class);
        //     assertNotNull(annotationColumn,"The name property is not properly annotated to make it unique in the DB");
        //     assertTrue(annotationColumn.unique(), "The name property is not properly annotated to make it unique in the DB");
        // }catch(NoSuchFieldException ex){
        //     fail("The FeedingType class should have a field that is not present: "+ex.getMessage());
        // }
    }    

}
