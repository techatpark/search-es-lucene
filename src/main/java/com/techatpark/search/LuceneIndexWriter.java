package com.techatpark.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class LuceneIndexWriter {

    static final String INDEX_PATH = "indexDir";
    static final String JSON_FILE_PATH = "docs.json";
    static final String STOPWORDS_FILE_PATH = "stopwords.txt";

    String indexPath;
    String jsonFilePath;
    IndexWriter indexWriter = null;

    public LuceneIndexWriter(String indexPath, String jsonFilePath) {
        this.indexPath = indexPath;
        this.jsonFilePath = jsonFilePath;
    }

    public void createIndex() throws FileNotFoundException {
        JSONArray jsonObjects = parseJSONFile();
        openIndex();
        addDocuments(jsonObjects);
        finish();
    }

    public JSONArray parseJSONFile() throws FileNotFoundException {
        InputStream jsonFile = new FileInputStream(jsonFilePath);
        Reader readerJson = new InputStreamReader(jsonFile);
        JSONArray arrayObjects = new JSONArray(readerJson);
        return arrayObjects;
    }

    public boolean openIndex() {
        try {
            InputStream stopWords = new FileInputStream(STOPWORDS_FILE_PATH);
            Reader readerStopWords = new InputStreamReader(stopWords);
            Directory dir = FSDirectory.open(new File(indexPath).toPath());
            StandardAnalyzer analyzer = new StandardAnalyzer(readerStopWords);
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            indexWriter = new IndexWriter(dir, iwc);
            return true;
        } catch (Exception e) {
            System.err.println("Error opening the index. " + e.getMessage());
        }
        return false;
    }

    /**
     * Add documents to the index
     */
    public void addDocuments(JSONArray jsonObjects) {
        jsonObjects.iterator().forEachRemaining(object -> {
            Document doc = new Document();
            final FieldType bodyOptions = new FieldType();
            bodyOptions.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
            bodyOptions.setStored(true);
            bodyOptions.setStoreTermVectors(true);
            bodyOptions.setTokenized(true);
            ((JSONObject)object).keySet().stream().forEach(field -> {
                doc.add(new Field(field, (String) ((JSONObject)object).get(field), bodyOptions));
            });
            try {
                System.out.println(doc);
                indexWriter.addDocument(doc);
            } catch (IOException ex) {
                System.err.println("Error adding documents to the index. " + ex.getMessage());
            }
        });
    }

    /**
     * Write the document to the index and close it
     */
    public void finish() {
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException ex) {
            System.err.println("We had a problem closing the index: " + ex.getMessage());
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        LuceneIndexWriter liw = new LuceneIndexWriter(INDEX_PATH, JSON_FILE_PATH);
        liw.createIndex();
    }

}