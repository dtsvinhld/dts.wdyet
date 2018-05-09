package vn.com.dtsgroup.wdyet.Class;


import android.util.Base64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Verdant on 3/8/2018.
 */

public class Module {

    public static int LOGINACTIVITYCODE = 101;
    public static int REGISTERACTIVITYCODE = 102;
    public static int ADDMENUACTIVITYCODE = 103;
    public static int CHANGEFNACTIVITYCODE = 104;
    public static int RATINGACTIVITYCODE = 105;

    public static String MAILLINK = "https://dtsvinhldwdye.000webhostapp.com/sendmail/send.php?";
//    public static String DOMAINNAME = "http://203.162.166.162:8080/api/wdyet/";
    public static String DOMAINNAME = "https://dtsvinhldwdye.000webhostapp.com/wdyet/";
    public static String IMAGELINK = "http://vivuproductions.96.lt/images/";

    public static boolean isConnectedInternet(){
        return false;
    }

    public static boolean isEmail(String string){
        String emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern regex = Pattern.compile(emailPattern);
        Matcher matcher = regex.matcher(string);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    public static String XoaKhoangTrang(String string){
        return string.trim().replaceAll("\\s+", " ");
    }

    public static String DanhTuRieng(String string){
        string = XoaKhoangTrang(string);
        String[] list = string.split(" ");
        string = "";
        for(int i=0; i<list.length; i++){
            string += String.valueOf(list[i].charAt(0)).toUpperCase() + list[i].substring(1).toLowerCase();
        }
        return string;
    }

    public static String removeAccent(String s) {
        s = s.replaceAll("đ", "d");
        s = s.replaceAll("Đ", "D");
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").toLowerCase();
    }

    public static String ContentURL(String stringURL){
        StringBuilder content = new StringBuilder();
        try    {
            URL url = new URL(stringURL);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), Charset.forName("UTF-8")));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
        return content.toString();
    }

//    public static String ContentURL(Context context, String stringURL, final ArrayList<String> keys, final ArrayList<String> values){
//        final String[] string = {""};
//
//        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(context);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, stringURL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        string[0] = response;
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        string[0] = error.getMessage();
//                    }
//                }
//        ){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//                for(int i=0; i<keys.size(); i++){
//                    params.put(keys.get(i), values.get(i));
//                }
//                return params;
//            }
//        };
//
//        requestQueue.add(stringRequest);
//        return string[0];
//    }

    public static String PasswordEncodeMD5(Object object){
        String string = String.valueOf(object);
        string += "DTSVinhLD@2018";
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(string.getBytes());
            BigInteger bigInteger = new BigInteger(1,digest.digest());
            return bigInteger.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String EncodeBase64(String string){
        return Base64.encodeToString(string.getBytes(), 1).trim();
    }

}
