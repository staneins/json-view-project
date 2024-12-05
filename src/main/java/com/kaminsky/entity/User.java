package com.kaminsky.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.kaminsky.views.Views;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.UserSummary.class)
    private Long id;

    @JsonView(Views.UserSummary.class)
    private String name;

    @JsonView(Views.UserSummary.class)
    @Email(message = "Неверный email")
    private String email;

    @OneToMany(mappedBy = "owner")
    @JsonView(Views.UserDetails.class)
    private List<Order> orders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }



}
