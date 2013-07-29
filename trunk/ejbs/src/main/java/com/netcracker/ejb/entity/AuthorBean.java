package com.netcracker.ejb.entity;


import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.ThinEntityWrapper;
import com.netcracker.utils.DBUtils;
import com.sun.org.apache.bcel.internal.generic.GOTO;

import javax.ejb.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class AuthorBean implements EntityBean{

    private EntityContext context;

    private int authorId;
    private String firstName;
    private String lastName;


                                           // Remote business methods

    public int getAuthorId() {
        System.out.println("------- [Author] getAuthorId");
        return authorId;
    }


    public String getAuthorFirstName() {
        System.out.println("------- [Author] getAuthorFirstName");
        return firstName;
    }
    public String getAuthorLastName() {
        System.out.println("------- [Author] getAuthorLastName");
        return lastName;
    }

                                         // Home business method

    public Collection<ThinEntityWrapper> ejbHomeGetAuthorsInfo() throws DataAccessException{
        ArrayList<ThinEntityWrapper> result = new ArrayList<ThinEntityWrapper>();
        String sql = "SELECT authorId, firstName, lastName from authors";
        try {
            Map<String, List> authorsDescriptions = DBUtils.executeSelect(getConnection(), sql, new Object[]{}, new int[]{});
            // Retrieve columns values
            Iterator<List> mapValuesIter = authorsDescriptions.values().iterator();
            // First column
            List ids = mapValuesIter.next();
            Iterator idsIter = ids.iterator();
            // Second column
            List firstNames = mapValuesIter.next();
            Iterator firstNamesIter = firstNames.iterator();
            //Third column
            List lastNames = mapValuesIter.next();
            Iterator lastNamesIter = lastNames.iterator();

            // Filling the result

            while (idsIter.hasNext() && firstNamesIter.hasNext() && lastNamesIter.hasNext()) {
//                ArrayList<String> allAuthorInfo = new ArrayList<String>();
//                allAuthorInfo.add(firstNamesIter.next().toString());
//                allAuthorInfo.add(lastNamesIter.next().toString());

                //TODO: error may occur in this place
                result.add(new ThinEntityWrapper(idsIter.next().toString(), firstNamesIter.next().toString(), lastNamesIter.next().toString()));
            }

            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Error occurs during the work with database", e);
        } catch (NamingException e) {
            throw new DataAccessException("Can't lookup datasource object", e);
        }
    }

    public List ejbHomeGetAuthorsAmount() throws  DataAccessException {

        return Collections.emptyList();
    }

                                     // Create and remove methods


    public Integer ejbCreate(String firstName, String lastName) throws CreateException {
        System.out.println("-------[Author] ejbCreate");
        this.firstName = firstName;
        this.lastName = lastName;

        final String sqlQuery = "INSERT INTO authors (firstName, lastName) VALUES (?, ?)";

        try {
            int authorId = DBUtils.executeInsert(getConnection(), sqlQuery, new Object[] {firstName, lastName});
            this.authorId = authorId;

            // If creation procedure has been performed successfully
            if (authorId != 0) {
                return new Integer(authorId);
            } else {
                throw new CreateException("Unsuccessful Author bean creation");
            }
        } catch (SQLException e) {
            throw new EJBException("Can't create Author bean due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }


    public void ejbPostCreate(String firstName, String lastName) throws CreateException {
        System.out.println("------- [Author] ejbPostCreate");
    }


    public void ejbRemove() throws RemoveException {
        System.out.println("-------[Author] ejbRemove");
        Integer authorPk = (Integer)context.getPrimaryKey();

        final String sqlQuery1 = "DELETE FROM balink WHERE authorId= ?";
        final String sqlQuery2 = "DELETE FROM authors WHERE authorId= ?";

        try {

            int affectedRows1 = DBUtils.executeUpdate(getConnection(), sqlQuery1, new Object[]{authorPk.intValue()});
            int affectedRows2 = DBUtils.executeUpdate(getConnection(), sqlQuery2, new Object[]{authorPk.intValue()});
            if (affectedRows2 == 0 && affectedRows1 == 0) {
                throw new RemoveException("Author bean hasn't been removed");
            }
        } catch (SQLException e) {
            throw new EJBException("Can't remove Author bean due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }

    }

                                            // Finder methods

    public Integer ejbFindByPrimaryKey(Integer authorPk) throws FinderException, DataAccessException {
        System.out.println("------[Author] ejbFindByPrimaryKey");
        final String sqlQuery = "SELECT authorId FROM authors WHERE authorId = ?";
        try {
            Object result = DBUtils.executeSelectSingle(getConnection(), sqlQuery, new Object[] {authorPk.intValue()});
            if (result == null) {
                throw new FinderException("There is no object with specified primary key");
            }
            return new Integer(Integer.parseInt(result.toString()));
        } catch (SQLException e) {
            throw new DataAccessException("Author EJB can't be find due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new DataAccessException("Can't lookup datasource object", e);
        }
    }

                                     // Beans lifecycle methods
    @Override
    public void setEntityContext(EntityContext entityContext) throws EJBException {
        System.out.println("------- [Author] setEntityContext");
        this.context = entityContext;
    }

    @Override
    public void unsetEntityContext() throws EJBException {
        System.out.println("------- [Author] unsetEntityContext");
        context = null;
    }


    @Override
    public void ejbActivate() throws EJBException {
        System.out.println("------- [Author] ejbActivate");
    }

    @Override
    public void ejbPassivate() throws EJBException {
        System.out.println("------- [Author] ejbPassivate");
    }

    @Override
    public void ejbLoad() throws EJBException {
        System.out.println("-----------[Author] ejbLoad");
        Integer authorPk = (Integer)context.getPrimaryKey();
        final String sqlQuery = "SELECT * FROM authors WHERE authorId = ?";
        try {
            Collection<List> queryRes = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{authorPk.intValue()},
                    new int[]{}).values();
            Iterator<List> queryResIter = queryRes.iterator();
            this.authorId = (Integer)(queryResIter.next().get(0));
            this.firstName = (String)(queryResIter.next().iterator().next());
            this.lastName = (String)(queryResIter.next().iterator().next());
        } catch (SQLException e) {
            throw new EJBException("Author EJB can't be load due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }

    }

    @Override
    public void ejbStore() throws EJBException {
        System.out.println("------[Author] ejbStore");
        final String sqlQuery = "UPDATE authors SET authors.firstName = ?, authors.lastName=? WHERE authorId = ?";
        try {
            int affectedRows = DBUtils.executeUpdate(getConnection(), sqlQuery, new Object[]{this.firstName, this.lastName, this.authorId});
            if (affectedRows == 0) {
                throw new EJBException("Author bean hasn't been properly stored into database");
            }
        } catch (SQLException e) {
            throw new EJBException("Can't store Author bean due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }

    }

    // Getting connection with the datasource
    private Connection getConnection() throws NamingException, SQLException {
        Context ctx = new InitialContext();
        DataSource ds =  (DataSource) ctx.lookup("java:comp/env/datasources/booksStoreDB");
        return ds.getConnection();
    }

    // For checking existing author in the database

    /**
     * Figure out is author with such first and last name already exist or not.
     *
     * @param firstName         author's first name
     * @param lastName          author's last name
     *
     * @return                  exist or not
     */
    public boolean ejbHomeIsAuthorExist(String firstName, String lastName) {
        System.out.println("------[Author] ejbHomeIsAuthorExist");
        final String sqlQuery = "SELECT authorId FROM authors WHERE firstName=? AND lastName=?";
        try {
            Object result = DBUtils.executeSelectSingle(getConnection(), sqlQuery, new Object[] {firstName, lastName});
            return (result != null);
        } catch (SQLException e) {
            throw new EJBException("Author EJB can't be find due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    public Integer ejbFindByName(String firstName, String lastName) throws FinderException {
        System.out.println("-----------[Author] ejbFindByName");
        this.firstName = firstName;
        this.lastName = lastName;

        final String sqlQuery = "SELECT authorId FROM authors WHERE firstName = ? AND lastName = ?";
        try {
            Collection<List> queryRes = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{firstName, lastName},
                    new int[]{}).values();
            Iterator<List> queryResIter = queryRes.iterator();
            this.authorId = (Integer)(queryResIter.next().get(0));
        } catch (SQLException e) {
            throw new EJBException("Author EJB can't be load due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
        return authorId;
    }
}
