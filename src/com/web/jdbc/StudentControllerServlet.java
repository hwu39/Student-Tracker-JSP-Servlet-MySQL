package com.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDbUtil studentDbUtil;

	// Define datasource/connection pool for Resource Injection
	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();

		// create student db util and pass in the connection pool
		try {
			setStudentDbUtil(new StudentDbUtil(dataSource));
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// list the students in MVC fashion
		try {
			// read command parameter
			String command = request.getParameter("command");

			// if command is missing, just list current students
			if (command == null) {
				command = "LIST";
			}

			// route to the appropriate method
			switch (command) {
			case ("LIST"):
				listStudents(request, response);
				break;
			case ("LOAD"):
				loadStudent(request, response);
				break;
			case ("UPDATE"):
				updateStudent(request, response);
				break;
			case ("DELETE"):
				deleteStudent(request, response);
				break;
			case ("SEARCH"):
				searchStudents(request, response);
				break;
			default:
				listStudents(request, response);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

	private void searchStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read in name from search form 
		String searchName = request.getParameter("searchName");
		
		// search students from database
		List<Student> students = studentDbUtil.searchStudents(searchName);
		
		// add search results to request
		request.setAttribute("student_list", students);
		
		// send to JSP page to display
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list_students.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// read in command
			String command = request.getParameter("command");

			// switch to appropriate method
			switch (command) {
			case ("ADD"):
				addStudent(request, response);
				break;
			default:
				listStudents(request, response);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read student info from form data
		String id = request.getParameter("studentID");

		// delete student object from database
		studentDbUtil.deleteStudent(id);

		// send back to list-students page
		listStudents(request, response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read student info from form data
		int id = Integer.parseInt(request.getParameter("studentID"));
		String newFirstName = request.getParameter("firstname");
		String newLastName = request.getParameter("lastname");
		String newEmail = request.getParameter("email");

		// update database with new student object
		studentDbUtil.updateStudent(new Student(id, newFirstName, newLastName, newEmail));

		// send back to list-students page
		listStudents(request, response);
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read student id from form data
		String newStudentID = request.getParameter("studentID");

		// get student from database
		Student newStudent = studentDbUtil.getStudent(newStudentID);

		// place student in the request attribute
		request.setAttribute("the_student", newStudent);

		// send to jsp page update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from form data
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String email = request.getParameter("email");

		// create a new student
		Student newStudent = new Student(firstname, lastname, email);

		// add student to database
		studentDbUtil.addStudent(newStudent);

		// send back to main page to display new student list
		response.sendRedirect(request.getContextPath() + "/StudentControllerServlet?command=LIST");
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// get students from db util
		List<Student> students = StudentDbUtil.getStudents();

		// add students to the request
		request.setAttribute("student_list", students);

		// send to JSP page
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list_students.jsp");
		dispatcher.forward(request, response);
	}

	public StudentDbUtil getStudentDbUtil() {
		return studentDbUtil;
	}

	public void setStudentDbUtil(StudentDbUtil studentDbUtil) {
		this.studentDbUtil = studentDbUtil;
	}

}
