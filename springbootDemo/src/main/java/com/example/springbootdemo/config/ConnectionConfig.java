package com.example.springbootdemo.config;

import connection.DBConnection;
import connection.MySQLConnection;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
public class ConnectionConfig {

    public static DBConnection dbConnection = new MySQLConnection("localhost", 3306,
            "root", "nhihuynh211", "schema");

    @PostConstruct
    public void openConnection(){
        dbConnection.open();
    }

    @PreDestroy
    public void closeConnection(){
        dbConnection.close();
    }
}
