package com.netcracker.servlets;

import com.netcracker.ejb.entity.GenreHome;
import com.netcracker.ejb.entity.PublisherHome;
import com.netcracker.ejb.session.BookSearchService;
import com.netcracker.ejb.session.BookSearchServiceHome;
import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.Helper;
import org.apache.commons.lang.StringUtils;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public class FindBook extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Lookup bean's home interface
            //--------------------------------------------------
            final String genreJNDI = "ear-1.0/ejbPart/GenreBean!com.netcracker.ejb.entity.GenreHome";
            Object obj1 = Helper.getInstance().getContext().lookup(genreJNDI);
            GenreHome genreHome = (GenreHome) PortableRemoteObject.narrow(obj1, GenreHome.class);

            final String publishJNDI = "ear-1.0/ejbPart/PublisherBean!com.netcracker.ejb.entity.PublisherHome";
            Object obj2 = Helper.getInstance().getContext().lookup(publishJNDI);
            PublisherHome publishHome = (PublisherHome) PortableRemoteObject.narrow(obj2, PublisherHome.class);
            //--------------------------------------------------
            req.setAttribute("genres", genreHome.getGenresInfo());
            req.setAttribute("publishers", publishHome.getPublishersInfo());
            req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/findBook.jsp").forward(req, resp);
        } catch (NamingException e) {
            throw new ServletException("Can't lookup genre's home interface", e);
        } catch (DataAccessException e) {
           throw new ServletException("Error occurs during the work with database", e);
        }
    }



    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final String searchJNDI = "ear-1.0/ejbPart/BookSearchService!com.netcracker.ejb.session.BookSearchServiceHome";
            Object obj1 = Helper.getInstance().getContext().lookup(searchJNDI);
            BookSearchServiceHome searchHome = (BookSearchServiceHome) PortableRemoteObject.narrow(obj1, BookSearchServiceHome.class);
            BookSearchService searchService = searchHome.create();

            String[] paramNames = {"title", "author", "publisher", "genre", "year"};
            String title = StringUtils.isNotBlank(req.getParameter(paramNames[0])) ? req.getParameter(paramNames[0]) : "";
            String author = StringUtils.isNotBlank(req.getParameter(paramNames[1])) ? req.getParameter(paramNames[1]) : "";
            int publisher = Integer.parseInt(StringUtils.isNotBlank(req.getParameter(paramNames[2])) ?
                                                                                      req.getParameter(paramNames[2]) :
                                                                                      String.valueOf(-1));
            int genreId = Integer.parseInt(StringUtils.isNotBlank(req.getParameter(paramNames[3])) ?
                                                                                    req.getParameter(paramNames[3]) :
                                                                                    String.valueOf(-1));
            int year = Integer.parseInt(StringUtils.isNotBlank(req.getParameter(paramNames[4])) ?
                                                                                 req.getParameter(paramNames[4]) :
                                                                                 String.valueOf(-1));

            req.setAttribute("searchResults", searchService.getBookInfoBySearchCriteria(title, author, publisher, genreId, year));
            doGet(req, resp);
        } catch (NamingException e) {
            throw new ServletException("Can't lookup book search service bean");
        } catch (CreateException e) {
            throw new ServletException("Can't create search service session bean", e);
        } catch (DataAccessException e) {
            throw new ServletException("Can't find books that satisfy a search criteria due to the errors " +
                                       "during the work with database", e);
        }
    }
}
