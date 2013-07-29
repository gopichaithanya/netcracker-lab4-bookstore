package com.netcracker.servlets;

import com.netcracker.ejb.entity.PublisherHome;
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

public class AddPublisher extends HttpServlet {

    private static final Logger log = Logger.getLogger(AddPublisher.class);

    private String name;
    private String url;

    private PublisherHome publisherHome;


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Context ctx = Helper.getInstance().getContext();
            name = req.getParameter("name");
            url = req.getParameter("url");
            Object obj = ctx.lookup("ear-1.0/ejbPart/PublisherBean!com.netcracker.ejb.entity.PublisherHome");
            publisherHome = (PublisherHome) PortableRemoteObject.narrow(obj, PublisherHome.class);
            if (isPublisherExist(name, url)) {
                req.setAttribute("alreadyExist", true);
                doGet(req, resp);
                req.setAttribute("alreadyExist", false);
            } else if (!isPublisherValid(name, url)) {
                req.setAttribute("error", true);
                doGet(req, resp);
                req.setAttribute("error", false);
            } else {
                publisherHome.create(name, url);
                log.info("PublisherBean with " + name + " successfully added.");

                if ((Boolean)req.getSession().getAttribute("fromAddBookPage")) {
                    resp.sendRedirect("http://localhost:8080/bookStore/addBook");
                } else {
                    req.getServletContext().getRequestDispatcher("/books").forward(req, resp);
                }

            }
        } catch (CreateException e) {
            log.error("Error occurs during adding publisher to the database", e);
            throw new ServletException(e);
        } catch (NamingException e) {
            log.error("Error occurs during adding publisher to the database", e);
            throw new ServletException(e);
        }

    }

    /**
     * Figure out is publisher with such name and URL already exist or not.
     *
     * @param name         publisher's name
     * @param url          publisher's URL
     * @return                  exist or not

     * @throws ServletException
     */
    private boolean isPublisherExist(String name, String url) throws ServletException {
        boolean publisherExist;
        try {
            publisherExist = publisherHome.isPublisherExist(name, url);

        } catch (RemoteException e) {
            log.error("Error occurs during finding out is publisher exist in the database", e);
            throw new ServletException(e);
        } catch (DataAccessException e) {
            log.error("Error occurs during finding out is publisher exist in the database", e);
            throw new ServletException(e);
        }
        return publisherExist;
    }

    private boolean isPublisherValid(String name, String url) {
        return !(name.isEmpty()
                || url.isEmpty());
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/addPublisher.jsp").forward(req, resp);
    }
}
