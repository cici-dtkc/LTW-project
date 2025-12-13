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

    public Optional<BankAccount> findByUserId(int userId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM bank_accounts WHERE user_id = :userId LIMIT 1")
                        .bind("userId", userId)
                        .mapToBean(BankAccount.class)
                        .findOne()
        );
    }

    public boolean insert(BankAccount b) {
        int rows = jdbi.withHandle(handle ->
                handle.createUpdate("""
                    INSERT INTO bank_accounts (user_id, bank_name, account_number, account_name, linked_date, status, updated_at)
                    VALUES (:userId, :bankName, :accountNumber, :accountName, NOW(), 1, NOW())
                """)
                        .bind("userId", b.getUserId())
                        .bind("bankName", b.getBankName())
                        .bind("accountNumber", b.getAccountNumber())
                        .bind("accountName", b.getAccountName())
                        .execute()
        );
        return rows > 0;
    }

    public boolean update(BankAccount b) {
        return jdbi.withHandle(handle -> {
            System.out.println("=== RUNNING UPDATE FOR userId = " + b.getUserId() + " ===");
            System.out.println("New bankName = " + b.getBankName());
            System.out.println("New accountNumber = " + b.getAccountNumber());
            System.out.println("New accountName = " + b.getAccountName());

            int rows = handle.createUpdate("""
            UPDATE bank_accounts
            SET bank_name = :bankName,
                account_number = :accountNumber,
                account_name = :accountName,
                updated_at = NOW()
            WHERE user_id = :userId
        """)
                    .bind("bankName", b.getBankName())
                    .bind("accountNumber", b.getAccountNumber())
                    .bind("accountName", b.getAccountName())
                    .bind("userId", b.getUserId())
                    .execute();

            System.out.println("Rows updated = " + rows);

            return rows > 0;
        });
    }

}
