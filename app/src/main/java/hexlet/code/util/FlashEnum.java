package hexlet.code.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FlashEnum {
    primary("primary"),
    secondary("secondary"),
    success("success"),
    danger("danger"),
    warning("warning"),
    info("info"),
    light("light"),
    dark("dark");

    private final String type;

    @Override
    public String toString() {
        return type;
    }
}