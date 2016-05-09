package com.httpstest;
import android.content.Context;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

public class CustomerSocketFactory extends SSLSocketFactory {

    private static final String PASSWD = "pw123456";

    public CustomerSocketFactory(KeyStore truststore)
            throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, UnrecoverableKeyException {
        super(truststore);
    }

    public static SSLSocketFactory getSocketFactory(Context context) {
        InputStream input = null;
        try {
            input = context.getResources().openRawResource(R.raw.example);
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());

            trustStore.load(input, PASSWD.toCharArray());

            SSLSocketFactory factory = new CustomerSocketFactory(trustStore);

            return factory;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                input = null;
            }
        }
    }

}