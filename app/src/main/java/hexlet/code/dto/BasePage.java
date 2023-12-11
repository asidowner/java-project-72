package hexlet.code.dto;

import hexlet.code.util.FlashEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasePage {
    private String flash;
    private FlashEnum flashType;
}
