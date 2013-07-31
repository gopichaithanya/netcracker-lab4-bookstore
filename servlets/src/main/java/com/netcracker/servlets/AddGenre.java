package com.netcracker.servlets;

import com.netcracker.ejb.entity.GenreHome;
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
import java.io.IOException;
import java.rmi.RemoteException;

public class AddGenre extends HttpServlet {

    private static final Logger log = Logger.getLogger(AddGenre.class);

    private GenreHome genreHome;


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Context ctx = Helper.getInstance().getContext();
            String name = req.getParameter("name");
            Object obj = ctx.lookup("ear-1.0/ejbPart/GenreBean!com.netcracker.ejb.entity.GenreHome");
            genreHome = (GenreHome) PortableRemoteObject.narrow(obj, GenreHome.class);
            if (isGenreExist(name)) {
                req.setAttribute("alreadyExist", true);
                doGet(req, resp);
                req.setAttribute("alreadyExist", false);
            } else if (!isGenreValid(name)) {
                req.setAttribute("error", true);
                doGet(req, resp);
                req.setAttribute("error", false);
            } else {
                genreHome.create(name);
                log.info("GenreBean  " + name +  " successfully added.");


                if ((Boolean)req.getSession().getAttribute("fromAddBookPage")) {
                    resp.sendRedirect("http://localhost:8080/bookStore/addBook");
                } else {
                    req.getServletContext().getRequestDispatcher("/books").forward(req, resp);
                }

            }
        } catch (CreateException e) {
            log.error("Error occurs during adding genre to the database", e);
            throw new ServletException(e);
        } catch (NamingException e) {
            log.error("Error occurs during adding genre to the database", e);
            throw new ServletException(e);
        }

    }

    /**
     * Figure out is genre with such name already exist or not.
     *
     * @param name              genres name
     * @return                  exist or not

     * @throws ServletException
     */
    private boolean isGenreExist(String name) throws ServletException {
        boolean genreExist;
        try {
            genreExist = genreHome.isGenreExist(name);

        } catch (RemoteException e) {
            log.error("Error occurs during finding out is genre exist in the database", e);
            throw new ServletException(e);
        } catch (DataAccessException e) {
            log.error("Error occurs during finding out is genre exist in the database", e);
            throw new ServletException(e);
        }
        return genreExist;
    }

    private boolean isGenreValid(String name) {
        return !(name.isEmpty()
                || name.matches(".*\\d.*"));
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/addGenre.jsp").forward(req, resp);
    }
}
