package org.northcoder.luceneindexes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class MyIndexWriter {

    private static final String INDEX_PATH = "index";
    private static final String TITLE_FIELD = "title";
    private static final String BODY_FIELD = "body";

    public static void buildIndex() throws IOException, FileNotFoundException, ParseException {
        final Directory dir = FSDirectory.open(Paths.get(INDEX_PATH));

        Analyzer analyzer = new StandardAnalyzer(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        // for human-readable index data - DO NOT USE this codec in production:
        iwc.setCodec(new SimpleTextCodec());

        List<String[]> myDocs = Arrays.asList(
                new String[]{"fox", "the quick and nimble brown fox"},
                new String[]{"dog", "the slow, slow shaggy brown dog"});

        FieldType fieldType = new FieldType();
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        fieldType.setStored(true); // default = false (same as Field.Store.NO)
        fieldType.setTokenized(true); // default = true (tokenize the content)
        fieldType.setOmitNorms(false); // default = false (used when scoring)
        fieldType.setStoreTermVectors(true); // captures frequencies
        fieldType.setStoreTermVectorPositions(true); // depends on freqs
        fieldType.setStoreTermVectorOffsets(true);

        Document doc;
        try (IndexWriter writer = new IndexWriter(dir, iwc)) {
            for (String[] myDoc : myDocs) {
                doc = new Document();
                doc.add(new TextField(TITLE_FIELD, myDoc[0], Field.Store.NO));
                doc.add(new Field(BODY_FIELD, myDoc[1], fieldType));
                writer.addDocument(doc);
            }
        }

    }

}
