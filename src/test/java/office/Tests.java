package office;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Nested
public class Tests {

    @BeforeEach
    void setUp() {
        Service.createDB(); // сброс базы
    }

    @AfterAll
    static void cleanUp() {
        Service.createDB(); // вернуть как было
    }

    @Test
    void testCreateDB() { //база корректно создана
        Service.createDB();
        assertEquals(3, Service.getAllDepartments().size());
        assertEquals(5, Service.getAllEmployees().size());
    }

    @Test
    void testAddDepartment() { //добавление департамент
        int depId = 4;
        String depName = "NewDep";
        Service.addDepartment(new Department(depId, depName));
        assertNotNull(Service.getDepartmentIdByName(depName));
        assertEquals(4, Service.getDepartmentIdByName(depName));
        System.out.println("Тест добавления отдела завершен успешно.");
    }

    @Test
    void testRemoveDepartment() {
        int depId = 1;
        String depName = "Accounting";
        Department accountingDepartment = new Department(depId, depName);
        List<Employee> employeesInAccounting = Service.findEmployeesByDepartmentId(depId);
        assertEquals(2, employeesInAccounting.size());
        Service.removeDepartment(accountingDepartment);
        System.out.println("Отдел " + depName + " удален.");
        assertNull(Service.getDepartmentIdByName(depName));
        System.out.println("Отдел Accounting действительно удален.");
        employeesInAccounting = Service.findEmployeesByDepartmentId(depId);
        assertEquals(0, employeesInAccounting.size());
        System.out.println("Количество сотрудников в отделе " + depName + " после удаления: 0");
    }

    @Test
    void testAddEmployee() {
        int empId = 6;
        int depId = 1;
        String name = "Zzz";
        Service.addEmployee(new Employee(empId, name, depId));
        List<Employee> johnEmployees = Service.findEmployeeByName(name);
        assertEquals(empId, johnEmployees.get(0).getEmployeeId());
        System.out.println("Сотрудник успешно добавлен");
    }

    @Test
    void testRemoveEmployee() {
        int empId = 1;
        int depId = 1;
        String name = "Pete";
        Service.removeEmployee( new Employee(empId, name, depId));
        List<Employee> peteEmployees = Service.findEmployeeByName("Pete");
        assertEquals(0, peteEmployees.size());
        System.out.println("Сотрудник удалён");
    }

    @Test
    void testFindEmployeeByName() {
        List<Employee> annEmployees = Service.findEmployeeByName("Ann");
        assertEquals(1, annEmployees.size());
    }

    @Test
    void testUpdateEmployeeDepartment() {
        List<Employee> emp = Service.findEmployeeByName("Ann");
        Employee annEmployee = emp.get(0);
        Integer hrDepartmentId = Service.getDepartmentIdByName("HR");
        Service.updateEmployeeDepartment(annEmployee.getEmployeeId(), hrDepartmentId);
        emp = Service.findEmployeeByName("Ann");
        assertEquals(hrDepartmentId, emp.get(0).getDepartmentId());
        System.out.println("Обновление отдела сотрудника успешно");
    }

    @Test
    void testGetDepartmentIdByName() {
        assertNotNull(Service.getDepartmentIdByName("Accounting"));
        assertEquals(1, Service.getDepartmentIdByName("Accounting"));
    }

    @Test
    void testFixEmployeeNames() {
        int fixedNamesCount = Service.fixEmployeeNames();
        assertEquals(0, fixedNamesCount);
    }

    @Test
    void testCountITEmployees() {
        int itEmployeesCount = Service.countItEmployees();
        assertEquals(2, itEmployeesCount);
    }

    @Test
    void testCheckEmployeesInDepartment() {
        int depId = 1;
        boolean isEmpty = Service.checkEmployeesInDepartment(depId);
        assertFalse(isEmpty);
    }
}