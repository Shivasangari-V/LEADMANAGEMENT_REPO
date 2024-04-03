package com.zoho.leadmanagement;

import com.zoho.api.authenticator.OAuthToken;
import com.zoho.api.authenticator.store.DBStore;
import com.zoho.crm.api.Initializer;
import com.zoho.crm.api.SDKConfig;
import com.zoho.crm.api.dc.USDataCenter;
import com.zoho.leadmanagement.api.UserService;
import com.zoho.leadmanagement.utill.DBUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
public class HomeController
{
    @RequestMapping("/oauthredirect")
    public void signUpUser(HttpServletResponse response, HttpServletRequest request) throws Exception
    {
        String code = request.getParameter("code");
        OAuthToken userToken = new OAuthToken.Builder()
        .clientID("1000.C9E6WHLM0A6E0PVBT46POBWNHORBOP")
        .clientSecret("b54949b3681744f31575edef38866d04ea8d5dbe2c")
        .redirectURL("http://localhost:8001/oauthredirect")
        .grantToken(code)
        .build();

         SDKConfig sdkConfig = new SDKConfig.Builder()
                .autoRefreshFields(false)
                .pickListValidation(true)
                .build();

         new Initializer.Builder()
                .environment(USDataCenter.PRODUCTION)
                .store(new DBStore.Builder().build())
                .token(userToken)
                .SDKConfig(sdkConfig)
                .resourcePath("/Users/shiva-17404/MyGitRepo/leadmanagement/.sdk")
                .initialize();

         userToken.generateToken();
         String sessionId = request.getSession().getId();
         String currentUser = new UserService().getCurrentUserId();
        JSONObject sessionJO =  DBUtil.getData("sessions", "id", sessionId, List.of("id"));
         if(currentUser != null &&  (sessionJO == null || sessionJO.length() ==0))
         {
             DBUtil.insertData("sessions", List.of("id", "token_id", "user_id"), List.of(sessionId, userToken.getId(), currentUser));
         }
        Cookie cookie = new Cookie("sessionId",sessionId);
        response.addCookie(cookie);
        response.sendRedirect("index.html");
    }

    @RequestMapping("/logout")
    public void signOutUser(HttpServletResponse response, HttpServletRequest request) throws Exception
    {
        String currentUser = new UserService().getCurrentUserId();

        if(DBUtil.getData("sessions", "user_id", currentUser, List.of("id")) != null)
        {
            DBUtil.deleteData("sessions", "user_id" , currentUser);
        }

        response.sendRedirect("login.html");
    }

}
