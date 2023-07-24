package org.northcoder.luceneindexes.readers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.northcoder.luceneindexes.MyIndexWriter;

public class MyPostingsDataTest {

    @BeforeAll
    public static void setUpClass() throws IOException, FileNotFoundException, ParseException {
        MyIndexWriter.buildIndex();
    }

    @Test
    public void testGetIndexData() throws Exception {
        System.out.println("Testing getIndexData");

        String expected = """

            POSTINGS INFO:
            field body
              term brown
                doc 0
                  freq 1
                  pos 4
                  startOffset 21
                  endOffset 26
                doc 1
                  freq 1
                  pos 4
                  startOffset 22
                  endOffset 27
              term dog
                doc 1
                  freq 1
                  pos 5
                  startOffset 28
                  endOffset 31
              term fox
                doc 0
                  freq 1
                  pos 5
                  startOffset 27
                  endOffset 30
              term nimble
                doc 0
                  freq 1
                  pos 3
                  startOffset 14
                  endOffset 20
              term quick
                doc 0
                  freq 1
                  pos 1
                  startOffset 4
                  endOffset 9
              term shaggy
                doc 1
                  freq 1
                  pos 3
                  startOffset 15
                  endOffset 21
              term slow
                doc 1
                  freq 2
                  pos 1
                  startOffset 4
                  endOffset 8
                  pos 2
                  startOffset 10
                  endOffset 14
            field title
              term dog
                doc 1
                  freq 1
                  pos 0
              term fox
                doc 0
                  freq 1
                  pos 0
                          """;

        String actual = MyPostingsData.getIndexData();
        assertEquals(expected, actual);
    }

    @Test
    public void testSeekData() throws Exception {
        System.out.println("Testing seekData");
        String fieldName = "body";
        String searchTerm = "brown";

        String expected = """

            SEEK DATA:
            field name: body
            search term: brown
              docs found: 2
                doc 0
                  freq 1
                  pos 4
                  startOffset 21
                  endOffset 26
                doc 1
                  freq 1
                  pos 4
                  startOffset 22
                  endOffset 27
                          """;

        String actual = MyPostingsData.seekData(fieldName, searchTerm);
        assertEquals(expected, actual);
    }

}
