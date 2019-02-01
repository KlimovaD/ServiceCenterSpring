package com.kspt.pms.entity;

import com.kspt.pms.enums.InvoiceStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="INVOICE")
public class Invoice {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATE", nullable = false)
    private Date date;

    @OneToOne
    private Receipt receipt;

    @Column(name = "PRICE", nullable = false)
    private double price;

    @ManyToOne
    private User client;

    @ManyToOne
    private User receiver;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }
}

