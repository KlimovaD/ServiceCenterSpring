package com.kspt.pms.controller;

import com.kspt.pms.entity.Device;
import com.kspt.pms.entity.Invoice;
import com.kspt.pms.entity.Receipt;
import com.kspt.pms.entity.User;
import com.kspt.pms.enums.InvoiceStatus;
import com.kspt.pms.enums.ReceiptStatus;
import com.kspt.pms.enums.RepairType;
import com.kspt.pms.enums.Role;
import com.kspt.pms.exception.*;
import com.kspt.pms.repository.DeviceRepository;
import com.kspt.pms.repository.InvoiceRepository;
import com.kspt.pms.repository.ReceiptRepository;
import com.kspt.pms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    ReceiptRepository receiptRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @RequestMapping("rest/user/{login}/authenticate")
    public String authenticate(@PathVariable String login,
                               @RequestParam("passwd") String passwd) {
        User user = userRepository.findUserByLogin(login)
                .orElseThrow(() -> new InvalidSignIn(login));
        if (!user.getPassword().equals(passwd))
            throw new InvalidSignIn(login);

        return "true";
    }

    @RequestMapping("rest/user/{login}")
    public User getUser(@PathVariable String login) {
        return userRepository.findUserByLogin(login)
                .orElseThrow(() -> new InvalidUser(String.format("User %s doesn't exist", login)));
    }

    @RequestMapping(value = "rest/user/{login}", method = RequestMethod.POST)
    public void addUser(@PathVariable String login, @RequestBody User user) {
        if( userRepository.findUserByLogin(login).isPresent())
                throw new CreationFailed(String.format("User with login %s already exists", login));

        userRepository.save(user);
    }

    @RequestMapping("/rest/user/{login}/receipts")
    public Collection<Receipt> getReceipts(@PathVariable String login, @RequestParam String status){

        User user = userRepository.findUserByLogin(login)
                .orElseThrow(() -> new ElementNotFound("User", login));

        Collection<Receipt> receipts;
        switch (user.getRole()){
            case Receiver:
                return filterReceipts(receiptRepository.findByReceiver(user), status);
            case Master:
                receipts = receiptRepository.findByMaster(user);
                receipts.addAll(receiptRepository.findByMaster(null));
                return filterReceipts(receipts, status);
            case Client:
                return filterReceipts(receiptRepository.findByClient(user), status);
            default:
                throw new InvalidUser("Cannot get user role");
        }
    }

    private Collection<Receipt> filterReceipts(Collection<Receipt> receipts, String status){
        switch (status){
            case "all":
                return receipts;
            case "opened":
                return receipts.stream()
                        .filter(r -> ReceiptStatus.Opened == r.getStatus())
                        .collect(Collectors.toList());
            case "in_progress":
                return receipts.stream()
                        .filter(r -> ReceiptStatus.Opened != r.getStatus() &&
                                ReceiptStatus.Closed != r.getStatus())
                        .collect(Collectors.toList());
            case "closed":
                return receipts.stream()
                        .filter(r -> ReceiptStatus.Closed == r.getStatus())
                        .collect(Collectors.toList());
            default:
                throw new InvalidArgument("status", "filterReceipts", status);

        }
    }

    @RequestMapping("/rest/user/{login}/invoices")
    public Collection<Invoice> getInvoices(@PathVariable String login, @RequestParam String status) {

        User user = userRepository.findUserByLogin(login)
                .orElseThrow(() -> new ElementNotFound("User", login));

        if(user.getRole() != Role.Receiver || user.getRole() != Role.Client)
            throw new AccessPermission(user);

        switch(status){
            case "waiting":
                return filterInvoices(invoiceRepository.findByClient(user), status);
            case "paid":
                return filterInvoices(invoiceRepository.findByReceiver(user), status);
            default:
                throw new InvalidArgument("status", "getInvoices", status);
        }
    }

    private Collection<Invoice> filterInvoices(Collection<Invoice> invoices, String status){
        switch (status){
            case "waiting":
                return invoices.stream()
                        .filter(i -> InvoiceStatus.Waiting_For_Payment == i.getStatus())
                        .collect(Collectors.toList());
            case "paid":
                return invoices.stream()
                        .filter(i -> InvoiceStatus.Paid == i.getStatus())
                        .collect(Collectors.toList());
            default:
                throw new InvalidArgument("status", "filterInvoices", status);
        }
    }

    @RequestMapping("/rest/user/{login}/devices")
    public Collection<Device> getUserDevices(@PathVariable String login) {

        User user = userRepository.findUserByLogin(login)
                .orElseThrow(() -> new ElementNotFound("User", login));

        if(user.getRole() == Role.Master){
            Collection<Device> masterDevices = new ArrayList<>();
            Collection<Receipt> receipts = receiptRepository.findByMaster(user);
            for (Receipt receipt : receipts)
                masterDevices.add(receipt.getDevice());

            return masterDevices;
        } else if (Role.Client == user.getRole())
            return deviceRepository.findByClient(user);
        else
            return deviceRepository.findAll();
    }

    @RequestMapping("/rest/enum/roles")
    public Collection<String> getRoles(){
        Collection<String> list = new ArrayList<>();

        for (Role role : Role.values())
            list.add(role.toString());

        return list;
    }

    @RequestMapping("rest/enum/invoicestatus")
    public Collection<String> getInvoiceStatus(){
        Collection<String> list = new ArrayList<>();

        for (InvoiceStatus status : InvoiceStatus.values())
            list.add(status.toString());

        return list;
    }

    @RequestMapping("rest/enum/receiptstatus")
    public Collection<String> getReceiptStatus(){
        Collection<String> list = new ArrayList<>();

        for (ReceiptStatus status : ReceiptStatus.values())
            list.add(status.toString());

        return list;
    }

    @RequestMapping("rest/enum/type")
    public Collection<String> getRepairType(){
        Collection<String> list = new ArrayList<>();

        for (RepairType status : RepairType.values())
            list.add(status.toString());

        return list;
    }
}
