/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.engine.manager.resource.provider.minio.crypto;

import static com.kosprov.jargon2.api.Jargon2.*;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MinioCrypter {

    private final int SALT_SIZE;
    private final int TAG_SIZE;
    private final int NONCE_SIZE;

    private final byte CIPHER;

    private MinioPackage _package;
    private String secretKey;
    private byte[] salt;
    private byte[] data;

    private String plainText;

    public MinioCrypter(String key, String text,
            int saltSize, int version, byte cipher) throws MinioCryptoException {

        // save params
        SALT_SIZE = saltSize;
        secretKey = key;
        CIPHER = cipher;

        // data
        plainText = text;

        // package
        if (version != 2) {
            // support only V2
            throw new MinioCryptoException("unsupported version");
        }

        _package = new MinioPackageV2();
        TAG_SIZE = MinioPackageV2.TAG_SIZE;
        NONCE_SIZE = MinioPackageV2.NONCE_SIZE;

        // generate salt now
        salt = generateSalt();

        // data is null until crypt call
        data = null;
    }

    public byte[] salt() {
        return salt;
    }

    public byte[] data() {
        return data;
    }

    public MinioPackage inspect() {
        return _package;
    }

    public byte[] generateSalt() throws MinioCryptoException {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[SALT_SIZE];
        random.nextBytes(bytes);

        return bytes;
    }

    public byte[] generateNonce(boolean isFinal) throws MinioCryptoException {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[NONCE_SIZE];
        random.nextBytes(bytes);

        if (isFinal) {
            // apply transformation otherwise won't match in decoding
            bytes[0] |= 0x80;
        }

        return bytes;
    }

    // key
    public byte[] idKey(String secretKey, byte[] salt) {
        Hasher hasher = jargon2Hasher()
                .type(Type.ARGON2id) // Data-dependent hashing
                .memoryCost(65536) // 64MB memory cost
                .timeCost(1) // 3 passes through memory
                .parallelism(4) // use 4 lanes and 4 threads
                .hashLength(32); //

        return hasher.salt(salt).password(secretKey.getBytes()).rawHash();
    }

    // crypto
    public byte[] crypt() throws MinioCryptoException {
        if (data == null) {
            // build nonce
            // always final since all messages are under segmentation threshold
            byte[] nonce = generateNonce(true);

            // check cipher
            if (CIPHER == 0x00) {
                // 0x00 is AES-256_GCM
                byte[] plainTextBytes = plainText.getBytes();

                // update headers
                _package.header().setCipher(CIPHER);
                _package.header().setFinal(true);
                _package.header().setNonce(nonce);
                // set length as plaintext length in bytes, should match cipherText
                _package.header().setLength(plainTextBytes.length);

                byte[] cipherText = _cryptAES(
                        idKey(secretKey, salt),
                        nonce,
                        plainTextBytes,
                        _package.header().getAddData(),
                        TAG_SIZE);

                // set in package
                _package.setCipherText(cipherText);
                data = _package.build();

            } else if (CIPHER == 0x01) {
                // 0x01 is CHACHA20_POLY1305
                throw new MinioCryptoException("unsupported cipher");
            } else {
                // unknown
                throw new MinioCryptoException("unknown cipher");
            }
        }

        byte[] raw = new byte[SALT_SIZE + data.length];
        // pack salt + data
        System.arraycopy(salt, 0, raw, 0, SALT_SIZE);
        System.arraycopy(data, 0, raw, SALT_SIZE, data.length);

        return raw;
    }

    private byte[] _cryptAES(byte[] key, byte[] nonce, byte[] plainTextBytes, byte[] addData, int tagSize)
            throws MinioCryptoException {
        try {
            // rebuild key using SecretKeySpec
            SecretKey aesKey = new SecretKeySpec(key, 0, key.length, "AES");

            // encrypt
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");

            // tag is X bytes
            GCMParameterSpec spec = new GCMParameterSpec(8 * tagSize, nonce);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, spec);

            cipher.updateAAD(addData);
            // can not update bytes during encryption, use doFinal
//            cipher.update(plainTextBytes); 

            byte[] cipherText = cipher.doFinal(plainTextBytes);

            return cipherText;

        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
            throw new MinioCryptoException(e.getMessage());

        } catch (InvalidKeyException e) {
            throw new MinioCryptoException(e.getMessage());

        } catch (InvalidAlgorithmParameterException e) {
            throw new MinioCryptoException(e.getMessage());

        } catch (IllegalBlockSizeException e) {
            throw new MinioCryptoException(e.getMessage());

        } catch (BadPaddingException e) {
            throw new MinioCryptoException(e.getMessage());

        }
    }

    // cleanup
    public void clear() {
        // zero fill buffers
        Arrays.fill(salt, (byte) 0);
        Arrays.fill(data, (byte) 0);

        this._package.clear();

        // TODO prevent storing of Strings?
        this.secretKey = null;
        this.plainText = null;

    }
}
