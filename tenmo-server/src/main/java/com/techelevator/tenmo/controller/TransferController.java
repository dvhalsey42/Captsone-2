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
    public List<Transfer> getTransferByUserId(@RequestBody Transfer transfer, @PathVariable int userId) {
        return transferDao.getTransfersByUserId(userId);
    }

    @GetMapping(path = "/transfer/{transferId}")
    public Transfer getTransferByTransferId(@RequestBody Transfer transfer, @PathVariable int transferId) {
        return transferDao.getTransferByTransferId(transferId);
    }
}
