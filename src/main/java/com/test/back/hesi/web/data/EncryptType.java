package com.test.back.hesi.web.data;

import lombok.Getter;

@Getter
public enum EncryptType {
    SA(1), SSH(2), ASH(3), SHAS(4), SSHA(5), SHSH(0);
    
    private final int value;
    
    EncryptType(int value) {
        this.value = value;
    }
    
    public static EncryptType findEncryptType(int value) {
        EncryptType[] types = EncryptType.values();
        for(EncryptType type : types) {
            if(type.value == value) 
                return type;
        }
        return EncryptType.SHSH;
    }
}
