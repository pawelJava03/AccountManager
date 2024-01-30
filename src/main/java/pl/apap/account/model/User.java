package pl.apap.account.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
public class User {

    @Getter
    @Setter

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String email;
    private LocalDate dateOfBirth;
    private BigDecimal investedMoney;
    private BigDecimal accountBalance;
    private BigDecimal totalEarned;
    private BigDecimal totalSpent;
    private String password;

}
