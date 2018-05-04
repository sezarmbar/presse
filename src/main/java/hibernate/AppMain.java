package hibernate;

import java.util.Date;

import hibernate.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class AppMain {

  static User userObj;
  static Session sessionObj;
  static SessionFactory sessionFactoryObj;

  private static SessionFactory buildSessionFactory() {
    // Creating Configuration Instance & Passing Hibernate Configuration File
    Configuration configObj = new Configuration();
    configObj.configure("hibernate.cfg.xml");

    // Since Hibernate Version 4.x, ServiceRegistry Is Being Used
    ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build();

    // Creating Hibernate SessionFactory Instance
    sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
    return sessionFactoryObj;
  }

  public static void main(String[] args) {
    System.out.println(".......Hibernate Maven Example.......\n");
    try {
      sessionObj = buildSessionFactory().openSession();
      sessionObj.beginTransaction();

      for(int i = 101; i <= 105; i++) {
        userObj = new User();
        userObj.setUserid(i);
        userObj.setUsername("Editor " + i);
        userObj.setCreatedBy("Administrator");
        userObj.setCreatedDate(new Date());

        sessionObj.save(userObj);
      }
      System.out.println("\n.......Records Saved Successfully To The Database.......\n");

      // Committing The Transactions To The Database
      sessionObj.getTransaction().commit();
    } catch(Exception sqlException) {
      if(null != sessionObj.getTransaction()) {
        System.out.println("\n.......Transaction Is Being Rolled Back.......");
        sessionObj.getTransaction().rollback();
      }
      sqlException.printStackTrace();
      sessionObj.close();
    } finally {
      if(sessionObj != null) {
        sessionObj.close();
      }
    }
  }
}
