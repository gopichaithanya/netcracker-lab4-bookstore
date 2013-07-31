package com.netcracker.servlets;

import com.netcracker.ejb.entity.*;
import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.Helper;
import org.apache.log4j.Logger;

import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;import java.lang.Object;import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;


public class ShowMoreHandler extends HttpServlet {

    private static final Logger log = Logger.getLogger(ShowMoreHandler.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getServletContext().getRequestDispatcher("/books").forward(req, resp);


    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int bookId = Integer.parseInt(req.getParameter("bookId"));
        try {
            Context ctx =  Helper.getInstance().getContext();Object obj = ctx.lookup("ear-1.0/ejbPart/BookBean!com.netcracker.ejb.entity.BookHome");
            BookHome bookHome = (BookHome) PortableRemoteObject.narrow(obj, BookHome.class);
            Book book = bookHome.findByPrimaryKey(bookId);

            Object obj1 = ctx.lookup("ear-1.0/ejbPart/AuthorBean!com.netcracker.ejb.entity.AuthorHome");
            AuthorHome authorHome = (AuthorHome) PortableRemoteObject.narrow(obj1, AuthorHome.class);



            Collection<Author> authors = new ArrayList<Author>();

            for (Integer authorId: book.getAuthorIds()) {
                authors.add(authorHome.findByPrimaryKey(authorId));
            }

            req.setAttribute("publisher", book.getPublisher());
            req.setAttribute("genre", book.getGenre());
            req.setAttribute("searchedBook", book);
            req.setAttribute("authors", authors);

            System.out.println("---------------------------------------------");
            System.out.println("Image Reference=  " + book.getImageReference() );
        } catch (NamingException e) {
            throw new ServletException("Can't lookup bean", e);
        } catch (FinderException e) {
            throw  new ServletException("Can't find EJB with specified primary key");
        } catch (DataAccessException e) {
            log.error("Error occurs during work with database", e);
           throw new ServletException("Error occurs during work with database", e);
        }

        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/showMore.jsp").forward(req, resp);
    }
}
