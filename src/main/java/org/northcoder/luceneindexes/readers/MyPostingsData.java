package org.northcoder.luceneindexes.readers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class MyPostingsData {

    private static final String INDEX_PATH = "index";
    private static final StringBuilder sb = new StringBuilder();

    //
    // Structure: field > term > doc > freq/pos/offset
    //
    public static String getIndexData() throws IOException {
        sb.setLength(0);
        try (IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_PATH)));) {

            sb.append("\nPOSTINGS INFO:\n");

            processSegments(indexReader);
        }
        return sb.toString();
    }

    private static void processSegments(IndexReader indexReader) throws IOException {
        List<LeafReaderContext> leafReaderContexts = indexReader.leaves();
        for (LeafReaderContext leafReaderContext : leafReaderContexts) {
            LeafReader leafReader = leafReaderContext.reader();
            processSegment(indexReader, leafReader);
        }
    }

    private static void processSegment(IndexReader indexReader, LeafReader leafReader) throws IOException {
        List<String> indexedFields = FieldInfos.getIndexedFields(indexReader)
                .stream().sorted().toList();
        for (String fieldName : indexedFields) {
            processField(leafReader, fieldName);
        }
    }

    private static void processField(LeafReader leafReader, String fieldName) throws IOException {
        sb.append("field ").append(fieldName).append("\n");

        Terms terms = leafReader.terms(fieldName);
        TermsEnum termsEnum = terms.iterator();

        BytesRef term;
        // iterate over the terms enum using next():
        while ((term = termsEnum.next()) != null) {
            processTerm(termsEnum, term);
        }
    }

    private static void processTerm(TermsEnum termsEnum, BytesRef term) throws IOException {
        sb.append("  term ").append(term.utf8ToString()).append("\n");

        // the null parameter means there is no postings enum to re-use:
        // the ALL parameter means we fetch all possible posting data:
        PostingsEnum postings = termsEnum.postings(null, PostingsEnum.ALL);

        int doc;
        // iterate over the postings enum using nextDoc():
        while ((doc = postings.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
            processDoc(postings, doc);
        }
    }

    private static void processDoc(PostingsEnum postings, int doc) throws IOException {
        sb.append("    doc ").append(doc).append("\n");

        int freq = postings.freq();
        sb.append("      freq ").append(freq).append("\n");

        for (int i = 0; i < freq; i++) {
            processPostingData(postings);
        }
    }

    private static void processPostingData(PostingsEnum postings) throws IOException {
        // iterate over a posting's entries using nextPosition():
        int position = postings.nextPosition();
        int startOffset = postings.startOffset();
        int endOffset = postings.endOffset();

        sb.append("      pos ").append(position).append("\n");
        if (startOffset > -1) {
            sb.append("      startOffset ").append(startOffset).append("\n");
            sb.append("      endOffset ").append(endOffset).append("\n");
        }
    }

    public static String seekData(String fieldName, String searchTerm) throws IOException {
        sb.setLength(0);
        try (IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_PATH)));) {

            sb.append("\nSEEK DATA:\n");
            sb.append("field name: ").append(fieldName).append("\n");
            sb.append("search term: ").append(searchTerm).append("\n");

            List<LeafReaderContext> leafReaderContexts = indexReader.leaves();
            for (LeafReaderContext leafReaderContext : leafReaderContexts) {
                LeafReader leafReader = leafReaderContext.reader();
                TermsEnum termsEnum = leafReader.terms(fieldName).iterator();
                boolean found = termsEnum.seekExact(new BytesRef(searchTerm));
                if (found) {
                    sb.append("  docs found: ").append(termsEnum.docFreq()).append("\n");
                    PostingsEnum postings = termsEnum.postings(null, PostingsEnum.ALL);
                    int doc;
                    while ((doc = postings.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
                        processDoc(postings, doc);
                    }
                }
            }
        }
        return sb.toString();
    }

}
