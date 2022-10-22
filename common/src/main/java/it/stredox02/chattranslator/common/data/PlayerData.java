package it.stredox02.chattranslator.common.data;

import it.stredox02.chattranslator.common.consts.Languages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
public class PlayerData {

    private final UUID playerUUID;

    @Getter @Setter private Languages sourceLanguage = Languages.English_American;
    @Getter @Setter private Languages targetLanguage = Languages.English_American;

}
