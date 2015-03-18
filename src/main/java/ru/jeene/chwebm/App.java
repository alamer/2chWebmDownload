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
package ru.jeene.chwebm;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import ru.jeene.chwebm.models.Model_Cache;
import ru.jeene.chwebm.models.Model_Webm;
import ru.jeene.chwebm.worker.WmLoadWorker;
import ru.jeene.chwebm.worker.utils.HTTPUtils;

/**
 *
 * @author ivc_ShherbakovIV
 */
public class App {

    private static App app;
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(App.class);
    @Option(name = "-t", usage = "Sets number of threads")        // no usage
    private int THREAD_NUMBER = 10;
    @Option(name = "-p", usage = "Sets number of pages to load")        // no usage
    private int PAGE_TO_SCAN = 20;
    @Option(name = "-dir", usage = "Dir for dump", required = true)        // no usage
    private String DL_DIR;
    @Option(name = "-topic", usage = "2ch topic", required = true)        // no usage
    private String topic;

    String main_url = "https://2ch.hk/";
    String cache_file = "cache.xml";
    Model_Cache cache;

    public App(String[] args) {

        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException ex) {
            logger.error(ex);
        }
        logger.info("Threads to use " + THREAD_NUMBER);
        logger.info("Pages to scan " + PAGE_TO_SCAN);
        main_url += topic + "/";
        logger.info("Url to scan " + main_url);
        logger.info("Dump dir " + DL_DIR);
        File f = new File(cache_file);

        if (f.exists()) {
            cache = Model_Cache.load(cache_file, "windows-1251");
        } else {
            cache = new Model_Cache();
        }
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);

        for (int i = 1; i <= PAGE_TO_SCAN; i++) {
            String tmp_str = loadJSON(main_url + i + ".json");
            ArrayList<Model_Webm> webm_list = parseJSON(tmp_str);
            if (webm_list.size() > 0) {
                logger.info("Thread: " + webm_list.get(0).getThread() + " Webm's: " + webm_list.size());
            } else {
                logger.info(main_url + i + ".json" + " no new webm");
            }
            for (Model_Webm model_Webm : webm_list) {
                //Добавляем в закачку
                Runnable worker = new WmLoadWorker(model_Webm, DL_DIR);
                executor.execute(worker);
            }

        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            System.out.print("\rThinking... ");
            System.out.flush();

        }
        try {
            Model_Cache.save(cache, cache_file);
            //Load json by id
            //Parse and get WM links
            //
        } catch (IOException ex) {
            logger.error(ex);
        }

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

    private String loadJSON(String url) {
        StringBuilder res = new StringBuilder();
        try {
            HttpsURLConnection connection = (HttpsURLConnection) openConnection(url);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(30000);
            try ( // open the stream and put it into BufferedReader
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))) {
                        String inputLine;
                        while ((inputLine = br.readLine()) != null) {
                            //System.out.println(inputLine);
                            res.append(inputLine);
                        }
                    }
                    logger.info("Done");
        } catch (KeyManagementException ex) {
            logger.error(ex);
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex);
        } catch (IOException ex) {
            logger.error(ex);
        }
        return res.toString();
    }

    private ArrayList<Model_Webm> parseJSON(String json) {
        ArrayList<Model_Webm> res = new ArrayList<>();
        res.clear();
        try {
            Pattern regex = Pattern.compile("src/(\\d+?)/(\\d+?\\.webm)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            Matcher regexMatcher = regex.matcher(json);
            while (regexMatcher.find()) {
                Model_Webm m = new Model_Webm();
                Model_Webm c = cache.getList().get(main_url + regexMatcher.group());
                m.setThread(regexMatcher.group(1));
                m.setFname(regexMatcher.group(2));
                m.setUrl(main_url + regexMatcher.group());
                if (c != null) {

                } else {
                    cache.getList().put(m.getUrl(), m);
                    res.add(m);
                }
                // matched text: regexMatcher.group()
                // match start: regexMatcher.start()
                // match end: regexMatcher.end()
            }
        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
            logger.error(ex);
        }

        return res;
    }

    public static void main(String[] args) {

        app = new App(args);
    }
}
