package controller;

import model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Comparator;

public class ManagingStaff {
    private final List<Employee> employees = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public ManagingStaff() {
        employees.add(new Employee("Marcos", "Moraes", 20000, "TI"));
        employees.add(new Employee("Joao", "Silva", 3000, "TI"));
        employees.add(new Employee("Maria", "Santos", 5000, "MEC"));
        employees.add(new Employee("Ana", "Goncalves", 8000, "MEC"));
    }

    public static void main(String[] args) {
        new ManagingStaff().run();
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("\n===== GESTÃO DE EMPREGADOS =====");
            System.out.println("1 - Listar todos");
            System.out.println("2 - Filtrar por departamento");
            System.out.println("3 - Cadastrar empregado");
            System.out.println("4 - Remover empregado");
            System.out.println("5 - Estatísticas salariais");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> listAll();
                case "2" -> filterByDepartment();
                case "3" -> addEmployee();
                case "4" -> removeEmployee();
                case "5" -> salaryStatistics();
                case "0" -> {
                    System.out.println("Programa encerrado.");
                    running = false;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
        scanner.close();
    }

    private void listAll() {
        if (employees.isEmpty()) {
            System.out.println("Nenhum empregado cadastrado.");
            return;
        }
        for (int i = 0; i < employees.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, employees.get(i));
        }
    }

    private void filterByDepartment() {
        System.out.print("Departamento: ");
        String department = scanner.nextLine().trim();
        if (department.isBlank()) {
            System.out.println("Informe um departamento.");
            return;
        }

        var matches = employees.stream()
                .filter(employee -> employee.getDepartment().equalsIgnoreCase(department))
                .toList();

        if (matches.isEmpty()) {
            System.out.println("Nenhum empregado encontrado nesse departamento.");
        } else {
            matches.forEach(System.out::println);
        }
    }

    private void addEmployee() {
        System.out.print("Nome: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Sobrenome: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Departamento: ");
        String department = scanner.nextLine().trim();

        if (firstName.isBlank() || lastName.isBlank() || department.isBlank()) {
            System.out.println("Nome, sobrenome e departamento são obrigatórios.");
            return;
        }

        System.out.print("Salário (ex.: 3500.50): ");
        try {
            double salary = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            if (!Double.isFinite(salary) || salary < 0) {
                System.out.println("Salário inválido.");
                return;
            }
            employees.add(new Employee(firstName, lastName, salary, department));
            System.out.println("Empregado cadastrado!");
        } catch (NumberFormatException exception) {
            System.out.println("Digite um salário numérico válido.");
        }
    }

    private void removeEmployee() {
        listAll();
        if (employees.isEmpty()) {
            return;
        }

        System.out.print("Número do empregado para remover: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index < 0 || index >= employees.size()) {
                System.out.println("Número inexistente.");
                return;
            }
            Employee removed = employees.remove(index);
            System.out.println("Removido: " + removed.getFirstName() + " " + removed.getLastName());
        } catch (NumberFormatException exception) {
            System.out.println("Informe um número inteiro válido.");
        }
    }

    private void salaryStatistics() {
        if (employees.isEmpty()) {
            System.out.println("Nenhum empregado para analisar.");
            return;
        }

        double total = employees.stream().mapToDouble(Employee::getSalary).sum();
        double average = employees.stream().mapToDouble(Employee::getSalary).average().orElse(0);
        Employee highest = employees.stream()
                .max(Comparator.comparingDouble(Employee::getSalary))
                .orElseThrow();

        System.out.println("Empregados: " + employees.size());
        System.out.printf("Total da folha: R$ %.2f%n", total);
        System.out.printf("Salário médio: R$ %.2f%n", average);
        System.out.printf("Maior salário: %s %s — R$ %.2f%n",
                highest.getFirstName(), highest.getLastName(), highest.getSalary());
    }
}
