/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jeene.chwebm.worker.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Работа с архивами
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
public class ZipUtils {

    /*
     * распаковываем file в path
     */
    public static void unzipFile(String zipFile, String outputFolder) {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = null;
        ZipEntry ze = null;
        try {
            zis = new ZipInputStream(new FileInputStream(zipFile));
            ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);
                System.out.println("file unzip : " + newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                ze = zis.getNextEntry();
            }

        } catch (IOException ex) {
            Logger.getLogger(ZipUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                zis.closeEntry();
                zis.close();
            } catch (IOException ex) {
                Logger.getLogger(ZipUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
