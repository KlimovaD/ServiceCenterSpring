package com.kspt.pms.controller;

import com.kspt.pms.entity.Receipt;
import com.kspt.pms.entity.User;
import com.kspt.pms.enums.ReceiptStatus;
import com.kspt.pms.enums.RepairType;
import com.kspt.pms.enums.Role;
import com.kspt.pms.exception.AccessPermission;
import com.kspt.pms.exception.CreationFailed;
import com.kspt.pms.exception.ElementNotFound;
import com.kspt.pms.exception.IllegalWarranty;
import com.kspt.pms.repository.DeviceRepository;
import com.kspt.pms.repository.ReceiptRepository;
import com.kspt.pms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
public class ReceiptController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ReceiptRepository receiptRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @RequestMapping(value="/rest/receipt", method = RequestMethod.POST)
	public void addReceipt(@RequestParam("user") String login,
							@RequestBody Receipt receipt) {

		Optional<User> user = userRepository.findUserByLogin(login);
		if (!user.isPresent())
			throw new ElementNotFound("User", login);

		if (user.get().getRole() != Role.Receiver)
			throw new AccessPermission(user.get());

        receipt.setDate(new Date());
        receipt.setStatus(ReceiptStatus.Opened);

		if (receiptRepository.findById(receipt.getId()).isPresent())
			throw new CreationFailed(String.format("Receipt with id %s already exists", receipt.getId()));

		if(!deviceRepository.findBySerial(receipt.getDevice().getSerial()).isPresent())
			throw new CreationFailed(String.format("Device with serial %s doesn't exist", receipt.getDevice().getSerial()));

		receipt.isWarrantyValid();

		receiptRepository.save(receipt);
	}

    @RequestMapping("rest/receipt/{id}")
    public Receipt getReceipt(@PathVariable Long id) {
        return receiptRepository.findById(id)
                .orElseThrow(() -> new ElementNotFound("Receipt", id.toString()));
    }

    @RequestMapping(value = "rest/receipt/{id}/set", method = RequestMethod.PUT)
	public void setMaster(@PathVariable Long id, @RequestParam("master") String login){
    	User user = userRepository.findUserByLogin(login)
				.orElseThrow(()-> new ElementNotFound("User", login));

    	if(Role.Master != user.getRole())
    		throw new AccessPermission(user);

    	Receipt receipt = receiptRepository.findById(id)
				.orElseThrow(()->new ElementNotFound("Receipt", id.toString()));

    	receipt.setMaster(user);
    	receipt.setStatus(ReceiptStatus.Waiting_for_Diagnosis);

    	receiptRepository.save(receipt);
	}

	@RequestMapping(value = "rest/receipt/{id}", method = RequestMethod.PUT)
    public void updateStatus(@PathVariable Long id,
                             @RequestParam("user") String login, @RequestBody Receipt receipt){
        User user = userRepository.findUserByLogin(login)
				.orElseThrow(()-> new ElementNotFound("User", login));

    	if(Role.Master != user.getRole() && Role.Receiver != user.getRole())
    		throw new AccessPermission(user);

    	Receipt updatedReceipt = receiptRepository.findById(id)
				.orElseThrow(()->new ElementNotFound("Receipt", id.toString()));

    	updatedReceipt.setStatus(receipt.getStatus());
    	updatedReceipt.setRepair_type(receipt.getRepair_type());
    	updatedReceipt.setMalfunction(receipt.getMalfunction());
    	updatedReceipt.setNote(receipt.getNote());

    	receiptRepository.save(receipt);
    }
}
