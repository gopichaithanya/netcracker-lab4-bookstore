package com.netcracker.servlets;

import com.netcracker.ejb.entity.AuthorHome;
import com.netcracker.ejb.entity.AuthorRemote;
import com.netcracker.ejb.entity.GenreHome;
import com.netcracker.ejb.entity.GenreRemote;
import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.Helper;
import org.apache.log4j.Logger;

import javax.ejb.FinderException;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MainPageHandler extends HttpServlet {

    private static final Logger log = Logger.getLogger(MainPageHandler.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Lookup bean's home interfaces
            //--------------------------------------------------
            final String genreJNDI = "ear-1.0/ejbPart/GenreBean!com.netcracker.ejb.entity.GenreHome";
            Object obj1 = Helper.getInstance().getContext().lookup(genreJNDI);
            GenreHome genreHome = (GenreHome) PortableRemoteObject.narrow(obj1, GenreHome.class);
            //--------------------------------------------------
            final String authorJNDI = "ear-1.0/ejbPart/AuthorBean!com.netcracker.ejb.entity.AuthorHome";
            Object obj2 = Helper.getInstance().getContext().lookup(authorJNDI);
            AuthorHome authorHome = (AuthorHome) PortableRemoteObject.narrow(obj2, AuthorHome.class);
            //--------------------------------------------------

            HttpSession session = req.getSession();
            //req.setAttribute("authorsList", dao.getAllAuthors());
            //req.setAttribute("genresList", dao.getAllGenres());
            req.setAttribute("authorsList", authorHome.getAuthorsInfo());
            req.setAttribute("genresList", genreHome.getGenresInfo());

            // Handling button's actions that were clicked on the main page (authors and genres lists)
            /*
             * Parse information about selected author
             * If user choose one of the author the session scope variable will contain information
             * about this author. If user hasn't chosen any author (clicked on "any" selector) this
             * variable will contain null reference
             * */

            String selectedAuthorId = req.getParameter("author");
            AuthorRemote selectedAuthor  = null;
            if (selectedAuthorId != null) {
                int authorId = Integer.parseInt(selectedAuthorId);
                // Negative value of the authorId variable shows that the user has chosen "any" selector
                if (authorId >= 0) {
                    selectedAuthor = authorHome.findByPrimaryKey(new Integer(authorId));
                }
            }
            session.setAttribute("selAuthor", selectedAuthor);


            /*
            * A behavior of the session scope variable selectedGenre is similar as a selectedAuthor
            * variable
            * */
            String selectedGenreId = req.getParameter("genre");
            GenreRemote selectedGenre = null;
            if (selectedGenreId != null) {
                int genreId = Integer.parseInt(selectedGenreId);
                if (genreId >= 0) {
                    selectedGenre = genreHome.findByPrimaryKey(new Integer(genreId));
                }
            }
            session.setAttribute("selGenre", selectedGenre);

            /*List<ShortBookDescr> books = dao.getBooksShortDescriptions(selectedAuthor, selectedGenre);
            req.setAttribute("searchResult", books);*/

            /*
             * Facility that allow user to scroll pages.
             * In other word if there are many books per one page this books divided on
             * the several pages and user can navigate through this pages
             * */

            /*String curPage = req.getParameter("page"); //Current (selected by user) page
            final int booksPerPage = 8; //Maximum number of books placed on one page
            final int pagesAmount;
            if (books.size() % booksPerPage == 0) {
                pagesAmount = books.size() / booksPerPage;
            } else {
                pagesAmount = books.size() / booksPerPage + 1;
            }
            req.setAttribute("pagesAmount", pagesAmount);

            if (curPage == null) {
                req.setAttribute("currentPage", "1");
                req.setAttribute("lowBookIndex", 0);
                req.setAttribute("topBookIndex", booksPerPage - 1);
            } else {
                req.setAttribute("currentPage", curPage);
                int currentPage = Integer.parseInt(curPage);
                int lowBookIndex = (currentPage - 1)*booksPerPage;
                int topBookIndex = currentPage*booksPerPage - 1;
                req.setAttribute("lowBookIndex", lowBookIndex);
                req.setAttribute("topBookIndex", topBookIndex);
            }*/

            // Pass control to the main page of application
            req.getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);

        } catch (NamingException e) {
            throw new ServletException("Can't lookup bean", e);
        } catch (DataAccessException e) {
            log.error("Error occurs during work with database", e);
            throw new ServletException("Errors occurs during the work with database", e);
        } catch (FinderException e) {
            throw new ServletException("Can't find EJB with specified primary key",e);
        }
    }
}
