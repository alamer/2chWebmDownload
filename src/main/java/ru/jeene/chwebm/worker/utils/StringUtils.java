/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package ru.jeene.chwebm.worker.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 *
 * @author scherb
 */
public class StringUtils {

    /**
     *
     * @param source
     * @param toReplace
     * @param replacement
     * @return
     */
    public static String replaceAll(String source, String toReplace, String replacement) {
        if (source!=null)
        {
        int idx = source.lastIndexOf(toReplace);
        if (idx != -1) {
            StringBuilder ret = new StringBuilder(source);
            ret.replace(idx, idx + toReplace.length(), replacement);
            while ((idx = source.lastIndexOf(toReplace, idx - 1)) != -1) {
                ret.replace(idx, idx + toReplace.length(), replacement);
            }
            source = ret.toString();
        }
        }
        return source;
    }

    /**
     * 
     * @param a
     * @return
     */
    public static String getStringFromNumber(int a) {
        if (a < 10 && a >= 0) {
            return "0" + a;
        } else {
            return a + "";
        }
    }

    /**
     * 
     * @param a
     * @return
     */
    public static String getStringFromNumber(long a) {
        if (a < 10 && a >= 0) {
            return "0" + a;
        } else {
            return a + "";
        }
    }


    /**
     * 
     * @param in
     * @return
     */
    public static ArrayList<String> parseStr(String in) {
        ArrayList res = new ArrayList<>();
        res.clear();
        StringTokenizer stringtokenizer = new StringTokenizer(in, ",");
        while (stringtokenizer.hasMoreTokens()) {
            String to = stringtokenizer.nextToken();
            res.add(to);
        }
        return res;
    }

    public static InputStream fromString(String str, String encoding) throws UnsupportedEncodingException {
            byte[] bytes = str.getBytes(encoding);
            return new ByteArrayInputStream(bytes);

    }
}
