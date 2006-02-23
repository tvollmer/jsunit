package net.jsunit.configuration;

import java.util.Arrays;
import java.util.List;

public enum ConfigurationType {
	STANDARD(
		new ConfigurationProperty[]{
			ConfigurationProperty.BROWSER_FILE_NAMES,
			ConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS,
			ConfigurationProperty.LOGS_DIRECTORY,
			ConfigurationProperty.LOG_STATUS,
			ConfigurationProperty.PORT,
			ConfigurationProperty.RESOURCE_BASE,
			ConfigurationProperty.TIMEOUT_SECONDS,
		},
		new ConfigurationProperty[]{
			ConfigurationProperty.URL,
		}
	),
	FARM(
		new ConfigurationProperty[]{
			ConfigurationProperty.LOGS_DIRECTORY,
			ConfigurationProperty.LOG_STATUS,
			ConfigurationProperty.PORT,
			ConfigurationProperty.REMOTE_MACHINE_URLS,
			ConfigurationProperty.TIMEOUT_SECONDS,
			ConfigurationProperty.IGNORE_UNRESPONSIVE_REMOTE_MACHINES,
		},
		new ConfigurationProperty [] {
            ConfigurationProperty.RESOURCE_BASE,
            ConfigurationProperty.URL,
		}
	);

	private List<ConfigurationProperty> requiredProperties;
	private List<ConfigurationProperty> optionalProperties;

	private ConfigurationType(ConfigurationProperty[] required, ConfigurationProperty[] optional) {
		this.requiredProperties = Arrays.asList(required);
		this.optionalProperties = Arrays.asList(optional);
	}
	
	public List<ConfigurationProperty> getRequiredConfigurationProperties() {
		return requiredProperties;
	}

	public List<ConfigurationProperty> getOptionalConfigurationProperties() {
		return optionalProperties;
	}
}
