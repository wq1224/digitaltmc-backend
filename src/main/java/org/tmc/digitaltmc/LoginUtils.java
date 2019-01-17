package org.tmc.digitaltmc;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.tmc.digitaltmc.modal.WeChatSession;

import com.google.gson.Gson;
 
public class LoginUtils{
 
	 private static final long serialVersionUID = 1L;
	
	 private static final String APPID = "wxfdef7ebe2a985224";  
	 private static final String SECRET = "3ce63e12fcc44c8b73e54ccffd98e0bb";
     
    //  public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv){
    //     // 被加密的数据
    //     byte[] dataByte = Base64.getDecoder().decode(encryptedData);
    //     // 加密秘钥
    //     byte[] keyByte = Base64.decode(sessionKey);
    //     // 偏移量
    //     byte[] ivByte = Base64.decode(iv);

    //     try {
    //         // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
    //         int base = 16;
    //         if (keyByte.length % base != 0) {
    //             int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
    //             byte[] temp = new byte[groups * base];
    //             Arrays.fill(temp, (byte) 0);
    //             System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
    //             keyByte = temp;
    //         }
    //         // 初始化
    //         Security.addProvider(new BouncyCastleProvider());
    //         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
    //         SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
    //         AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
    //         parameters.init(new IvParameterSpec(ivByte));
    //         cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
    //         byte[] resultByte = cipher.doFinal(dataByte);
    //         if (null != resultByte && resultByte.length > 0) {
    //             String result = new String(resultByte, "UTF-8");
    //             return JSONObject.fromObject(result);
    //         }
    //     } catch (NoSuchAlgorithmException e) {
    //         e.printStackTrace();
    //     } catch (NoSuchPaddingException e) {
    //         e.printStackTrace();
    //     } catch (InvalidParameterSpecException e) {
    //         e.printStackTrace();
    //     } catch (IllegalBlockSizeException e) {
    //         e.printStackTrace();
    //     } catch (BadPaddingException e) {
    //         e.printStackTrace();
    //     } catch (UnsupportedEncodingException e) {
    //         e.printStackTrace();
    //     } catch (InvalidKeyException e) {
    //         e.printStackTrace();
    //     } catch (InvalidAlgorithmParameterException e) {
    //         e.printStackTrace();
    //     } catch (NoSuchProviderException e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }

    public static String getSha1(String str){
        if(StringUtils.isEmpty(str)){
            return null;
        }
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9', 'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        }catch (Exception e){
            return null;
        }
    }

	 //获取凭证校检接口
	 public static WeChatSession login(String code)  
	 {
		 //微信的接口
		 String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+APPID+
				 "&secret="+SECRET+"&js_code="+ code +"&grant_type=authorization_code";
		 RestTemplate restTemplate = new RestTemplate();
		 //进行网络请求,访问url接口
	     ResponseEntity<String>  responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);  
		 //根据返回值进行后续操作 
	     if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
            String sessionData = responseEntity.getBody();
            Gson gson = new Gson();
            //解析从微信服务器获得的openid和session_key;
            WeChatSession weChatSession = gson.fromJson(sessionData,WeChatSession.class);
            //获取用户的唯一标识
            String openid = weChatSession.getOpenid();
            //获取会话秘钥
            String session_key = weChatSession.getSession_key();
            return weChatSession;
         }else{
            return null;
         }		
	 }
}