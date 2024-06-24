package org.tptacs.domain.entities;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.tptacs.domain.enums.OrderStatus;
import org.tptacs.domain.enums.Rol;
import org.tptacs.infraestructure.config.CustomAuthorityDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;

@Getter
public class User implements UserDetails {
    private String id;
    private String username;
    private String email;
    private String password;
    private Rol rol;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean credentialsNonExpired = true;
    private boolean accountNonLocked = true;
    private List<String> orderShared = new LinkedList<String>();

    public User(String id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = List.of(new SimpleGrantedAuthority(Rol.BASIC.name()));
        this.rol = Rol.BASIC;
    }

    private User() {}

    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    public void addOrderShared(String orderId) {
    	if(this.getOrderShared().contains(orderId)) {
    		//TODO
    	}else{
    		//    public Order(String id, String userId, String name, List<ItemOrder> item, OrderStatus status) {
    		this.getOrderShared().add(orderId);
    	}
    }
    
    public List<String> getOrderShared(){
    	return this.orderShared != null ? this.orderShared : new LinkedList<String>();
    }
    
    
}
