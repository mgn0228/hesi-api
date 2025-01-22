package com.test.back.hesi.utils;

import com.test.back.hesi.utils.encrypt.AES256;
import com.test.back.hesi.utils.encrypt.KISASeedCbc;
import com.test.back.hesi.web.data.EncryptType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.time.LocalDateTime;

@Component
public class PasswordUtil {

    @Value("${encrypt.seed.key}")
    public String seedKey;

    @Value("${encrypt.seed.iv}")
    public String seedIv;

    @Value("${encrypt.aes.key}")
    public String aesKey;

    @Value("${encrypt.aes.iv}")
    public String aesIv;

    public static final String CHAR_SET = "UTF-8";

    private AES256 aes256;
    
    private void initAES256() {
    	if(aes256 != null) return;
    	
    	aes256 = new AES256(aesKey);
    }

    public String encode(String data) throws Exception {
        int encryptType = generateEncryptType();
        String encryptSeperateCode = generateSeperateCode();
        String encryptBody = generateEncrypt(data, encryptType);
        initAES256();
        return aes256.encrypt(encryptType + encryptSeperateCode + encryptBody);
    }

    public boolean checkEqual(String encryptData, String targetData) throws Exception {
        // encryptData : 디비에 저장된 암호화 데이터, targetData : raw 데이터
        initAES256();
        String data = aes256.decrypt(encryptData);
        int encryptType = Integer.parseInt(data.substring(0, 1));
        String encryptBody = data.substring(2);
        String targetEncryptBody = generateEncrypt(targetData, encryptType);

        return encryptBody.equals(targetEncryptBody);
    }

    private int generateEncryptType() {
        int typeCode = LocalDateTime.now().getSecond() % EncryptType.values().length;
        return typeCode;
    }

    private String generateSeperateCode() {
        String[] typeCode = {":", "|", "?", ":", ";"};
        return typeCode[(((int)Math.round(Math.random() * 400)) % 5)];
    }

    private String generateEncrypt(String data, int typeCode) throws Exception {
        EncryptType type = EncryptType.findEncryptType(typeCode);
        initAES256();

        switch (type) {
            case SA:
                return aes256.encrypt(doEncryptSeed(data));
            case SSH:
                return doEncryptSha256(doEncryptSeed(data));
            case ASH:
                return doEncryptSha256(aes256.encrypt(data));
            case SHAS:
                return doEncryptSeed(aes256.encrypt(doEncryptSha256(data)));
            case SSHA:
                return aes256.encrypt(doEncryptSha256(doEncryptSeed(data)));
            default:
                return doEncryptSha256(doEncryptSha256(data));
        }
    }

    public String doEncryptSeed(String target) {
        try {
            return new String(encryptSeed(target), CHAR_SET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String doEncryptSha256(String msg) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(msg.getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte b : md.digest()) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    private byte[] encryptSeed(String target) {
        try {
            byte[] tmp = null;
            byte[] pbszUserKey = seedKey.getBytes();
            byte[] pbszIV = seedIv.getBytes();
            tmp = KISASeedCbc.SEED_CBC_Encrypt(pbszUserKey, pbszIV, target.getBytes(CHAR_SET), 0,
                target.getBytes(CHAR_SET).length);
            Encoder encoder = Base64.getEncoder();
            return encoder.encode(tmp);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
