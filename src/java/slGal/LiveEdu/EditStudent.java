/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu;

import slGal.LiveEdu.ORM.Student;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mlch
 */
public class EditStudent extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EditStudent</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditStudent at " + request.getContextPath () + "</h1>");
            out.println(request.getParameter("id"));
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        int studentId = Integer.parseInt(request.getParameter("id"));
        Student student = (Student) Student.getById(studentId);
        request.setAttribute("student", student );
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/UpdateStudent.jsp");
        dispatcher.forward(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
//        Map<String, String[]> mapRequest = request.getParameterMap();
//        Set<String> keys = mapRequest.keySet();
//        for(String key : keys){
//           String[] strArray = mapRequest.get(key);
//           for(int i = 0; i < strArray.length; i++){
//              strArray[i] = new String(strArray[i].getBytes("ISO-8859-1"), "UTF-8");
//           }
//           mapRequest.put(key, strArray);
//        }
        
        
        int id = Integer.parseInt(request.getParameter("id"));
        
//        String fName = new String(request.getParameter("firstName").getBytes("ISO-8859-1"),"UTF-8");
//        String lName = new String(request.getParameter("lastName").getBytes("ISO-8859-1"),"UTF-8");
//        String patronymic = new String(request.getParameter("patronymic").getBytes("ISO-8859-1"),"UTF-8");
//        String email = new String(request.getParameter("liveEmail").getBytes("ISO-8859-1"),"UTF-8");
//        String pass = new String(request.getParameter("password").getBytes("ISO-8859-1"),"UTF-8");
//        String faculty = new String(request.getParameter("faculty").getBytes("ISO-8859-1"),"UTF-8");
//        String groupa = new String(request.getParameter("group").getBytes("ISO-8859-1"),"UTF-8");
//        int kurs = Integer.parseInt(request.getParameter("cource"));
//        String spec = new String(request.getParameter("spec").getBytes("ISO-8859-1"),"UTF-8");
        
        String fName = request.getParameter("firstName");
        String lName = request.getParameter("lastName");
        String patronymic = request.getParameter("patronymic");
        String email = request.getParameter("liveEmail");
        String pass = request.getParameter("password");
        String faculty = request.getParameter("faculty");
        String groupa = request.getParameter("group");
        int kurs = Integer.parseInt(request.getParameter("cource"));
        String spec = request.getParameter("spec");

        
        Student.UpdateById(id, fName, lName, patronymic, email, pass, faculty, groupa, kurs, spec);
        request.setAttribute("updata", new String("1"));
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Filter");
        dispatcher.forward(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
