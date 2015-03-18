/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jeene.chwebm.worker.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Some File Description ...
 *
 *
 * @category
 * @author Ivan V. Scherbakov <antilamer87@mail.ru,ivc_ShherbakovIV@esrr.rzd>
 * @changedby $Author$
 * @version SVN: $Id$
 * @revision SVN: $Revision$
 * @link       $HeadURL$
 * @date $Date$
 * @copyright (c) 2009-2012 by
 * @see
 * @since
 */
public class HashUtils {

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String MD5File(File f) throws FileNotFoundException, NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        //File f = new File("c:\\myfile.txt");
        InputStream is = new FileInputStream(f);
        String output = null;
        byte[] buffer = new byte[8192];
        int read = 0;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            String result = "";

            for (int i = 0; i < md5sum.length; i++) {
                result += Integer.toString((md5sum[i] & 0xff) + 0x100, 16).substring(1);
            }            
            output=result;
            System.out.println("MD5: " + output);
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
            }
        }
        return output;
    }
}
