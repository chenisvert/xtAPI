package com.api.freeapi.api;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

/***
 *
 * 实名认证接口调用(二要素)
 * @Author chen
 * @Date  14:57
 * @Param
 * @Return
 * @Since version-11

 */
@Slf4j
@Component
public class  Authentication{
    public Boolean check(String idCard,String name) throws IOException {
            log.info("Authentication.check 入参 idCard:{} name:{}",idCard,name);
            JSONObject resultJson = readJsonFromUrl("http://api.ojwyun.cn/index/sm_api?key=96c054ebf976a6e604888f82c5673832&idcard="+idCard+"&name="+name);
//            address = ((JSONObject) resultJson.get("data")).getString("music");
          Integer res = Integer.valueOf(resultJson.get("code").toString());

          log.info("实名认证 code:{}",res);
          if (res == 200){
            return true;
          }else {
              log.info("实名认证接口返回信息:{}",resultJson);
              return false;
          }
    }

    /**
     * 读取
     *
     * @param rd
     * @return
     * @throws IOException
     */
    private static String readAll(BufferedReader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * 创建链接
     *
     * @param url
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = JSONObject.parseObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}
