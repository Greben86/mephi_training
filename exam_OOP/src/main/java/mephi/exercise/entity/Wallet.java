package mephi.exercise.entity;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class Wallet implements Serializable {

    @Serial
    private static final long serialVersionUID = -6643974352186640899L;

    private Integer id;
    private Integer userId;
    private BigDecimal amount;
}
