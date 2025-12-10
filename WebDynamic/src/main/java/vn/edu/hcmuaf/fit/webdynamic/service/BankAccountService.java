package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.BankAccountDao;
import vn.edu.hcmuaf.fit.webdynamic.model.BankAccount;

import java.util.Optional;

public class BankAccountService {
    private final BankAccountDao bankAccountDao;

    public BankAccountService() {
        this.bankAccountDao = new BankAccountDao();
    }
    public Optional<BankAccount> getBankAccountByUserId(int userId) {
        return bankAccountDao.findOneByUserId(userId);
    }
    public boolean updateBankAccount(BankAccount bankAccount) {
        // Kiểm tra tài khoản có tồn tại không
        Optional<BankAccount> existing = bankAccountDao.findById(bankAccount.getId());
        if (existing.isEmpty()) {
            return false;
        }

        // Kiểm tra tài khoản thuộc về user này
        if (existing.get().getUserId() != bankAccount.getUserId()) {
            return false;
        }

        return bankAccountDao.update(bankAccount);
    }
}
