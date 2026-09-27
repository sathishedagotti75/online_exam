package jdbc;

import java.sql.*;
import java.util.Scanner;

public class online_exam {

    // =====================================================
    // DATABASE DETAILS
    // =====================================================

    static String url = "jdbc:mysql://localhost:3306/";
    static String database = "ONLINE_EXAMINATION";
    static String username = "root";
    static String password = "root";

    static Connection con;
    static Scanner sc = new Scanner(System.in);

    // =====================================================
    // MAIN METHOD
    // =====================================================

    public static void main(String[] args) {

        try {

            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL server
            con = DriverManager.getConnection(
                    url,
                    username,
                    password
            );

            System.out.println("MySQL Connected Successfully!");

            // Create database
            createDatabase();

            // Close server connection
            con.close();

            // Connect to ONLINE_EXAMINATION database
            con = DriverManager.getConnection(
                    url + database,
                    username,
                    password
            );

            // Create tables
            createTables();

            // Start menu
            int choice;

            do {

                System.out.println("\n");
                System.out.println("================================================");
                System.out.println("        ONLINE EXAMINATION SYSTEM");
                System.out.println("================================================");
                System.out.println("1.  Student Registration");
                System.out.println("2.  Student Login");
                System.out.println("3.  Create Exam");
                System.out.println("4.  Add Question");
                System.out.println("5.  View Exams");
                System.out.println("6.  View Questions");
                System.out.println("7.  Take Exam");
                System.out.println("8.  View All Results");
                System.out.println("9.  Search Result");
                System.out.println("10. Update Student");
                System.out.println("11. Delete Student");
                System.out.println("12. Exam Statistics");
                System.out.println("13. Exit");
                System.out.println("================================================");

                System.out.print("Enter your choice: ");
                choice = sc.nextInt();

                switch (choice) {

                    case 1:
                        studentRegistration();
                        break;

                    case 2:
                        studentLogin();
                        break;

                    case 3:
                        createExam();
                        break;

                    case 4:
                        addQuestion();
                        break;

                    case 5:
                        viewExams();
                        break;

                    case 6:
                        viewQuestions();
                        break;

                    case 7:
                        takeExam();
                        break;

                    case 8:
                        viewResults();
                        break;

                    case 9:
                        searchResult();
                        break;

                    case 10:
                        updateStudent();
                        break;

                    case 11:
                        deleteStudent();
                        break;

                    case 12:
                        examStatistics();
                        break;

                    case 13:
                        System.out.println(
                                "Thank you for using Online Examination System!"
                        );
                        break;

                    default:
                        System.out.println("Invalid choice!");

                }

            } while (choice != 13);

            con.close();
            sc.close();

        } catch (Exception e) {

            System.out.println(
                    "Database Error: " + e.getMessage()
            );
        }
    }

    // =====================================================
    // CREATE DATABASE
    // =====================================================

    static void createDatabase() throws SQLException {

        Statement stmt = con.createStatement();

        String sql =
                "CREATE DATABASE IF NOT EXISTS ONLINE_EXAMINATION";

        stmt.executeUpdate(sql);

        stmt.close();

        System.out.println("Database ready.");
    }

    // =====================================================
    // CREATE TABLES
    // =====================================================

    static void createTables() throws SQLException {

        Statement stmt = con.createStatement();

        // =================================================
        // STUDENTS TABLE
        // =================================================

        String studentTable =
                "CREATE TABLE IF NOT EXISTS students (" +
                "student_id INT PRIMARY KEY AUTO_INCREMENT," +
                "student_name VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) UNIQUE NOT NULL," +
                "password VARCHAR(100) NOT NULL" +
                ")";

        stmt.executeUpdate(studentTable);

        // =================================================
        // EXAMS TABLE
        // =================================================

        String examTable =
                "CREATE TABLE IF NOT EXISTS exams (" +
                "exam_id INT PRIMARY KEY AUTO_INCREMENT," +
                "exam_name VARCHAR(100) NOT NULL," +
                "subject VARCHAR(100) NOT NULL," +
                "total_marks INT NOT NULL" +
                ")";

        stmt.executeUpdate(examTable);

        // =================================================
        // QUESTIONS TABLE
        // =================================================

        String questionTable =
                "CREATE TABLE IF NOT EXISTS questions (" +
                "question_id INT PRIMARY KEY AUTO_INCREMENT," +
                "exam_id INT NOT NULL," +
                "question_text VARCHAR(500) NOT NULL," +
                "option_a VARCHAR(200)," +
                "option_b VARCHAR(200)," +
                "option_c VARCHAR(200)," +
                "option_d VARCHAR(200)," +
                "correct_answer CHAR(1)," +
                "FOREIGN KEY(exam_id) REFERENCES exams(exam_id)" +
                ")";

        stmt.executeUpdate(questionTable);

        // =================================================
        // RESULTS TABLE
        // =================================================

        String resultTable =
                "CREATE TABLE IF NOT EXISTS results (" +
                "result_id INT PRIMARY KEY AUTO_INCREMENT," +
                "student_id INT NOT NULL," +
                "exam_id INT NOT NULL," +
                "marks INT NOT NULL," +
                "total_questions INT NOT NULL," +
                "percentage DECIMAL(5,2)," +
                "exam_date DATE," +
                "FOREIGN KEY(student_id) " +
                "REFERENCES students(student_id)," +
                "FOREIGN KEY(exam_id) " +
                "REFERENCES exams(exam_id)" +
                ")";

        stmt.executeUpdate(resultTable);

        stmt.close();

        System.out.println("Tables ready.");
    }

    // =====================================================
    // 1. STUDENT REGISTRATION
    // =====================================================

    static void studentRegistration() {

        try {

            sc.nextLine();

            System.out.print("Enter student name: ");
            String name = sc.nextLine();

            System.out.print("Enter email: ");
            String email = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            String sql =
                    "INSERT INTO students " +
                    "(student_name, email, password) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);

            ps.executeUpdate();

            System.out.println(
                    "Student registered successfully!"
            );

            ps.close();

        } catch (SQLException e) {

            System.out.println(
                    "Registration failed: " +
                    e.getMessage()
            );
        }
    }

    // =====================================================
    // 2. STUDENT LOGIN
    // =====================================================

    static void studentLogin() {

        try {

            sc.nextLine();

            System.out.print("Enter email: ");
            String email = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            String sql =
                    "SELECT * FROM students " +
                    "WHERE email = ? AND password = ?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                System.out.println("\n");
                System.out.println(
                        "================================"
                );

                System.out.println(
                        "          LOGIN SUCCESS"
                );

                System.out.println(
                        "================================"
                );

                System.out.println(
                        "Student ID : " +
                        rs.getInt("student_id")
                );

                System.out.println(
                        "Name       : " +
                        rs.getString("student_name")
                );

                System.out.println(
                        "Email      : " +
                        rs.getString("email")
                );

                System.out.println(
                        "================================"
                );

            } else {

                System.out.println(
                        "Invalid email or password."
                );
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }

    // =====================================================
    // 3. CREATE EXAM
    // =====================================================

    static void createExam() {

        try {

            sc.nextLine();

            System.out.print("Enter exam name: ");
            String examName = sc.nextLine();

            System.out.print("Enter subject: ");
            String subject = sc.nextLine();

            System.out.print("Enter total marks: ");
            int totalMarks = sc.nextInt();

            String sql =
                    "INSERT INTO exams " +
                    "(exam_name, subject, total_marks) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, examName);
            ps.setString(2, subject);
            ps.setInt(3, totalMarks);

            ps.executeUpdate();

            System.out.println(
                    "Exam created successfully!"
            );

            ps.close();

        } catch (SQLException e) {

            System.out.println(
                    "Exam creation failed: " +
                    e.getMessage()
            );
        }
    }

    // =====================================================
    // 4. ADD QUESTION
    // =====================================================

    static void addQuestion() {

        try {

            sc.nextLine();

            System.out.print("Enter exam ID: ");
            int examId = sc.nextInt();

            sc.nextLine();

            // Check whether exam exists
            String checkSQL =
                    "SELECT exam_id FROM exams " +
                    "WHERE exam_id = ?";

            PreparedStatement checkPS =
                    con.prepareStatement(checkSQL);

            checkPS.setInt(1, examId);

            ResultSet checkRS =
                    checkPS.executeQuery();

            if (!checkRS.next()) {

                System.out.println(
                        "Exam does not exist."
                );

                checkRS.close();
                checkPS.close();

                return;
            }

            checkRS.close();
            checkPS.close();

            System.out.print("Enter question: ");
            String question = sc.nextLine();

            System.out.print("Enter option A: ");
            String optionA = sc.nextLine();

            System.out.print("Enter option B: ");
            String optionB = sc.nextLine();

            System.out.print("Enter option C: ");
            String optionC = sc.nextLine();

            System.out.print("Enter option D: ");
            String optionD = sc.nextLine();

            System.out.print(
                    "Enter correct answer (A/B/C/D): "
            );

            String correctAnswer =
                    sc.nextLine().toUpperCase();

            if (!correctAnswer.equals("A") &&
                !correctAnswer.equals("B") &&
                !correctAnswer.equals("C") &&
                !correctAnswer.equals("D")) {

                System.out.println(
                        "Invalid correct answer."
                );

                return;
            }

            String sql =
                    "INSERT INTO questions " +
                    "(exam_id, question_text, " +
                    "option_a, option_b, option_c, " +
                    "option_d, correct_answer) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, examId);
            ps.setString(2, question);
            ps.setString(3, optionA);
            ps.setString(4, optionB);
            ps.setString(5, optionC);
            ps.setString(6, optionD);
            ps.setString(7, correctAnswer);

            ps.executeUpdate();

            System.out.println(
                    "Question added successfully!"
            );

            ps.close();

        } catch (SQLException e) {

            System.out.println(
                    "Question addition failed: " +
                    e.getMessage()
            );
        }
    }

    // =====================================================
    // 5. VIEW EXAMS
    // =====================================================

    static void viewExams() {

        try {

            String sql =
                    "SELECT e.exam_id, " +
                    "e.exam_name, " +
                    "e.subject, " +
                    "e.total_marks, " +
                    "COUNT(q.question_id) AS " +
                    "total_questions " +
                    "FROM exams e " +
                    "LEFT JOIN questions q " +
                    "ON e.exam_id = q.exam_id " +
                    "GROUP BY e.exam_id, " +
                    "e.exam_name, e.subject, e.total_marks";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            System.out.println("\n");
            System.out.println(
                    "========== AVAILABLE EXAMS =========="
            );

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println(
                        "Exam ID        : " +
                        rs.getInt("exam_id")
                );

                System.out.println(
                        "Exam Name      : " +
                        rs.getString("exam_name")
                );

                System.out.println(
                        "Subject        : " +
                        rs.getString("subject")
                );

                System.out.println(
                        "Total Marks    : " +
                        rs.getInt("total_marks")
                );

                System.out.println(
                        "Total Questions: " +
                        rs.getInt("total_questions")
                );

                System.out.println(
                        "--------------------------------------"
                );
            }

            if (!found) {

                System.out.println(
                        "No exams available."
                );
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }

    // =====================================================
    // 6. VIEW QUESTIONS
    // =====================================================

    static void viewQuestions() {

        try {

            System.out.print("Enter exam ID: ");
            int examId = sc.nextInt();

            String sql =
                    "SELECT e.exam_name, " +
                    "q.question_id, " +
                    "q.question_text, " +
                    "q.option_a, " +
                    "q.option_b, " +
                    "q.option_c, " +
                    "q.option_d " +
                    "FROM exams e " +
                    "JOIN questions q " +
                    "ON e.exam_id = q.exam_id " +
                    "WHERE e.exam_id = ?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, examId);

            ResultSet rs =
                    ps.executeQuery();

            System.out.println("\n");
            System.out.println(
                    "========== QUESTIONS =========="
            );

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println(
                        "Exam: " +
                        rs.getString("exam_name")
                );

                System.out.println(
                        "Question ID: " +
                        rs.getInt("question_id")
                );

                System.out.println(
                        "Q. " +
                        rs.getString("question_text")
                );

                System.out.println(
                        "A. " +
                        rs.getString("option_a")
                );

                System.out.println(
                        "B. " +
                        rs.getString("option_b")
                );

                System.out.println(
                        "C. " +
                        rs.getString("option_c")
                );

                System.out.println(
                        "D. " +
                        rs.getString("option_d")
                );

                System.out.println(
                        "--------------------------------"
                );
            }

            if (!found) {

                System.out.println(
                        "No questions found."
                );
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }

    // =====================================================
    // 7. TAKE EXAM
    // =====================================================

    static void takeExam() {

        boolean transactionStarted = false;

        try {

            System.out.print("Enter student ID: ");
            int studentId = sc.nextInt();

            System.out.print("Enter exam ID: ");
            int examId = sc.nextInt();

            // =============================================
            // CHECK STUDENT
            // =============================================

            String studentSQL =
                    "SELECT student_name " +
                    "FROM students " +
                    "WHERE student_id = ?";

            PreparedStatement studentPS =
                    con.prepareStatement(studentSQL);

            studentPS.setInt(1, studentId);

            ResultSet studentRS =
                    studentPS.executeQuery();

            if (!studentRS.next()) {

                System.out.println(
                        "Student does not exist."
                );

                studentRS.close();
                studentPS.close();

                return;
            }

            String studentName =
                    studentRS.getString("student_name");

            studentRS.close();
            studentPS.close();

            // =============================================
            // CHECK EXAM
            // =============================================

            String examSQL =
                    "SELECT exam_name, subject " +
                    "FROM exams " +
                    "WHERE exam_id = ?";

            PreparedStatement examPS =
                    con.prepareStatement(examSQL);

            examPS.setInt(1, examId);

            ResultSet examRS =
                    examPS.executeQuery();

            if (!examRS.next()) {

                System.out.println(
                        "Exam does not exist."
                );

                examRS.close();
                examPS.close();

                return;
            }

            String examName =
                    examRS.getString("exam_name");

            String subject =
                    examRS.getString("subject");

            examRS.close();
            examPS.close();

            // =============================================
            // GET QUESTIONS
            // =============================================

            String questionSQL =
                    "SELECT question_id, " +
                    "question_text, " +
                    "option_a, option_b, " +
                    "option_c, option_d, " +
                    "correct_answer " +
                    "FROM questions " +
                    "WHERE exam_id = ? " +
                    "ORDER BY question_id";

            PreparedStatement questionPS =
                    con.prepareStatement(questionSQL);

            questionPS.setInt(1, examId);

            ResultSet questionRS =
                    questionPS.executeQuery();

            int totalQuestions = 0;
            int marks = 0;

            System.out.println("\n");
            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "             ONLINE EXAM"
            );

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "Student : " + studentName
            );

            System.out.println(
                    "Exam    : " + examName
            );

            System.out.println(
                    "Subject : " + subject
            );

            System.out.println(
                    "========================================"
            );

            while (questionRS.next()) {

                totalQuestions++;

                System.out.println("\n");

                System.out.println(
                        "Question " + totalQuestions
                );

                System.out.println(
                        questionRS.getString(
                                "question_text"
                        )
                );

                System.out.println(
                        "A. " +
                        questionRS.getString("option_a")
                );

                System.out.println(
                        "B. " +
                        questionRS.getString("option_b")
                );

                System.out.println(
                        "C. " +
                        questionRS.getString("option_c")
                );

                System.out.println(
                        "D. " +
                        questionRS.getString("option_d")
                );

                System.out.print(
                        "Enter your answer (A/B/C/D): "
                );

                sc.nextLine();

                String answer =
                        sc.nextLine().toUpperCase();

                String correct =
                        questionRS.getString(
                                "correct_answer"
                        );

                if (answer.equals(correct)) {

                    marks++;

                    System.out.println(
                            "Correct!"
                    );

                } else {

                    System.out.println(
                            "Wrong!"
                    );

                    System.out.println(
                            "Correct answer: " +
                            correct
                    );
                }
            }

            questionRS.close();
            questionPS.close();

            // =============================================
            // CHECK QUESTIONS
            // =============================================

            if (totalQuestions == 0) {

                System.out.println(
                        "This exam has no questions."
                );

                return;
            }

            // =============================================
            // CALCULATE PERCENTAGE
            // =============================================

            double percentage =
                    ((double) marks /
                    totalQuestions) * 100;

            // =============================================
            // INSERT RESULT
            // =============================================

            con.setAutoCommit(false);
            transactionStarted = true;

            String resultSQL =
                    "INSERT INTO results " +
                    "(student_id, exam_id, marks, " +
                    "total_questions, percentage, exam_date) " +
                    "VALUES (?, ?, ?, ?, ?, CURDATE())";

            PreparedStatement resultPS =
                    con.prepareStatement(
                            resultSQL
                    );

            resultPS.setInt(1, studentId);
            resultPS.setInt(2, examId);
            resultPS.setInt(3, marks);
            resultPS.setInt(4, totalQuestions);
            resultPS.setDouble(5, percentage);

            resultPS.executeUpdate();

            resultPS.close();

            con.commit();

            con.setAutoCommit(true);
            transactionStarted = false;

            // =============================================
            // DISPLAY RESULT
            // =============================================

            System.out.println("\n");
            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "              EXAM RESULT"
            );

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "Student         : " +
                    studentName
            );

            System.out.println(
                    "Exam            : " +
                    examName
            );

            System.out.println(
                    "Total Questions : " +
                    totalQuestions
            );

            System.out.println(
                    "Correct Answers : " +
                    marks
            );

            System.out.println(
                    "Wrong Answers   : " +
                    (totalQuestions - marks)
            );

            System.out.println(
                    "Percentage      : " +
                    String.format("%.2f", percentage) +
                    "%"
            );

            if (percentage >= 40) {

                System.out.println(
                        "Result          : PASS"
                );

            } else {

                System.out.println(
                        "Result          : FAIL"
                );
            }

            System.out.println(
                    "========================================"
            );

        } catch (Exception e) {

            if (transactionStarted) {

                try {

                    con.rollback();
                    con.setAutoCommit(true);

                } catch (SQLException ex) {

                    System.out.println(
                            ex.getMessage()
                    );
                }
            }

            System.out.println(
                    "Exam failed: " +
                    e.getMessage()
            );
        }
    }

    // =====================================================
    // 8. VIEW ALL RESULTS
    // =====================================================

    static void viewResults() {

        try {

            String sql =
                    "SELECT r.result_id, " +
                    "s.student_name, " +
                    "e.exam_name, " +
                    "e.subject, " +
                    "r.marks, " +
                    "r.total_questions, " +
                    "r.percentage, " +
                    "r.exam_date " +
                    "FROM results r " +
                    "JOIN students s " +
                    "ON r.student_id = s.student_id " +
                    "JOIN exams e " +
                    "ON r.exam_id = e.exam_id " +
                    "ORDER BY r.result_id";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            System.out.println("\n");
            System.out.println(
                    "========== ALL RESULTS =========="
            );

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println(
                        "Result ID       : " +
                        rs.getInt("result_id")
                );

                System.out.println(
                        "Student         : " +
                        rs.getString("student_name")
                );

                System.out.println(
                        "Exam            : " +
                        rs.getString("exam_name")
                );

                System.out.println(
                        "Subject         : " +
                        rs.getString("subject")
                );

                System.out.println(
                        "Marks           : " +
                        rs.getInt("marks") +
                        "/" +
                        rs.getInt("total_questions")
                );

                System.out.println(
                        "Percentage      : " +
                        rs.getDouble("percentage") +
                        "%"
                );

                System.out.println(
                        "Exam Date       : " +
                        rs.getDate("exam_date")
                );

                System.out.println(
                        "--------------------------------"
                );
            }

            if (!found) {

                System.out.println(
                        "No results available."
                );
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }

    // =====================================================
    // 9. SEARCH RESULT
    // =====================================================

    static void searchResult() {

        try {

            System.out.print("Enter result ID: ");
            int resultId = sc.nextInt();

            String sql =
                    "SELECT r.*, " +
                    "s.student_name, " +
                    "s.email, " +
                    "e.exam_name, " +
                    "e.subject " +
                    "FROM results r " +
                    "JOIN students s " +
                    "ON r.student_id = s.student_id " +
                    "JOIN exams e " +
                    "ON r.exam_id = e.exam_id " +
                    "WHERE r.result_id = ?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, resultId);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                System.out.println("\n");
                System.out.println(
                        "================================"
                );

                System.out.println(
                        "             RESULT"
                );

                System.out.println(
                        "================================"
                );

                System.out.println(
                        "Result ID   : " +
                        rs.getInt("result_id")
                );

                System.out.println(
                        "Student     : " +
                        rs.getString("student_name")
                );

                System.out.println(
                        "Email       : " +
                        rs.getString("email")
                );

                System.out.println(
                        "Exam        : " +
                        rs.getString("exam_name")
                );

                System.out.println(
                        "Subject     : " +
                        rs.getString("subject")
                );

                System.out.println(
                        "Marks       : " +
                        rs.getInt("marks") +
                        "/" +
                        rs.getInt("total_questions")
                );

                System.out.println(
                        "Percentage  : " +
                        rs.getDouble("percentage") +
                        "%"
                );

                System.out.println(
                        "Exam Date   : " +
                        rs.getDate("exam_date")
                );

                System.out.println(
                        "================================"
                );

            } else {

                System.out.println(
                        "Result not found."
                );
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }

    // =====================================================
    // 10. UPDATE STUDENT
    // =====================================================

    static void updateStudent() {

        try {

            System.out.print("Enter student ID: ");
            int studentId = sc.nextInt();

            sc.nextLine();

            System.out.print("Enter new name: ");
            String name = sc.nextLine();

            System.out.print("Enter new email: ");
            String email = sc.nextLine();

            System.out.print("Enter new password: ");
            String password = sc.nextLine();

            String sql =
                    "UPDATE students " +
                    "SET student_name = ?, " +
                    "email = ?, " +
                    "password = ? " +
                    "WHERE student_id = ?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setInt(4, studentId);

            int result =
                    ps.executeUpdate();

            if (result > 0) {

                System.out.println(
                        "Student updated successfully!"
                );

            } else {

                System.out.println(
                        "Student not found."
                );
            }

            ps.close();

        } catch (SQLException e) {

            System.out.println(
                    "Update failed: " +
                    e.getMessage()
            );
        }
    }

    // =====================================================
    // 11. DELETE STUDENT
    // =====================================================

    static void deleteStudent() {

        try {

            System.out.print("Enter student ID: ");
            int studentId = sc.nextInt();

            // Check whether student has results
            String checkSQL =
                    "SELECT COUNT(*) " +
                    "FROM results " +
                    "WHERE student_id = ?";

            PreparedStatement checkPS =
                    con.prepareStatement(checkSQL);

            checkPS.setInt(1, studentId);

            ResultSet rs =
                    checkPS.executeQuery();

            rs.next();

            int resultCount =
                    rs.getInt(1);

            rs.close();
            checkPS.close();

            if (resultCount > 0) {

                System.out.println(
                        "Cannot delete student."
                );

                System.out.println(
                        "Student has examination results."
                );

                return;
            }

            String sql =
                    "DELETE FROM students " +
                    "WHERE student_id = ?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, studentId);

            int result =
                    ps.executeUpdate();

            if (result > 0) {

                System.out.println(
                        "Student deleted successfully!"
                );

            } else {

                System.out.println(
                        "Student not found."
                );
            }

            ps.close();

        } catch (SQLException e) {

            System.out.println(
                    "Delete failed: " +
                    e.getMessage()
            );
        }
    }

    // =====================================================
    // 12. EXAM STATISTICS
    // =====================================================

    static void examStatistics() {

        try {

            String sql =
                    "SELECT " +
                    "e.exam_name, " +
                    "COUNT(r.result_id) AS " +
                    "students_attempted, " +
                    "AVG(r.percentage) AS " +
                    "average_percentage, " +
                    "MAX(r.percentage) AS " +
                    "highest_percentage, " +
                    "MIN(r.percentage) AS " +
                    "lowest_percentage " +
                    "FROM exams e " +
                    "LEFT JOIN results r " +
                    "ON e.exam_id = r.exam_id " +
                    "GROUP BY e.exam_id, e.exam_name";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            System.out.println("\n");
            System.out.println(
                    "========== EXAM STATISTICS =========="
            );

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println(
                        "Exam Name          : " +
                        rs.getString("exam_name")
                );

                System.out.println(
                        "Students Attempted : " +
                        rs.getInt("students_attempted")
                );

                System.out.println(
                        "Average Percentage : " +
                        String.format(
                                "%.2f",
                                rs.getDouble(
                                        "average_percentage"
                                )
                        ) +
                        "%"
                );

                System.out.println(
                        "Highest Percentage : " +
                        String.format(
                                "%.2f",
                                rs.getDouble(
                                        "highest_percentage"
                                )
                        ) +
                        "%"
                );

                System.out.println(
                        "Lowest Percentage  : " +
                        String.format(
                                "%.2f",
                                rs.getDouble(
                                        "lowest_percentage"
                                )
                        ) +
                        "%"
                );

                System.out.println(
                        "-------------------------------------"
                );
            }

            if (!found) {

                System.out.println(
                        "No exam statistics available."
                );
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }
}
