package mephi.exercise.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Объект пользователя
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
@Setter
@Getter
@EqualsAndHashCode()
public final class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = -8358054458692846515L;

    private Integer id;
    private Integer userId;
    private String name;
    private BigDecimal limit;

    @Override
    public String toString() {
        return new StringBuilder()
                .append(id).append(" => [")
                .append("Name: \"").append(name).append("\", ")
                .append("Limit: \"").append(limit).append("\"")
                .append("]")
                .toString();
    }
}
