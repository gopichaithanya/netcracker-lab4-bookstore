

package com.netcracker.ejb.entity;


import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.ThinEntityWrapper;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.Collection;

public interface BookHome extends EJBHome {

    public Book create(String title, int publishId, int genreId, String description, String imgRef, int year)
            throws CreateException, RemoteException;

    public Book create(String title, int publishId, int genreId, String description, String imgRef, int year, Collection<Integer> authorsId)
            throws CreateException, RemoteException;


    public Book findByPrimaryKey(Integer authorPk)
            throws DataAccessException, FinderException, RemoteException;

}
