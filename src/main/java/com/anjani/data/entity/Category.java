package com.anjani.data.entity;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
@Builder
public class Category {
    private Long id;
    private String category;
    private String stock;
}