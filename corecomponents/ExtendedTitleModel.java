import org.apache.sling.api.SlingHttpServletRequest;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.wcm.core.components.models.Title;
import com.day.cq.wcm.api.Page;

@Model(adaptables = SlingHttpServletRequest.class,
	   adapters = Title.class,
	   resourceType = "digital/components/content/title")
public class ExtendedTitleModel implements Title{
	
	@ScriptVariable private Page currentPage;
	
	@Self @Via(type = ResourceSuperType.class)
	private Title title;
	
	@Override
	public String getText()
	{
		return "Extended"+ title.getText();
	}

}
