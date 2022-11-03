package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.Column;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.feeding.Feeding;
import org.springframework.samples.petclinic.feeding.FeedingRepository;
import org.springframework.samples.petclinic.pet.PetRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test1 {

    @Autowired(required = false)
    FeedingRepository fr;

    @Autowired
    PetRepository pr;
    

    @Test
    public void test1(){
        repositoryExists();
        repositoryContainsMethod();
        testConstraints();
        testAnnotations();
    }

    
    public void repositoryExists(){
        assertNotNull(fr,"The repository was not injected into the tests, its autowired value was null");
    }

    
    public void repositoryContainsMethod(){
        if(fr!=null){
            Optional<Feeding> v=fr.findById(12);
            assertFalse(v.isPresent(),"No result (null) should be returned for a feeding that does not exist");
        }else
            fail("The repository was not injected into the tests, its autowired value was null");
    }

    void testConstraints(){
        Feeding f=new Feeding();
        f.setId(7);
        f.setWeeksDuration(3);
        assertThrows(ConstraintViolationException.class,() -> fr.save(f),
        "You are not constraining "+
        "the startDate property, it should be mandatory");

        Feeding f3=new Feeding();
        f3.setId(7);
        f3.setStartDate(LocalDate.of(2021, 12, 31));
        assertThrows(Exception.class,() -> fr.save(f3),
        "You are not constraining the weeks duration to be mandatory");

        Feeding f4=new Feeding();
        f4.setId(7);
        f4.setStartDate(LocalDate.of(2021, 12, 31));
        f4.setWeeksDuration(-1);
        assertThrows(Exception.class,() -> fr.save(f4),
        "You are not constraining the weeks duration to be greater or equal than 1");
    }

    void testAnnotations(){
        try{
            Field date=Feeding.class.getDeclaredField("startDate");
            DateTimeFormat dateformat=date.getAnnotation(DateTimeFormat.class);
            assertNotNull(dateformat, "The date property is not annotated with a DateTimeFormat");
            assertEquals(dateformat.pattern(),"yyyy/MM/dd");            
            Column annotationColumn=date.getAnnotation(Column.class);
            assertNotNull(annotationColumn,"The start date property is not properly annotated");
            assertEquals("start_date",annotationColumn.name(),"The name of the attribute startDate is not properly customized in the database");

            Field weeksDuration=Feeding.class.getDeclaredField("weeksDuration");
            annotationColumn=weeksDuration.getAnnotation(Column.class);
            assertNotNull(annotationColumn,"The weeks duration property is not properly annotated");
            assertEquals("weeks_duration",annotationColumn.name(),"The name of the attribute weeksDuration is not properly customized in the database");
        }catch(NoSuchFieldException ex){
            fail("The Feeding class should have a field that is not present: "+ex.getMessage());
        }
    }
}
