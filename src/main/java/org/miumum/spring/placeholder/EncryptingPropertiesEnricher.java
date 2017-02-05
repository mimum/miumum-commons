package org.miumum.spring.placeholder;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.util.Assert;

import java.util.Properties;

/**
 * This enricher finds out properties that looks like ENC(some.property.name) and decrypts them using encryptor
 *
 * @see org.jasypt.encryption.StringEncryptor
 * @see org.jasypt.util.text.TextEncryptor
 * @author Roman Valina
 */
public class EncryptingPropertiesEnricher implements PropertiesEnricher {

    /*
     * Only one of these instances will be initialized, the other one will be
	 * null.
	 */
    protected final StringEncryptor stringEncryptor;

    protected final TextEncryptor textEncryptor;

    /**
     * <p>
     * Creates an <tt>EncryptingPropertiesEnricher</tt> instance
     * which will use the passed {@link org.jasypt.encryption.StringEncryptor} object to decrypt
     * encrypted values.
     * </p>
     *
     * @param stringEncryptor the {@link org.jasypt.encryption.StringEncryptor} to be used do decrypt values. It
     *                        can not be null.
     */
    public EncryptingPropertiesEnricher(final StringEncryptor stringEncryptor) {
        Assert.notNull(stringEncryptor, "Encryptor cannot be null");
        this.stringEncryptor = stringEncryptor;
        this.textEncryptor = null;
    }

    /**
     * <p>
     * Creates an <tt>EncryptingPropertiesEnricher</tt> instance which will use the
     * passed {@link org.jasypt.util.text.TextEncryptor} object to decrypt encrypted values.
     * </p>
     *
     * @param textEncryptor the {@link org.jasypt.util.text.TextEncryptor} to be used do decrypt values. It can
     *                      not be null.
     */
    public EncryptingPropertiesEnricher(final TextEncryptor textEncryptor) {
        Assert.notNull(textEncryptor, "Encryptor cannot be null");
        this.stringEncryptor = null;
        this.textEncryptor = textEncryptor;
    }

    @Override
    public void enrichProperties(Properties properties) {
        for (String key : properties.stringPropertyNames()) {
            String originalValue = properties.getProperty(key);
            String newValue;
            if (PropertyValueEncryptionUtils.isEncryptedValue(originalValue) == false) {
                newValue = originalValue;
            } else if (this.stringEncryptor != null) {
                newValue = PropertyValueEncryptionUtils.decrypt(originalValue, this.stringEncryptor);

            } else {
                newValue = PropertyValueEncryptionUtils.decrypt(originalValue, this.textEncryptor);
            }
            properties.put(key, newValue);
        }
    }

}
