package me.encryptors;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.log4j.Logger;

/**
 * An advanced secure data decryptor implementation based on the PBKDF2
 * algorithm.
 *
 * @author Akis Papadopoulos, iakopap@gmail.com
 */
public class AdvancedSecureEncoder implements DataEncryptor {

    // Logger
    private static final Logger logger = Logger.getLogger(AdvancedSecureEncoder.class);
    
    // Iterations
    private int iterations;

    /**
     * A constructor creating a PBKDF2 algorithm based data encryptor given the
     * number of the iterations.
     *
     * @param iterations the secure number of iterations.
     */
    public AdvancedSecureEncoder(int iterations) {
        this.iterations = iterations;
    }

    /**
     * A method encrypting the given salted using the advanced secure encoder
     * based on the PBKDF2 algorithm.
     *
     * @param data the data to be encrypted.
     * @param salt the salt used to secure the encryption.
     * @return the encrypted data.
     */
    @Override
    public String encrypt(String data, String salt) {
        String digest = null;

        try {
            char[] dataChars = data.toCharArray();

            byte[] saltBytes = new byte[1];

            if (salt != null && !salt.isEmpty()) {
                saltBytes = salt.getBytes();
            }

            PBEKeySpec pbe = new PBEKeySpec(dataChars, saltBytes, iterations, 64 * 8);

            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = skf.generateSecret(pbe).getEncoded();

            digest = iterations + ":" + toHex(saltBytes) + ":" + toHex(hash);
        } catch (NullPointerException exc) {
            logger.error("A null flavored error occurred encrypting data: '" + data + "'.");
        } catch (NoSuchAlgorithmException exc) {
            logger.error("An error occurred loading the encryption algorithm: '" + exc.getMessage() + "'.");
        } catch (InvalidKeySpecException exc) {
            logger.error("An error occurred loading an invalid key spec: '" + exc.getMessage() + "'.");
        } catch (IllegalArgumentException exc) {
            logger.error("An error occurred loading an invalid argument: '" + exc.getMessage() + "'.");
        }

        return digest;
    }
    
    /**
     * A method returning the hexadecimal form of a given byte array.
     *
     * @param bytes the byte array to convert.
     * @return the hexadecimal form of the given bytes.
     */
    private String toHex(byte[] bytes) {
        String result = null;

        if (bytes != null) {
            BigInteger bi = new BigInteger(1, bytes);

            String hex = bi.toString(16);

            int paddingLength = (bytes.length * 2) - hex.length();

            if (paddingLength > 0) {
                result = String.format("%0" + paddingLength + "d", 0) + hex;
            } else {
                result = hex;
            }
        }

        return result;
    }
}
