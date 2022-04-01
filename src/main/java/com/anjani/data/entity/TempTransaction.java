package com.anjani.data.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class TempTransaction {
    private Long id;
    private Item item;
    private Float quantity;
    private Float amount;
    private TableMaster tableMaster;
    private Float printqty;
    private Float rate;
    private Employee employee;


}