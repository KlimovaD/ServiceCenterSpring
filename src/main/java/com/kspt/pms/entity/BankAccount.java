package com.kspt.pms.entity;

import com.kspt.pms.enums.InvoiceStatus;
import com.kspt.pms.exception.InsufficientFunds;
import com.kspt.pms.exception.InvalidPaymentData;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ACCOUNT")
public class BankAccount {

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    private User client;

    @Column(name = "VALID", nullable = false)
    private Date valid;

    @Column(name = "CVC", nullable = false)
    private int cvc;

    @Column(name = "BALANCE", nullable = false)
    private double balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public void setValid(Date valid) {
        this.valid = valid;
    }

    public int getCvc() {
        return cvc;
    }

    public void setCvc(int cvc) {
        this.cvc = cvc;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean Eq(BankAccount account) throws InvalidPaymentData {
        if (account.getId().equals(this.id) &&
                account.getClient().equals(this.client) &&
                account.valid.equals(this.valid) &&
                account.cvc == this.cvc)
            return true;
        throw new InvalidPaymentData("Invalid payment data");
    }

    public boolean payForRepair(Invoice invoice) throws InsufficientFunds, InvalidPaymentData{
        if(invoice.getStatus().equals(InvoiceStatus.Paid))
            throw new InvalidPaymentData("Attempt to pay for warranty repair");
        if(invoice.getPrice() > balance)
            throw new InsufficientFunds("Insufficient funds");

        balance -= invoice.getPrice();
        return true;
    }

    public boolean isValid() throws InvalidPaymentData {
        if(valid.before(new Date()))
            throw new InvalidPaymentData("Bank account expired");

        return true;
    }

}
