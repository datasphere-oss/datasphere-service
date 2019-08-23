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

package com.datasphere.datalayer.resource.manager.crypt;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CryptoService {
	private final static Logger _log = LoggerFactory.getLogger(CryptoService.class);

	private final static int IV_LENGTH = 16;

	@Value("${encrypt.enabled}")
	private boolean enabled;

	@Value("${encrypt.key}")
	private String key;

	private SecureRandom secureRandom;
	private SecretKeySpec keySpec;

	@PostConstruct
	public void init() {
		secureRandom = new SecureRandom();
		try {
			keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		} catch (Exception e) {
			_log.error("error on init " + e.getMessage());
			enabled = false;
		}
	}

	public String encrypt(String content)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {

		_log.trace("encrypt " + content);

		if (!enabled) {
			return content;
		}

		byte[] iv = generateIV();
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));

		// concatenate IV and content
		ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encrypted.length);
//		ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + encrypted.length);
//		byteBuffer.putInt(iv.length);
		byteBuffer.put(iv);
		byteBuffer.put(encrypted);
		byte[] message = byteBuffer.array();

		// return as base64
		String result = Base64.getEncoder().encodeToString(message);
		_log.trace("encrypted " + result);

		return result;

	}

	public String decrypt(String raw)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {
		_log.trace("decrypt " + raw);

		if (!enabled) {
			return raw;
		}

		// base64 decode and unpack
		byte[] message = Base64.getDecoder().decode(raw.getBytes("UTF-8"));

		ByteBuffer byteBuffer = ByteBuffer.wrap(message);
		// read IV length and content from buffer
//		int ivLength = byteBuffer.getInt();
//		if (ivLength < 12 || ivLength >= 16) {
//			throw new IllegalArgumentException("invalid iv length");
//		}
		byte[] iv = new byte[IV_LENGTH];
		byteBuffer.get(iv);
		byte[] encrypted = new byte[byteBuffer.remaining()];
		byteBuffer.get(encrypted);

		// decrypt
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		byte[] content = cipher.doFinal(encrypted);

		// return content as string
		String result = new String(content);
		_log.trace("decrypted " + result);

		return result;

	}

	/*
	 * Internal
	 */
	private byte[] generateIV() {
		byte[] bytes = new byte[IV_LENGTH];
		secureRandom.nextBytes(bytes);

		return bytes;
	}

}
