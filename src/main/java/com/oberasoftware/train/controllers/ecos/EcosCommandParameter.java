package com.oberasoftware.train.controllers.ecos;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class EcosCommandParameter {
    private final String parameter;
    private final List<Object> attributes;

    public EcosCommandParameter(String parameter, List<Object> attributes) {
        this.parameter = parameter;
        this.attributes = attributes;
    }

    public String getParameter() {
        return parameter;
    }

    public List<Object> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "EcosCommandParameter{" +
                "parameter='" + parameter + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
