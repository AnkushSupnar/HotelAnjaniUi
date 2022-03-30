package com.anjani.data.entity;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Customer {
    private Long id;
    private String name;
    private String address;
    private String contact;
    private String mobile;
}