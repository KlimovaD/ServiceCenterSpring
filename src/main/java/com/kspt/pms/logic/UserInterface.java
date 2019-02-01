package com.kspt.pms.logic;


import com.kspt.pms.entity.User;
import com.kspt.pms.repository.DeviceRepository;
import com.kspt.pms.repository.ReceiptRepository;
import com.kspt.pms.repository.UserRepository;

public interface UserInterface {
	User getUser();
	UserRepository getUserRepository();
	ReceiptRepository getReceiptRepository();
	DeviceRepository getDeviceRepository();
}
