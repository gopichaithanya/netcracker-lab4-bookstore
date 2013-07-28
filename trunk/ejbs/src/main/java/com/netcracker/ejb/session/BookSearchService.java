package com.netcracker.ejb.session;

import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.ThinEntityWrapper;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.Collection;

public interface  BookSearchService extends EJBObject {
    public Collection<ThinEntityWrapper> getBooksShortInfo(int authorId, int genreId)
            throws DataAccessException, RemoteException;

}
