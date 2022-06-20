package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao transferDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @PostMapping(path = "/transfer")
    public boolean createTransfer(@RequestBody Transfer transfer) {
        return transferDao.createTransfer(transfer);
    }

    @GetMapping(path = "/transfers/{userId}")
    public List<Transfer> getTransfersByUserId(@PathVariable int userId) {
        return transferDao.getTransfersByUserId(userId);
    }

    @GetMapping(path = "/transfer/{transferId}")
    public Transfer getTransferByTransferId(@RequestBody Transfer transfer, @PathVariable int transferId) {
        return transferDao.getTransferByTransferId(transferId);
    }

    @GetMapping(path = "/transfers/pending/{userId}")
    public List<Transfer> getPendingTransfers(@PathVariable int userId) {
        return transferDao.getPendingTransfers(userId);
    }

    @GetMapping(path = "/transfers/pending/currentuser/{userId}")
    public List<Transfer> getUsersPendingTransfers(@PathVariable int userId){
        return transferDao.getUsersPendingTransfers(userId);
    }

    @PutMapping(path = "/transfer")
    public boolean updateTransfer(@RequestBody Transfer transfer) {
        return transferDao.updateTransfer(transfer);
    }
}
