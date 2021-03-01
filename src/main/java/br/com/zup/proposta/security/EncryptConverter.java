package br.com.zup.proposta.security;

import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.AttributeConverter;

public class EncryptConverter implements AttributeConverter<String, String> {

    @Value("${encrypt.doc.key}")
    private String key;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
        textEncryptor.setPassword(key);
        return textEncryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
        textEncryptor.setPassword(key);
        return textEncryptor.decrypt(dbData);
    }
}
