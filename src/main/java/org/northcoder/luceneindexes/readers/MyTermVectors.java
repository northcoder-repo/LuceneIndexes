package org.northcoder.luceneindexes.readers;

import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.TermVectors;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.FSDirectory;

public class MyTermVectors {

    private static final String INDEX_PATH = "index";

    //
    // Structure: doc > field > term > freq/pos > start/end offsets
    //
    public static String getTermVectors() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_PATH)));) {

            sb.append("\nTERM VECTORS:\n");

            int numDocs = indexReader.numDocs();
            TermVectors termVectors = indexReader.termVectors();
            for (int i = 0; i < numDocs; i++) {

                sb.append("doc ").append(i).append("\n");

                Fields tvFields = termVectors.get(i);

                int j = 1;
                for (String field : tvFields) {
                    Terms terms = tvFields.terms(field);
                    sb.append("  field ").append((j++)).append("\n");
                    sb.append("    name ").append(field).append("\n");
                    sb.append("    positions ").append(terms.hasPositions()).append("\n");
                    sb.append("    offsets   ").append(terms.hasOffsets()).append("\n");
                    sb.append("    payloads  ").append(terms.hasPayloads()).append("\n");

                    TermsEnum termsEnum = terms.iterator();
                    while (termsEnum.next() != null) {
                        PostingsEnum postingsEnum = termsEnum.postings(null, PostingsEnum.ALL);
                        while (postingsEnum.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
                            sb.append("    term ").append(termsEnum.term().utf8ToString()).append("\n");
                            int freq = postingsEnum.freq();
                            sb.append("      freq ").append(freq).append("\n");
                            for (int k = 0; k < freq; k++) {
                                sb.append("      position ").append(postingsEnum.nextPosition()).append("\n");
                                sb.append("        startoffset ").append(postingsEnum.startOffset()).append("\n");
                                sb.append("        endoffset ").append(postingsEnum.endOffset()).append("\n");
                            }
                        }
                    }
                }

            }
        }
        return sb.toString();
    }

}
