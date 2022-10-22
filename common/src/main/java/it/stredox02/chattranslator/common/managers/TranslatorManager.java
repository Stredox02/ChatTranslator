package it.stredox02.chattranslator.common.managers;

import com.google.common.net.UrlEscapers;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.stredox02.chattranslator.common.cache.CachedSentence;
import it.stredox02.chattranslator.common.data.PlayerData;
import it.stredox02.chattranslator.common.plugin.ChatTranslatorPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.logging.Level;

@RequiredArgsConstructor
public class TranslatorManager {

    private final ChatTranslatorPlugin enhancedTranslator;

    private boolean quotaExceeded;

    public void translate(Player player, String messageToTranslate, Consumer<String> callback) {
        PlayerData playerData = PlayerDataManager.getInstance().getData(player);
        if (playerData == null) {
            if(enhancedTranslator.isDebug()){
                enhancedTranslator.log(Level.SEVERE,"Unable to translate: PlayerData object is null");
            }
            callback.accept(messageToTranslate);
            return;
        }
        if (playerData.getSourceLanguage() == null) {
            if(enhancedTranslator.isDebug()){
                enhancedTranslator.log(Level.SEVERE,"Unable to translate: Source Language is null");
            }
            callback.accept(messageToTranslate);
            return;
        }

        CachedSentence cachedSentence = WordCacheManager.getInstance().getCachedSentence(messageToTranslate, playerData.getTargetLanguage());
        if (cachedSentence != null) {
            if(enhancedTranslator.isDebug()){
                enhancedTranslator.log(Level.INFO,"Using the cached sentence in order of preserve word usage from DeepL");
            }
            callback.accept(cachedSentence.getTranslatedSentence());
            return;
        }

        if (quotaExceeded) {
            if(enhancedTranslator.isDebug()){
                enhancedTranslator.log(Level.SEVERE,"Unable to translate because you've exceeded the amount of word that can be translated");
            }
            callback.accept(messageToTranslate);
            return;
        }

        String escapedMessage = UrlEscapers.urlFragmentEscaper().escape(messageToTranslate);

        String url = "https://api-free.deepl.com/v2/translate?auth_key=" + enhancedTranslator.getDeepLApiKey() + "&text=" + escapedMessage +
                "&source_lang=" + playerData.getSourceLanguage().getSourceLang() +
                "&target_lang=" + playerData.getTargetLanguage().getTargetLang();

        if(enhancedTranslator.isDebug()){
            enhancedTranslator.log(Level.INFO,"Calling url: " + url);
        }

        SchedulerManager.getInstance().scheduleNow(() -> {
            HttpURLConnection connection = null;
            try {
                URL obj = new URL(url);
                connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();

                if (responseCode == 456) {
                    quotaExceeded = true;
                    callback.accept(messageToTranslate);
                    return;
                }

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();

                    String inputLine;
                    while ((inputLine = bufferedReader.readLine()) != null) {
                        response.append(inputLine);
                    }
                    bufferedReader.close();

                    JsonElement parsed = new JsonParser().parse(response.toString());
                    JsonObject jsonObject = parsed.getAsJsonObject();
                    JsonArray array = jsonObject.getAsJsonArray("translations");

                    String newText = array.get(0).getAsJsonObject().get("text").getAsString();

                    byte[] byteText = newText.getBytes();
                    String translatedString = new String(byteText, StandardCharsets.UTF_8);

                    if (!translatedString.isEmpty()) {
                        WordCacheManager.getInstance().addSentenceInCache(messageToTranslate, playerData.getSourceLanguage(),
                                translatedString, playerData.getTargetLanguage());

                        if(enhancedTranslator.isDebug()){
                            enhancedTranslator.log(Level.INFO,"&aSuccessfully translated the string:" +
                                    "\nOriginal: " + escapedMessage +
                                    "\nTranslated: " + translatedString);
                        }

                        callback.accept(translatedString);
                        return;
                    }
                }
                callback.accept(messageToTranslate);

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

}
