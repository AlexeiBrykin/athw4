package office;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Service {

    public static void createDB() {
        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {
            Statement stm = con.createStatement();
            stm.executeUpdate("DROP TABLE IF EXISTS Department");
            stm.executeUpdate("CREATE TABLE Department(ID INT PRIMARY KEY, NAME VARCHAR(255))");
            stm.executeUpdate("INSERT INTO Department VALUES(1,'Accounting')");
            stm.executeUpdate("INSERT INTO Department VALUES(2,'IT')");
            stm.executeUpdate("INSERT INTO Department VALUES(3,'HR')");

            stm.executeUpdate("DROP TABLE IF EXISTS Employee");
            stm.executeUpdate("CREATE TABLE Employee(ID INT PRIMARY KEY, NAME VARCHAR(255), DepartmentID INT)");
            stm.executeUpdate("INSERT INTO Employee VALUES(1,'Pete',1)");
            stm.executeUpdate("INSERT INTO Employee VALUES(2,'Ann',1)");

            stm.executeUpdate("INSERT INTO Employee VALUES(3,'Liz',2)");
            stm.executeUpdate("INSERT INTO Employee VALUES(4,'Tom',2)");

            stm.executeUpdate("INSERT INTO Employee VALUES(5,'Todd',3)");

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void addDepartment(Department d) {
        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {
            PreparedStatement stm = con.prepareStatement("INSERT INTO Department VALUES(?,?)");
            stm.setInt(1, d.departmentID);
            stm.setString(2, d.getName());
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void removeDepartment(Department d) {
        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {
            PreparedStatement stm = con.prepareStatement("DELETE FROM Department WHERE ID=?");
            stm.setInt(1, d.departmentID);
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void addEmployee(Employee empl) {
        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {
            PreparedStatement stm = con.prepareStatement("INSERT INTO Employee VALUES(?,?,?)");
            stm.setInt(1, empl.getEmployeeId());
            stm.setString(2, empl.getName());
            stm.setInt(3, empl.getDepartmentId());
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void removeEmployee(Employee empl) {
        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {
            PreparedStatement stm = con.prepareStatement("DELETE FROM Employee WHERE ID=?");
            stm.setInt(1, empl.getEmployeeId());
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static List<Employee> findEmployeeByName(String name) {
        List<Employee> employees = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {
            PreparedStatement stm = con.prepareStatement("SELECT ID, NAME, DepartmentID FROM Employee WHERE NAME = ?");
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("DepartmentID"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return employees;
    }

    public static void updateEmployeeDepartment(int employeeId, int departmentId) {
        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {
            PreparedStatement stm = con.prepareStatement("UPDATE Employee SET DepartmentID = ? WHERE ID = ?");
            stm.setInt(1, departmentId);
            stm.setInt(2, employeeId);
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static Integer getDepartmentIdByName(String departmentName) {
        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {
            PreparedStatement stm = con.prepareStatement("SELECT ID FROM Department WHERE NAME = ?");
            stm.setString(1, departmentName);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public static int fixEmployeeNames() {
        int count = 0;
        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT ID, NAME FROM Employee");
            while (rs.next()) {
                int employeeId = rs.getInt("ID");
                String name = rs.getString("NAME");
                if (!name.isEmpty() && Character.isLowerCase(name.charAt(0))) {
                    String correctedName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                    updateEmployeeName(employeeId, correctedName);
                    count++;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return count;
    }

    public static void updateEmployeeName(int employeeId, String newName) {
        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {
            PreparedStatement stm = con.prepareStatement("UPDATE Employee SET NAME = ? WHERE ID = ?");
            stm.setString(1, newName);
            stm.setInt(2, employeeId);
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public static int countItEmployees() {
        int count = 0;
        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {
            PreparedStatement stm = con.prepareStatement(
                    "SELECT COUNT(*) FROM Employee JOIN Department ON Employee.DepartmentID = Department.ID WHERE Department.NAME = 'IT'"
            );
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return count;
    }
}