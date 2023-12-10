package hexlet.code.util;


import lombok.Getter;

@Getter
public class Settings {
    private final int applicationPort;
    private final boolean debug;

    public Settings() {
        this.applicationPort = Integer.parseInt(
                System.getenv().getOrDefault("PORT", "7070")
        );
        this.debug = Boolean.parseBoolean(System.getenv().getOrDefault("DEBUG", "false"));
    }
}
