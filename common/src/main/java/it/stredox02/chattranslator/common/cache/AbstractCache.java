package it.stredox02.chattranslator.common.cache;

import it.stredox02.chattranslator.common.consts.Languages;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class AbstractCache {

    protected final Languages originalTranslation;
    protected final Languages targetTranslation;
    protected final long timestamp;

}
