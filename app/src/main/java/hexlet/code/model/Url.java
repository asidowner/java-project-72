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
    private Timestamp createdAt;
    private Timestamp lastCheckDate;
    private int status;

    public Url(String name, Timestamp createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }
}
