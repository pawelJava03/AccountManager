package pl.apap.account.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.apap.account.model.User;
import pl.apap.account.repositories.UsersRepository;

import java.math.BigDecimal;

@Service
public class WithdrawService {

    private final UsersRepository repository;

    @Autowired
    public WithdrawService(UsersRepository repository) {
        this.repository = repository;
    }

    public void withdraw(BigDecimal amount, User user){
        if (amount.compareTo(BigDecimal.ZERO) > 0){
            user.setAccountBalance(user.getAccountBalance().subtract(amount));
            user.setTotalSpent(user.getTotalSpent().add(amount));
            repository.save(user);
        }else {
            throw new IllegalArgumentException("Amount must be greater than zero for withdraw.");
        }
    }


}
