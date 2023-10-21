package group.test.dtos;

import group.test.entities.CustomerType;
import group.test.entities.History;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class HistoryDto {
    private Integer id;
    private String numList;
    private String dateTime;
    private CustomerType customerType;
    private BigDecimal discount;
    private BigDecimal total;
}
