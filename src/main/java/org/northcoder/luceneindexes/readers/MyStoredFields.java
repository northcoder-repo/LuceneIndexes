package org.northcoder.luceneindexes.readers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.store.FSDirectory;

public class MyStoredFields {

    private static final String INDEX_PATH = "index";

    //
    // Structure: doc > field > term name/value
    //
    public static String getStoredFields() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_PATH)));) {

            sb.append("\nSTORED FIELDS:\n");

            StoredFields storedFields = indexReader.storedFields();
            int numDocs = indexReader.numDocs();
            for (int i = 0; i < numDocs; i++) {
                Document doc = storedFields.document(i);
                List<IndexableField> indexableFields = doc.getFields();
                if (!indexableFields.isEmpty()) { // may have no stored data
                    sb.append("doc ").append(i).append("\n");
                    for (int j = 0; j < indexableFields.size(); j++) {
                        IndexableField field = indexableFields.get(j);
                        sb.append("  field ").append((j + 1)).append("\n");
                        sb.append("    name ").append(field.name()).append("\n");
                        sb.append("    value ").append(doc.get(field.name())).append("\n");
                    }
                }
            }
        }
        return sb.toString();
    }

}
