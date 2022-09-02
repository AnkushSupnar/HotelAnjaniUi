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
    String itemname;
    private Float quantity;
    private Float rate;
    private Float amount;
    private TableMaster tableMaster;
    private Employee employee;
    private Float printqty;




}