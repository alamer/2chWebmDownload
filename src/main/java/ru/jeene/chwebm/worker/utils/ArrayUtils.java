/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jeene.chwebm.worker.utils;

/**
 *
 * @author ivc_ShherbakovIV
 */
public class ArrayUtils {

    /**
     * Транспонирование матрицы
     * @param a - матрица
     * @param d1 
     * @param d2
     * @return
     */
    public static String[][] transpondMatrix(String[][] a,int d1,int d2) {
        String[][] res = new String[d2][d1];
        for (int i = 0; i < d1; ++i) {
            for (int j = 0; j < d2; ++j) {
                res[j][i] = a[i][j];
            }
        }
        return res;
    }
}
