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
    void setHrToAnn() {
        List<Employee> annEmployees = Service.findEmployeeByName("Ann");
        Employee annEmployee = annEmployees.get(0);
        Integer hrDepartmentId = Service.getDepartmentIdByName("HR");
        Service.updateEmployeeDepartment(annEmployee.getEmployeeId(), hrDepartmentId);
        System.out.println("Департамент сотрудника Ann успешно изменен на HR.");
    }

    @Test
    void toUpperCaseNames() {
        System.out.println("Количество исправленных имен: " + Service.fixEmployeeNames());
    }

    @Test
    void countITEmployees() {
        System.out.println("Количество работников ИТ департамента: " + Service.countItEmployees());
    }

    @Test
    void testDeleteDepartment() {
        int depId = 1;
        String depName = "Accounting";
        Department accountingDepartment = new Department(depId, depName);
        List<Employee> employeesInAccounting = Service.findEmployeesByDepartmentId(depId);
        assertEquals(2, employeesInAccounting.size());
        Service.removeDepartment(accountingDepartment);
        assertNull(Service.getDepartmentIdByName(depName));
        System.out.println("Отдел " + depName + " удален.");
        employeesInAccounting = Service.findEmployeesByDepartmentId(depId);
        assertEquals(0, employeesInAccounting.size());
        System.out.println("Осталось 0 сотрудников в отделе " + depName);
    }
}