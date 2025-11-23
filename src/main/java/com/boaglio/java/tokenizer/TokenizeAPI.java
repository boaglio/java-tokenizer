package com.boaglio.java.tokenizer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TokenizeAPI {

    Logger logger = LoggerFactory.getLogger(TokenizeAPI.class.getSimpleName());

    @GetMapping("/api/simple-tokenization")
    public List<String> process() {

        logger.info(">>> Init Tokenization...");

        var result = new ArrayList<String>();

        var texto = """
                Machine learning é um campo da inteligência artificial
                que permite que computadores aprendam padrões a partir de dados.
                """;

        var document = new CoreDocument(texto) ;

        TokenizeUtil.pipeline.annotate(document);

        logger.info("Tokens encontrados:");
        for (CoreLabel token : document.tokens()) {
            result.add(token.word());
        }

        return result;

    }

}
