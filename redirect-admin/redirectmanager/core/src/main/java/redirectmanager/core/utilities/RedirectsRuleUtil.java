package redirectmanager.core.utilities;

import com.day.cq.commons.Externalizer;
import redirectmanager.core.models.RedirectRule;
import redirectmanager.core.models.RuleResourceModel;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.client.utils.URIBuilder;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.validator.routines.UrlValidator.*;

public final class RedirectsRuleUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedirectsRuleUtil.class);
	private static final String CONTENT_PREFIX = "/content";
	private static final String ASSET_PATH = "/content/dam";
	private static final String SPACE = "\t";
	private static final String HTTPS = "https";
	private static final String DOT_HTML = ".html";
	private static final UrlValidator urlValidator = new UrlValidator(ALLOW_ALL_SCHEMES | ALLOW_2_SLASHES | ALLOW_LOCAL_URLS);

	public static RedirectRule buildRedirectRule(final RuleResourceModel ruleModel, final SlingHttpServletRequest request) {
		final String source = ruleModel.getSource();
		final String target = ruleModel.getTarget();
		if (isNotBlank(source) && isNotBlank(target)) {
			String ruleSource="";
			String ruleTarget="";
			String config ="";
			boolean isValidSource = false;
			boolean isValidTarget = false;
			
			try{
				final Externalizer externalizer = request.getResourceResolver().adaptTo(Externalizer.class);
				ruleSource = isContentPagePath(source) ? externalizer.relativeLink(request, addExt(source)) : source;
				isValidSource = isValidSource(ruleSource);
				ruleTarget = isContentPagePath(target) ? externalizer.absoluteLink(request, HTTPS, addExt(target)) : target;
				isValidTarget = urlValidator.isValid(ruleTarget) && !containsWhitespace(target);
				config = join(ruleSource, SPACE, ruleTarget);				
				return RedirectRule.builder().rawSource(source).rawTarget(target).source(ruleSource).target(ruleTarget)
						.configuration(config).validSource(isValidSource).validTarget(isValidTarget).build();
			}catch(Exception e)
			{
				return RedirectRule.builder().rawSource(source).rawTarget(target).source(ruleSource).target(ruleTarget)
						.configuration(config).validSource(isValidSource).validTarget(isValidTarget).build();
			}		

		} else {
			LOGGER.debug("Redirect Rule has blank source or destination");
			return RedirectRule.builder().rawSource(source).rawTarget(target).build();
		}
	}

	private static String addExt(final String contentPath) {
		try {
			final URIBuilder uriBuilder = new URIBuilder(contentPath);
			final String path = uriBuilder.getPath();
			final String transformedPath = path.endsWith(DOT_HTML) ? path : path + DOT_HTML;
			uriBuilder.setPath(transformedPath);
			return uriBuilder.toString();
		} catch (final URISyntaxException uriError) {
			LOGGER.error("Unable to construct URI for given content path: {}, Error message : {}", contentPath, uriError.getMessage());
			return contentPath;
		}
	}

	private static boolean isContentPagePath(final String path) {
		return path.startsWith(CONTENT_PREFIX) && !path.startsWith(ASSET_PATH);
	}

	public static boolean isValidSource(final String url) {
		return isNotBlank(url) && url.startsWith("/") && !containsWhitespace(url);
	}
}
