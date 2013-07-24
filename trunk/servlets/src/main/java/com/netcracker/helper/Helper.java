package com.netcracker.helper;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

public class Helper {
    private static Helper instance;
    private static String DATASOURCE_NAME = "java:jboss/datasources/bookStoreDB";

    private Helper() {}

    public static Helper getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new Helper();
            return instance;
        }
    }

    public Context getContext() throws NamingException {
        Properties props = new Properties();

        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        props.put(Context.PROVIDER_URL, "remote://localhost:4447");

        props.put(Context.SECURITY_PRINCIPAL, "user");
        props.put(Context.SECURITY_CREDENTIALS, "pass");
        //props.put("jboss.naming.client.ejb.context", true);
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

        return new InitialContext(props);

    }

    /*public Connection getConnection() throws NamingException, SQLException {
        return getDataSource().getConnection();
    }

    public DataSource getDataSource() throws NamingException {
        return (DataSource) getContext().lookup(DATASOURCE_NAME);
    }*/
}
