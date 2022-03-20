package com.anjani.data.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Kirana {
    private Long id;
   // @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate date;
    private PurchaseParty party;
    private Float nettotal;
    private Float transaport;
    private Float other;
    private Float discount;
    private Float grandtotal;
    private Float paid;
    @JsonManagedReference
    private List<KiranaTransaction> kiranaTransactions = new ArrayList<>();
    private Bank bank;
/*
    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public PurchaseParty getParty() {
        return party;
    }

    public Float getNettotal() {
        return nettotal;
    }


    public Float getTransaport() {
        return transaport;
    }

    public Float getOther() {
        return other;
    }

    public Float getDiscount() {
        return discount;
    }

    public Float getGrandtotal() {
        return grandtotal;
    }

    public Float getPaid() {
        return paid;
    }

    @JsonManagedReference
    public List<KiranaTransaction> getKiranaTransactions() {
        return kiranaTransactions;
    }

    public Bank getBank() {
        return bank;
    }*/
}