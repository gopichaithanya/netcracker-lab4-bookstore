package com.netcracker.ejb.entity;


import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.ThinEntityWrapper;
import com.netcracker.utils.DBUtils;

import javax.ejb.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class PublisherBean implements EntityBean {

    public int publisherId;
    public String publisherName;
    public String publisherURL;

    public EntityContext context;

    //------------------------------------------ Home Business methods ---------------------------------------------

    public List ejbHomeGetPublishersAmount() throws DataAccessException {
        return Collections.emptyList();
    }


    public Collection<ThinEntityWrapper> ejbHomeGetPublishersInfo() throws DataAccessException {
        ArrayList<ThinEntityWrapper> result = new ArrayList<ThinEntityWrapper>();
        String sql = "SELECT publishId, publishName, publishURL from publishers";
        try {
            Map<String, List> publishDescription = DBUtils.executeSelect(getConnection(), sql, new Object[]{}, new int[]{});
            // Retrieve columns values
            Iterator<List> mapValuesIter = publishDescription.values().iterator();
            // First column
            List ids = mapValuesIter.next();
            Iterator idsIter = ids.iterator();
            // Second column
            List names = mapValuesIter.next();
            Iterator namesIter = names.iterator();
            // Third column
            List urls =  mapValuesIter.next();
            Iterator urlIter = urls.iterator();

            // Filling the result
            while (idsIter.hasNext() && namesIter.hasNext() && urlIter.hasNext()) {
                //TODO:!!! error may occur in this place
//                ArrayList<String> allPublishersInfo = new ArrayList<String>();
//                allPublishersInfo.add(namesIter.next().toString());
//                allPublishersInfo.add(urlIter.next().toString());

                result.add(new ThinEntityWrapper(idsIter.next().toString(), namesIter.next().toString() + urlIter.next().toString() ));
            }

            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Error occurs during the work with database", e);
        } catch (NamingException e) {
            throw new DataAccessException("Can't lookup datasource object", e);
        }

    }

    //--------------------------------------------------------------------------------------------------------------




    //------------------------------------------ Remote Business methods -------------------------------------------

    public int getPublisherId() {
        System.out.println("------- [Publisher] getPublisherId");
        return publisherId;
    }

    public String getPublisherName() {
        System.out.println("-------[Publisher] getPublisherName");
        return publisherName;
    }

    public String getPublisherURL() {
        System.out.println("-------[Publisher] getPublisherURL");
        return publisherURL;
    }

    //--------------------------------------------------------------------------------------------------------------




    //------------------------------------------ Finder methods ----------------------------------------------------

    public Integer ejbFindByPrimaryKey(Integer publisherPk) throws DataAccessException, FinderException {
        System.out.println("------[Publisher] ejbFindByPrimaryKey");
        final String sqlQuery = "SELECT publishId FROM publishers WHERE publishId = ?";
        try {
            Object result = DBUtils.executeSelectSingle(getConnection(), sqlQuery, new Object[] {publisherPk.intValue()});
            if (result == null) {
                throw new FinderException("There is no object with specified primary key");
            }
            return new Integer(Integer.parseInt(result.toString()));
        } catch (SQLException e) {
            throw new DataAccessException("Publisher EJB can't be find due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new DataAccessException("Can't lookup datasource object", e);
        }
    }

    //--------------------------------------------------------------------------------------------------------------


    //----------------------------------------- Create and Remove methods ------------------------------------------

    public Integer ejbCreate(int publisherId, String publisherName, String publisherURL) throws CreateException {
        System.out.println("-------[Publisher] ejbCreate");
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.publisherURL = publisherURL;

        final String sqlQuery = "INSERT INTO publishers (publishName, publishUrl) VALUES (?, ?)";

        try {
            int affectedRows = DBUtils.executeUpdate(getConnection(), sqlQuery, new Object[] {publisherId, publisherName, publisherURL});

            // If creation procedure has been performed successfully
            if (affectedRows != 0) {
                return new Integer(publisherId);
            } else {
                throw new CreateException("Unsuccessful Publisher bean creation");
            }
        } catch (SQLException e) {
            throw new EJBException("Can't create Publisher bean due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    public void ejbPostCreate(int publisherId, String publisherName, String publisherURL) throws CreateException {
        System.out.println("--------[Publisher] ejbPostCreate");
    }

    @Override
    public void ejbRemove() throws RemoveException, EJBException {
        System.out.println("-------[Publisher] ejbRemove");
        Integer publisherPk = (Integer) context.getPrimaryKey();
        final String sqlQuery = "DELETE FROM publishers WHERE publishId = ?";
        try {
            int affectedRows = DBUtils.executeUpdate(getConnection(), sqlQuery, new Object[]{publisherPk.intValue()});
            if (affectedRows == 0) {
                throw new RemoveException("Publisher bean hasn't been removed");
            }
        } catch (SQLException e) {
            throw new EJBException("Can't remove Publisher bean due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    //--------------------------------------------------------------------------------------------------------------




    //----------------------------------------- Bean's life cycle methods -------------------------------------------

    @Override
    public void setEntityContext(EntityContext entityContext) throws EJBException {
        System.out.println("**************");
        System.out.println("--------[Publisher] setEntityContext");
        this.context = entityContext;
    }

    @Override
    public void unsetEntityContext() throws EJBException {
        System.out.println("---------[Publisher] unsetEntityContext");
        System.out.println("**************");
        context = null;
    }

    @Override
    public void ejbActivate() throws EJBException {
        System.out.println("-------- [Publisher] ejbActivate");
    }

    @Override
    public void ejbPassivate() throws EJBException {
        System.out.println("--------[Publisher] ejbPassivate");
    }

    @Override
    public void ejbLoad() throws EJBException {
        System.out.println("-----------[Publisher] ejbLoad");
        Integer publisherPk = (Integer)context.getPrimaryKey();
        final String sqlQuery = "SELECT * FROM publishers WHERE publishId = ?";
        try {
            Collection<List> queryRes = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{publisherPk.intValue()},
                    new int[]{}).values();
            Iterator<List> queryResIter = queryRes.iterator();
            this.publisherId = (Integer)(queryResIter.next().get(0));
            this.publisherName = (String)(queryResIter.next().iterator().next());
            this.publisherURL = (String)(queryResIter.next().iterator().next());
        } catch (SQLException e) {
            throw new EJBException("Publisher EJB can't be load due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    @Override
    public void ejbStore() throws EJBException {
        System.out.println("------[Publisher] ejbStore");
        final String sqlQuery = "UPDATE publishers SET publishName = ?, publishUrl = ? WHERE publishId = ?";
        try {
            int affectedRows = DBUtils.executeUpdate(getConnection(), sqlQuery, new Object[]{this.publisherName, this.publisherURL, this.publisherId});
            if (affectedRows == 0) {
                throw new EJBException("Publisher bean hasn't been properly stored into database");
            }
        } catch (SQLException e) {
            throw new EJBException("Can't store Publisher bean due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }

    }

    //--------------------------------------------------------------------------------------------------------------




    //------------------------------------------ Helper methods ----------------------------------------------------

    private Connection getConnection() throws NamingException, SQLException {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/datasources/booksStoreDB");
        return ds.getConnection();
    }

    //--------------------------------------------------------------------------------------------------------------


}
