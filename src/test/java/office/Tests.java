package office;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

@Nested
public class Tests {
    @Test
    void setHrToAnn(){
        List<Employee> annEmployees = Service.findEmployeeByName("Ann");
        Employee annEmployee = annEmployees.get(0);
        Integer hrDepartmentId = Service.getDepartmentIdByName("HR");
        Service.updateEmployeeDepartment(annEmployee.getEmployeeId(), hrDepartmentId);
        System.out.println("Департамент сотрудника Ann успешно изменен на HR.");
    }
    @Test
    void toUpperCaseNames(){
        System.out.println("Количество исправленных имен: " + Service.fixEmployeeNames());
    }
    @Test
    void countITEmployees(){
        System.out.println("Количество работников ИТ департамента: " + Service.countItEmployees());
    }
}