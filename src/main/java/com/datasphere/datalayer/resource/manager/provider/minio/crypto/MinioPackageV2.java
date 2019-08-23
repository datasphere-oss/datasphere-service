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

package com.datasphere.datalayer.resource.manager.provider.minio.crypto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class MinioPackageV2 implements MinioPackage {

    public final static int HEADER_SIZE = 16;
    public final static int TAG_SIZE = 16;
    public final static int NONCE_SIZE = 12;

    byte[] ciphertext;
    final MinioHeaderV2 header;

    public MinioPackageV2() {
        header = new MinioHeaderV2();
        ciphertext = new byte[0];
    }

    public MinioPackageV2(byte[] bytes) throws MinioCryptoException {
        // check at least 4 bytes for content
        if (bytes.length < (HEADER_SIZE + TAG_SIZE + 4)) {
            throw new MinioCryptoException("packet size too small");
        }

        // parse
        byte[] h = Arrays.copyOf(bytes, HEADER_SIZE);
        header = new MinioHeaderV2(h);
        ciphertext = Arrays.copyOfRange(bytes, HEADER_SIZE, bytes.length);
    }

    @Override
    public byte version() {
        return 0x20; // version 2
    }

    @Override
    public MinioHeaderV2 header() {
        return header;
    }

    @Override
    public byte[] payload() {
//        return Arrays.copyOfRange(raw, HEADER_SIZE, (raw.length - TAG_SIZE));
        return Arrays.copyOfRange(ciphertext, 0, (ciphertext.length - TAG_SIZE));
    }

    @Override
    public byte[] cipherText() {
        return ciphertext;
    }

    @Override
    public int length() {
        return HEADER_SIZE + TAG_SIZE + header.getLength();
    }

    @Override
    public byte[] nonce() {
        return header.getNonce();
    }

    @Override
    public byte[] addData() {
        return header.getAddData();
    }

    /*
     * build
     */

    public void setCipherText(byte[] cipherText) {
        this.ciphertext = cipherText;
    }

    public byte[] build() throws MinioCryptoException {

        int len = HEADER_SIZE + ciphertext.length;
        byte[] raw = new byte[len];

        // pack
        System.arraycopy(header.getRaw(), 0, raw, 0, HEADER_SIZE);
        System.arraycopy(ciphertext, 0, raw, HEADER_SIZE, ciphertext.length);

        return raw;
    }

    public void clear() {
        Arrays.fill(this.ciphertext, (byte) 0);
        this.header.clear();
    }

    public class MinioHeaderV2 implements MinioHeader {

        private final byte[] header;
        private boolean _final;

        public MinioHeaderV2() {
            header = new byte[HEADER_SIZE];
            // zero fill
            clear();

            // set version as 2
            header[0] = 0x20;

            _final = false;
        }

        public MinioHeaderV2(byte[] bytes) throws MinioCryptoException {
            if (bytes.length != HEADER_SIZE) {
                throw new MinioCryptoException("wrong header size");
            }

            header = bytes;
            _final = ((header[4] & 0x80) == 0x80);
        }

        @Override
        public byte getVersion() {
            return header[0];
        }

        @Override
        public byte getCipher() {
            return header[1];
        }

        public void setCipher(byte b) {
            header[1] = b;
        }

        @Override
        public int getLength() {

            ByteBuffer bb = ByteBuffer.allocate(2);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.put(header[2]);
            bb.put(header[3]);
            short dSize = bb.getShort(0);

            int size = dSize >= 0 ? dSize : 0x10000 + dSize;
            // add +1
            size++;
            return size;
        }

        public void setLength(int i) {
            // remember to subtract 1
            i--;
            if (i >= 0) {
                ByteBuffer bb = ByteBuffer.allocate(2);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                bb.putShort((short) i);
                header[2] = bb.get(0);
                header[3] = bb.get(1);
            } else {
                header[2] = (byte) 0;
                header[3] = (byte) 0;
            }
        }

        @Override
        public boolean isFinal() {
            return _final;
        }

        public void setFinal(boolean b) {
            _final = b;
        }

        @Override
        public byte[] getNonce() {
            // nonce is all header bytes except first 4 (ie add data)
            byte[] nonce = Arrays.copyOfRange(header, 4, header.length);
            if (isFinal()) {
                nonce[0] |= 0x80;
            }

            // check size
            // DISABLED

            return nonce;
        }

        public void setNonce(byte[] b) {
            // nonce is 12 bytes
            System.arraycopy(b, 0, header, 4, NONCE_SIZE);
        }

        @Override
        public byte[] getAddData() {
            // add data is first 4 bytes
            return Arrays.copyOf(header, 4);
        }

        @Override
        public byte[] getRaw() {
            byte[] bytes = new byte[HEADER_SIZE];
            System.arraycopy(header, 0, bytes, 0, HEADER_SIZE);
            if (isFinal()) {
                bytes[4] |= 0x80;
            }

            return bytes;
        }

        public void clear() {
            Arrays.fill(this.header, (byte) 0);
        }

    }

}
