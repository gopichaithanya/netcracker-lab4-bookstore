package com.netcracker.servlets;

import com.netcracker.ejb.entity.BookHome;
import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.Helper;
import org.apache.log4j.Logger;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class DeleteBookHandler extends HttpServlet {

    private static final Logger log = Logger.getLogger(DeleteBookHandler.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int bookId = Integer.parseInt(req.getParameter("bookId"));
        try {
            Context ctx = Helper.getInstance().getContext();
            Object obj = ctx.lookup("ear-1.0/ejbPart/BookBean!com.netcracker.ejb.entity.BookHome");
            BookHome bookHome = (BookHome) PortableRemoteObject.narrow(obj, BookHome.class);
            bookHome.findByPrimaryKey(bookId).remove();
            log.info("Book with id= " + bookId + " successfully removed.");
            req.getServletContext().getRequestDispatcher("/books").forward(req, resp);

        }  catch (NamingException e) {
            throw new ServletException("Can't lookup bean", e);
        } catch (FinderException e) {
            throw new ServletException("Can't find EJB with specified primary key",e);
        } catch (DataAccessException e) {
            log.error("Error occurs during work with database", e);
            throw new ServletException("Errors occurs during the work with database", e);
        } catch (RemoveException e) {
            throw new ServletException("Errors occurs during book removing. The Book bean or the container does not allow the EJB object to be removed", e);
        }
    }

}
