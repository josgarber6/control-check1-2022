package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.feeding.FeedingService;
import org.springframework.samples.petclinic.feeding.FeedingType;
import org.springframework.samples.petclinic.feeding.FeedingTypeFormatter;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class Test8 {

    @Autowired
    FeedingTypeFormatter formatter;

    @Autowired
    FeedingService fs;

    @Test
    public void test8(){
        validateFindFeedingTypeByName();
        validateNotFoundFeedingTypeByName();
        testFormatterIsInjected();
        testFormatterObject2String();
        testFormatterString2Object();
        testFormatterString2ObjectNotFound();
    }
    
    private void testFormatterIsInjected(){
        assertNotNull(formatter);
    }
    
    private void testFormatterObject2String(){
        FeedingType feedingType=new FeedingType();
        feedingType.setName("Prueba");
        String result=formatter.print(feedingType, null);
        assertEquals("Prueba",result, "The method print of the formatter is not working properly.");
    }
    
    private void testFormatterString2Object(){
        String name="High Protein Puppy Food";
        FeedingType feedingType;
        try {
            feedingType = formatter.parse(name, null);
            assertNotNull(feedingType, "The method parse of the formatter is not working properly.");
            assertEquals(name, feedingType.getName(), "The method parse of the formatter is not working properly.");
        } catch (ParseException e) {           
            fail("The method parse of the formatter is not working properly.", e);
        }        
    }
    
    private void testFormatterString2ObjectNotFound(){
        String name="This is not a feeding type";
        assertThrows(ParseException.class, () -> formatter.parse(name, null), "The method parse of the formatter is not working properly.");          
    }

    private void validateFindFeedingTypeByName(){
        String typeName="High Protein Puppy Food";
        FeedingType feedingType=fs.getFeedingType(typeName);
        assertNotNull(feedingType, "getFeedingType by name is returning null");
        assertEquals(typeName,feedingType.getName(), "getFeedingType by name is not returning an existing feeding type");
    }
    
    private void validateNotFoundFeedingTypeByName(){
        String typeName="This is not a valid feeding type name";
        FeedingType feedingType=fs.getFeedingType(typeName);
        assertNull(feedingType, "getFeedingType by name is returning a feeding type that does not exist");
    }

}