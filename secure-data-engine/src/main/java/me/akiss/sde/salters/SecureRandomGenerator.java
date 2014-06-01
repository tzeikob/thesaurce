package me.akiss.sde.salters;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.apache.log4j.Logger;

/**
 * A secure random salter implementation using the SHA algorithm.
 *
 * @author Akis Papadopoulos, iakopap@gmail.com
 */
public class SecureRandomGenerator implements Salter {

    // Logger
    private static final Logger logger = Logger.getLogger(SecureRandomGenerator.class);

    /**
     * A method returning random salt using the SHA1PRNG algorithm a
     * cryptographically strong pseudo-random number generator based on the
     * SHA-1 message digest algorithm.
     *
     * @return the random salt message.
     */
    @Override
    public String spill() {
        String salt = null;

        try {
            // Creating the random salt message
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");

            byte[] bytes = new byte[16];
            sr.nextBytes(bytes);

            salt = bytes.toString();
        } catch (NoSuchAlgorithmException exc) {
            logger.error("An error occurred loading the secure random algorithm: '" + exc.getMessage() + "'.");
        }

        return salt;
    }
}
