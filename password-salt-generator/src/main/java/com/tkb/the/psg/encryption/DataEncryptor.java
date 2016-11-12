package com.tkb.the.psg.encryption;

/**
 * A generic data encryptor interface.
 * 
 * @author Akis Papadopoulos, iakopap@gmail.com
 */
public interface DataEncryptor {
    
    public String encrypt(String data, String salt);
}
