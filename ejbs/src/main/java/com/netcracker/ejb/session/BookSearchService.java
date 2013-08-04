package com.netcracker.ejb.session;

import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.ThinEntityWrapper;

import javax.ejb.EJBObject;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Collection;

public interface  BookSearchService extends EJBObject {
    public Collection<ThinEntityWrapper> getBooksShortInfo(int authorId, int genreId)
            throws DataAccessException, RemoteException;

    public Collection<ThinEntityWrapper> getBookInfoBySearchCriteria(String title, String author, int publisherId,
                                                                     int genreId, int year)
            throws DataAccessException, RemoteException;

}
