package vn.edu.hcmuaf.fit.webdynamic.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;
import vn.edu.hcmuaf.fit.webdynamic.model.BankAccount;

import java.util.Optional;

public class BankAccountDao {
    private final Jdbi jdbi;
    public BankAccountDao() {
        this.jdbi = DBConnect.getJdbi();
    }
    public Optional<BankAccount> findById(int id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, user_id, bank_name, account_number, account_name FROM bank_accounts WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(BankAccount.class)
                        .findOne()
        );
    }
    public Optional<BankAccount> findOneByUserId(int userId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, user_id, bank_name, account_number, account_name  FROM bank_accounts WHERE user_id = :userId LIMIT 1")
                        .bind("userId", userId)
                        .mapToBean(BankAccount.class)
                        .findOne()
        );
    }
    public boolean update(BankAccount bankAccount) {
        return jdbi.withHandle(handle -> {
            int rowsAffected = handle.createUpdate("UPDATE bank_accounts SET bank_name = :bankName, account_number = :accountNumber, owner_name = :ownerName WHERE id = :id")
                    .bind("id", bankAccount.getId())
                    .bind("bankName", bankAccount.getBankName())
                    .bind("accountNumber", bankAccount.getBankAccountNumber())
                    .bind("ownerName", bankAccount.getAccountName())
                    .execute();

            return rowsAffected > 0;
        });
    }
}
