package com.kspt.pms.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="DEVICE")
public class Device {

    @Id
    @Column(name = "SERIAL")
    private Long serial;

    @Column(name="TYPE", nullable = false)
    private String type;

    @Column(name="BRAND", nullable = false)
    private String brand;

    @Column(name="MODEL", nullable = false)
    private String model;

    @Column(name="PURCHASE")
    private Date purchase;

    @Column(name="WARRANTYEXP")
    private Date warrantyexp;

    @Column(name="PREVIOUS")
    private Date previous;

    @Column(name="REPAIREXP")
    private Date repairexp;

    @ManyToOne
    private User client;

    public Long getSerial() {
        return serial;
    }

    public void setSerial(Long serial) {
        this.serial = serial;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getPurchase() {
        return purchase;
    }

    public void setPurchase(Date purchase) {
        this.purchase = purchase;
    }

    public Date getWarrantyexp() {
        return warrantyexp;
    }

    public void setWarrantyexp(Date warrantyexp) {
        this.warrantyexp = warrantyexp;
    }

    public Date getPrevious() { return previous; }

    public void setPrevious(Date previous) { this.previous = previous; }

    public Date getRepairexp() {
        return repairexp;
    }

    public void setRepairexp(Date repairexp) {
        this.repairexp = repairexp;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

}
