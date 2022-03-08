package com.anjani.data.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Login {

    private Integer Id;

    private String username;

	private String password;
    
	private Employee employee;
}
