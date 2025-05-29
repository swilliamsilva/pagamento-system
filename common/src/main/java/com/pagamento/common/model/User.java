package com.pagamento.auth.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String username;
    
    private String password;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<String> roles;

    // Getters e Setters
}
