package com.kspt.pms.repository;

import com.kspt.pms.entity.Device;
import com.kspt.pms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>{
    Optional<Device> findBySerial (Long serial);
	Collection<Device> findByClient (User user);
}
