package com.netcracker.ejb.entity;

import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.ThinEntityWrapper;
import com.netcracker.utils.DBUtils;

import javax.ejb.*;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.*;

public class GenreBean implements EntityBean {

    private EntityContext context;

    private int genreId;
    private String genreName;

    //------------------------------------------ Home Business methods ---------------------------------------------

    public List ejbHomeGetGenresAmount() throws DataAccessException {
        return Collections.emptyList();
    }


    public Collection<ThinEntityWrapper> ejbHomeGetGenresInfo() throws DataAccessException {
        ArrayList<ThinEntityWrapper> result = new ArrayList<ThinEntityWrapper>();
        String sql = "SELECT genreId, name from genres";
        try {
            Map <String, List> genresDescription = DBUtils.executeSelect(getConnection(), sql, new Object[]{}, new int[]{});
            // Retrieve columns values
            Iterator<List> mapValuesIter = genresDescription.values().iterator();
            // First column
            List ids = mapValuesIter.next();
            Iterator idsIter = ids.iterator();
            // Second column
            List names = mapValuesIter.next();
            Iterator namesIter = names.iterator();

            // Filling the result
            while (idsIter.hasNext() && namesIter.hasNext()) {
                result.add(new ThinEntityWrapper(idsIter.next().toString(), namesIter.next().toString()));
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

    public int getGenreId() {
        System.out.println("------- [Genre] getGenreId");
        return genreId;
    }

    public String getGenreName() {
        System.out.println("-------[Genre] getGenreName");
        return genreName;
    }

    //--------------------------------------------------------------------------------------------------------------




    //------------------------------------------ Finder methods ----------------------------------------------------

    public Integer ejbFindByPrimaryKey(Integer genrePk) throws DataAccessException, FinderException {
        System.out.println("------[Genre] ejbFindByPrimaryKey");
        final String sqlQuery = "SELECT genreId FROM genres WHERE genreId = ?";
        try {
            Object result = DBUtils.executeSelectSingle(getConnection(), sqlQuery, new Object[] {genrePk.intValue()});
            if (result == null) {
                throw new FinderException("There is no object with specified primary key");
            }
            return new Integer(Integer.parseInt(result.toString()));
        } catch (SQLException e) {
            throw new DataAccessException("Genre EJB can't be find due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new DataAccessException("Can't lookup datasource object", e);
        }
    }

    //--------------------------------------------------------------------------------------------------------------


    //----------------------------------------- Create and Remove methods ------------------------------------------

    public Integer ejbCreate(int genreId, String genreName) throws CreateException {
        System.out.println("-------[Genre] ejbCreate");
        this.genreId = genreId;
        this.genreName = genreName;

        final String sqlQuery = "INSERT INTO genres VALUES (?, ?)";

        try {
            int affectedRows = DBUtils.executeUpdate(getConnection(), sqlQuery, new Object[] {genreId, genreName});

            // If creation procedure has been performed successfully
            if (affectedRows != 0) {
                return new Integer(genreId);
            } else {
                throw new CreateException("Unsuccessful Genre bean creation");
            }
        } catch (SQLException e) {
            throw new EJBException("Can't create Genre bean due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    public void ejbPostCreate(int genreId, String genreName) throws CreateException {
        System.out.println("--------[Genre] ejbPostCreate");
    }

    @Override
    public void ejbRemove() throws RemoveException, EJBException {
        System.out.println("-------[Genre] ejbRemove");
        Integer genrePk = (Integer) context.getPrimaryKey();
        final String sqlQuery = "DELETE FROM genres WHERE genreId = ?";
        try {
            int affectedRows = DBUtils.executeUpdate(getConnection(), sqlQuery, new Object[]{genrePk.intValue()});
            if (affectedRows == 0) {
                throw new RemoveException("Genre bean hasn't been removed");
            }
        } catch (SQLException e) {
            throw new EJBException("Can't remove Genre bean due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    //--------------------------------------------------------------------------------------------------------------




    //----------------------------------------- Bean's life cycle methods -------------------------------------------

    @Override
    public void setEntityContext(EntityContext entityContext) throws EJBException {
        System.out.println("**************");
        System.out.println("--------[Genre] setEntityContext");
        this.context = entityContext;
    }

    @Override
    public void unsetEntityContext() throws EJBException {
        System.out.println("---------[Genre] unsetEntityContext");
        System.out.println("**************");
        context = null;
    }

    @Override
    public void ejbActivate() throws EJBException {
        System.out.println("-------- [Genre] ejbActivate");
    }

    @Override
    public void ejbPassivate() throws EJBException {
        System.out.println("--------[Genre] ejbPassivate");
    }

    @Override
    public void ejbLoad() throws EJBException {
        System.out.println("-----------[Genre] ejbLoad");
        Integer genrePk = (Integer)context.getPrimaryKey();
        final String sqlQuery = "SELECT * FROM genres WHERE genreId = ?";
        try {
            Collection<List> queryRes = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{genrePk.intValue()},
                                                              new int[]{}).values();
            Iterator<List> queryResIter = queryRes.iterator();
            this.genreId = (Integer)(queryResIter.next().get(0));
            this.genreName = (String)(queryResIter.next().iterator().next());
        } catch (SQLException e) {
            throw new EJBException("Genre EJB can't be load due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    @Override
    public void ejbStore() throws EJBException {
        System.out.println("------[Genre] ejbStore");
        final String sqlQuery = "UPDATE genres SET genres.name = ? WHERE genreId = ?";
        try {
            int affectedRows = DBUtils.executeUpdate(getConnection(), sqlQuery, new Object[]{this.genreName, this.genreId});
            if (affectedRows == 0) {
                throw new EJBException("Genre bean hasn't been properly stored into database");
            }
        } catch (SQLException e) {
            throw new EJBException("Can't store Genre bean due to the errors during the work with database", e);
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
