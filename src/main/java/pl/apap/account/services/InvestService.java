package pl.apap.account.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.apap.account.model.User;
import pl.apap.account.repositories.UsersRepository;

import java.math.BigDecimal;

@Service
public class InvestService {

    @Autowired
    UsersRepository usersRepository;

    public void invest(BigDecimal amount, User user){

        user.setAccountBalance(user.getAccountBalance().subtract(amount));
        user.setInvestedMoney(user.getInvestedMoney().add(amount));
        usersRepository.save(user);
    }

}
