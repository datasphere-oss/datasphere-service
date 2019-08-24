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

import static com.kosprov.jargon2.api.Jargon2.jargon2Hasher;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.kosprov.jargon2.api.Jargon2.*;

public class MinioDecrypter {

    private final int SALT_SIZE;
    private final int TAG_SIZE;

    private MinioPackage _package;
    private String secretKey;
    private byte[] salt;
    private byte[] data;

    private String plainText;

    public MinioDecrypter(String key, byte[] raw,
            int saltSize) throws MinioCryptoException {

        // save params
        SALT_SIZE = saltSize;

        // data
        secretKey = key;
        // unpack: salt size given, the remaining is data
        salt = extractSalt(raw);
        data = extractData(raw);

        // read package from data
        _package = readPackage(data);

        // set tag size from package constants
        if (_package.version() == 0x20) {
            TAG_SIZE = MinioPackageV2.TAG_SIZE;
        } else {
            throw new MinioCryptoException("version unsupported");
        }

        // init text as null
        plainText = null;
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

    public byte[] extractSalt(byte[] raw) throws MinioCryptoException {
        if (raw.length < SALT_SIZE) {
            throw new MinioCryptoException("packet size too small");
        }

        return Arrays.copyOf(raw, SALT_SIZE);
    }

    public byte[] extractData(byte[] raw) throws MinioCryptoException {
        if (raw.length < (SALT_SIZE + 1)) {
            throw new MinioCryptoException("packet size too small");
        }

        return Arrays.copyOfRange(raw, SALT_SIZE, raw.length);
    }

    public MinioPackage readPackage(byte[] data) throws MinioCryptoException {
        // check version
        byte version = data[0];

        boolean v1 = (version == 0x10);
        boolean v2 = (version == 0x20);

        if (v1) {
            throw new MinioCryptoException("V1 is unsupported");
        } else if (v2) {
            return new MinioPackageV2(data);
        } else {
            throw new MinioCryptoException("version unknown");
        }

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
    public String decrypt() throws MinioCryptoException {
        if (plainText == null) {
            // check cipher
            if (_package.header().getCipher() == 0x00) {
                // 0x00 is AES-256_GCM
                plainText = _decryptAES(
                        idKey(secretKey, salt),
                        _package.nonce(),
                        _package.cipherText(),
                        _package.addData(),
                        TAG_SIZE);
            } else if (_package.header().getCipher() == 0x01) {
                // 0x01 is CHACHA20_POLY1305
                throw new MinioCryptoException("unsupported cipher");
            } else {
                // unknown
                throw new MinioCryptoException("unknown cipher");
            }
        }

        return plainText;
    }

    private String _decryptAES(byte[] key, byte[] nonce, byte[] cipherText, byte[] addData, int tagSize)
            throws MinioCryptoException {
        try {
            // rebuild key using SecretKeySpec
            SecretKey aesKey = new SecretKeySpec(key, 0, key.length, "AES");

            // decrypt
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");

            // tag is X bytes
            GCMParameterSpec spec = new GCMParameterSpec(8 * tagSize, nonce);
            cipher.init(Cipher.DECRYPT_MODE, aesKey, spec);

            cipher.updateAAD(addData);
            // MUST provide bytes here instead of doFinal for decrypt
            cipher.update(cipherText);

            byte[] plainText = cipher.doFinal();

            return new String(plainText);

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
