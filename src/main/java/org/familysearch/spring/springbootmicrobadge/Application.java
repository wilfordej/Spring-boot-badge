package org.familysearch.spring.springbootmicrobadge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		setActiveProfile();
		SpringApplication.run(Application.class, args);
	}

	private static void setActiveProfile() {
		String activeProfiles = getSystemOrEnvProperty("FS_SYSTEM_NAME");
		if (activeProfiles == null) {
			activeProfiles = "local-int,localhostDynamo";
		}
		System.setProperty("spring.profiles.active", activeProfiles);
	}

	private static String getSystemOrEnvProperty(String propertyName) {
		Object propertyValue = System.getProperties().get(propertyName);
		if (propertyValue == null) {
			propertyValue = System.getenv().get(propertyName);
		}
		return (String) propertyValue;
	}
}
