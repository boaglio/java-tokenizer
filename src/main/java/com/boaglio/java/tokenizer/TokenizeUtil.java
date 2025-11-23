package com.boaglio.java.tokenizer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.PropertiesUtils;

import java.util.List;
import java.util.stream.Collectors;

public class TokenizeUtil {

    static StanfordCoreNLP pipeline = new StanfordCoreNLP(
            PropertiesUtils.asProperties(
                    "annotators", "tokenize,ssplit",
                    "tokenize.language", "es", // melhor aproximação para PT
                    "encoding", "UTF-8"
            )
    );

    public static List<String> tokenize(String text) {
        CoreDocument doc = new CoreDocument(text.toLowerCase());
        pipeline.annotate(doc);

        return doc.tokens()
                .stream()
                .map(CoreLabel::word)
                .filter(w -> w.matches("[a-zA-Z0-9áéíóúàãõâêôç]+"))
                .collect(Collectors.toList());
    }


}
