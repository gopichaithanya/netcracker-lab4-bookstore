
package com.netcracker.ejb.entity;


import com.netcracker.exceptions.DataAccessException;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.Collection;

public interface Book extends EJBObject {

    public int getBookId() throws RemoteException;

    public String getTitle() throws RemoteException;

    public int getGenreId() throws RemoteException;

    public Genre getGenre() throws DataAccessException, RemoteException;

    public int getPublisherId() throws RemoteException;

    public Publisher getPublisher() throws DataAccessException, RemoteException;

    public Collection<Integer> getAuthorIds() throws RemoteException;

    public Collection<Author> getAuthors() throws DataAccessException, RemoteException;

    public String getDescription() throws RemoteException;

    public String getImageReference() throws RemoteException;

    public int getYear() throws RemoteException;



    public void setTitle(String title) throws RemoteException;

    public void setPublishId(int publishId) throws RemoteException;

    public void setPublisher(Publisher publisher) throws RemoteException;

    public void setGenreId(int genreId) throws RemoteException;

    public void setGenre(Genre genre) throws RemoteException;

    public void setAuthorIds(Collection<Integer> authorIds) throws RemoteException;

    public void setAuthors(Collection<Author> authors) throws RemoteException;

    public void setDescription(String description) throws RemoteException;

    public void setImgRef(String imgRef) throws RemoteException;

    public void setYear(int year) throws RemoteException;

}

