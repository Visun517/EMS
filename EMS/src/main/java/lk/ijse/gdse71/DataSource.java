package lk.ijse.gdse71;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.SQLException;

@WebListener
public class DataSource implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/employee");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("Ijse@1234");
        basicDataSource.setInitialSize(50);
        basicDataSource.setMaxTotal(100);

        ServletContext sc = sce.getServletContext();
        sc.setAttribute("basicDataSource", basicDataSource);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            ServletContext servletContext = sce.getServletContext();
            BasicDataSource basicDataSource = (BasicDataSource) servletContext.getAttribute("basicDataSource");
            basicDataSource.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
