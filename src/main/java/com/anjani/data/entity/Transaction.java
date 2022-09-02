package com.anjani.data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString//(exclude = {"bill"})
@Builder
public class Transaction {
    private Long id;

    @JsonBackReference
    private Bill bill;
    private String itemname;
    private Float quantity;
    private Float rate;
    private Float amount;
}