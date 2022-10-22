package it.stredox02.chattranslator.common.consts;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public enum Languages {

    Bulgarian("Bulgarian", "BG", "BG", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTkwMzllMWZkODhjNzhkOWQ3YWRjNWFhZDVhYjE2ZTM1NmJlMTM0NjQ5MzRlZDllMmIwY2VmMjA1MWM1YjUzNCJ9fX0="),
    Czech("Czech", "CS", "CS", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDgxNTJiNzMzNGQ3ZWNmMzM1ZTQ3YTRmMzVkZWZiZDJlYjY5NTdmYzdiZmU5NDIxMjY0MmQ2MmY0NmU2MWUifX19"),
    Danish("Danish", "DA", "DA", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTBjMjMwNTVjMzkyNjA2ZjdlNTMxZGFhMjY3NmViZTJlMzQ4OTg4ODEwYzE1ZjE1ZGM1YjM3MzM5OTgyMzIifX19"),
    German("German", "DE", "DE", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU3ODk5YjQ4MDY4NTg2OTdlMjgzZjA4NGQ5MTczZmU0ODc4ODY0NTM3NzQ2MjZiMjRiZDhjZmVjYzc3YjNmIn19fQ=="),
    Greek("Greek", "EL", "EL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTUxNGRlNmRkMmI3NjgyYjFkM2ViY2QxMDI5MWFlMWYwMjFlMzAxMmI1YzhiZWZmZWI3NWIxODE5ZWI0MjU5ZCJ9fX0="),
    English_British("British", "EN", "EN-GB", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQzOWQ3ZjljNjdmMzJkY2JiODZiNzAxMGIxZTE0YjYwZGU5Njc3NmEzNWY2MWNlZTk4MjY2MGFhY2Y1MjY0YiJ9fX0="),
    English_American("American", "EN", "EN-US", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNhYzk3NzRkYTEyMTcyNDg1MzJjZTE0N2Y3ODMxZjY3YTEyZmRjY2ExY2YwY2I0YjM4NDhkZTZiYzk0YjQifX19"),
    Spanish("Spanish", "ES", "ES", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJiZDQ1MjE5ODMzMDllMGFkNzZjMWVlMjk4NzQyODc5NTdlYzNkOTZmOGQ4ODkzMjRkYThjODg3ZTQ4NWVhOCJ9fX0="),
    Estonian("Estonian", "ET", "ET", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDhjY2U2NjI3ZTRkYmE5NjY0MGRhN2JiY2E4Y2Q0M2E3ODM0OTUxNjdiYWYyODM0YWE5OTExNzAxOGFkZiJ9fX0="),
    Finnish("Finnish", "FI", "FI", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTlmMjM0OTcyOWE3ZWM4ZDRiMTQ3OGFkZmU1Y2E4YWY5NjQ3OWU5ODNmYmFkMjM4Y2NiZDgxNDA5YjRlZCJ9fX0="),
    French("French", "FR", "FR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTEyNjlhMDY3ZWUzN2U2MzYzNWNhMWU3MjNiNjc2ZjEzOWRjMmRiZGRmZjk2YmJmZWY5OWQ4YjM1Yzk5NmJjIn19fQ=="),
    Hungarian("Hungarian", "HU", "HU", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGE5YzNjNGI2YzUwMzEzMzJkZDJiZmVjZTVlMzFlOTk5ZjhkZWZmNTU0NzQwNjVjYzg2OTkzZDdiZGNkYmQwIn19fQ=="),
    Indonesian("Indonesian", "ID", "ID", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2JlNjNmY2FhMDIwM2Y4MDQ2ZmI5NjM3NWQwNzlhNWMwOWI0MTYxZDAxMTlkNzE5NDU2NmU2ODljYWIxOGY2OCJ9fX0="),
    Italian("Italian", "IT", "IT", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODVjZTg5MjIzZmE0MmZlMDZhZDY1ZDhkNDRjYTQxMmFlODk5YzgzMTMwOWQ2ODkyNGRmZTBkMTQyZmRiZWVhNCJ9fX0="),
    Japanese("Japanese", "JA", "JA", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODA0M2FlOWJiZmE4YjhiYmI1Yzk2NGJiY2U0NWZiZTc5YTNhZDc0MmJlMDdiNTY2MDdjNjhjOGUxMTE2NCJ9fX0="),
    Lithuanian("Lithuanian", "LT", "LT", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGUzZDgyOTc0MTk3NmQ4MzMwZDY5NDdkNmVlYTY0ZWU1N2FlZDJmMjYyODZmZjYzODQ0ZTkwODkzNjdjMTEifX19"),
    Latvian("Latvian", "LV", "LV", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjYyYTQ5MzhiNTk0NDdmOTk2YjVlZDk0MTAxZGYwNzQyOWQxYWQzNDc3NmQ1OTFmZmM2ZmQ3NWI3OTQ3M2MifX19"),
    Dutch("Dutch", "VL", "VL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzIzY2YyMTBlZGVhMzk2ZjJmNWRmYmNlZDY5ODQ4NDM0ZjkzNDA0ZWVmZWFiZjU0YjIzYzA3M2IwOTBhZGYifX19"),
    Polish("Polish", "PL", "PL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTIxYjJhZjhkMjMyMjI4MmZjZTRhMWFhNGYyNTdhNTJiNjhlMjdlYjMzNGY0YTE4MWZkOTc2YmFlNmQ4ZWIifX19"),
    Portuguese_Brazilian("Brazilian", "PT", "PT-BR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWE0NjQ3NWQ1ZGNjODE1ZjZjNWYyODU5ZWRiYjEwNjExZjNlODYxYzBlYjE0ZjA4ODE2MWIzYzBjY2IyYjBkOSJ9fX0="),
    Portuguese_ALL("Portuguese", "PT", "PT-PT", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWJkNTFmNDY5M2FmMTc0ZTZmZTE5NzkyMzNkMjNhNDBiYjk4NzM5OGUzODkxNjY1ZmFmZDJiYTU2N2I1YTUzYSJ9fX0="),
    Romanian("Romanian", "RO", "RO", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGNlYjE3MDhkNTQwNGVmMzI2MTAzZTdiNjA1NTljOTE3OGYzZGNlNzI5MDA3YWM5YTBiNDk4YmRlYmU0NjEwNyJ9fX0="),
    Russian("Russian", "RU", "RU", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTZlYWZlZjk4MGQ2MTE3ZGFiZTg5ODJhYzRiNDUwOTg4N2UyYzQ2MjFmNmE4ZmU1YzliNzM1YTgzZDc3NWFkIn19fQ=="),
    Slovak("Slovak", "SK", "SK", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmM3MmE4YzExNWExZmI2NjlhMjU3MTVjNGQxNWYyMjEzNmFjNGMyNDUyNzg0ZTQ4OTRiM2Q1NmJjNWIwYjkifX19"),
    Slovenian("Slovenian", "SL", "SL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWEyODg4NTUxZjk1MTVjNGViMmY4ZjI5ZWFmZjhlN2QwOGU5Yzk3MjFkNDc4ZmRjYmE1MDZiMWMxM2EwMGM1NSJ9fX0="),
    Swedish("Swedish", "SV", "SV", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Q4NjI0MmIwZDk3ZWNlOTk5NDY2MGYzOTc0ZDcyZGY3Yjg4N2Y2MzBhNDUzMGRhZGM1YjFhYjdjMjEzNGFlYyJ9fX0="),
    Turkish("Turkish", "TR", "TR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg1MmI5YWJhMzQ4MjM0ODUxNGMxMDM0ZDBhZmZlNzM1NDVjOWRlNjc5YWU0NjQ3Zjk5NTYyYjVlNWY0N2QwOSJ9fX0="),
    Ukrainian("Ukrainian", "UK", "UK", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjhiOWY1MmUzNmFhNWM3Y2FhYTFlN2YyNmVhOTdlMjhmNjM1ZThlYWM5YWVmNzRjZWM5N2Y0NjVmNWE2YjUxIn19fQ=="),
    Chinese("Chinese", "ZH", "ZH", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y5YmMwMzVjZGM4MGYxYWI1ZTExOThmMjlmM2FkM2ZkZDJiNDJkOWE2OWFlYjY0ZGU5OTA2ODE4MDBiOThkYyJ9fX0=");

    private final String formatName;
    private final String sourceLang;
    private final String targetLang;
    private final String headTextureValue;

    Languages(String formatName, String sourceLang, String targetLang, String headTextureValue) {
        this.formatName = formatName;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.headTextureValue = headTextureValue;
    }

    public static Languages getFromShortCode(@NotNull String shortCode) {
        for (Languages value : values()) {
            if (value.targetLang.equalsIgnoreCase(shortCode) || value.targetLang.endsWith(shortCode.toUpperCase())) {
                return value;
            }
        }
        return null;
    }

}
