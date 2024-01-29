package pl.apap.account.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.apap.account.model.User;
import pl.apap.account.repositories.UsersRepository;

import java.math.BigDecimal;

@Service
public class DepositService {

    private final UsersRepository repository;

    @Autowired
    public DepositService(UsersRepository repository) {
        this.repository = repository;
    }

    public void deposit(User user, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            user.setAccountBalance(user.getAccountBalance().add(amount));
            user.setTotalEarned(user.getTotalEarned().add(amount));
            repository.save(user);
        } else {
            throw new IllegalArgumentException("Amount must be greater than zero for deposit.");
        }
    }
}
