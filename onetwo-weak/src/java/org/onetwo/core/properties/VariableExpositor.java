package org.onetwo.core.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.onetwo.core.util.SimpleExpression;
import org.onetwo.core.util.ValueProvider;

public class VariableExpositor {
	// private static final Log log =
	// LogFactory.getLog(VariableExpositor.class);
	private static final String PRE_FIX = "${";
	private static final String POST_FIX = "}";
	private static final Pattern PATTERN = Pattern.compile("\\$\\{[\\w\\.]+\\}", Pattern.CASE_INSENSITIVE);

	protected SimpleExpression se = new SimpleExpression("${", "}");
	private VariableSupporter variabler;
	private Map<String, String> cache;
	private boolean cacheable;

	public VariableExpositor(VariableSupporter variabler) {
		this.variabler = variabler;
		this.cache = new HashMap<String, String>();
	}

	public VariableExpositor(VariableSupporter variabler, boolean cacheable) {
		this(variabler);
		this.cacheable = cacheable;
	}

	public String explainVariable(String source) {
		String result = null;
		if (this.cacheable)
			result = cache.get(source);

		if (result != null || source == null)
			return result;

		if (this.se.isExpresstion(source)) {
			result = this.se.parse(source, new ValueProvider() {
				@Override
				public String findString(String var) {
					return variabler.getVariable(var);
				}
			});
		}else
			result = source;
		
		this.cache.put(source, result);

		return result;
	}

	public String explainVariable2(String source) {
		String result = null;
		if (this.cacheable)
			result = cache.get(source);

		if (result != null || source == null)
			return result;

		StringBuffer sb = new StringBuffer(source);
		Matcher matcher = PATTERN.matcher(source);

		String var = null;
		String varId = null;
		String value = null;
		while (matcher.find()) {
			var = matcher.group();
			varId = var.substring(PRE_FIX.length(), var.length() - POST_FIX.length());
			value = this.variabler.getVariable(varId);
			// log.info("varid : " + varId + "---value : " + value);

			if (value == null)
				continue;
			int start = sb.indexOf(var);
			sb.replace(start, start + var.length(), value);
		}

		result = sb.toString();
		this.cache.put(source, result);

		return result;
	}

	public void clear() {
		cache.clear();
	}

	public boolean isCacheable() {
		return cacheable;
	}

	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

}
