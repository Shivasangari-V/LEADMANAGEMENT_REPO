package com.zoho.leadmanagement.api;

import com.zoho.crm.api.ParameterMap;
import com.zoho.crm.api.record.RecordOperations;
import com.zoho.crm.api.record.ResponseHandler;
import com.zoho.crm.api.util.APIResponse;
import com.zoho.crm.api.util.Model;
import org.springframework.stereotype.Service;

@Service
public class LeadsService
{
    public Model getLeads(Integer page, Integer perPage) throws Exception
    {
        RecordOperations recordOperations = new RecordOperations("Leads");

        ParameterMap paramInstance = new ParameterMap();
        paramInstance.add(RecordOperations.GetRecordsParam.PAGE, page);
        paramInstance.add(RecordOperations.GetRecordsParam.PER_PAGE, perPage);
        paramInstance.add(RecordOperations.GetRecordsParam.FIELDS, String.join(",", "Email", "Company", "Last_Name"));

        APIResponse<ResponseHandler> response = recordOperations.getRecords(paramInstance,null);
        return response.getModel();
    }
}
