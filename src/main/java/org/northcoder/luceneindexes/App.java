package org.northcoder.luceneindexes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import org.northcoder.luceneindexes.readers.MyPostingsData;
import org.northcoder.luceneindexes.readers.MyStoredFields;
import org.northcoder.luceneindexes.readers.MyTermVectors;

public class App {

    public static void main(String[] args) throws IOException,
            FileNotFoundException, ParseException {
        //
        // https://lucene.apache.org/core/9_7_0/core/org/apache/lucene/index/package-summary.html
        //
        MyIndexWriter.buildIndex();

        // extract indexed data:
        System.out.println(MyStoredFields.getStoredFields());
        System.out.println(MyPostingsData.getIndexData());
        System.out.println(MyTermVectors.getTermVectors());

        // search for a specific value in a specific field:
        String fieldName = "body";
        String searchTerm = "brown";
        System.out.println(MyPostingsData.seekData(fieldName, searchTerm));
    }
}
