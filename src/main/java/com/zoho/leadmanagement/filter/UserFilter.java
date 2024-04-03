package com.zoho.leadmanagement.filter;

import com.zoho.api.authenticator.Token;
import com.zoho.api.authenticator.store.DBStore;
import com.zoho.api.authenticator.store.TokenStore;
import com.zoho.crm.api.Initializer;
import com.zoho.crm.api.dc.USDataCenter;
import com.zoho.leadmanagement.utill.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.core.annotation.Order;
import java.io.IOException;
import java.util.List;

@Order(1)
public class UserFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String sessionId = httpRequest.getSession().getId();
        String tokenId = null;

        JSONObject sessionJO = null;

        try
        {
            sessionJO = DBUtil.getData("sessions", "id", sessionId, List.of("token_id"));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        if(sessionJO == null || sessionJO.length() == 0 || sessionJO.isNull("token_id"))
        {
            httpResponse.sendRedirect("/login.html");
        }
        else
        {
            try
            {
                tokenId = sessionJO.optString("token_id");

                TokenStore store =  new DBStore.Builder().build();
                Token token = store.findTokenById(tokenId);

                Initializer.Builder init =   new Initializer.Builder()
                        .environment(USDataCenter.PRODUCTION)
                        .store(new DBStore.Builder().build())
                        .token(token)
                        .resourcePath("/Users/shiva-17404/MyGitRepo/leadmanagement/.sdk");
                init.initialize();
                init.switchUser();

                filterChain.doFilter(request,response);

            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }


    }
}
