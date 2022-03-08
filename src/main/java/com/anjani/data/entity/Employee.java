package com.anjani.data.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Employee {
    private Long id;
    private String name;
    private String nickname;
    private String address;
    private String contact;
    private String designation;
    private Float salary;
    private String salarytype;
    private String status;

}