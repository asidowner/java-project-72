package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class Url {
    private Long id;
    private String name;
    private Timestamp createAt;

    public Url(String name, Timestamp createAt) {
        this.name = name;
        this.createAt = createAt;
    }
}
