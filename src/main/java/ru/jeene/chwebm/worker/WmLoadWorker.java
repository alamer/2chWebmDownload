/*
 $Author$
 $Date$
 $Revision$
 $Source$
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jeene.chwebm.worker;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import ru.jeene.chwebm.models.Model_Webm;

/**
 *
 * @author ivc_ShherbakovIV
 */
public class WmLoadWorker implements Runnable {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(WmLoadWorker.class);

    Model_Webm m;
    String output_dir;

    public WmLoadWorker(Model_Webm m, String output_dir) {
        this.m = m;
        this.output_dir = output_dir;
    }

    @Override
    public void run() {
        //System.out.println(Thread.currentThread().getName() + " Start. Command=" + command);
        processCommand();
        //System.out.println(Thread.currentThread().getName() + " End.");
    }

    private URLConnection openConnection(String url_string) throws KeyManagementException, NoSuchAlgorithmException, MalformedURLException, IOException {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        /*
         * end of the fix
         */

        URL url = new URL(url_string);
        URLConnection con = url.openConnection();
        return con;
    }

    private void processCommand() {
        OutputStream out = null;
        InputStream in = null;
        try {
            //Thread.sleep(500);
            HttpsURLConnection connection = (HttpsURLConnection) openConnection(m.getUrl());
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(30000);
            File f = new File(output_dir + "/" + m.getThread());
            if (!f.exists()) {
                f.mkdirs();
            }
            out = new BufferedOutputStream(new FileOutputStream(output_dir + "/" + m.getThread() + "/" + m.getFname()));

            in = connection.getInputStream();
            byte[] buffer = new byte[1024];

            int numRead;
            long numWritten = 0;

            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
            }

            //logger.info(command + " " + tmp.getDesc());
        } catch (Exception ex) {
            //logger.error();
            logger.error(ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
            }
        }
    }

}
