package com.kspt.pms.entity;

import com.kspt.pms.enums.Role;

import javax.persistence.*;

@Entity
@Table(name="USERS")
public class User {

    @Id
    @Column(name="ID")
    @GeneratedValue
    private Long id;

    @Column(name="NAME", nullable = false)
    private String name;

    @Column(name="SURNAME", nullable = false)
    private String surname;

    @Column(name="PATRONYMIC")
    private String patronymic;

    @Column(name="PHONENUMBER", nullable = false)
    private String phone;

    @Column(name="EMAIL", nullable = false)
    private String email;

    @Column(name="LOGIN", unique = true, nullable = false)
    private String login;

    @Column(name="PASSWORD", nullable = false)
    private String password;

    @Column(name="ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
