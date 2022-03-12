package com.anjani.data.entity;

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

    private LocalDate date;
    private PurchaseParty party;
    private Float nettotal;
    private Float transaport;
    private Float other;
    private Float discount;
    private Float grandtotal;
    private Float paid;
    private List<KiranaTransaction> kiranaTransactions = new ArrayList<>();
    private Bank bank;

}