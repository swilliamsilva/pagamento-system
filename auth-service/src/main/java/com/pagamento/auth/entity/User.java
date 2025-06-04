package com.pagamento.auth.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String roles;
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getRoles() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setUsername(String string) {
		// TODO Auto-generated method stub
		
	}
	public void setRoles(String string) {
		// TODO Auto-generated method stub
		
	}
    
    // Getters e Setters
}