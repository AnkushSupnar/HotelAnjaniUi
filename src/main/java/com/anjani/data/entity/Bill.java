package com.anjani.data.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder

public class Bill {
    private Long id;
    private LocalDate date;
    private Float netamount;
    private Float discount;
    private Float grandtotal;
    private Float paid;
    private Bank bank;
    private Employee waitor;
    private Employee login;
    private TableMaster table;
    @JsonManagedReference
    private List<Transaction> transactions = new ArrayList<>();
    private Customer customer;
    private String status;
}