package com.example.demo.util;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/*
 *@author yaqiwe
 *@data 2019-05-28 13:12
 *@notes 发起网络请求
 **/
public class HttpClientUtil {
    public static String doGet(String url, Map<String, String> map) {
        //创建httpClient对象
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String re = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            if (map != null) {
                for (String key : map.keySet())
                    builder.addParameter(key, map.get(key));
            }
            URI uri = builder.build();
            //创建HttpGet请求
            HttpGet get = new HttpGet(uri);
            //执行请求
            response = client.execute(get);
            //判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                re = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null)
                    response.close();
                if (client != null)
                    client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return re;
    }

    public static String doGet(String url){
        return doGet(url,null);
    }
}
