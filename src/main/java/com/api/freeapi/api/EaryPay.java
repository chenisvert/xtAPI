package com.api.freeapi.api;

import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/***
 * 易支付
 * @Author chen
 * @Date  14:13
 * @Param
 * @Return
 * @Since version-11

 */
public class EaryPay {

    public String pay(String type,String outTradeNo,String returnUrl,String name,String money){


        String url = "https://ypay.2sha.cn";//支付地址
        String pid = "1114";//商户id
//        String type = "alipay";//支付类型
//        String outTradeNo = "20160806151343349";//商户单号
        String notifyUrl = "http://www.pay.com/notify_url.php";//异步通知
//        String returnUrl = "http://www.pay.com/notify_url.php";//跳转地址
//        name = "测试商品";//商品名
//        money = "1.00";//价格
        String signType = "MD5";//签名类型
        String key = "bVoJ4VM9Q88uUvvIIyqi8UMVm4s0o0ob";//商户密钥

        //参数存入 map
        Map<String,String> sign = new HashMap<>();
        sign.put("pid",pid);
        sign.put("type",type);
        sign.put("out_trade_no",outTradeNo);
        sign.put("notify_url",notifyUrl);
        sign.put("return_url",returnUrl);
        sign.put("name",name);
        sign.put("money",money);

        //根据key升序排序
        sign = sortByKey(sign);

        String signStr = "";

        //遍历map 转成字符串
        for(Map.Entry<String,String> m :sign.entrySet()){
            signStr += m.getKey() + "=" +m.getValue()+"&";
        }

        //去掉最后一个 &
        signStr = signStr.substring(0,signStr.length()-1);

        //最后拼接上KEY
        signStr += key;

        //转为MD5
        signStr = DigestUtils.md5DigestAsHex(signStr.getBytes());

        sign.put("sign_type",signType);
        sign.put("sign",signStr);
        String html = "<form id='paying' action='"+url+"/submit.php' method='post'>";
        for(Map.Entry<String,String> m :sign.entrySet()){
             html =  html + "<input type='hidden' name='"+m.getKey()+"' value='"+m.getValue()+"'/>";
        }
        html =  html +"<input type='submit' value='正在跳转'></form>";
        html = html+"<script>document.forms['paying'].submit();</script>";
        return html;
    }



    public static <K extends Comparable<? super K>, V > Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();

        map.entrySet().stream()
                .sorted(Map.Entry.<K, V>comparingByKey()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }


}