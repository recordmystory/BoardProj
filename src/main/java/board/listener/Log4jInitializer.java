package board.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;

public class Log4jInitializer implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		String log4jConfigFile = event.getServletContext().getRealPath("/WEB-INF/classes/log4j.properties");
        PropertyConfigurator.configure(log4jConfigFile);
	}
	
	
}
