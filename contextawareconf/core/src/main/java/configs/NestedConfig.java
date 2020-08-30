package configs;


import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label="Nested Configuration", description="Nested Configurations")
public @interface NestedConfig {  	
    @Property(label="Parameter #1", description="Describe me")
    String nested1();  
	
    @Property(label="Parameter with Default value", description="Describe me")
    SampleConfig sampleConfig();
}
