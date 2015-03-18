/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jeene.chwebm.worker.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author ivc_ShherbakovIV
 */
public class HTTPUtils {

    /**
     * Загрузка содержимого страницы
     *
     * @param server сервер
     * @param port порт
     * @param path путь до страницы
     * @return текст страницы
     */
    public static String loadPage(String server, int port, String path) {

        return loadPage(server, port, path, "utf-8");
    }

    /**
     * Загрузка содержимого страницы
     *
     * @param server сервер
     * @param port порт
     * @param path путь до страницы
     * @param encode кодировка страницы
     * @return текст страницы
     */
    public static String loadPage(String server, int port, String path, String encode) {
        String res = "";
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            HttpHost target = new HttpHost(server, port, "http");
            HttpGet req = new HttpGet(path);
            HttpResponse rsp = httpclient.execute(target, req);
            HttpEntity entity = rsp.getEntity();

            if (entity != null) {
                //System.out.println(EntityUtils.toString(entity));
                //System.out.println(EntityUtils.toString(entity, "utf-8"));
                res = EntityUtils.toString(entity, encode);
            }

        } catch (IOException | ParseException e) {

        }

        return res;
    }

    /*
     * Загружаем файл
     */
    public static void loadFile(String url, String path) throws IOException {
        System.out.println("Connecting...");
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);

        InputStream input = null;
        OutputStream output = null;
        byte[] buffer = new byte[1024];

        try {
            System.out.println("Downloading file...");
            input = response.getEntity().getContent();
            output = new FileOutputStream(path);
            for (int length; (length = input.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
            System.out.println("File successfully downloaded!");
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException logOrIgnore) {
                    System.out.println(logOrIgnore.toString());
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException logOrIgnore) {
                    System.out.println(logOrIgnore.toString());
                }
            }
        }
    }
}
