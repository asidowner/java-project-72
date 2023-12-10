package hexlet.code.util;


import lombok.Getter;

@Getter
public class Settings {
    private final int applicationPort;
    private final boolean debug;
    private final String databaseUrl;

    public Settings() {
        this.applicationPort = Integer.parseInt(
                System.getenv().getOrDefault("PORT", "7070")
        );
        this.debug = Boolean.parseBoolean(System.getenv().getOrDefault("DEBUG", "false"));
        this.databaseUrl = System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:hexlet;DB_CLOSE_DELAY=-1;");
    }
}
