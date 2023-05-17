package com.guio.guio;
import jakarta.persistence.*;
import java.util.List;
@Entity
@Table(name="telegramUser")
public class TelegramUser {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private final Long id;
    @Column(name="firstName")
    private   String firstName;
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

    public TelegramUser(Long id, String firstName, String secondName, String emailAdress, String telegramToken, String userName) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.emailAdress = emailAdress;
        this.telegramToken = telegramToken;
        this.userName = userName;
    }
}
