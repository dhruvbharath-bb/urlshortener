package com.example.urlshortener.util;

public class ShortCodeEncoder{
    private static final String BASE62_ALPHABET= "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    //Hardcoded value for encoding

    public static String encodeId(long inputNumber){
        if (inputNumber <0){
            throw new IllegalArgumentException(
                    "Base62 encoding requires a positive number (value > 0)"
            );
        } else if (inputNumber==0) {
            return String.valueOf(BASE62_ALPHABET.charAt(0));
        }
        StringBuilder stringBuilder = new StringBuilder();
        while(inputNumber>0) {
            int remainder = (int)(inputNumber % 62);
            inputNumber = inputNumber / 62;
            stringBuilder.append(BASE62_ALPHABET.charAt(remainder));
        }
        return stringBuilder.reverse().toString();
    }
}