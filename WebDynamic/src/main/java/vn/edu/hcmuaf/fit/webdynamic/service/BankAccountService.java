package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.BankAccountDao;
import vn.edu.hcmuaf.fit.webdynamic.model.BankAccount;

import java.util.Optional;

public class BankAccountService {
    private final BankAccountDao dao = new BankAccountDao();

    public Optional<BankAccount> getByUserId(int userId) {
        return dao.findByUserId(userId);
    }

    public boolean insert(BankAccount b) {
        return dao.insert(b);
    }

    public boolean update(BankAccount b) {
        return dao.update(b);
    }

}
