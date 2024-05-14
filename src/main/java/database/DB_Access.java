package database;

import pojos.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DB_Access implements AutoCloseable {
    private static DB_Access instance = null;
    private DB_Database db;

    private PreparedStatement insertGradeStatement = null;

    private static final String INSERT_GRADE_SQL = "INSERT INTO grade " +
            "(student_id, subject, given_grade) " +
            "VALUES (?, ?, ?)";

    public static DB_Access getInstance() {
        if (instance == null) {
            instance = new DB_Access();
        }
        return instance;
    }

    private DB_Access() {
        db = DB_Database.getInstance();
    }

    /**
     * Get a list of all students and their average grade
     * @return students
     * @throws SQLException
     */
    public List<Student> getStudentsWithAvgGrade() throws SQLException {
        List<Student> students = new ArrayList<>();

        String sqlString = "SELECT student.student_id, student.cat_number, student.first_name, " +
                "student.last_name, student.date_of_birth, student.size, AVG(grade.given_grade) AS avg " +
                "FROM student INNER JOIN grade ON student.student_id = grade.student_id " +
                "GROUP BY student.student_id, student.cat_number, student.first_name, " +
                "student.last_name, student.date_of_birth, student.size";

        Statement statement = db.getStatement();
        ResultSet rs = statement.executeQuery(sqlString);

        while (rs.next()) {
            students.add(new Student(
                    rs.getInt("student_id"),
                    rs.getInt("cat_number"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("date_of_birth").toLocalDate(),
                    rs.getFloat("size"),
                    rs.getFloat("avg")
            ));
        }

        rs.close();
        db.releaseStatement(statement);

        return students;
    }

    public void insertNewGrade(Integer student_id, String subject, int given_grade) throws SQLException {
        // Be aware of SQL Injection!
        if(insertGradeStatement == null) {
            insertGradeStatement = db.getConnection().prepareStatement(INSERT_GRADE_SQL);
        }

        insertGradeStatement.setInt(1, student_id);
        insertGradeStatement.setString(2, subject);
        insertGradeStatement.setInt(3, given_grade);

        insertGradeStatement.executeUpdate();
    }

    @Override
    public void close() throws Exception {
        db.close();
        System.out.println("Everything done - Bye!");
    }

    public static void main(String[] args) {

        try (DB_Access dba = DB_Access.getInstance()) {
            List<Student> students = dba.getStudentsWithAvgGrade();
            for (Student student : students) {
                System.out.println(student);
            }

            System.out.println("----------------------------");
            dba.insertNewGrade(2, "POS", 4);

            students = dba.getStudentsWithAvgGrade();
            for (Student student : students) {
                System.out.println(student);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
