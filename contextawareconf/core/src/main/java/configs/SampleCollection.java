package configs;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(collection = true, label = "SampleCollection",description = "SampleCollection")
public @interface SampleCollection {

	
	@Property(label="Parameter #1", description="Describe me") 
	String name();

	@Property(label="Parameter #2", description="Describe me")  
	String pagePath();

}
