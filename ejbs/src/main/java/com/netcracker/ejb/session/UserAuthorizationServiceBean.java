package com.netcracker.ejb.session;


import com.netcracker.exceptions.DataAccessException;
import com.netcracker.utils.DBUtils;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class UserAuthorizationServiceBean implements SessionBean{

    //------------------------------------------- Business methods ----------------------------------------

    public boolean isUserAuthorized(String nick, String pass, String status) throws DataAccessException {

        /**
         * users 	(	id int NOT NULL auto_increment,
         nick varchar(50) NOT NULL,
         password varchar(80) NOT NULL,
         status varchar(50) NOT NULL,
         CONSTRAINT usersId_pk PRIMARY KEY (id),
         CONSTRAINT users_uk UNIQUE KEY (nick)
         */

        final String sqlQuery = "SELECT id FROM users WHERE nick=? AND password=? AND status=?";
        try {
            Object result = DBUtils.executeSelectSingle(getConnection(), sqlQuery, new Object[]{nick, pass, status});
            return (result != null);
        } catch (SQLException e) {
            throw new EJBException("User can't be find due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }



    //-----------------------------------------------------------------------------------------------------

    //----------------------------------------- Life cycle methods ----------------------------------------
    public void setSessionContext(SessionContext sessionContext) throws EJBException {

    }
    public void ejbCreate() throws CreateException {

    }
    public void ejbRemove() throws EJBException{

    }
    public void ejbActivate() throws EJBException {

    }

    public void ejbPassivate() throws EJBException {

    }

    //--------------------------------------------- Service methods ---------------------------------------

    /**
     * Returns connection to the database.
     *
     * @throws javax.naming.NamingException  in case when can't lookup datasource object
     * @throws java.sql.SQLException     when any errors occurs during the work with database
     */
    private Connection getConnection() throws NamingException, SQLException {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/datasources/booksStoreDB");
        return ds.getConnection();
    }
}
