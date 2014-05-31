package me.akiss.sde.encryptors;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;

/**
 * A message digest data encryption implementation based on MD5 hash function.
 *
 * @author Akis Papadopoulos, iakopap@gmail.com
 */
public class MessageDigestHasher implements DataEncryptor {

    // Logger
    private static final Logger logger = Logger.getLogger(MessageDigestHasher.class);
    
    // Hashing algorithm
    private String algorithm;

    /**
     * A constructor creating a hash data encryptor given the hash algorithm.
     *
     * @param algorithm the hash message digest algorithm.
     */
    public MessageDigestHasher(String algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * A method returning the encrypted given data using the message digest hash
     * function based on the MD5 algorithm.
     *
     * @param data the data to be encrypted.
     * @param salt the salt message to enforce security.
     * @return the digest encrypted data.
     */
    @Override
    public String encrypt(String data, String salt) {
        String digest = null;

        try {
            // Building the encrypted digest message
            MessageDigest md = MessageDigest.getInstance(algorithm);

            // Applying the salt message first
            if (salt != null && !salt.isEmpty()) {
                md.update(salt.getBytes());
            }

            md.update(data.getBytes());

            // Converting bytes to hexadecimal format
            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            digest = sb.toString();
        } catch (NoSuchAlgorithmException exc) {
            logger.error("An error occurred loading hashing algorithm: '" + exc.getMessage() + "'.");
        }

        return digest;
    }
}
