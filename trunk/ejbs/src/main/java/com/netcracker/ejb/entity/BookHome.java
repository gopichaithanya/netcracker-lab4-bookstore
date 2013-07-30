

package com.netcracker.ejb.entity;


import com.netcracker.exceptions.DataAccessException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import java.rmi.RemoteException;

public interface BookHome extends EJBHome {

    public Book create(String title, int publishId, int genreId, String description, String imgRef, int year)
            throws CreateException, RemoteException;


    public Book findByPrimaryKey(Integer authorPk)
            throws DataAccessException, FinderException, RemoteException;

    /*
    public Book findByAuthorAndGenreIDs(int authorId, int genreId)
            throws DataAccessException, FinderException, RemoteException;

    public Collection findByAuthorID(int authorId)
            throws DataAccessException, FinderException, RemoteException;

    public Collection findByGenreID(int genreId)
            throws DataAccessException, FinderException, RemoteException;
    */


    //----------------------------- Home business methods -----------------------------

    /*
    public Collection<ThinEntityWrapper> getBooksInfo()
            throws RemoteException, DataAccessException;

    public List getBooksAmount()
            throws RemoteException, DataAccessException;
    */

    //---------------------------------------------------------------------------------
}
