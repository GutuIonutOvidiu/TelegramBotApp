package com.guio.guio.entity;


import jakarta.persistence.*;

@Entity
@Table(name="task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final long id;

    @Column(name = "description")
    private final String description;

    @Column(name = "taskDone")
    private boolean taskDone = false;

    @ManyToOne
    @JoinColumn(name = "list_id")
    private TodoList todoList;

    @ManyToOne
    @JoinColumn(name = "asignee_id")
    private TelegramUser asignee;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private TelegramUser reporter;

    public Task(long id, String description, boolean taskDone) {
        this.id = id;
        this.description = description;
        this.taskDone = taskDone;
    }
}
