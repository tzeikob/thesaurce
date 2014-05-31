package me.akiss.sde.units;

import java.util.HashSet;
import java.util.Set;
import me.akiss.sde.encryptors.DataEncryptor;
import me.akiss.sde.encryptors.MessageDigestHasher;
import me.akiss.sde.salters.Salter;
import me.akiss.sde.salters.SecureRandomGenerator;
import org.apache.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * A generic unit test case implementation.
 *
 * @author Akis Papadopoulos, iakopap@gmail.com
 */
public class GenericTestCase {

    // Logger
    private static final Logger logger = Logger.getLogger(GenericTestCase.class);

    @Test
    public void testSpill() {
        Salter salter = new SecureRandomGenerator();

        Set<String> salts = new HashSet<>();

        for (int i = 0; i < 10000; i++) {
            salts.add(salter.spill());
        }

        assertTrue(salts.size() == 10000);
    }

    @Test
    public void testMD5EncryptionMethod() {
        DataEncryptor de = new MessageDigestHasher();

        String password = "password";

        String encryptedPassword = de.encrypt(password);

        logger.info("Encryption[MD5]: '" + password + "':'" + encryptedPassword + "'");

        String password2 = "password";

        String encryptedPassword2 = de.encrypt(password2);

        assertTrue(encryptedPassword.equals(encryptedPassword2));
    }
}
