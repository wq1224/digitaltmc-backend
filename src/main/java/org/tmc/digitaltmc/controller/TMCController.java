package org.tmc.digitaltmc.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tmc.digitaltmc.LoginUtils;
import org.tmc.digitaltmc.modal.User;
import org.tmc.digitaltmc.modal.WeChatSession;

@RestController
public class TMCController {

    static private String[] roles = {"TMD"};
    static private HashMap<String,User> booking_info = new HashMap<String,User>();
    static {
        for (String role : roles) {
            booking_info.put(role, null);
        }
    }

    @RequestMapping(value = "/auth")
	public String auth(HttpServletRequest request,HttpServletResponse response, @RequestParam(value = "code", required = true) String code) {
        WeChatSession wechatInfo = LoginUtils.login(code);
        HttpSession session = request.getSession();
        session.setAttribute("openid", wechatInfo.getOpenid());
        session.setAttribute("session_key", wechatInfo.getSession_key());
        System.out.println("openId: " + wechatInfo.getOpenid());
        System.out.println("session_key: " + wechatInfo.getSession_key());
        String sessionId = request.getSession().getId();
		return sessionId;
    }

    @RequestMapping("/book_info")
	public String getBookInfo() {
		return booking_info.toString();
    }

    // @RequestMapping(value = "/booking", method = {RequestMethod.POST})
    // public String joinDebtor(@RequestParam(value = "userId", required = true) Long userId,
    //                                          @RequestParam(value = "enterpriseId", required = true) Long enterpriseId) {
 
    //     debtorEnterpriseService.joinDebtorEnterprise(new RequestContext(), userId, enterpriseId);
 
    //     return new ResponseEntity(new HttpResponse<>(), HttpStatus.OK);

    @RequestMapping(value = "/book")
    public String book(HttpServletRequest request,HttpServletResponse response, @RequestParam(value = "role_name", required = true) String roleName, @RequestParam(value = "user_name", required = true) String userName) {
        HttpSession session = request.getSession();
        String openid = (String)session.getAttribute("openid");
        String session_key = (String)session.getAttribute("session_key");
        if (booking_info.get(roleName) == null){
            User u = new User();
            u.setOpenid(openid);
            u.setName(userName);
            booking_info.replace(roleName, u);
            return "success";
        }else{
            return "booked";
        }
    }

    @RequestMapping(value = "/unbook")
    public String unbook(HttpServletRequest request,HttpServletResponse response, @RequestParam(value = "role_name", required = true) String roleName, @RequestParam(value = "user_name", required = true) String userName) {
        HttpSession session = request.getSession();
        String openid = (String)session.getAttribute("openid");
        String session_key = (String)session.getAttribute("session_key");
        if (booking_info.get(roleName) != null && (booking_info.get(roleName).getOpenid() == openid)){
            booking_info.replace(roleName, null);
            return "success";
        }else{
            return "unbook";
        }
    }

}

