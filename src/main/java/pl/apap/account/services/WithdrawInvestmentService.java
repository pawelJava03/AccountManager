package pl.apap.account.services;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.apap.account.model.User;
import pl.apap.account.repositories.UsersRepository;

import java.math.BigDecimal;

@Service
public class WithdrawInvestmentService {

    @Autowired
    UsersRepository usersRepository;

    public void withdrawInvestment(BigDecimal amount, User user) {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(user.getInvestedMoney()) <= 0) {
            user.setInvestedMoney(user.getInvestedMoney().subtract(amount));
            user.setAccountBalance(user.getAccountBalance().add(amount));
            usersRepository.save(user);
        }else {
            throw new IllegalArgumentException("Amount must be greater than zero and less or equal to invested amount");
        }
    }


}
