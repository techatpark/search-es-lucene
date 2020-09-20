package com.techatpark.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

class LuceneFileSearchTest {
    @Test
    public void givenSearchQueryWhenFetchedFileNamehenCorrect() throws IOException, URISyntaxException {
        String indexPath = "index";
        String dataPath = "data/employees.json";

        Directory directory = FSDirectory.open(Paths.get(indexPath));
        LuceneFileSearch luceneFileSearch = new LuceneFileSearch(directory, new StandardAnalyzer());

        luceneFileSearch.addFileToIndex(dataPath);

        List<Document> docs = luceneFileSearch.searchFiles("contents", "at@ipsum.ca");

        assertEquals("employees.json", docs.get(0).get("filename"));
    }
}