package redirectmanager.core.models;

import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

import static redirectmanager.core.utilities.RedirectsRuleUtil.buildRedirectRule;
import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = OPTIONAL)
public class RuleConfigModel {
	@Self
	private SlingHttpServletRequest request;

	@Getter
	private RedirectRule redirectRule;

	@PostConstruct
	protected void init() {
		redirectRule = buildRedirectRule(request.getResource().adaptTo(RuleResourceModel.class), request);
	}
}
