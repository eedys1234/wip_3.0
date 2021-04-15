package com.wip.bool.domain.cmmn.encrypt;

import com.google.common.io.BaseEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * PBKDF2 라이브러리
 * https://www.javatips.net/api/JavaSecurity-master/crypto-hash/src/main/java/de/dominikschadow/javasecurity/hash/PBKDF2.java
 */

public class PBKDF2 {

    private static final Logger log = LoggerFactory.getLogger(PBKDF2.class);
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int ITERATIONS = 10000;
    // salt size at least 32 byte
    private static final int SALT_SIZE = 32;
    private static final int HASH_SIZE = 512;

    private String password;
    /**
     * Private constructor.
     */
    public PBKDF2(String password) {
        this.password = password;
    }

    public String hash() {
        char[] password = "TotallySecurePassword12345".toCharArray();

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] salt = generateSalt();

            log.info("Hashing password {} with hash algorithm {}, hash size {}, # of iterations {} and salt {}",
                    String.valueOf(password), ALGORITHM, HASH_SIZE, ITERATIONS, BaseEncoding.base16().encode(salt));

            byte[] hash = calculateHash(skf, password, salt);

            return convertString(hash);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new IllegalStateException("PBKDF2 error occur");
        }
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);

        return salt;
    }

    private String convertString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for(byte b : bytes)
        {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    private byte[] calculateHash(SecretKeyFactory skf, char[] password, byte[] salt) throws InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, HASH_SIZE);

        return skf.generateSecret(spec).getEncoded();
    }

    private boolean verifyPassword(SecretKeyFactory skf, byte[] originalHash, char[] password, byte[] salt) throws
            InvalidKeySpecException {
        byte[] comparisonHash = calculateHash(skf, password, salt);

        log.info("hash 1: {}", BaseEncoding.base16().encode(originalHash));
        log.info("hash 2: {}", BaseEncoding.base16().encode(comparisonHash));

        return comparePasswords(originalHash, comparisonHash);
    }

    /**
     * Compares the two byte arrays in length-constant time using XOR.
     *
     * @param originalHash   The original password hash
     * @param comparisonHash The comparison password hash
     * @return True if both match, false otherwise
     */
    private static boolean comparePasswords(byte[] originalHash, byte[] comparisonHash) {
        int diff = originalHash.length ^ comparisonHash.length;
        for (int i = 0; i < originalHash.length && i < comparisonHash.length; i++) {
            diff |= originalHash[i] ^ comparisonHash[i];
        }

        return diff == 0;
    }
}
