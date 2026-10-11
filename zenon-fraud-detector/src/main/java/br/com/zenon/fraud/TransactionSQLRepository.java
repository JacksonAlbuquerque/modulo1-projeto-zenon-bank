package br.com.zenon.fraud;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository {
    Connection conn;

    public TransactionSQLRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Optional<Transaction> findByOriginName(String customerName) {
        String sql2 = "SELECT * FROM TRANSACTIONS WHERE name_orig = ? ";
        try (PreparedStatement ps = conn.prepareStatement(sql2)) {
            ps.setString(1, customerName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TransactionCustomer customerOrig = new TransactionCustomer(rs.getString("name_orig"),
                        rs.getBigDecimal("old_balance_orig"),
                        rs.getBigDecimal("new_balance_orig"));
                TransactionCustomer customerDest = new TransactionCustomer(rs.getString("name_dest"),
                        rs.getBigDecimal("old_balance_dest"),
                        rs.getBigDecimal("new_balance_dest"));
                Transaction transaction = new Transaction(rs.getInt("step"),
                        TransactionType.valueOf(rs.getString("type")),
                        rs.getBigDecimal("amount"),
                        customerOrig,
                        customerDest,
                        rs.getBoolean("is_fraud"),
                        rs.getBoolean("is_flagged_fraud")
                );
                return Optional.of(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Transaction transaction) {
        String sql = "INSERT INTO TRANSACTIONS (step, type, amount, name_orig, old_balance_orig, new_balance_orig, name_dest, old_balance_dest, new_balance_dest, is_fraud, is_flagged_fraud) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, transaction.step());
            ps.setString(2, transaction.type().name());
            ps.setBigDecimal(3, transaction.amount());
            ps.setString(4, transaction.customerOrig().name());
            ps.setBigDecimal(5, transaction.customerOrig().oldBalance());
            ps.setBigDecimal(6, transaction.customerOrig().newBalance());
            ps.setString(7, transaction.customerDest().name());
            ps.setBigDecimal(8, transaction.customerDest().oldBalance());
            ps.setBigDecimal(9, transaction.customerDest().newBalance());
            ps.setBoolean(10, transaction.isFraud());
            ps.setBoolean(11, transaction.isFlaggedFraud());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAll(List<Transaction> transactions) {

        String sql = "INSERT INTO TRANSACTIONS (step, type, amount, name_orig, old_balance_orig, new_balance_orig, name_dest, old_balance_dest, new_balance_dest, is_fraud, is_flagged_fraud) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {//para ficar com  auto commit comentar essa linha
            conn.setAutoCommit(false); // sem auto Commit, para ficar com  auto commit comentar essa linha
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Transaction transaction : transactions) {
                    ps.setInt(1, transaction.step());
                    ps.setString(2, transaction.type().name());
                    ps.setBigDecimal(3, transaction.amount());
                    ps.setString(4, transaction.customerOrig().name());
                    ps.setBigDecimal(5, transaction.customerOrig().oldBalance());
                    ps.setBigDecimal(6, transaction.customerOrig().newBalance());
                    ps.setString(7, transaction.customerDest().name());
                    ps.setBigDecimal(8, transaction.customerDest().oldBalance());
                    ps.setBigDecimal(9, transaction.customerDest().newBalance());
                    ps.setBoolean(10, transaction.isFraud());
                    ps.setBoolean(11, transaction.isFlaggedFraud());
                    ps.addBatch(); // usando batch
                }
                ps.executeBatch(); // usando batch
                conn.commit();// para ficar com  auto commit comentar essa linha
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                conn.setAutoCommit(true);           // (d) devolve a conexão como recebeu // para ficar com  auto commit comentar essa linha
            }
        } catch (SQLException e) { // para ficar com  auto commit comentar essa linha
            throw new RuntimeException(e);// para ficar com  auto commit comentar essa linha
        }// para ficar com  auto commit comentar essa linha

    }


}
