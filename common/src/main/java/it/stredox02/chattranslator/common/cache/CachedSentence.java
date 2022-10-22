package it.stredox02.chattranslator.common.cache;

import it.stredox02.chattranslator.common.consts.Languages;
import lombok.Getter;

@Getter
public class CachedSentence extends AbstractCache {

    private final String originalSentence;
    private final String translatedSentence;

    public CachedSentence(Languages originalTranslation, Languages targetTranslation, String originalSentence, String translatedSentence) {
        super(originalTranslation, targetTranslation,System.currentTimeMillis());

        this.originalSentence = originalSentence;
        this.translatedSentence = translatedSentence;
    }

}
