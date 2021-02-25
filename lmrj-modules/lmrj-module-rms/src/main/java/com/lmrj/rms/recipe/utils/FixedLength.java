package com.lmrj.rms.recipe.utils;

public class FixedLength {

    public static String toFixedLengthString(String str, Integer length) {
        if (str.length() < length) {
            StringBuilder strBuilder = new StringBuilder(str);
            for (int i = strBuilder.length(); i < length; i++) {
                strBuilder.append(" ");
            }
            str = strBuilder.toString();
        }
        return str;
    }
}
