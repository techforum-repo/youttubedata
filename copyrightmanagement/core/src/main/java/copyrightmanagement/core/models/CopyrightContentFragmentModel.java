package copyrightmanagement.core.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import com.day.cq.wcm.api.Page;

import org.apache.sling.models.annotations.Source;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;

/**
* Get the copyright message from content fragment based on the design mapping
* @author issaca1
*
*/

@Model(adaptables = SlingHttpServletRequest.class)
public class CopyrightContentFragmentModel {
    private static final String COPYRIGHT_SYMBOL="&#169;";

    @Getter
    private String copyRightMessage = COPYRIGHT_SYMBOL+Calendar.getInstance().get(Calendar.YEAR)+" "+"All rights reserved.";   
	
	@Inject
    @Source("script-bindings")
    Page currentPage;
	
    @Inject
    private Resource resource;
   
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    protected void init() {         
        String variation = null;
        String renditionvariation;
        String cfInputRend=null;
		String cfInput =null;
        try {
            InheritanceValueMap iProperties = new HierarchyNodeInheritanceValueMap(currentPage.getContentResource());
            cfInput = iProperties.getInherited("copyrightcfdata", String.class);
			variation = iProperties.getInherited("copyrightcfvariation", String.class);
            if (variation==null || variation.equals("master")) {
                renditionvariation = "original";
            }else{
				renditionvariation=variation;
            }       
			cfInputRend = cfInput + "/jcr:content/renditions/" + renditionvariation + "/jcr:content";
            Node cfNode = null;         
            Resource res= resource.getResourceResolver().getResource(cfInputRend);
			if(res!=null)
            {
                cfNode=res.adaptTo(Node.class);
                copyrightMessageFromRendtions(cfNode);
			}else{  
				if (variation==null){
					variation="master";
				}
				cfInput = cfInput + "/jcr:content/data/" + variation;
				copyrightMessageFromDataNode(cfInput,cfNode);
            }           
		} catch (Exception e) {
           logger.debug("Exception while getting the copyright message: {}",e.getMessage());       
        }
    }

    /**

     * Get the copyright message from renditions folder

     * @param cfNode

     */

    void copyrightMessageFromRendtions(Node cfNode){

        try {

            if (cfNode != null&&cfNode.hasProperty("jcr:data") && cfNode.hasProperty("jcr:mimeType") && cfNode.getProperty("jcr:mimeType").getString().equals("text/html")) {

                InputStream in = cfNode.getProperty("jcr:data").getBinary().getStream();
                try (BufferedReader buffer = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                    copyRightMessage= buffer.lines().collect(Collectors.joining("\n"));
                    if(copyRightMessage!=null)
					{
                        copyRightMessage=COPYRIGHT_SYMBOL+Calendar.getInstance().get(Calendar.YEAR)+" "+copyRightMessage.replace("<p>","").replace("</p>","");  
					}
				} 
			}

        } catch (RepositoryException | IOException e) {
            logger.error("Inside copyrightMessage Exception while getting the copyright message: {}",e.getMessage());
        }

    }

    /**
    * @param cfInput
     * @param cfNode
     */

    void copyrightMessageFromDataNode(String cfInput,Node cfNode) {
        Resource res=resource.getResourceResolver().getResource(cfInput);
        if(res!=null)
        {
            cfNode=res.adaptTo(Node.class);
        }
        try {
            if(cfNode != null&&cfNode.hasProperty("copyrightcfmodeldata")){
                copyRightMessage=COPYRIGHT_SYMBOL+Calendar.getInstance().get(Calendar.YEAR)+" "+cfNode.getProperty("copyrightcfmodeldata").getValue().getString();
            }
        } catch (IllegalStateException | RepositoryException e) {
            logger.error("Inside copyrightMessageData Exception while getting the copyright message: {}",e.getMessage());
        }
    }
}