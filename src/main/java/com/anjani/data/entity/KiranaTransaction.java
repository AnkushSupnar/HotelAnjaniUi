package com.anjani.data.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class KiranaTransaction {
    private Long id;
   // @ToString.Exclude
   @JsonBackReference
    private Kirana kirana;
    private String itemname;
    private String unit;
    private Float quantity;
    private Float rate;
    private Float amount;
/*
    public Long getId() {
        return id;
    }

    @JsonBackReference
    public Kirana getKirana() {
        return kirana;
    }

    public String getItemname() {
        return itemname;
    }

    public String getUnit() {
        return unit;
    }

    public Float getQuantity() {
        return quantity;
    }

    public Float getRate() {
        return rate;
    }

    public Float getAmount() {
        return amount;
    }

 */
}