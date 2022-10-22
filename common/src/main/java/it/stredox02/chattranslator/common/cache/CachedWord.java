package it.stredox02.chattranslator.common.cache;

import it.stredox02.chattranslator.common.consts.Languages;
import lombok.Getter;

@Getter
public class CachedWord extends AbstractCache {

    private final String originalWord;
    private final String translatedWord;

    public CachedWord(Languages originalTranslation, Languages targetTranslation, String originalWord, String translatedWord) {
        super(originalTranslation, targetTranslation, System.currentTimeMillis());

        this.originalWord = originalWord;
        this.translatedWord = translatedWord;
    }

}
