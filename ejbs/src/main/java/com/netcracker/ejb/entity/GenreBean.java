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
        List<ThinEntityWrapper> result = new ArrayList<ThinEntityWrapper>();
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
        System.out.println("------- [GenreBean] getGenreId");
        return genreId;
    }

    public String getName() {
        System.out.println("-------[GenreBean] getName");
        return genreName;
    }

    //--------------------------------------------------------------------------------------------------------------




    //------------------------------------------ Finder methods ----------------------------------------------------

    public Integer ejbFindByPrimaryKey(Integer genrePk) throws DataAccessException, FinderException {
        System.out.println("------[GenreBean] ejbFindByPrimaryKey");
        final String sqlQuery = "SELECT genreId FROM genres WHERE genreId = ?";
        try {
            Object result = DBUtils.executeSelectSingle(getConnection(), sqlQuery, new Object[] {genrePk.intValue()});
            if (result == null) {
                throw new FinderException("There is no object with specified primary key");
            }
            return new Integer(Integer.parseInt(result.toString()));
        } catch (SQLException e) {
            throw new DataAccessException("GenreBean EJB can't be find due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new DataAccessException("Can't lookup datasource object", e);
        }
    }

    //--------------------------------------------------------------------------------------------------------------


    //----------------------------------------- Create and Remove methods ------------------------------------------

    public Integer ejbCreate(String genreName) throws CreateException {
        System.out.println("-------[GenreBean] ejbCreate");
        this.genreName = genreName;

        final String sqlQuery = "INSERT INTO genres (name) VALUES (?)";

        try {
            int newGenreId = DBUtils.executeInsert(getConnection(), sqlQuery, new Object[] {genreName}, true);
            this.genreId = newGenreId;

            // If creation procedure has been performed successfully
            if (newGenreId != 0) {
                return new Integer(genreId);
            } else {
                throw new CreateException("Unsuccessful GenreBean bean creation");
            }
        } catch (SQLException e) {
            throw new EJBException("Can't create GenreBean bean due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    public void ejbPostCreate(String genreName) throws CreateException {
        System.out.println("--------[GenreBean] ejbPostCreate");
    }

    @Override
    public void ejbRemove() throws RemoveException, EJBException {
        System.out.println("-------[GenreBean] ejbRemove");
        Integer genrePk = (Integer) context.getPrimaryKey();
        final String sqlQuery = "DELETE FROM genres WHERE genreId = ?";
        try {
            int affectedRows = DBUtils.executeUpdate(getConnection(), sqlQuery, new Object[]{genrePk.intValue()});
            if (affectedRows == 0) {
                throw new RemoveException("GenreBean bean hasn't been removed");
            }
        } catch (SQLException e) {
            throw new EJBException("Can't remove GenreBean bean due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    //--------------------------------------------------------------------------------------------------------------




    //----------------------------------------- Bean's life cycle methods -------------------------------------------

    @Override
    public void setEntityContext(EntityContext entityContext) throws EJBException {
        System.out.println("**************");
        System.out.println("--------[GenreBean] setEntityContext");
        this.context = entityContext;
    }

    @Override
    public void unsetEntityContext() throws EJBException {
        System.out.println("---------[GenreBean] unsetEntityContext");
        System.out.println("**************");
        context = null;
    }

    @Override
    public void ejbActivate() throws EJBException {
        System.out.println("-------- [GenreBean] ejbActivate");
    }

    @Override
    public void ejbPassivate() throws EJBException {
        System.out.println("--------[GenreBean] ejbPassivate");
    }

    @Override
    public void ejbLoad() throws EJBException {
        System.out.println("-----------[GenreBean] ejbLoad");
        Integer genrePk = (Integer)context.getPrimaryKey();
        final String sqlQuery = "SELECT * FROM genres WHERE genreId = ?";
        try {
            Collection<List> queryRes = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{genrePk.intValue()},
                                                              new int[]{}).values();
            Iterator<List> queryResIter = queryRes.iterator();
            this.genreId = (Integer)(queryResIter.next().get(0));
            this.genreName = (String)(queryResIter.next().iterator().next());
        } catch (SQLException e) {
            throw new EJBException("GenreBean EJB can't be load due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    public void ejbStore() throws EJBException {
        System.out.println("------[GenreBean] ejbStore");
        final String sqlQuery = "UPDATE genres SET genres.name = ? WHERE genreId = ?";
        try {
            int affectedRows = DBUtils.executeUpdate(getConnection(), sqlQuery, new Object[]{this.genreName, this.genreId});
            if (affectedRows == 0) {
                throw new EJBException("GenreBean bean hasn't been properly stored into database");
            }
        } catch (SQLException e) {
            throw new EJBException("Can't store GenreBean bean due to the errors during the work with database", e);
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

    public boolean ejbHomeIsGenreExist(String name) throws DataAccessException {
        System.out.println("------[GenreBean] ejbHomeIsGenreExist");
        final String sqlQuery = "SELECT genreId FROM genres WHERE name=?";
        try {
            Object result = DBUtils.executeSelectSingle(getConnection(), sqlQuery, new Object[] {name});
            return (result != null);
        } catch (SQLException e) {
            throw new EJBException("GenreBean EJB can't be find due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }

    }

    //--------------------------------------------------------------------------------------------------------------

}
