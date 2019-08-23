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

package com.datasphere.datalayer.resource.manager.provider.minio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.xmlpull.v1.XmlPullParserException;

import com.datasphere.datalayer.resource.manager.provider.minio.crypto.MinioCrypter;
import com.datasphere.datalayer.resource.manager.provider.minio.crypto.MinioCrypto;
import com.datasphere.datalayer.resource.manager.provider.minio.crypto.MinioCryptoException;
import com.datasphere.datalayer.resource.manager.provider.minio.crypto.MinioDecrypter;
import com.datasphere.datalayer.resource.manager.provider.minio.crypto.MinioPackage;
import com.datasphere.datalayer.resource.manager.provider.minio.crypto.MinioSha256;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import io.minio.messages.Upload;
import uk.co.lucasweb.aws.v4.signer.HttpRequest;
import uk.co.lucasweb.aws.v4.signer.Signer;
import uk.co.lucasweb.aws.v4.signer.credentials.AwsCredentials;

public class MinioS3Client {

    private final static Logger _log = LoggerFactory.getLogger(MinioS3Client.class);

    private final String HOST;
    private final int PORT;
    private final String ENDPOINT;
    private final String ACCESS_KEY;
    private final String SECRET_KEY;

    public MinioS3Client(String host, int port, boolean ssl, String accessKey, String secretKey) {
        super();

        HOST = host;
        PORT = port;

        // build endpoint
        StringBuilder sb = new StringBuilder();
        if (ssl) {
            sb.append("https://");
        } else {
            sb.append("http://");
        }

        sb.append(host).append(":").append(port);

        ENDPOINT = sb.toString();
        ACCESS_KEY = accessKey;
        SECRET_KEY = secretKey;
    }

    private MinioClient connect() throws MinioException {
        return new MinioClient(
                ENDPOINT,
                ACCESS_KEY, SECRET_KEY);
    }

    public boolean ping() {
        try {
            MinioClient minio = connect();
            // list buckets to check status
            // TODO replace with REST api call
            minio.listBuckets();

            return true;
        } catch (Exception ex) {
            _log.error(ex.getMessage());
            return false;
        }
    }

    public boolean hasBucket(String bucketName) throws MinioException {
        MinioClient minio = connect();
        try {
            return minio.bucketExists(bucketName);
        } catch (MinioException mex) {
            _log.error(mex.getMessage());
            throw mex;
        } catch (InvalidKeyException | NoSuchAlgorithmException | IOException | XmlPullParserException ex) {
            _log.error(ex.getMessage());
            throw new MinioException(ex.getMessage());
        }
    }

    public String createBucket(String bucketName) throws MinioException {
        MinioClient minio = connect();

        try {
            if (minio.bucketExists(bucketName)) {
                throw new MinioException("bucket exists");
            }

            _log.debug("create bucket " + bucketName);

            // make bucket is void *-*
            minio.makeBucket(bucketName);

            return bucketName;
        } catch (MinioException mex) {
            _log.error(mex.getMessage());
            throw mex;
        } catch (InvalidKeyException | NoSuchAlgorithmException | IOException | XmlPullParserException ex) {
            _log.error(ex.getMessage());
            throw new MinioException(ex.getMessage());
        }

    }

    public void deleteBucket(String bucketName, boolean cleanup) throws MinioException {
        MinioClient minio = connect();
        try {
            if (minio.bucketExists(bucketName)) {

                // remove bucket does NOT remove the objects
                if (cleanup) {
                    _log.debug("cleanup uploads for bucket " + bucketName);

                    // could be a long run - TODO async
                    Iterable<Result<Upload>> incompleteUploads = minio.listIncompleteUploads(bucketName);
                    for (Result<Upload> result : incompleteUploads) {
                        Upload upload = result.get();
                        String name = upload.objectName();

                        _log.debug("delete upload " + name + " from bucket " + bucketName);
                        minio.removeIncompleteUpload(bucketName, name);
                    }

                    _log.debug("cleanup objects for bucket " + bucketName);

                    Iterable<Result<Item>> items = minio.listObjects(bucketName);
                    for (Result<Item> result : items) {
                        Item item = result.get();
                        String name = item.objectName();

                        _log.debug("delete object " + name + " from bucket " + bucketName);
                        minio.removeObject(bucketName, name);
                    }

                }

                _log.debug("delete bucket " + bucketName);

                // now remove bucket
                minio.removeBucket(bucketName);
            }
        } catch (MinioException mex) {
            _log.error(mex.getMessage());
            throw mex;
        } catch (InvalidKeyException | NoSuchAlgorithmException | IOException | XmlPullParserException ex) {
            _log.error(ex.getMessage());
            throw new MinioException(ex.getMessage());
        }
    }

    // direct api calls for user management and policies
    // NOTE: reverse engineering of minio admin API and DARE encryption schema

    public String createPolicy(String bucketName, String type) throws MinioException {
        try {
            _log.debug("create policy " + type + " for bucket " + bucketName);

            // build json
            JSONObject policy = new JSONObject();
            // static version declaration
            policy.put("Version", "2012-10-17");

            JSONObject aws = new JSONObject("{'AWS': ['*']}");
            JSONArray statements = new JSONArray();

            // root policy
            JSONObject rootStatement = new JSONObject();
            rootStatement.put("Effect", "Allow");
            rootStatement.put("Principal", aws);

            JSONArray rootActions = new JSONArray();
            rootActions.put("s3:ListAllMyBuckets");
            rootActions.put("s3:HeadBucket");
            rootStatement.put("Action", rootActions);

            JSONArray rootResource = new JSONArray();
            rootResource.put("arn:aws:s3:::*");
            rootStatement.put("Resource", rootResource);

            statements.put(rootStatement);

            // bucket policy
            JSONObject bucketStatement = new JSONObject();
            bucketStatement.put("Effect", "Allow");
            bucketStatement.put("Principal", aws);

            JSONArray bucketActions = new JSONArray();
            bucketActions.put("s3:GetBucketLocation");
            bucketActions.put("s3:ListBucket");
            bucketActions.put("s3:ListBucketMultipartUploads");
            bucketStatement.put("Action", bucketActions);

            JSONArray bucketResource = new JSONArray();
            bucketResource.put("arn:aws:s3:::" + bucketName);
            bucketStatement.put("Resource", bucketResource);

            statements.put(bucketStatement);

            // object policy
            JSONObject objectStatement = new JSONObject();
            objectStatement.put("Effect", "Allow");
            objectStatement.put("Principal", aws);

            JSONArray objectActions = new JSONArray();
            if (type.equals(POLICY_RW)) {
                objectActions.put("s3:AbortMultipartUpload");
                objectActions.put("s3:DeleteObject");
                objectActions.put("s3:GetObject");
                objectActions.put("s3:ListMultipartUploadParts");
                objectActions.put("s3:PutObject");
            } else if (type.equals(POLICY_RO)) {
                objectActions.put("s3:GetObject");
            }

            objectStatement.put("Action", objectActions);

            JSONArray objectResource = new JSONArray();
            objectResource.put("arn:aws:s3:::" + bucketName + "/*");
            objectStatement.put("Resource", objectResource);

            statements.put(objectStatement);

            policy.put("Statement", statements);

            String name = bucketName + "_" + type;

            // prepare signature
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// server timezone

            String hostname = HOST + ":" + Integer.toString(PORT);
            String url = ENDPOINT + "/minio/admin/v1/add-canned-policy?name=" + name;
            String date = dateFormat.format(new Date());
            String content = policy.toString();
            String contentSha256 = MinioSha256.get(content, Charset.forName("UTF-8"));

            _log.trace("policy json " + content);
            _log.trace("content sha " + contentSha256);

            HttpRequest request = new HttpRequest("PUT", new URI(url));
            String signature = Signer.builder()
                    .awsCredentials(new AwsCredentials(ACCESS_KEY, SECRET_KEY))
                    .header("Host", hostname)
                    .header("x-amz-date", date)
                    .header("x-amz-content-sha256", contentSha256)
                    .buildS3(request, contentSha256)
                    .getSignature();

            _log.trace("request url " + url);
            _log.trace("request signature " + signature);

            RestTemplate template = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            headers.set("Authorization", signature);
            headers.set("X-Amz-Content-Sha256", contentSha256);
            headers.set("X-Amz-Date", date);

            HttpEntity<String> entity = new HttpEntity<>(content, headers);

            ResponseEntity<String> response = template.exchange(url, HttpMethod.PUT, entity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // no response expected from Minio
                return name;
            } else {
                _log.error("response error " + response.getStatusCode().toString());
                throw new MinioException("response error " + response.getStatusCode().toString());
            }

        } catch (URISyntaxException uex) {
            _log.error(uex.getMessage());
            throw new MinioException(uex.getMessage());
        } catch (RestClientException rex) {
            throw new MinioException("rest error " + rex.getMessage());
        }

    }

    public void removePolicy(String bucketName, String type) throws MinioException {
        try {
            _log.debug("remove policy " + type + " for bucket " + bucketName);

            String name = bucketName + "_" + type;
            // prepare signature
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// server timezone

            String hostname = HOST + ":" + Integer.toString(PORT);
            String url = ENDPOINT + "/minio/admin/v1/remove-canned-policy?name=" + name;
            String date = dateFormat.format(new Date());
            String content = ""; // empty content needed for sha256
            String contentSha256 = MinioSha256.get(content, Charset.forName("UTF-8"));

            HttpRequest request = new HttpRequest("DELETE", new URI(url));
            String signature = Signer.builder()
                    .awsCredentials(new AwsCredentials(ACCESS_KEY, SECRET_KEY))
                    .header("Host", hostname)
                    .header("x-amz-date", date)
                    .header("x-amz-content-sha256", contentSha256)
                    .buildS3(request, contentSha256)
                    .getSignature();

            _log.trace("request url " + url);
            _log.trace("request signature " + signature);

            RestTemplate template = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            headers.set("Authorization", signature);
            headers.set("X-Amz-Content-Sha256", contentSha256);
            headers.set("X-Amz-Date", date);

            HttpEntity<String> entity = new HttpEntity<>(content, headers);

            ResponseEntity<String> response = template.exchange(url, HttpMethod.DELETE, entity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.NOT_FOUND) {
                // ok
            } else {
                _log.error("response error " + response.getStatusCode().toString());
                throw new MinioException("response error " + response.getStatusCode().toString());
            }
        } catch (URISyntaxException uex) {
            _log.error(uex.getMessage());
            throw new MinioException(uex.getMessage());
        } catch (RestClientException rex) {
            throw new MinioException("rest error " + rex.getMessage());
        }
    }

    public void createUser(String bucket, String accessKey, String secretKey, String policy) throws MinioException {
        try {
            _log.debug("create user " + accessKey + " for bucket " + bucket);

            // build user info
            JSONObject userInfo = new JSONObject();
            userInfo.put("secretKey", secretKey);
            userInfo.put("status", "enabled");

            _log.trace("userInfo json " + userInfo.toString());

            // encrypt payload
            MinioCrypter crypter = MinioCrypto.getCrypter(SECRET_KEY, userInfo.toString());
            byte[] content = crypter.crypt();

            MinioPackage pkg = crypter.inspect();
            _log.trace("package version " + String.valueOf(pkg.header().getVersion()));
            _log.trace("package cipher " + String.valueOf(pkg.header().getCipher()));
            _log.trace("package nonce " + Arrays.toString(pkg.header().getNonce()));
            _log.trace("package length " + String.valueOf(pkg.header().getLength()));
            _log.trace("string length " + String.valueOf(userInfo.toString().getBytes().length));

            MinioDecrypter decrypter = MinioCrypto.getDecrypter(SECRET_KEY, content);

            pkg = decrypter.inspect();
            String plainText = decrypter.decrypt();
            _log.trace("decrypt plainText " + plainText);

            decrypter.clear();

            // clear
            crypter.clear();

            // prepare signature
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// server timezone

            String hostname = HOST + ":" + Integer.toString(PORT);
            String url = ENDPOINT + "/minio/admin/v1/add-user?accessKey=" + accessKey;
            String date = dateFormat.format(new Date());

            String contentSha256 = MinioSha256.get(content);
            _log.trace("content sha " + contentSha256);

            HttpRequest request = new HttpRequest("PUT", new URI(url));
            String signature = Signer.builder()
                    .awsCredentials(new AwsCredentials(ACCESS_KEY, SECRET_KEY))
                    .header("Host", hostname)
                    .header("x-amz-date", date)
                    .header("x-amz-content-sha256", contentSha256)
                    .buildS3(request, contentSha256)
                    .getSignature();

            _log.trace("request url " + url);
            _log.trace("request signature " + signature);

            RestTemplate template = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            headers.set("Authorization", signature);
            headers.set("X-Amz-Content-Sha256", contentSha256);
            headers.set("X-Amz-Date", date);

            // binary payload
            HttpEntity<byte[]> entity = new HttpEntity<>(content, headers);

            ResponseEntity<String> response = template.exchange(url, HttpMethod.PUT, entity,
                    String.class);

            _log.trace("response status code " + response.getStatusCode().toString());

            if (response.getStatusCode() == HttpStatus.OK) {
                // expect policy to exists
                String policyName = bucket + "_" + policy;

                _log.debug("set user " + accessKey + " policy " + policyName);

                url = ENDPOINT + "/minio/admin/v1/set-user-policy?accessKey=" + accessKey + "&name=" + policyName;
                content = new byte[0];
                contentSha256 = MinioSha256.get(content);

                request = new HttpRequest("PUT", new URI(url));
                signature = Signer.builder()
                        .awsCredentials(new AwsCredentials(ACCESS_KEY, SECRET_KEY))
                        .header("Host", hostname)
                        .header("x-amz-date", date)
                        .header("x-amz-content-sha256", contentSha256)
                        .buildS3(request, contentSha256)
                        .getSignature();

                _log.trace("request url " + url);
                _log.trace("request signature " + signature);

                template = new RestTemplate();
                headers = new HttpHeaders();

                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

                headers.set("Authorization", signature);
                headers.set("X-Amz-Content-Sha256", contentSha256);
                headers.set("X-Amz-Date", date);

                entity = new HttpEntity<>(content, headers);

                response = template.exchange(url, HttpMethod.PUT, entity,
                        String.class);

                _log.trace("response status code " + response.getStatusCodeValue());

                if (response.getStatusCode() == HttpStatus.OK) {
                    // ok
                } else {
                    _log.error("response error for set user policy" + response.getStatusCode().toString());
                    throw new MinioException("response error " + response.getStatusCode().toString());
                }

            } else {
                _log.error("response error for add user " + response.getStatusCode().toString());
                throw new MinioException("response error " + response.getStatusCode().toString());
            }

        } catch (URISyntaxException uex) {
            _log.error(uex.getMessage());
            throw new MinioException(uex.getMessage());
        } catch (RestClientException rex) {
            throw new MinioException("rest error " + rex.getMessage());
        } catch (MinioCryptoException e) {
            _log.error(e.getMessage());
            throw new MinioException(e.getMessage());
        }
    }

    public void removeUser(String accessKey) throws MinioException {
        try {
            _log.debug("remove user " + accessKey);

            // prepare signature
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// server timezone

            String hostname = HOST + ":" + Integer.toString(PORT);
            String url = ENDPOINT + "/minio/admin/v1/remove-user?accessKey=" + accessKey;
            String date = dateFormat.format(new Date());
            String content = ""; // empty content needed for sha256
            String contentSha256 = MinioSha256.get(content, Charset.forName("UTF-8"));

            HttpRequest request = new HttpRequest("DELETE", new URI(url));
            String signature = Signer.builder()
                    .awsCredentials(new AwsCredentials(ACCESS_KEY, SECRET_KEY))
                    .header("Host", hostname)
                    .header("x-amz-date", date)
                    .header("x-amz-content-sha256", contentSha256)
                    .buildS3(request, contentSha256)
                    .getSignature();

            RestTemplate template = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            headers.set("Authorization", signature);
            headers.set("X-Amz-Content-Sha256", contentSha256);
            headers.set("X-Amz-Date", date);

            HttpEntity<String> entity = new HttpEntity<>(content, headers);

            ResponseEntity<String> response = template.exchange(url, HttpMethod.DELETE, entity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.NOT_FOUND) {
                // ok
            } else {
                _log.error("response error " + response.getStatusCode().toString());
                throw new MinioException("response error " + response.getStatusCode().toString());
            }
        } catch (URISyntaxException uex) {
            _log.error(uex.getMessage());
            throw new MinioException(uex.getMessage());
        } catch (RestClientException rex) {
            throw new MinioException("rest error " + rex.getMessage());
        }
    }

    public static final String POLICY_RW = "readwrite";
    public static final String POLICY_RO = "readonly";
}
