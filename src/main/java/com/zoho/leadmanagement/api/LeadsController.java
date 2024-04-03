package com.zoho.leadmanagement.api;

import com.zoho.crm.api.util.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/leads")
public class LeadsController

{
    @Autowired
    private LeadsService service;
    @GetMapping
    public Model getLeads(@RequestParam(defaultValue = "1") Integer page, @RequestParam(name = "per_page" ,defaultValue = "200") Integer perPage) throws Exception
    {
        return service.getLeads(page, perPage);
    }
}
