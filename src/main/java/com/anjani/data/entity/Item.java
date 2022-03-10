package com.anjani.data.entity;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Item {
   private Long id;
    String itemname;
    Category catid;
    Float rate;
    Integer itemcode;
}