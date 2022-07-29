package com.magdyradwan.my_file_sender.helpers;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

public class Utility {

    private Utility() {}

    public static String convertByteArrayToString(byte[] arr) {
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < arr.length; i++){
            if(i == arr.length - 1) {
                str.append(arr[i]);
            }
            else {
                str.append(arr[i]).append(",");
            }
        }

        return str.toString();
    }

    public static String convertFromByteArrToBase64(byte[] data) {
        return new String(Base64.encode(data, Base64.NO_WRAP));
    }
}
