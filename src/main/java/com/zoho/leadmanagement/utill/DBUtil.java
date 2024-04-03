package com.zoho.leadmanagement.utill;

import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class DBUtil
{
    public static Connection getConnection() throws IOException, SQLException
    {
           ClassPathResource appPropertiesResource =  new ClassPathResource("application.properties");
           Properties prop = new Properties();
           prop.load(appPropertiesResource.getInputStream());
           String user = prop.getProperty("spring.datasource.username");
           String password = "";
           String databaseName = prop.getProperty("spring.datasource.databaseName");
           String connectionUrl = prop.getProperty("spring.datasource.url")+"/"+databaseName;

           DriverManager.registerDriver(new com.mysql.jdbc.Driver());

         Connection connection = DriverManager.getConnection(connectionUrl, user, password);

         return connection;
    }

    public static void insertData(String table, List<String> keys, List<?> values) throws Exception
    {
        StringBuilder query = new StringBuilder();
        query.append("insert into ").append(table).append("(");

        int len = keys.size();

        for (int i = 0; i < len; i++)
        {
            query.append(keys.get(i));
            if (i != len - 1)
                query.append(",");
            else
                query.append(")values(");
        }

        for (int i = 0; i < len; i++)
        {
            query.append("'").append(values.get(i)).append("'");
            if (i != len - 1)
                query.append(",");
            else
                query.append(")");
        }

        try(Connection connection = getConnection(); Statement statement = connection.createStatement();)
        {
            statement.executeUpdate(query.toString());
        }
    }

    public static JSONObject getData(String table, String key, Object value, List<String> fields) throws Exception
    {
        ResultSet resultSet = null;
        JSONObject response = new JSONObject();
        StringBuilder query = new StringBuilder();
        query.append("select * from " + table + " where " + key + "='" + value.toString() + "'");

        try(Connection connection = getConnection(); Statement statement = connection.createStatement();)
        {
            resultSet = statement.executeQuery(query.toString());

            while (resultSet.next())
            {
                for (int i = 0; i < fields.size(); i++)
                {
                    response.put(fields.get(i), resultSet.getString(fields.get(i)));
                }
            }
        }
        return response;
    }

    public static void deleteData(String table, String key,String value) throws Exception
    {
        try(Connection connection = getConnection(); Statement statement = connection.createStatement();)
        {
            String query = "Delete from " + table + " where "+ key + "='" + value + "'";
            statement.executeUpdate(query);
        }
    }



}
