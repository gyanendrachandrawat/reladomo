package com.example.reladomo;

import com.example.reladomo.model.Employee;
import com.example.reladomo.model.EmployeeFinder;
import com.gs.fw.common.mithra.MithraManager;
import com.gs.fw.common.mithra.MithraManagerProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class ReladomoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReladomoApplication.class, args);

		try {
			ReladomoConnectionManager.getInstance().createTables();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MithraManager mithraManager = MithraManagerProvider.getMithraManager();
		mithraManager.setTransactionTimeout(120);

		try (InputStream is = ReladomoApplication.class.getClassLoader()
				.getResourceAsStream("ReladomoRuntimeConfig.xml")) {
			MithraManagerProvider.getMithraManager()
					.readConfiguration(is);

			System.out.println("insert new Employee : ");
			Employee employee = new Employee(1, "emp1", 25, 25000);

			employee.insert();

			System.out.println("retrieve inserted Employee");
			Employee employeeRetrieved = EmployeeFinder.findByPrimaryKey(1);
			System.out.println(employeeRetrieved.getName() + " " + employeeRetrieved.getAge() + " " + employeeRetrieved.getSalary());

		}
		catch (IOException exc){
			exc.printStackTrace();
		}

	}

}
