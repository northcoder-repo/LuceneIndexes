package org.northcoder.luceneindexes.readers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.northcoder.luceneindexes.MyIndexWriter;

public class MyTermVectorsTest {

    @BeforeAll
    public static void setUpClass() throws IOException, FileNotFoundException,
            ParseException {
        MyIndexWriter.buildIndex();
    }

    @Test
    public void testGetTermVectors() throws Exception {
        System.out.println("Testing getTermVectors");

        String expected = """

            TERM VECTORS:
            doc 0
              field 1
                name body
                positions true
                offsets   true
                payloads  false
                term brown
                  freq 1
                  position 4
                    startoffset 21
                    endoffset 26
                term fox
                  freq 1
                  position 5
                    startoffset 27
                    endoffset 30
                term nimble
                  freq 1
                  position 3
                    startoffset 14
                    endoffset 20
                term quick
                  freq 1
                  position 1
                    startoffset 4
                    endoffset 9
            doc 1
              field 1
                name body
                positions true
                offsets   true
                payloads  false
                term brown
                  freq 1
                  position 4
                    startoffset 22
                    endoffset 27
                term dog
                  freq 1
                  position 5
                    startoffset 28
                    endoffset 31
                term shaggy
                  freq 1
                  position 3
                    startoffset 15
                    endoffset 21
                term slow
                  freq 2
                  position 1
                    startoffset 4
                    endoffset 8
                  position 2
                    startoffset 10
                    endoffset 14
                           """;

        String actual = MyTermVectors.getTermVectors();
        assertEquals(expected, actual);
    }

}
