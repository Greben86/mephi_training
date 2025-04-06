package mephi.exercise.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Объект денежной операции
 */
@AllArgsConstructor
@Builder(setterPrefix = "set")
@Getter
public final class Operation implements Serializable {

    @Serial
    private static final long serialVersionUID = 5825084285742431299L;

    private final Integer id;
    // Храню значение идентификатора объекта, а не самого объекта
    // так как это упрощает проверку ссылочной целостности при сохранении в файл и чтении из файла
    private final Integer userId;
    @Setter
    private Integer categoryId;
    private final Date time;
    private final BigDecimal value;

    @Override
    public String toString() {
        return new StringBuilder()
                .append(id).append(" => [")
                .append("Time: \"").append(time).append("\", ")
                .append("Value: \"").append(value).append("\"")
                .append("]")
                .toString();
    }
}
