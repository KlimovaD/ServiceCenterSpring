package com.kspt.pms.repository;

import com.kspt.pms.entity.BankAccount;
import com.kspt.pms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<BankAccount, Long>{
    Optional<BankAccount> findById (Long id);
    Collection<BankAccount> findByClient (User client);
}
