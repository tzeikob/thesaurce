package com.tkb.the.psg.unit;

import java.util.HashSet;
import java.util.Set;
import com.tkb.the.psg.encryption.AdvancedSecureEncoder;
import com.tkb.the.psg.encryption.DataEncryptor;
import com.tkb.the.psg.encryption.MessageDigestHasher;
import com.tkb.the.psg.salt.Salter;
import com.tkb.the.psg.salt.SecureRandomGenerator;
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

        Set<String> salts = new HashSet<String>();

        for (int i = 0; i < 10000; i++) {
            salts.add(salter.spill());
        }

        assertTrue(salts.size() == 10000);
    }

    @Test
    public void testMessageDigestMethod() {
        Salter salter = new SecureRandomGenerator();
        
        String salt = salter.spill();
        
        String algo = "SHA-1";
        
        DataEncryptor de = new MessageDigestHasher(algo);

        String password = "password";

        String encryptedPassword = de.encrypt(password, salt);

        logger.info("Encryption[" + algo + "+]: '" + password + "[" + salt + "]':'" + encryptedPassword + "'");

        String password2 = "password";

        String encryptedPassword2 = de.encrypt(password2, salt);

        assertTrue(encryptedPassword.equals(encryptedPassword2));
    }
    
    @Test
    public void testPBEEncryptionMethod() {
        Salter salter = new SecureRandomGenerator();
        
        String salt = salter.spill();
        
        int iterations = 1000;
        
        DataEncryptor de = new AdvancedSecureEncoder(iterations);

        String password = "password";

        String encryptedPassword = de.encrypt(password, salt);

        logger.info("Encryption[PBE+]: '" + password + "[" + salt + "]':'" + encryptedPassword + "'");

        String password2 = "password";

        String encryptedPassword2 = de.encrypt(password2, salt);

        assertTrue(encryptedPassword.equals(encryptedPassword2));
    }
}
