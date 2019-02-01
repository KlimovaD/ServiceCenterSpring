package com.kspt.pms.controller;

import com.kspt.pms.entity.Invoice;
import com.kspt.pms.entity.Receipt;
import com.kspt.pms.entity.User;
import com.kspt.pms.enums.InvoiceStatus;
import com.kspt.pms.enums.ReceiptStatus;
import com.kspt.pms.enums.Role;
import com.kspt.pms.exception.AccessPermission;
import com.kspt.pms.exception.CreationFailed;
import com.kspt.pms.exception.ElementNotFound;
import com.kspt.pms.repository.InvoiceRepository;
import com.kspt.pms.repository.ReceiptRepository;
import com.kspt.pms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
public class InvoiceController {
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReceiptRepository receiptRepository;

    @RequestMapping(value="rest/invoice", method = RequestMethod.POST)
    public void addInvoice(@PathVariable Long id, @RequestParam("user") String login,
                           @RequestBody Invoice invoice){
        Optional<User> user = userRepository.findUserByLogin(login);
        if(!user.isPresent())
            throw new ElementNotFound("User", login);

        if(user.get().getRole() != Role.Receiver)
            throw new AccessPermission(user.get());

        if(invoiceRepository.findById(id).isPresent())
            throw new CreationFailed(String.format("Invoice with id %s already exists", id));

        Receipt receipt = receiptRepository.findById(invoice.getReceipt().getId())
                .orElseThrow(()-> new ElementNotFound("Receipt", invoice.getReceipt().getId().toString()));

        if(ReceiptStatus.Ready_for_extr != receipt.getStatus())
            throw new CreationFailed(String
                    .format("Cannot create invoice for %s receipt. Device is not ready for extraction",
                            receipt.getId().toString()));

        invoice.setDate(new Date());

        invoiceRepository.save(invoice);
    }

    @RequestMapping(value="rest/invoice/{id}")
    public Invoice getInvoice(@PathVariable Long id){
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ElementNotFound("Invoice", id.toString()));
    }

    @RequestMapping("rest/invoice/receipt/{id}")
    public Invoice getInvoiceForReceipt(@PathVariable Long id){
        return invoiceRepository.findByReceiptId(id)
                .orElseThrow(() -> new ElementNotFound("Invoice by Receipt id", id.toString()));
    }

    @RequestMapping(value = "rest/invoice/{id}",method = RequestMethod.PUT)
    public void setInvoiceStatus(@PathVariable Long id, @RequestParam("user") String login,
                                 @RequestBody InvoiceStatus status){
        User user = userRepository.findUserByLogin(login)
                .orElseThrow(() -> new ElementNotFound("User", login));

        if(Role.Receiver != user.getRole())
            throw new AccessPermission(user);

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ElementNotFound("Invoice", id.toString()));

        Receipt receipt = receiptRepository.findById(invoice.getReceipt().getId())
                .orElseThrow(() -> new ElementNotFound("Receipt", invoice.getReceipt().getId().toString()));

        invoice.setStatus(status);
        receipt.setStatus(ReceiptStatus.Closed);

        invoiceRepository.save(invoice);
        receiptRepository.save(receipt);
    }
}
