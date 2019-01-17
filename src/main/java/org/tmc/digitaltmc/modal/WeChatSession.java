package org.tmc.digitaltmc.modal;
import lombok.Data;

@Data public class WeChatSession{

    String openid;
    String session_key;
    String unionid;
    String errcode;
    String errmsg;
}