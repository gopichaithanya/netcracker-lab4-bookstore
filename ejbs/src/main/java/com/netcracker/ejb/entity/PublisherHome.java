package com.netcracker.ejb.entity;


import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.ThinEntityWrapper;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

public interface PublisherHome extends EJBHome {

    public PublisherRemote create(int genreId, String genreName, String publisherURL) throws RemoteException, CreateException;

    public PublisherRemote findByPrimaryKey(Integer publisherPk) throws RemoteException, FinderException, DataAccessException;

    public List getPublishersAmount() throws DataAccessException, RemoteException;

    public Collection<ThinEntityWrapper> getPublishersInfo() throws DataAccessException, RemoteException;
}
