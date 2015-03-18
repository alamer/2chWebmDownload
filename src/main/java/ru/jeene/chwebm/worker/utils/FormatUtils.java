/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jeene.chwebm.worker.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author scherb
 */
public class FormatUtils {

    /**
     *
     * @param q
     * @return
     */
    public static String FormatFloat_(float q) {
        String res = "";
        NumberFormat formatter = new DecimalFormat("#.##");
        StringUtils sh = new StringUtils();
        res = formatter.format(q);
        return StringUtils.replaceAll(res, ".", ",");
    }

    public static String FormatFloat_(float q, String f) {
        NumberFormat formatter = new DecimalFormat(f);
        String res = formatter.format(q);
        return StringUtils.replaceAll(res, ".", ",");
    }

    /*
     * Убирает секунды и миллисекунды из строки
     */
    /**
     *
     * @param in
     * @return
     */
    public static String FormatDateString(String in) {
        String res = "";
        if (in != null) {
            try {
                Pattern regex = Pattern.compile("(\\d{4})(?:-)(\\d{2})(?:-)(\\d{2})(?: )(\\d{2})(?::)(\\d{2})", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                Matcher regexMatcher = regex.matcher(in);
                while (regexMatcher.find()) {
                        // matched text: regexMatcher.group()
                    // match start: regexMatcher.start()
                    res = regexMatcher.group(1) + "-" + regexMatcher.group(2) + "-" + regexMatcher.group(3) + " " + regexMatcher.group(4) + ":" + regexMatcher.group(5);
                    // match end: regexMatcher.end()
                }
            } catch (PatternSyntaxException ex) {
                // Syntax error in the regular expression
            }
        }
        return res;
    }

    /*
     * Округление double до двух знаков после запятой
     * (обрезание)
     */
    /**
     *
     * @param q
     * @return
     */
    public static String FormatDoubleD(double q) {
        String res = "";
        NumberFormat formatter = new DecimalFormat("#.##");
        res = StringUtils.replaceAll(formatter.format(q), ".", ",");
        //return Double.valueOf(sh.replaceAll(res, ",", "."));
        return res;
    }

    /*
     * Округление double до двух знаков после запятой
     * (обрезание)
     */
    /**
     *
     * @param q
     * @return
     */
    public static String FormatDoubleDot(double q) {
        String res = "";
        NumberFormat formatter = new DecimalFormat("#");
        res = StringUtils.replaceAll(formatter.format(q), ",", ".");
        //return Double.valueOf(sh.replaceAll(res, ",", "."));
        return res;
    }

    /**
     * Округление double до precise знаков после запятой
     *
     * @param d
     * @param precise
     * @return
     */
    public static double roundResult(double d, int precise) {

        precise = 10 ^ precise;
        d = d * precise;
        int i = (int) Math.round(d);
        return (double) i / precise;

    }

    /**
     * Округление double до precise знаков после запятой
     *
     * @param d
     * @param precise
     * @return
     */
    public static double roundActual(double d, int precise) {

        BigDecimal x = new BigDecimal(d);
        x = x.setScale(precise, BigDecimal.ROUND_HALF_UP);
        return x.doubleValue();

    }

}
