first build the project using "mvn clean install"

add the below constructor with all the fields into the Employee model class
public Employee(long id, String name, int age, int salary) {
super();
this.setId(id);
this.setName(name);
this.setAge(age);
this.setSalary(salary);
}

public Department (long departmentId, String departmentName) {
super();
this.setDepartmentId(departmentId);
this.setDepartmentName(departmentName);
}

then run the main method to insert the data into database table and retrieve it using Reladomo ORM
