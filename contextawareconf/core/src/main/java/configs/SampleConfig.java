package configs;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label="Sample Configuration", description="Sample Context Aware Configurations")
public @interface SampleConfig {  	
    @Property(label="Parameter #1", description="Describe me")
    String param1();  
	
    @Property(label="Parameter with Default value", description="Describe me")
    String paramWithDefault() default "defValue";   
	
    @Property(label="Integer parameter", description="Describe me")
    int intParam();
}

