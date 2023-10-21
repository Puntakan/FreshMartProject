package group.test.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT", name = "numList")
    private String numList;

    @Column(insertable = false, updatable = false)
    private ZonedDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    private BigDecimal discount;

    private BigDecimal total;
}