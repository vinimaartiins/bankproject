package com.bank.credsystem.util;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import java.security.Key;
import java.util.Base64;

@Configuration("application-test.properties")
@Component
public class AESEncryptor implements AttributeConverter<String, String> {
    private static final String AES = "AES";

    private final Key key;
    private final Cipher cipher;

    public AESEncryptor(@Value("${aes.encryption.key}") String secret) throws Exception {
        key = new SecretKeySpec(secret.getBytes(), AES);
        cipher = Cipher.getInstance(AES);
    }

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(String value) {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes()));
    }

    @SneakyThrows
    @Override
    public String convertToEntityAttribute(String value) {
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.getDecoder().decode(value)));
    }
}
