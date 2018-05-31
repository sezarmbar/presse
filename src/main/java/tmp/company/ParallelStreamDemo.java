package tmp.company;

import java.util.ArrayList;
import java.util.List;

public class ParallelStreamDemo {

	public static void main(String[] args) {

		long t1, t2;
		List<Employee> eList = new ArrayList<Employee>();
		for(int i=0; i<100; i++) {
			eList.add(new Employee("A", 20000));
			eList.add(new Employee("B", 3000));
			eList.add(new Employee("C", 15002));
			eList.add(new Employee("D", 7856));
			eList.add(new Employee("E", 200));
			eList.add(new Employee("F", 50000));
		}

		/***** Here We Are Creating A 'Sequential Stream' & Displaying The Result *****/
		t1 = System.currentTimeMillis();
		System.out.println("Sequential Stream Count?= " + eList.stream().filter(e -> e.getSalary() > 15000).count());

		t2 = System.currentTimeMillis();
		System.out.println("Sequential Stream Time Taken?= " + (t2-t1) + "\n");

		/***** Here We Are Creating A 'Parallel Stream' & Displaying The Result *****/
		t1 = System.currentTimeMillis();
		System.out.println("Parallel Stream Count?= " + eList.parallelStream().filter(e -> e.getSalary() > 15000).count());

		t2 = System.currentTimeMillis();
		System.out.println("Parallel Stream Time Taken?= " + (t2-t1));
	}
}

class Employee{
  private String name;
  private int salary;

  public Employee(String name, int salary) {
    this.name = name;
    this.salary = salary;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getSalary() {
    return salary;
  }

  public void setSalary(int salary) {
    this.salary = salary;
  }
}
