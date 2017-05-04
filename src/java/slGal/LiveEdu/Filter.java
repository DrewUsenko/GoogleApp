/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu;

import slGal.LiveEdu.ORM.Student;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import slGal.LiveEdu.DB.HibernateUtil;

/**
 *
 * @author mlch
 */
public class Filter extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session ses = null;
        ses = HibernateUtil.currentSession();
        List students = new ArrayList<Student>();
        Criteria cr = ses.createCriteria(Student.class);
        //String str = request.getAttribute("updata");
        
        if (request.getAttribute("updata") == null) {
            // restictions
            String str = request.getParameter("cource");
            if (str != null && !str.equals("")) {
                if (str.equals("null")) {
                    cr = cr.add(Restrictions.isNull("kurs"));
                } else {
                    int kurs = Integer.parseInt(str);
                    cr = cr.add(Restrictions.eq("kurs", kurs));
                }
            }
            str = request.getParameter("spec");
            if (str != null && !str.equals("")) {
                if (str.equals("null")) {
                    cr = cr.add(Restrictions.isNull("spec"));
                } else {
                    //int spec = Integer.parseInt(str);
                    cr = cr.add(Restrictions.eq("spec", str));
                }
            }

            str = request.getParameter("group");
            if (str != null && !str.equals("")) {
                cr = cr.add(Restrictions.eq("groupa", str));
            } // restrictions
        }
        students = cr.list();
        HibernateUtil.closeSession();
        request.setAttribute("students", students);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/StudentsList.jsp");
        dispatcher.forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
