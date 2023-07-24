package org.northcoder.luceneindexes.readers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.northcoder.luceneindexes.MyIndexWriter;

public class MyStoredFieldsTest {

    @BeforeAll
    public static void setUpClass() throws IOException, FileNotFoundException,
            ParseException {
        MyIndexWriter.buildIndex();
    }

    @Test
    public void testGetStoredFields() throws Exception {
        System.out.println("Testing getStoredFields");

        String expected = """

           STORED FIELDS:
           doc 0
             field 1
               name body
               value the quick and nimble brown fox
           doc 1
             field 1
               name body
               value the slow, slow shaggy brown dog
                           """;

        String actual = MyStoredFields.getStoredFields();
        assertEquals(expected, actual);
    }

}
