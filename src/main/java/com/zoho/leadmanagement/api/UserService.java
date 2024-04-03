package com.zoho.leadmanagement.api;

import com.zoho.crm.api.HeaderMap;
import com.zoho.crm.api.ParameterMap;
import com.zoho.crm.api.exception.SDKException;
import com.zoho.crm.api.users.ResponseHandler;
import com.zoho.crm.api.users.ResponseWrapper;
import com.zoho.crm.api.users.UsersOperations;
import com.zoho.crm.api.util.APIResponse;
import com.zoho.crm.api.util.Choice;
import org.springframework.stereotype.Service;


@Service
public class UserService
{
    public String getCurrentUserId() throws SDKException
    {
        UsersOperations usersOperations = new UsersOperations();
        ParameterMap paramInstance = new ParameterMap();
        paramInstance.add(UsersOperations.GetUsersParam.TYPE, new Choice<String>("CurrentUser"));

        APIResponse<ResponseHandler> response = usersOperations.getUsers(paramInstance, new HeaderMap());

        ResponseHandler responseHandler = response.getObject();
        ResponseWrapper responseWrapper = (ResponseWrapper) responseHandler;
        com.zoho.crm.api.users.Users user = responseWrapper.getUsers().get(0);

        if(user != null)
        {
            return user.getZuid().toString();
        }
        return null;
    }

}
