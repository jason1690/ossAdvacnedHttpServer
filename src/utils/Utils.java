package utils;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

public class Utils {

    public static final String alphabet="abcdefghijklmnopqrstuvwxyz";
    public static final char[] fullAlphabet=(alphabet+alphabet.toUpperCase()+"1234567890").toCharArray();

    public static final String WEBSOCKET_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

    public static byte[] trimByteArray(byte[] data,int length){
        byte[] retv = new byte[length];
        for(int i=0;i<length;i++)
            retv[i]=data[i];
        return retv;
    }

    public static String convertWSKey(String webSocketKey){
        try{
            MessageDigest crypt = MessageDigest.getInstance("sha-1");
            crypt.reset();
            crypt.update((webSocketKey+WEBSOCKET_GUID).getBytes());
            return new String (Base64.getEncoder().encode(crypt.digest()));
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isValidFile(String path){
        File file = new File(path);
        if(!file.exists())
            return false;
        if(file.isDirectory())
            return false;
        return true;
    }

    public static String[] trimStringArray (String[] data,int start,int end){
        if(start>end)
            return new String[]{""};
        String[] newArray = new String[end-start];
        int newi=0;
        for(int i=start;i<end;i++){
            newArray[newi]=data[i];
            newi++;
        }
        return newArray;
    }

    public static String genRandomString(int len){
        char[] chars = new char[len];
        Random rand = new Random();
        for(int i=0;i<chars.length;i++)
            chars[i]=fullAlphabet[rand.nextInt(fullAlphabet.length)];
        return new String(chars);
    }
}
