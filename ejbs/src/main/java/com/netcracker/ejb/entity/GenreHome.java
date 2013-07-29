package com.netcracker.ejb.entity;

import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.ThinEntityWrapper;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

public interface GenreHome extends EJBHome {
    public Genre create(String genreName) throws RemoteException, CreateException;
    public Genre findByPrimaryKey(Integer genrePk) throws DataAccessException, RemoteException, FinderException;
    public List getGenresAmount() throws DataAccessException, RemoteException;

    /**
     * This method returns information that shortly describe all available genres stored in the database.
     * Information represented by collection of pairs (genreId:genreName). Each pair of (genreId:genreName)
     * encapsulated in ThinEntityWrapper instance.
     *
     * @throws DataAccessException if any errors during the work with database are occurs
     * @throws RemoteException if any connection problems occurs
     */
    public Collection<ThinEntityWrapper> getGenresInfo() throws DataAccessException, RemoteException;

    public boolean isGenreExist(String name) throws DataAccessException, RemoteException;
}
