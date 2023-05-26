package com.guio.guio.entity;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name="telegramUser")
public class TelegramUser {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(name="firstName")
    private   String firstName;

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    @Column(name="secondName")
    private   String secondName;
    @Column(name="userName",unique = true)
    private   String userName;
    @Column(name="emailAdress",unique = true)
    private   String emailAdress;
    @Column(name="telegramToken",unique = true)
    private final  String telegramToken;

    @OneToMany(mappedBy = "asignee")
    private List<Task> my_task;

    public TelegramUser(String firstName, String secondName, String userName,String emailAdress, String telegramToken) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.emailAdress = emailAdress;
        this.telegramToken = telegramToken;
        this.userName = userName;
    }



}
