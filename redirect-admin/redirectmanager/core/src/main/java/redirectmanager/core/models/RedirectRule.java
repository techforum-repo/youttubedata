package redirectmanager.core.models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RedirectRule {
	public static RedirectRule EMPTY = RedirectRule.builder().build();
	private String rawSource;
	private String rawTarget;
	private String source;
	private String target;
	private String configuration;
	private boolean validSource;
	private boolean validTarget;
}