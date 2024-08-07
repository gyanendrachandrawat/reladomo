first build the project using "mvn clean install"

add the below constructor with all the fields into the Employee model class
public Employee(long id, String name, int age, int salary) {
super();
this.setId(id);
this.setName(name);
this.setAge(age);
this.setSalary(salary);
}

then run the main method to insert the data into database table and retrieve it using Reladomo ORM
