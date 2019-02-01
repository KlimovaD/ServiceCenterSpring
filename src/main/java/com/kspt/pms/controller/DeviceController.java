package com.kspt.pms.controller;

import com.kspt.pms.entity.Device;
import com.kspt.pms.entity.User;
import com.kspt.pms.enums.Role;
import com.kspt.pms.exception.*;
import com.kspt.pms.repository.DeviceRepository;
import com.kspt.pms.repository.ReceiptRepository;
import com.kspt.pms.repository.UserRepository;
//import org.omg.CORBA.DynAnyPackage.Invalid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class DeviceController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    ReceiptRepository receiptRepository;

    @RequestMapping(value = "/rest/device/{serial}", method = RequestMethod.POST)
    public void addDevice(@PathVariable Long serial, @RequestParam("user") String login,
                          @RequestBody Device device) {

        Optional<User> user = userRepository.findUserByLogin(login);
        if(!user.isPresent())
            throw new ElementNotFound("User", login);

        if(user.get().getRole() != Role.Receiver)
            throw new AccessPermission(user.get());

        if (deviceRepository.findBySerial(serial).isPresent())
            throw new CreationFailed(String.format("Device with serial %s already exists", serial));

        if(!userRepository.findUserById(device.getClient().getId()).isPresent())
            throw new InvalidUser("User with id" + device.getClient().getId() + "couldn't be found");

        if(Role.Client != userRepository.findUserById(device.getClient().getId()).get().getRole())
            throw new InvalidUser("Client with id" + device.getClient().getId() + "couldn't be found");

        if(device.getPurchase() != null && device.getWarrantyexp() != null)
            if(device.getPurchase().after(device.getWarrantyexp()))
                throw new IllegalWarranty("Date of purchase can't be after date of warranty expiration");

        if(device.getPurchase() == null && device.getWarrantyexp() != null)
            throw new IllegalWarranty("Please, specify date of purchase");

        if(device.getPrevious() != null && device.getRepairexp() != null)
            if(device.getPrevious().after(device.getRepairexp()))
                throw new IllegalWarranty("Date of previous repair couldn't be after day of repair warranty expiration");

        if(device.getPrevious() == null && device.getRepairexp() != null)
            throw new IllegalWarranty("Please, specify date of previous repair");

        deviceRepository.save(device);
    }

    @RequestMapping("/rest/device/{serial}")
    public Device getDevice(@PathVariable Long serial) {

        return deviceRepository.findBySerial(serial)
                .orElseThrow(() -> new ElementNotFound("Device", serial.toString()));
    }

    @RequestMapping(value = "/rest/device/{serial}", method = RequestMethod.PUT)
    public void updateDevice(@PathVariable Long serial,
                             @RequestParam ("user") String login, @RequestBody Device device) {

        Optional<User> user = userRepository.findUserByLogin(login);
        if (!user.isPresent())
            throw new ElementNotFound("User", login);

        if(user.get().getRole() != Role.Receiver)
            throw new AccessPermission(user.get());

        Device dev = deviceRepository.findBySerial(serial)
                .orElseThrow(() -> new ElementNotFound("Device", serial.toString()));

        dev.setPrevious(device.getPrevious());
        dev.setWarrantyexp(device.getPrevious());
        dev.setPurchase(device.getPurchase());
        dev.setRepairexp(device.getRepairexp());

        deviceRepository.save(dev);
    }
}
