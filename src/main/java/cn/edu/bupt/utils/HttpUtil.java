package cn.edu.bupt.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import okhttp3.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/12/23.
 */
public class HttpUtil {

    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String  tockenurl = "http://10.108.217.227:8080/api/auth/login";


    public static String sendPostToThingsboard(String url, Map<String,String> headers, JsonObject requestBody,HttpSession session) throws Exception{

        RequestBody body = RequestBody.create(JSON, requestBody.toString());
        Request.Builder buider = new Request.Builder()
                .url(url)
                .post(body);

        String tocken = (String)session.getAttribute("token");
        buider.header("X-Authorization","Bearer "+tocken);

        if(headers!=null){
            for(Map.Entry<String,String> entry:headers.entrySet()){
                buider.header(entry.getKey(),entry.getValue());
            }
        }
        Request request = buider.build();
        Response response = httpClient.newCall(request).execute();
        if(response.isSuccessful()){
            return response.body().string();
        }else if(response.code() == 401){
            return "";
        }
        return "";
    }

    public static String sendGetToThingsboard(String url, Map<String,String> headers, HttpSession session) throws Exception{

        Request.Builder buider = new Request.Builder()
                .url(url)
                .get() ;

        String tocken = (String)session.getAttribute("token");
        buider.header("X-Authorization","Bearer "+tocken);

        if(headers!=null){
            for(Map.Entry<String,String> entry:headers.entrySet()){
                buider.header(entry.getKey(),entry.getValue());
            }
        }
        Request request = buider.build();
        Response response = httpClient.newCall(request).execute();
        if(response.isSuccessful()){
            return response.body().string();
        }else if(response.code() == 401){
            return "";
        }
        return "";
    }

    public static boolean getAccessToken(HttpSession session){
//        JsonPrimitive username = (JsonPrimitive)session.getAttribute("username");
//        JsonPrimitive password = (JsonPrimitive)session.getAttribute("password");
//        if(username==null||password==null) return false;
        JsonObject json = new JsonObject();
//        json.addProperty("username",username.getAsString());
//        json.addProperty("password",password.getAsString());
        json.addProperty("username","tenant@thingsboard.org");
        json.addProperty("password","tenant");
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request.Builder buider = new Request.Builder()
                .url(tockenurl)
                .post(body);
        Request request = buider.build();
        try{
            // 第一次获取token
            Response response = execute(request);
            if(response.isSuccessful()){
                String res = response.body().string();
                JsonObject obj = new JsonParser().parse(res).getAsJsonObject();
                session.setAttribute("token",obj.get("token").getAsString());
                session.setAttribute("refreshToken",obj.get("refreshToken").getAsString());
                return true;
            }else{
                throw new Exception("the first fail!") ;
            }
        }catch (Exception e){
            // 第二次获取token
            try {
                Response response = execute(request);
                String res = response.body().string();
                JsonObject obj = new JsonParser().parse(res).getAsJsonObject();
                session.setAttribute("token",obj.get("token").getAsString());
                session.setAttribute("refreshToken",obj.get("refreshToken").getAsString());
                return true ;
            } catch (IOException e1) {
            }
            return false;
        }
    }

    /**
     * 向Sever发送http请求，有无fileStr区分POST和GET
     * @param url
     * @param headers
     * @param fileStr
     * @return
     * @throws IOException
     */
    public static String getStringFromServer(String url, Map<String, String> headers, String fileStr) throws IOException {

        Request.Builder requestBuilders = new Request.Builder()
                .url(url);
        for(Map.Entry<String, String> item : headers.entrySet()) {
            requestBuilders = requestBuilders.header(item.getKey(), item.getValue());
        }

        if (fileStr == null) {
            requestBuilders = requestBuilders.get();
        } else {
            RequestBody body = RequestBody.create(JSON, fileStr);
            requestBuilders = requestBuilders.post(body);
        }
        Request request = requestBuilders.build();

        Response response = execute(request);
        if (response.isSuccessful()) {
            String string = response.body().string();
            return string ;
        } else {
            throw new IOException("Unexpected code " + response) ;
        }
    }

    /**
     * 同步方法
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute() ;
    }

    private static final OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();
}
