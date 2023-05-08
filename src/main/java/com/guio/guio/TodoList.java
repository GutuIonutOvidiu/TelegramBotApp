package com.guio.guio;
import jakarta.persistence.*;
import java.util.List;
@Entity
@Table(name="toDoList")
public class TodoList {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private final Long id;
    @OneToMany(mappedBy = "todoList")
    private List<Task> tasks;
    @ManyToOne
    @JoinColumn(name="user_id")
    private TelegramUser creator;

    public TodoList(Long id, List<Task> tasks, TelegramUser creator) {
        this.id = id;
        this.tasks = tasks;
        this.creator = creator;
    }
}
