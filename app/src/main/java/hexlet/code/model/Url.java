package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class Url {
    private Long id;
    private String name;
    private ZonedDateTime createAt;

    public Url(String name, ZonedDateTime createAt) {
        this.name = name;
        this.createAt = createAt;
    }
}
