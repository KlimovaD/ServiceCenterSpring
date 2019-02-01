package com.kspt.pms.logic;

import com.kspt.pms.entity.Receipt;
import com.kspt.pms.entity.User;
import com.kspt.pms.enums.ReceiptStatus;
import com.kspt.pms.exception.AccessPermission;
import com.kspt.pms.repository.DeviceRepository;
import com.kspt.pms.repository.ReceiptRepository;
import com.kspt.pms.repository.UserRepository;

public class Master implements UserInterface {

    private User user;
    ReceiptRepository receiptRepository;
    DeviceRepository deviceRepository;
    UserRepository userRepository;

    public Receipt assignOnRepair(Receipt receipt){
        receipt.setMaster(getUser());
        return receipt;
    }

    public Receipt setRecStatus(String status, Receipt receipt) throws AccessPermission {
        if(!status.equals(ReceiptStatus.Diagnostics.toString()) &&
                !status.equals(ReceiptStatus.Under_Repair.toString()) &&
                !status.equals(ReceiptStatus.Ready_for_extr .toString()))
            throw new AccessPermission(getUser());

        receipt.setStatus(ReceiptStatus.valueOf(status));
        return receipt;
    }

    @Override
    public User getUser() { return user; }

    @Override
    public ReceiptRepository getReceiptRepository() { return receiptRepository; }

    @Override
    public DeviceRepository getDeviceRepository() { return deviceRepository; }

    @Override
    public UserRepository getUserRepository() { return userRepository; }
}
