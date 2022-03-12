package com.anjani.data.entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Bank {

    private Long id;
    private String name;
    private String accountno;
    private String ifsc;
    private String address;
    private Float balance;

}