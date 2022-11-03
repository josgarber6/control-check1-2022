package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.feeding.Feeding;
import org.springframework.samples.petclinic.feeding.FeedingRepository;
import org.springframework.samples.petclinic.feeding.FeedingType;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetRepository;
import org.springframework.samples.petclinic.pet.PetType;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
public class Test7 {
    @Autowired(required = false)
    FeedingRepository fr;

    @Autowired
    PetRepository pr;
    
    @Autowired(required = false)
    EntityManager em;


    @Test
    @Transactional
    public void test7(){
        testAnnotationsFeeding();
        testConstraintsFeeding();
        testAnnotationsFeedingType();
        testConstraintsFeedingType();
        testInitialFeedingTypesLinkedWithPetType();
        testInitialFeedingsLinkedWithFeedingType();
        testInitialFeedingsLinkedWithPet();
    }
    
    void testAnnotationsFeeding(){
        try{
            Field feedingType=Feeding.class.getDeclaredField("feedingType");
            ManyToOne annotationManytoOne=feedingType.getAnnotation(ManyToOne.class);
            assertNotNull(annotationManytoOne,"The feedingType property is not properly annotated");
        }catch(NoSuchFieldException ex){
            fail("The Feeding class should have an attribute called feedingType that is not present: "+ex.getMessage());
        }

        try{
            Field petField=Feeding.class.getDeclaredField("pet");
            ManyToOne annotationManytoOne=petField.getAnnotation(ManyToOne.class);
            assertNotNull(annotationManytoOne,"The pet property is not properly annotated");
        }catch(NoSuchFieldException ex){
            fail("The Feeding class should have an attribute called pet that is not present: "+ex.getMessage());
        }
    }    

    private void testAnnotationsFeedingType() {
        try{
            Field petType=FeedingType.class.getDeclaredField("petType");
            ManyToOne annotationManytoOne=petType.getAnnotation(ManyToOne.class);
            assertNotNull(annotationManytoOne,"The petType property is not properly annotated");
            // JoinColumn annotationJoinColumn=petType.getAnnotation(JoinColumn.class);
            // assertNotNull(annotationJoinColumn,"The petType property is not properly annotated");
            // assertEquals("pet_type_id",annotationJoinColumn.name(),"The name of the join column in petType is not properly customized");
        }catch(NoSuchFieldException ex){
            fail("The FeedingType class should have a field that is not present: "+ex.getMessage());
        }
    }    
 

    private void testConstraintsFeeding() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Feeding f2=new Feeding();
        f2.setStartDate(LocalDate.of(2021, 12, 31));
        f2.setWeeksDuration(3);

        assertFalse(validator.validate(f2).isEmpty(), "You are not constraining the pet and/or the feedingType to be mandatory");
    }

    private void testConstraintsFeedingType(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        FeedingType feedingType=new FeedingType();       
        feedingType.setName("High Protein Puppy Food");        
        feedingType.setDescription("Using a standard 8 oz/250 ml measuring cup which contains approximately 112 g of food: For a body weight of 3 - 12, feed with 1/2 to 2/3 cups until 3 months.");
        assertFalse(validator.validate(feedingType).isEmpty(), "It should not allow empty pet type");
    }

    
    private void testInitialFeedingsLinkedWithFeedingType() {
        Field feedingType;
        FeedingType ft;

        try {
            feedingType = Feeding.class.getDeclaredField("feedingType");
            feedingType.setAccessible(true);
        
            Optional<Feeding> f1=fr.findById(1);
            assertTrue(f1.isPresent(),"Feeding with id:1 not found");
            ft = (FeedingType) feedingType.get(f1.get());
            assertNotNull(ft,"The feeding with id:1 has not a feeding type associated");
            assertEquals(2,ft.getId(),"The id of the feeding type associated to the feeding with id:1 is not 2");

            Optional<Feeding> f2=fr.findById(2);
            assertTrue(f2.isPresent(),"Feeding with id:2 not found");
            ft = (FeedingType) feedingType.get(f2.get());
            assertNotNull(ft,"The feeding with id:2 has not a feeding type associated");
            assertEquals(1, ft.getId(),"The id of the feeding type associated to the feeding with id:2 is not 1");
        } catch (NoSuchFieldException e) {
            fail("The Feeding class should have an attribute called feedingType that is not present: "+e.getMessage());
        } catch (IllegalArgumentException e) {
            fail("The Feeding class should have an attribute called feedingType that is not present: "+e.getMessage());
        } catch (IllegalAccessException e) {
            fail("The Feeding class should have an attribute called feedingType that is not present: "+e.getMessage());
        }
    }

    private void testInitialFeedingsLinkedWithPet() {
        Field pet;
        Pet p;

        try {
            pet = Feeding.class.getDeclaredField("pet");
            pet.setAccessible(true);
        
            Optional<Feeding> f1=fr.findById(1);
            assertTrue(f1.isPresent(),"Feeding with id:1 not found");
            p = (Pet) pet.get(f1.get());
            assertNotNull(p,"The feeding with id:1 has not a pet associated");
            assertEquals(7, p.getId(),"The id of the pet associated to the feeding with id:1 is not 7");

            Optional<Feeding> f2=fr.findById(2);
            assertTrue(f2.isPresent(),"Feeding with id:2 not found");
            p = (Pet) pet.get(f2.get());
            assertNotNull(p,"The feeding with id:2 has not a pet associated");
            assertEquals(4, p.getId(),"The id of the pet associated to the feeding with id:2 is not 4");
        } catch (NoSuchFieldException e) {
            fail("The Feeding class should have an attribute called pet that is not present: "+e.getMessage());
        } catch (IllegalArgumentException e) {
            fail("The Feeding class should have an attribute called pet that is not present: "+e.getMessage());
        } catch (IllegalAccessException e) {
            fail("The Feeding class should have an attribute called pet that is not present: "+e.getMessage());
        }
    }

    private void testInitialFeedingTypesLinkedWithPetType() {
        Field petTypeField;
        PetType p;

        try {
            petTypeField = FeedingType.class.getDeclaredField("petType");
            petTypeField.setAccessible(true);

            List<FeedingType> feedingTypes = new ArrayList<FeedingType>();
            try {
                Method findAllFeedingTypes = fr.getClass().getDeclaredMethod("findAllFeedingTypes");
                if(fr!=null){
                    feedingTypes = (List<FeedingType>) findAllFeedingTypes.invoke(fr);
                }else
                    fail("The repository was not injected into the tests, its autowired value was null");
            } catch(NoSuchMethodException e) {
                fail("There is no method findAllFeedingTypes in FeedingRepository", e);
            } catch (IllegalAccessException e) {
                fail("There is no public method findAllFeedingTypes in FeedingRepository", e);
            } catch (IllegalArgumentException e) {
                fail("There is no method findAllFeedingTypes() in FeedingRepository", e);
            } catch (InvocationTargetException e) {
                fail("There is no method findAllFeedingTypes() in FeedingRepository", e);
            }
    
            assertTrue(feedingTypes.size()==2,"Exactly two feeding types should be present in the DB");
    
            for(FeedingType v:feedingTypes) {
                if(v.getId()==1){
                    p = (PetType) petTypeField.get(v);
                    assertNotNull(p,"The feeding with id:1 has not a pet associated");
                    assertEquals(2, p.getId(), "The pet type of the feeding type with id:1 is not correct");
                }else if(v.getId()==2){
                    p = (PetType) petTypeField.get(v);
                    assertNotNull(p,"The feeding with id:1 has not a pet associated");
                    assertEquals(1, p.getId(), "The pet type of the feeding type with id:2 is not correct");
                }else {
                    fail("The id of the feeding types should be either 1 or 2");
                }
            }
    
        } catch (NoSuchFieldException e) {
            fail("The Feeding class should have an attribute called pet that is not present: "+e.getMessage());
        } catch (IllegalArgumentException e) {
            fail("The Feeding class should have an attribute called pet that is not present: "+e.getMessage());
        } catch (IllegalAccessException e) {
            fail("The Feeding class should have an attribute called pet that is not present: "+e.getMessage());
        }
    }

}
