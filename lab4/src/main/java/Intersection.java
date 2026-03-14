import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter

public class Intersection {
    private String name;

    public Intersection(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other){
        return switch (other) {
            case null -> false;
            case Intersection intersection -> Objects.equals(this.name, intersection.name);
            default -> false;
        };
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
