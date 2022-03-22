package com.anjani.data.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class TableMaster {
    private Integer id;
    private String tablename;
    private TableGroup tableGroup;
    private Float othercharges;

}