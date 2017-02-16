package fr.treeptik.cloudunit.dto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.hateoas.ResourceSupport;

public class EnvironmentVariableResource extends ResourceSupport {
    private static EnvironmentVariableResource fromEnvLine(String line) {
        Matcher m = Pattern.compile("([0-9A-Z_]*)=(.*)").matcher(line);
        if (!m.matches() && m.groupCount() == 2) {
            throw new IllegalArgumentException(String.format("Line cannot be parsed as an environment variable: %s", line));
        }
        
        return new EnvironmentVariableResource(m.group(1), m.group(2));
    }
    
    public static List<EnvironmentVariableResource> fromEnv(String env) {
        if (StringUtils.isBlank(env)) {
            return Collections.emptyList();
        }
        env = env.trim();
        List<EnvironmentVariableResource> resources = Arrays.stream(env.split("\\n"))
                .map(EnvironmentVariableResource::fromEnvLine)
                .sorted((k1, k2) -> k1.getKey().compareTo(k2.getKey()))
                .collect(Collectors.toList());
        return resources;
    }
    
    private String key;
    
    private String value;
    
    public EnvironmentVariableResource(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EnvironmentVariableResource)) return false;
        EnvironmentVariableResource other = (EnvironmentVariableResource) obj;
        
        return new EqualsBuilder()
                .append(this.key, other.key)
                .append(this.value, other.value)
                .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(key)
                .append(value)
                .toHashCode();
    }
}
