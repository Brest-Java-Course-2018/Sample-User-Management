package com.epam.brest.course.client;

import com.epam.brest.course.model.Department;
import com.epam.brest.course.service.DepartmentService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

/**
 * REST client console application demo.
 */
public class DemoApp {

    private DepartmentService departmentService;

    private Scanner sc = new Scanner(System.in);

    public DemoApp(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public static void main(String[] args) {

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context.xml");
        DepartmentService departmentService = ctx.getBean(DepartmentService.class);
        DemoApp demoApp = new DemoApp(departmentService);
        demoApp.menu();
    }

    private void menu() {

        System.out.println("=================================");
        System.out.println("|   MENU SELECTION DEMO         |");
        System.out.println("=================================");
        System.out.println("| Options:                      |");
        System.out.println("|      1. Get all departments   |");
        System.out.println("|      2. Get department by id  |");
        System.out.println("|      3. Add department        |");
        System.out.println("|      4. Exit                  |");
        System.out.println("=================================");

        int swValue = 0;
        while (swValue != 4) {
            System.out.print("Select option: ");
            if (sc.hasNextInt()) {
                swValue = sc.nextInt();
                try {
                    checkValue(swValue);
                } catch (ServerDataAccessException e) {
                    System.out.println("RESPONSE ERR: " + e.getMessage());
                }
            } else {
                System.out.println("Bad value: " + sc.next());
            }
        }
    }

    private void checkValue(int item) {
        switch (item) {
            case 1:
                getAllDepartments();
                break;
            case 2:
                getDepartmentById();
                break;
            case 3:
                addDepartment();
                break;
            case 4:
                System.out.println("Good bye.");
                break;
            default:
                System.out.println("Invalid selection.");
        }
    }

    private void getAllDepartments() {
        System.out.println("departments: ");
        departmentService.findAllDepartmentDTOs()
                .forEach(System.out::println);
    }

    private void getDepartmentById() {
        System.out.print("    Enter department id: ");
        int id;
        if (sc.hasNextInt() && (id = sc.nextInt()) > 0) {
            Department department = departmentService.findById(id);
            System.out.println("department: " + department);
        } else {
            System.out.println("Bad value: " + sc.next());
        }
    }

    private void addDepartment() {
        System.out.print("    Enter department name: ");
        String name = sc.next();

        System.out.print("    Enter department description: ");
        String desc = sc.next();

        Department department = new Department(name, desc);
        department.setDepartmentId(departmentService.create(department));
        System.out.println("department: " + department);
    }
}
