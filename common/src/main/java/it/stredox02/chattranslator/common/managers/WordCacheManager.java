package it.stredox02.chattranslator.common.managers;

import it.stredox02.chattranslator.common.consts.Languages;
import it.stredox02.chattranslator.common.cache.CachedSentence;
import it.stredox02.chattranslator.common.cache.CachedWord;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class WordCacheManager {

    private static WordCacheManager instance;

    private final Map<String, List<CachedWord>> singleWordCache;
    private final Map<String, List<CachedSentence>> sentenceCache;

    private WordCacheManager(){
        singleWordCache = new ConcurrentHashMap<>();
        sentenceCache = new ConcurrentHashMap<>();

        SchedulerManager.getInstance().scheduleRepeating(() -> {
            for (List<CachedSentence> sentences : sentenceCache.values()) {
                for (CachedSentence sentence : sentences) {
                    if(sentence.getTimestamp() + (3 * 60 * 1000) > System.currentTimeMillis()){
                        continue;
                    }
                    removeSentence(sentence.getOriginalSentence(), sentence);
                }
            }
        },0,1, TimeUnit.SECONDS);
    }

    public static WordCacheManager getInstance() {
        if(instance == null){
            instance = new WordCacheManager();
        }
        return instance;
    }

    public void addSentenceInCache(String original, Languages originalTranslation, String translated, Languages targetTranslation) {
        sentenceCache.computeIfAbsent(original, s -> new CopyOnWriteArrayList<>())
                .add(new CachedSentence(originalTranslation, targetTranslation, original, translated));
    }

    public void addSingleWordInCache(String original, Languages originalTranslation, String translated, Languages targetTranslation) {
        singleWordCache.computeIfAbsent(original, s -> new CopyOnWriteArrayList<>())
                .add(new CachedWord(originalTranslation, targetTranslation, original, translated));
    }

    public CachedWord getCachedWord(String original, Languages targetTranslation){
        List<CachedWord> cachedWords = singleWordCache.get(original);
        if(cachedWords == null){
            return null;
        }
        for (CachedWord cachedWord : cachedWords) {
            if(cachedWord.getOriginalWord().equalsIgnoreCase(original) && cachedWord.getTargetTranslation() == targetTranslation){
                return cachedWord;
            }
        }
        return null;
    }

    public CachedSentence getCachedSentence(String original, Languages targetTranslation){
        List<CachedSentence> cachedWords = sentenceCache.get(original);
        if(cachedWords == null){
            return null;
        }
        for (CachedSentence cachedSentence : cachedWords) {
            if(cachedSentence.getOriginalSentence().equalsIgnoreCase(original) && cachedSentence.getTargetTranslation() == targetTranslation){
                return cachedSentence;
            }
        }
        return null;
    }

    public void removeSentence(String original, CachedSentence cachedSentence){
        List<CachedSentence> cachedSentences = sentenceCache.get(original);
        if(cachedSentences == null){
            return;
        }
        cachedSentences.removeIf(cachedSentence::equals);
        if(cachedSentences.isEmpty()){
            sentenceCache.remove(original);
        }
    }

}
