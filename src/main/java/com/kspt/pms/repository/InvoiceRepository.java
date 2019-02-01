package com.kspt.pms.repository;

import com.kspt.pms.entity.Invoice;
import com.kspt.pms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String>{
    Optional<Invoice> findById (Long id);
    Collection<Invoice> findByClient (User user);
    Collection<Invoice> findByReceiver (User user);
    Optional<Invoice> findByReceiptId(Long receiptId);

}
