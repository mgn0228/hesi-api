package com.test.back.hesi.web.data;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public enum UserType implements GrantedAuthority {
	NONE("비회원", 1), 
    NORMAL("로그인 사용자", 101),
    PERSONAL("Personal 사용자", 110), 
    TEAM_USER("Team 사용자(그룹원)", 121), 
    TEAM_MANAGER("Team 사용자(관리자)", 122),
    TEAM("Team 사용자(모두: [121, 122, 129] 의 경우, [PERSONAL(110) / TEAM(123)] 으로만 구분할 시에는 [123])", 123),
    TEAM_MASTER("Team 사용자(마스터-결제자)", 129), 
	ADMIN("관리자", 201);
	
	private final String description;
    
    private final int code;
	
	UserType(String description, int code) {
		this.description = description;
		this.code = code;
	}

    @Override
    public String getAuthority() {
        return description;
    }
    
    public Collection<GrantedAuthority> getAuthorities(){
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for(UserType type : UserType.values()) {
            if(type.code < this.code) {
                authorities.add(new SimpleGrantedAuthority(description));
            }
        }
        return authorities;
    }
}
