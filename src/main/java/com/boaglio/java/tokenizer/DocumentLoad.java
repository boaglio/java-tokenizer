package com.boaglio.java.tokenizer;

import edu.stanford.nlp.pipeline.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DocumentLoad implements ApplicationRunner  {

    Logger logger = LoggerFactory.getLogger(DocumentLoad.class.getSimpleName());

    public static Analyzer analyzer = new SimpleAnalyzer();
    public static ByteBuffersDirectory directory = new ByteBuffersDirectory();

    public static List<String> documents = Arrays.asList(
            "Machine learning é um campo da inteligência artificial que permite que computadores aprendam padrões a partir de dados.",
            "O aprendizado de máquina dá aos sistemas a capacidade de melhorar seu desempenho sem serem explicitamente programados.",
            "Em vez de seguir apenas regras fixas, o machine learning descobre relações escondidas nos dados.",
            "Esse campo combina estatística, algoritmos e poder computacional para extrair conhecimento.",
            "O objetivo é criar modelos capazes de generalizar além dos exemplos vistos no treinamento.",
            "Aplicações de machine learning vão desde recomendações de filmes até diagnósticos médicos.",
            "Os algoritmos de aprendizado de máquina transformam dados brutos em previsões úteis.",
            "Diferente de um software tradicional, o ML adapta-se conforme novos dados chegam.",
            "O aprendizado pode ser supervisionado, não supervisionado ou por reforço, dependendo do tipo de problema.",
            "Na prática, machine learning é o motor que impulsiona muitos avanços em visão computacional e processamento de linguagem natural.",
            "Mais do que encontrar padrões, o machine learning ajuda a tomar decisões baseadas em evidências."
    );

    @Override
    public void run(ApplicationArguments args) throws Exception {

        logger.info(">>> Init doc load...");

        //   TF — Term Frequency (Frequência do termo)
        //  IDF — Inverse Document Frequency (Frequência inversa do documento)

        // -------------------------------
        // Pré-processamento
        // -------------------------------
        List<String> preprocessed = documents.stream()
                .map(doc -> String.join(" ", TokenizeUtil.tokenize(doc)))
                .toList();

        logger.info("Documentos após preprocessamento:");
        preprocessed.forEach(logger::info);

        // -------------------------------
        // Construção da matriz TF-IDF via Lucene
        // -------------------------------
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory, config);

        for (String text : preprocessed) {
            Document doc = new Document();
            doc.add(new TextField("content", text, Field.Store.YES));
            writer.addDocument(doc);
        }
        writer.close();

        logger.info(">>> End doc load...");

    }
}
