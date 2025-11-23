package com.boaglio.java.tokenizer;


import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.boaglio.java.tokenizer.DocumentLoad.analyzer;
import static com.boaglio.java.tokenizer.DocumentLoad.directory;

@RestController
public class SimpleTFIDFAPI {

    //   TF — Term Frequency (Frequência do termo)
    //  IDF — Inverse Document Frequency (Frequência inversa do documento)

    Logger logger = LoggerFactory.getLogger(SimpleTFIDFAPI.class.getSimpleName());

    @GetMapping("/api/simple-tfidfapi")
    public List<String> findDocuments(@RequestParam(required = false) @Schema(defaultValue = "machine learning") String query) throws IOException, ParseException {

        logger.info(">>> query: " +query);

        var result = new ArrayList<String>();

        // Índice
        IndexReader reader = DirectoryReader.open(directory);
        var searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new ClassicSimilarity());

        // -------------------------------
        // Busca - search_tfidf
        // -------------------------------

        var queryLucene = new QueryParser("content", analyzer).parse(query);

        TopDocs topDocs = searcher.search(queryLucene, 5);

        logger.info("=== Top 5 documentos para a query: [" + query + "] === ");
        for (ScoreDoc sd : topDocs.scoreDocs) {
            int docId = sd.doc;
            float score = sd.score;
            result.add(DocumentLoad.documents.get(docId));
            logger.info("Doc " + docId + " | score=" + score + " → " + DocumentLoad.documents.get(docId));
        }

        reader.close();

        return result;
    }

    @GetMapping("/api/all-docs")
    public List<String> findAllDocuments() {
        return DocumentLoad.documents;
    }

}