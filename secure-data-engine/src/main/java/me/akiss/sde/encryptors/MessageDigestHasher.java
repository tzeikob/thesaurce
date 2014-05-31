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

    /**
     * A method returning the encrypted given data using the message digest hash
     * function based on the MD5 algorithm.
     *
     * @param data the data to be encrypted.
     * @return the digest encrypted data.
     */
    @Override
    public String encrypt(String data) {
        String digest = null;

        try {
            // Building the encrypted digest message
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(data.getBytes());

            // Converting bytes to hexadecimal format
            byte[] bytes = md.digest();
            
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            digest = sb.toString();
        } catch (NoSuchAlgorithmException exc) {
            logger.error("An error occurred loading MD5 algorithm: '" + exc.getMessage() + "'.");
        }

        return digest;
    }
}
