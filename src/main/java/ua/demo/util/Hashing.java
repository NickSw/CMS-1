package ua.demo.util;

import java.security.MessageDigest;

/**
 * auxilary class with single static method getHash that encrypts String to 64 hex format sha-256 string
 *
 * Created by Sergey on 08.06.2015.
 */
public class Hashing {
    //receive string and return 64 hex format sha-256 string
    public static String getHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}