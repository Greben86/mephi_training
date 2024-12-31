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
public final class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 727844574013310195L;

    private Integer id;
    private String login;
    private String password;
    private BigDecimal amount;

    @Override
    public String toString() {
        var builder = new StringBuilder()
                .append(login).append(" => [")
                .append("ID: \"").append(id).append("\"")
                .append("]");
        return builder.toString();
    }
}
