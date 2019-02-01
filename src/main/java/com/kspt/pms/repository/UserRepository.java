package com.kspt.pms.repository;

import com.kspt.pms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findUserById (Long id);
    Optional<User> findUserByLogin(String login);
    Optional<User> findUserByNameAndSurnameAndPatronymic(String name, String surname, String patronymic);
}
