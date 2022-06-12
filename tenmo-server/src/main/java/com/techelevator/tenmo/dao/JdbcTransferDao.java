package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, ?);";
        return  jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount()) == 1;
    }

    @Override
    public List<Transfer> getTransfersByUserId(int userId) {
        String sql = "SELECT transfer_id, username,  amount " ;

        return null;
    }

    @Override
    public Transfer getTransfersByTransferId(int transferId) {
        Transfer transfer = null;

        String sql = "SELECT transfer_id, account_from, account_to, transfer_type_desc, transfer_status_desc, amount " +
                "FROM transfer " +
                "NATURAL JOIN transfer_type " +
                "NATURAL JOIN transfer_status " +
                "WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {

        }

        return null;
    }

    @Override
    public List<Transfer> getAllTransfers() {
        return null;
    }

    @Override
    public List<Transfer> getPendingTransfers(int userId) {
        return null;
    }

    @Override
    public void updateTransfer(Transfer transfer) {

    }

    private Transfer mapRowToTransfer(Transfer transfer) {
        return null;
    }
}
