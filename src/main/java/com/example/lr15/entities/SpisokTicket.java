package com.example.lr15.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "spisokticket")
public class SpisokTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "passenger")
    private String passenger;

    @Column(name = "route")
    private String route;

    @Column(name = "price")
    private Integer price;
}
