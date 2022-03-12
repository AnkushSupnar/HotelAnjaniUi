package com.anjani.data.entity;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class KiranaTransaction {
    private Long id;
    @ToString.Exclude
    private Kirana kirana;
    private String itemname;
    private String unit;
    private Float quantity;
    private Float rate;
    private Float amount;
}