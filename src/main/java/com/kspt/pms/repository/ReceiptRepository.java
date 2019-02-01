package com.kspt.pms.repository;

import com.kspt.pms.entity.Receipt;
import com.kspt.pms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, String>{
    Optional<Receipt> findById (Long id);
    Collection<Receipt> findByClient(User client);
    Collection<Receipt> findByReceiver (User receiver);
    Collection<Receipt> findByMaster (User master);
}
