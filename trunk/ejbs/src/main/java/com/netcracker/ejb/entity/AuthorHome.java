package com.netcracker.ejb.entity;


import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.ThinEntityWrapper;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

/**
 * Define beans lifecycle methods (creating, finding)
 * and home business methods: getting information of all authors
 * and getting authors amount.
 */
public interface AuthorHome extends EJBHome {

    public AuthorRemote create(String firstName, String lastName)
            throws CreateException, RemoteException;

    public AuthorRemote findByName(String firstName, String lastName)
            throws RemoteException, FinderException;

    public AuthorRemote findByPrimaryKey(Integer authorPk)
            throws FinderException, RemoteException, DataAccessException;

// Home business methods
    public Collection<ThinEntityWrapper> getAuthorsInfo()
            throws RemoteException, DataAccessException;

    public List getAuthorsAmount()
            throws RemoteException, DataAccessException;

    public boolean isAuthorExist(String firstName, String lastName)
            throws RemoteException, FinderException;


}
