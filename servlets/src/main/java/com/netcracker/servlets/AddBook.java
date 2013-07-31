package com.netcracker.servlets;

import com.netcracker.ejb.entity.*;
import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.Helper;
import org.apache.log4j.Logger;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class AddBook extends HttpServlet {

    private static final Logger log = Logger.getLogger(AddBook.class);


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Context ctx = Helper.getInstance().getContext();
            String title = req.getParameter("title");
            int publishId = Integer.parseInt(req.getParameter("publisherId"));
            int genreId = Integer.parseInt(req.getParameter("genreId"));
            int authorId = Integer.parseInt(req.getParameter("authorId"));
            String description = req.getParameter("descript");
            String imgRef = req.getParameter("imgRef");
            int year = Integer.parseInt(req.getParameter("year"));
            Object obj = ctx.lookup("ear-1.0/ejbPart/BookBean!com.netcracker.ejb.entity.BookHome");
            BookHome bookHome = (BookHome) PortableRemoteObject.narrow(obj, BookHome.class);


            if (isBookValid(title)) {
                //TODO: error may occurs

                Collection<Integer> authorsId = new ArrayList<Integer>();
                authorsId.add(authorId);
                bookHome.create(title, publishId, genreId, description, imgRef, year, authorsId);

                log.info("Book with title: " + title + " successfully added.");
                req.getServletContext().getRequestDispatcher("/books").forward(req, resp);
            }
            else {
                req.setAttribute("error", true);
                doGet(req, resp);
                req.setAttribute("error", false);
                log.error("Error occurs during checking book parameters typing by user.");
            }

  } catch (CreateException e) {
            log.error("Error occurs during adding author to the database. Can't retrieve Book bean.", e);
            throw new ServletException("Can't retrieve Book bean.", e);
        } catch (NamingException e) {
            log.error("Error occurs during adding author to the database. Can't lookup Book bean.", e);
            throw new ServletException("Can't lookup Book bean.", e);
        }
    }

    private boolean isBookValid(String title) {
        return !(title.isEmpty()
                || title.matches(".*\\d.*"));
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.setAttribute("fromAddBookPage", true);
        try {
            Context ctx = Helper.getInstance().getContext();
            Object authorObj = ctx.lookup("ear-1.0/ejbPart/AuthorBean!com.netcracker.ejb.entity.AuthorHome");
            AuthorHome authorHome = (AuthorHome) PortableRemoteObject.narrow(authorObj, AuthorHome.class);
            req.setAttribute("authorsList", authorHome.getAuthorsInfo());

            Object genreObj = ctx.lookup("ear-1.0/ejbPart/GenreBean!com.netcracker.ejb.entity.GenreHome");
            GenreHome genreHome = (GenreHome) PortableRemoteObject.narrow(genreObj, GenreHome.class);
            req.setAttribute("genresList", genreHome.getGenresInfo());

            Object publisherObj = ctx.lookup("ear-1.0/ejbPart/PublisherBean!com.netcracker.ejb.entity.PublisherHome");
            PublisherHome publisherHome = (PublisherHome) PortableRemoteObject.narrow(publisherObj, PublisherHome.class);
            req.setAttribute("publishersList", publisherHome.getPublishersInfo());


        } catch (DataAccessException e) {
            log.error("Error occurs during work with database", e);
            throw new ServletException("Errors occurs during the work with database", e);
        } catch (NamingException e) {
            throw new ServletException("Can't lookup bean", e);
        }
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/addBook.jsp").forward(req, resp);
    }
}
