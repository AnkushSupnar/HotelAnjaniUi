package com.anjani.data.entity;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PurchaseParty {
    private Integer id;
    private String name;
    private String address;
    private String contact;
    private String email;
    String gstno;
    String pan;
}
