package com.kspt.pms.logic;

import com.kspt.pms.entity.Device;
import com.kspt.pms.entity.Invoice;
import com.kspt.pms.entity.Receipt;
import com.kspt.pms.entity.User;
import com.kspt.pms.enums.ReceiptStatus;
import com.kspt.pms.enums.RepairType;
import com.kspt.pms.enums.Role;
import com.kspt.pms.exception.AccessPermission;
import com.kspt.pms.exception.CreationFailed;
import com.kspt.pms.exception.IllegalWarranty;
import com.kspt.pms.repository.DeviceRepository;
import com.kspt.pms.repository.ReceiptRepository;
import com.kspt.pms.repository.UserRepository;

import java.util.Date;

public class Receiver implements UserInterface {

    private User user;
    ReceiptRepository receiptRepository;
    DeviceRepository deviceRepository;
    UserRepository userRepository;

    public Receipt createReceipt(Long id, Date date, RepairType type,
                                 Device device, String malfunction, String note,
                                 ReceiptStatus status) throws IllegalWarranty {
        Receipt receipt = new Receipt();
        receipt.setId(id);
        receipt.setDate(date);
        receipt.setRepair_type(type);
        receipt.setDevice(device);
        receipt.setMalfunction(malfunction);
        receipt.setNote(note);
        receipt.setStatus(status);

        receipt.isWarrantyValid();
        getReceiptRepository().save(receipt);
        return receipt;
    }

    public Device createDevice(Long serial, String type, String brand,
                               String model, User owner, Date purchase,
                               Date warrantyExp, Date prevRepair,
                               Date repWarrantyExp) throws IllegalWarranty {
        Device device = new Device();
        device.setSerial(serial);
        device.setType(type);
        device.setBrand(brand);
        device.setModel(model);
        device.setClient(owner);
        device.setPurchase(purchase);
        device.setWarrantyexp(warrantyExp);
        device.setPrevious(prevRepair);
        device.setRepairexp(repWarrantyExp);

        getDeviceRepository().save(device);
        return device;
    }

    public Receipt setRecStatus(String status, Receipt receipt) throws AccessPermission {
        if(!status.equals(ReceiptStatus.Opened.toString()) &&
                !status.equals(ReceiptStatus.Waiting_for_Diagnosis.toString()) &&
                !status.equals(ReceiptStatus.Closed.toString()))
            throw new AccessPermission(getUser());

        receipt.setStatus(ReceiptStatus.valueOf(status));
        return receipt;
    }

    public User addUser(String name, String surname, String patronymic,
                        String login, String phone, String email,
                        String pwd, Role role) throws CreationFailed {

        if(getUserRepository().findUserByLogin(login).isPresent())
            throw new CreationFailed(String.format("User with login %s already exists", login));

        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setPatronymic(patronymic);
        user.setLogin(login);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(pwd);
        user.setRole(role);

        getUserRepository().save(user);
        return user;
    }

    public Invoice createInvoice(Date currentDate, Receipt receipt, String price)
            throws CreationFailed{

        double priceValue = Double.parseDouble(price);
        if(priceValue == 0 && !RepairType.Warranty.equals(receipt.getRepair_type()))
            throw new CreationFailed("Repair can not be free if it is not warranty");
        else if(RepairType.Warranty.equals(receipt.getRepair_type()) && priceValue != 0)
            throw new CreationFailed("Repair can not be with price if it is warranty");

        Invoice invoice = new Invoice();
        invoice.setId(receipt.getId());
        invoice.setDate(currentDate);
        invoice.setClient(receipt.getClient());
        invoice.setReceipt(receipt);
        invoice.setPrice(priceValue);
        return invoice;
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
