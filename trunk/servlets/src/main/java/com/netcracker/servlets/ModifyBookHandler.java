package com.netcracker.servlets;

import com.netcracker.ejb.entity.*;
import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.Helper;
import com.netcracker.utils.DBUtils;
import org.apache.log4j.Logger;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModifyBookHandler extends HttpServlet {

    private static final Logger log = Logger.getLogger(ModifyBookHandler.class);

    private String title;
    private String description;
    private String imgRef;
    private int year;


    private int publishId;
    private int genreId;
    private int authorId;

    private  int newBookId;

    private Context ctx;

    private AuthorHome authorHome;
    private GenreHome genreHome;
    private PublisherHome publisherHome;


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        try {
//            Context ctx = Helper.getInstance().getContext();
//            title = req.getParameter("title");
//            publishName = req.getParameter("publishName");
//            publishUrl = req.getParameter("publishUrl");
//            genreName = req.getParameter("genreName");
//            description = req.getParameter("description");
//            imgRef = req.getParameter("imgRef");
//            year = Integer.parseInt(req.getParameter("year"));
//            authorFirstName = req.getParameter("firstName");
//            authorLastName = req.getParameter("lastName");
//            Object obj = ctx.lookup("ear-1.0/ejbPart/BookBean!com.netcracker.ejb.entity.BookHome");
//            BookHome bookHome = (BookHome) PortableRemoteObject.narrow(obj, BookHome.class);
//
//            if (isBookValid(title) && isAuthorValid(authorFirstName,authorLastName)) {
//                bookHome.create(title,publishName, publishUrl, genreName, description, imgRef, year, authorFirstName, authorLastName);
//                log.info("Book with title: " + title + " successfully added.");
//                req.getServletContext().getRequestDispatcher("/books").forward(req, resp);
//            }
//            else {
//                req.setAttribute("error", true);
//                doGet(req, resp);
//                req.setAttribute("error", false);
//                log.error("Error occurs during checking book parameters typing by user.");
//            }
//
//        } catch (CreateException e) {
//            log.error("Error occurs during adding author to the database", e);
//            throw new ServletException(e);
//        } catch (NamingException e) {
//            log.error("Error occurs during adding author to the database", e);
//            throw new ServletException(e);
//        }




//        this.title = req.getParameter("title");
//
//        this.description = req.getParameter("descript");
//        this.imgRef = req.getParameter("imgRef");
//        this.year = Integer.parseInt(req.getParameter("year"));
//        this.publishId = Integer.valueOf(req.getParameter("publisherId"));
//        this.genreId = Integer.valueOf(req.getParameter("genreId"));
//        this.authorId = Integer.valueOf(req.getParameter("authorId"));
//
//
//
//        try {
//
//            ctx = Helper.getInstance().getContext();
//
//            if (isBookValid(title)) {
//                Object obj4 = ctx.lookup("ear-1.0/ejbPart/BookBean!com.netcracker.ejb.entity.BookHome");
//                BookHome bookHome = (BookHome) PortableRemoteObject.narrow(obj4, BookHome.class);
//                Book bookRemote = bookHome.create(title, publishId,genreId,description,imgRef,year);
//                this.newBookId = bookRemote .getBookId();
//                log.info("Book with id " + newBookId + " successfully created.");
//                log.info("author id = " + authorId);
//                bookHome.bindBalinkTable(authorId, newBookId);
//                log.info("Book with id " + newBookId + " binded to the author with id " + authorId + ".");
//
//                req.getServletContext().getRequestDispatcher("/books").forward(req, resp);
//            } else {
//                req.setAttribute("error", true);
//                doGet(req, resp);
//                req.setAttribute("error", false);
//            }
//
//
//
//
//        } catch (NamingException e) {
//            throw new EJBException(e);
//        } catch (RemoteException e) {
//            throw new EJBException(e);
//        } catch (CreateException e) {
//            throw new EJBException(e);
//        } catch (DataAccessException e) {
//            throw new EJBException(e);
//        }
    }



    private boolean isBookValid(String title) {
        return !(title.isEmpty()
                || title.matches(".*\\d.*"));
    }

    private boolean isAuthorValid(String firstName, String lastName) {
        return !(firstName.isEmpty()
                || lastName.isEmpty()
                || firstName.matches(".*\\d.*")
                || lastName.matches(".*\\d.*"));
    }

    /**
     * Figure out is author with such first and last name already exist or not.
     *
     * @param firstName         author's first name
     * @param lastName          author's last name
     * @return                  exist or not

     * @throws ServletException
     */
    private boolean isAuthorExist(String firstName, String lastName) throws ServletException {
        boolean authorExist;
        try {
            authorExist = authorHome.isAuthorExist(firstName, lastName);

        } catch (RemoteException e) {
            log.error("Error occurs during finding out is author exist in the database", e);
            throw new ServletException(e);
        } catch (FinderException e) {
            log.error("Error occurs during finding out is author exist in the database", e);
            throw new ServletException(e);
        }
        return authorExist;
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.setAttribute("fromAddBookPage", true);
        try {
            ctx = Helper.getInstance().getContext();
            Object authorObj = ctx.lookup("ear-1.0/ejbPart/AuthorBean!com.netcracker.ejb.entity.AuthorHome");
            authorHome = (AuthorHome) PortableRemoteObject.narrow(authorObj, AuthorHome.class);
            req.setAttribute("authorsList", authorHome.getAuthorsInfo());

            Object genreObj = ctx.lookup("ear-1.0/ejbPart/GenreBean!com.netcracker.ejb.entity.GenreHome");
            genreHome = (GenreHome) PortableRemoteObject.narrow(genreObj, GenreHome.class);
            req.setAttribute("genresList", genreHome.getGenresInfo());

            Object publisherObj = ctx.lookup("ear-1.0/ejbPart/PublisherBean!com.netcracker.ejb.entity.PublisherHome");
            publisherHome = (PublisherHome) PortableRemoteObject.narrow(publisherObj, PublisherHome.class);
            req.setAttribute("publishersList", publisherHome.getPublishersInfo());



        } catch (DataAccessException e) {
            throw new ServletException(e);
        } catch (NamingException e) {
            throw  new ServletException(e);
        }
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/addBook.jsp").forward(req, resp);
    }
}
