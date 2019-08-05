package com.web.jdbc;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.sql.DataSource;

public class StudentDbUtil {

	// Define datasource/connection pool for Resource Injection
	@Resource(name = "jdbc/web_student_tracker")
	private static DataSource dataSource;

	public StudentDbUtil(DataSource thisDataSource) {
		dataSource = thisDataSource;
	}

	public static List<Student> getStudents() throws Exception {

		List<Student> students = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;

		// create prepared statement
		String sql = "select * from web_student_tracker.student order by last_name";

		try {

			// get a connection to database
			con = dataSource.getConnection();

			pstmt = con.prepareStatement(sql);

			// execute query
			rst = pstmt.executeQuery(sql);

			// process result set
			while (rst.next()) {
				// retrieve data from result set
				int id = rst.getInt("id");
				String firstname = rst.getString("first_name");
				String lastname = rst.getString("last_name");
				String email = rst.getString("email");
				// add it to list of students
				students.add(new Student(id, firstname, lastname, email));
			}

			for (Student p : students) {
				System.out.println(p.getFirstName() + "+" + p.getLastName());
			}

			return students;

		} finally {
			// close JDBC objects
			close(con, pstmt, rst);
		}

	}

	private static void close(Connection con, PreparedStatement pstmt, ResultSet rst) {
		try {
			if (rst != null) {
				rst.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				// only closes connection pool for current object
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addStudent(Student newStudent) throws SQLException {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// get mySQL connection, then create query to insert
			con = dataSource.getConnection();
			String query = "insert into web_student_tracker.student" + "(first_name, last_name, email) "
					+ "values(?, ?, ?)";
			pstmt = con.prepareStatement(query);

			// set the parameter values for student
			pstmt.setString(1, newStudent.getFirstName());
			pstmt.setString(2, newStudent.getLastName());
			pstmt.setString(3, newStudent.getEmail());

			System.out.print(newStudent.getFirstName());
			System.out.print(newStudent.getLastName());
			System.out.println(newStudent.getEmail());

			// execute query insert
			pstmt.execute();
		} finally {
			// clean up JDBC objects
			close(con, pstmt, null);
		}
	}

	public static void main(String[] args) throws Exception {

//		List<Student> temp = null;
//		try {
//			temp = StudentDbUtil.getStudents();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		for (Student p : temp) {
//			System.out.println(p.getFirstName() + " " + p.getLastName());
//		}
//		Student student = new Student("Mike", "Cob", "mikecob@gmail.com");
//		StudentDbUtil.addStudent(student);
	}

	public Student getStudent(String newStudentID) throws SQLException {

		Student newStudent = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		int studentID;

		try {
			// convert student id to int
			studentID = Integer.parseInt(newStudentID);

			// get connection to database
			con = dataSource.getConnection();

			// create query to get selected student
			String query = "select * from student where id = ?;";

			// assign prepared statement
			pstmt = con.prepareStatement(query);

			// set parameters
			pstmt.setInt(1, studentID);

			// execute prepared statement
			rst = pstmt.executeQuery();

			// retrieve data from result set row
			if (rst.next()) {
				String firstname = rst.getString("first_name");
				String lastname = rst.getString("last_name");
				String email = rst.getString("email");

				// use the student ID for construction
				newStudent = new Student(studentID, firstname, lastname, email);
			} else {
				throw new SQLException("Could not find student id: " + studentID);
			}

			return newStudent;

		} finally {
			// close connections
			close(con, pstmt, rst);
		}
	}

	public void updateStudent(Student student) throws SQLException {

		// set up database connection
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// connect to database
			con = dataSource.getConnection();

			// update database with new student info
			String query = "update student set first_name = ?, last_name = ?, email = ? where id = ?;";

			pstmt = con.prepareStatement(query);

			// set parameters
			pstmt.setString(1, student.getFirstName());
			pstmt.setString(2, student.getLastName());
			pstmt.setString(3, student.getEmail());
			pstmt.setInt(4, student.getId());

			// execute query
			pstmt.execute();
		} finally {
			// clean up
			close(con, pstmt, null);
		}
	}

	public void deleteStudent(String id) throws SQLException {

		// initialize connections
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// connect to database
			con = dataSource.getConnection();

			// update database with delete query
			String query = "delete from student where id = ?;";

			pstmt = con.prepareStatement(query);

			pstmt.setInt(1, Integer.parseInt(id));

			// execute query
			pstmt.execute();
		} finally {
			close(con, pstmt, null);
		}
	}

	public List<Student> searchStudents(String searchName) throws Exception {

		List<Student> students = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String query = "select * from student " + "where lower(first_name) like ? " + "or lower(last_name) like ?";

		try {
			// connect to database
			con = dataSource.getConnection();

			// search if name field is not empty
			if (searchName.trim().length() > 0 && searchName != null) {
				// create prepared statement
				pstmt = con.prepareStatement(query);
				// set parameters for pstmt
				pstmt.setString(1, "%" + searchName.toLowerCase() + "%");
				pstmt.setString(2, "%" + searchName.toLowerCase() + "%");
			} else {
				// return all students in database
				String sql = "select * from student order by last_name";
				// create prepared statement
				pstmt = con.prepareStatement(sql);
			}

			// execute prepared statement
			rst = pstmt.executeQuery();

			// result set to render data
			while (rst.next()) {
				// retrieve data to be assigned
				int id = rst.getInt("id");
				String firstName = rst.getString("first_name");
				String lastName = rst.getString("last_name");
				String email = rst.getString("email");

				// append to student list
				students.add(new Student(id, firstName, lastName, email));
			}
			return students;
		} finally {
			// close JDBC objects
			close(con, pstmt, rst);
		}
	}

}