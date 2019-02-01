package com.kspt.pms.entity;

import com.kspt.pms.enums.ReceiptStatus;
import com.kspt.pms.enums.RepairType;
import com.kspt.pms.exception.IllegalWarranty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="RECEIPT")
public class Receipt {

    @Id
    @Column(name="ID")
    @GeneratedValue
    private Long id;

    @Column(name="RECEIPTDATE", nullable = false)
    private Date date;

    @ManyToOne
    // @Column(name="DEVICE", nullable = false)
    private Device device;

    @ManyToOne
    // @Column(name="CLIENT", nullable = false)
    private User client;

    @ManyToOne
    // @Column(name="RECEIVER", nullable = false)
    private User receiver;

    @Column(name="MALFUNCTION", nullable = false)
    private String malfunction;

    @Column(name="NOTE")
    private String note;

    @ManyToOne
    // @Column(name="MASTER", nullable = false)
    private User master;

    @Column(name="REPAIRTYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private RepairType repair_type;

    @Column(name="STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReceiptStatus status;

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

    public String getMalfunction() {
        return malfunction;
    }

    public void setMalfunction(String malfunction) {
        this.malfunction = malfunction;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public RepairType getRepair_type() {
        return repair_type;
    }

    public void setRepair_type(RepairType repair_type) {
        this.repair_type = repair_type;
    }

    public ReceiptStatus getStatus() {
        return status;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
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

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public void setStatus(ReceiptStatus status) {
        this.status = status;
    }

    public boolean isWarrantyValid() throws IllegalWarranty {
        if(repair_type.equals(RepairType.Warranty)) {
            if((device.getWarrantyexp() == null && device.getPurchase() == null) &&
                    (device.getRepairexp() == null && device.getPrevious() == null)) {
                throw new IllegalWarranty("Couldn't set warranty repair without " +
                        "warranty expiration date");
            }
            if(device.getWarrantyexp() != null &&
                    device.getWarrantyexp().before(new Date()) ||
                    device.getRepairexp() != null &&
                            device.getRepairexp().before(new Date())) {
                throw new IllegalWarranty("Couldn't set warranty repair without " +
                        "warranty expiration date");
            }
        }
        return true;
    }
}
