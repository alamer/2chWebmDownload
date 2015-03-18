/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package ru.jeene.chwebm.worker.utils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.PatternSyntaxException;

/**
 * Помощник для работы с файлами
 *
 * @author scherb
 */
public class FileUtils {

    /**
     * Проверяем путь, если папки не существуют то создаем
     *
     * @param path Путь
     * @return успех\неудача
     */
    public static boolean CheckDir(String path) {
        boolean res = false;
        File f = new File(path);
        if (!f.exists()) {
            res = f.mkdirs();
        }
        return res;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String children1 : children) {
                boolean success = deleteDir(new File(dir, children1));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it 
        return dir.delete();
    }

    /**
     * Получаем список вложенных папок
     *
     * @param path Путь
     * @return список вложенных папок
     */
    public static ArrayList<String> getSubDirs(String path) {
        File file = new File(path);
        ArrayList<String> res = new ArrayList<>();
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        if (directories.length > 0) {
            res = new ArrayList<>(Arrays.asList(directories));
        }
        return res;
    }

    public static void clearDir(String dir) {
        String list[] = getFileList(dir);
        for (String list1 : list) {
            deleteFile(dir + list1);
        }
    }
    /**
     * Размер блока.
     */
    protected static final int MINBLOCKSIZE = 4096;

    /**
     * Копирование файла по блокам.
     *
     * @param source Исходный файл
     * @param destination Файл назначения
     * @param buffersize размер буффера
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static synchronized void copyFileBuffered(String source, String destination, int buffersize) throws FileNotFoundException, IOException {
        OutputStream out;
        try (InputStream in = new FileInputStream(source)) {
            out = new FileOutputStream(destination);
            if (buffersize <= 0) {
                buffersize = MINBLOCKSIZE;
            }   int len = 0, count = 0;
        byte[] b = new byte[buffersize];
            while ((count = in.read(b)) != -1) {
                out.write(b, 0, count);
                len += count;
            }
        }
        out.flush();
        out.close();
    }

    /**
     * Копирование потоков.
     *
     * @param in Входящий поток
     * @param out Исходящий поток
     * @throws IOException
     */
    public static synchronized void copyFile(InputStream in, OutputStream out) throws IOException {
        int len = 0, b = 0;
        while ((b = in.read()) != -1) {
            out.write(b);
            len += b;
        }
        in.close();
        out.flush();
        out.close();
    }

    /**
     * Простое копирование файлов.
     *
     * @param source Файл источник
     * @param destination Файл назначения
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static synchronized void copyFile(String source, String destination) throws FileNotFoundException, IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destination));
        copyFile(in, out);
    }

    /**
     * Копируем все файлы из одной папки в другую
     *
     * @param from_dir Папка источник
     * @param to_date_dir Папка назначения
     */
    public static void CopyFileInDir(String from_dir, String to_date_dir) {
        // Вытаскиваем список файлов в папке
        File f = new File(from_dir);
        String list[] = FileUtils.getFileList(from_dir);
        // Каждый файл копируем и удаляем в исходной
        File f_out = new File(to_date_dir);
        // Если выходной папки нет, то создаем
        if (!f_out.exists()) {
            f_out.mkdir();
        }
        for (String list1 : list) {
            // Копируем
            boolean exists = (new File(from_dir + '/' + list1)).exists();
            if (new File(from_dir + '/' + list1).isDirectory()) {
                CopyFileInDir(from_dir + '/' + list1, to_date_dir + '/' + list1);
            } else {
                if (exists) {
                    try {
                        copyFile(from_dir + "/" + list1, to_date_dir + "/" + list1);
                    }catch (IOException e) {
                    }
                }
                // Проверяем
                exists = (new File(to_date_dir + "/" + list1).exists());
                if (exists) {
                    // Удаляем
                    File f_del = new File(from_dir + "/" + list1);
                    f_del.delete();
                }
            }
        }

    }

    /**
     * Копирование через каналы
     *
     * @param src Файл источник
     * @param dest Файл назначения
     * @throws IOException
     */
    public static void copyFile(File src, File dest) throws IOException {

        FileOutputStream dest2;
        try (FileInputStream src2 = new FileInputStream(src)) {
            dest2 = new FileOutputStream(dest);
            FileChannel destChannel;
            try (FileChannel srcChannel = src2.getChannel()) {
                destChannel = dest2.getChannel();
                srcChannel.transferTo(0, srcChannel.size(), destChannel);
            }
            destChannel.close();
        }
        dest2.close();
    }

    /**
     * Вытаскиваем список файлов в папке
     *
     * @param dir папка
     * @return список файлов
     */
    public static String[] getFileList(String dir) {

        File f = new File(dir);
        String list[] = f.list();
        return list;
    }

    /**
     * Файл с последней датой изменения в папке
     *
     * @param dir папка в которой ищем
     * @param reg не используется
     * @return Последний изменненный файл в директории
     */
    public static String getLastFileInDir(String dir, String reg) {
        String res = "";
        String list[] = getFileList(dir);
        long lm = 0;
        for (String list1 : list) {
            File f = new File(dir + '/' + list1);
            if (f.lastModified() > lm) {
                res = list1;
            }
        }
        return res;
    }

    /**
     * Считываем файл в строку- подходит для считывания больших файлов
     *
     * @param filename Путь до файла
     * @param encode Кодировка
     * @return текст файла
     */
    public static String readPage(String filename, String encode) {
        String str = "";
        StringBuilder str_b = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(filename);
            InputStreamReader isr = new InputStreamReader(
                    new FileInputStream(filename), encode);
            try (BufferedReader in = new BufferedReader(isr)) {
                while (((str = in.readLine()) != null)) {
                    str_b.append(str).append("\r\n");
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return str_b.toString();
    }

    /**
     * Считывает файл в строку в кодировке вин-1251
     *
     * @param filename имя файла
     * @return текст файйла
     */
    public static String readPage(String filename) {
        String encode = "windows-1251";
        return readPage(filename, encode);

    }

    private static String ByteToHexString(byte hash[]) {
        StringBuilder buf = new StringBuilder(hash.length * 2);
        int i;
        for (i = 0; i < hash.length; i++) {
            if ((hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(hash[i] & 0xff, 16));
        }
        return buf.toString();
    }

    private static String digest(String input, String algorithm) throws NoSuchAlgorithmException {
        // Calculate Hash from input
        MessageDigest md = MessageDigest.getInstance(algorithm);
        // Convert byte array hash to string
        return ByteToHexString(md.digest(input.getBytes()));
    }

    /**
     * Выдаем MD5 от строки
     *
     * @param input Строка
     * @return MD5
     * @throws NoSuchAlgorithmException
     */
    public static String MD5(String input) throws NoSuchAlgorithmException {
        return digest(input, "MD5");
    }

    public static void writeFile(String stmp, String mess) throws FileNotFoundException, IOException {

        try (RandomAccessFile randomaccessfile = new RandomAccessFile(stmp, "rw")) {
            randomaccessfile.writeBytes(mess);
        }

    }

    public static boolean writeByteArrayToFile(byte[] inArr, String destination) throws FileNotFoundException, IOException {
        boolean res = false;
        if (inArr != null) {
            try (OutputStream out = new FileOutputStream(destination)) {
                out.write(inArr);
                out.flush();
            }
            res = true;
        }
        return res;
    }

    public static void deleteFile(String fname) {
        File f = new File(fname);
        if (f.exists()) {
            f.delete();
        }
    }

    public static boolean checkMask(String mask, String str) {
        boolean res = false;
        try {
            res = str.matches(mask);
        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
            System.out.println(ex.toString());
        }
        return res;
    }

    public static void copyFolder(File src, File dest)
            throws IOException {

        if (src.isDirectory()) {

            //if directory not exists, create it
            if (!dest.exists()) {
                dest.mkdir();
                System.out.println("Directory copied from "
                        + src + "  to " + dest);
            }

            //list all the directory contents
            String files[] = src.list();

            for (String file : files) {
                //construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                //recursive copy
                copyFolder(srcFile, destFile);
            }

        } else {
            OutputStream out;
            try ( //if file, then copy it
            //Use bytes stream to support all file types
                    InputStream in = new FileInputStream(src)) {
                out = new FileOutputStream(dest);
                if (dest.exists()) {
                    dest.delete();
                }   byte[] buffer = new byte[1024];
            int length;
                //copy the file content in bytes
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
            out.close();
            System.out.println("File copied from " + src + " to " + dest);
        }
    }

    /*
     *  Список всех файлов в папке рекурсивный
     */
    public static List<File> addFiles(List<File> files, File dir) {
        if (files == null) {
            files = new LinkedList<>();
        }

        if (!dir.isDirectory()) {
            files.add(dir);
            return files;
        }

        for (File file : dir.listFiles()) {
            addFiles(files, file);
        }
        return files;
    }

    /*
     *  Список всех файлов в папке (через очередь)
     */
    public static List<File> addFilesQueue(File dir) {
        List<File> allFiles = new ArrayList<>();
        Queue<File> dirs = new LinkedList<>();
        dirs.add(dir);
        while (!dirs.isEmpty()) {
            for (File f : dirs.poll().listFiles()) {
                if (f.isDirectory()) {
                    dirs.add(f);
                } else if (f.isFile()) {
                    allFiles.add(f);
                }
            }
        }
        return allFiles;
    }
}
