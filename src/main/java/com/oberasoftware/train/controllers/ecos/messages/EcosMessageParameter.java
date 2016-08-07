package com.oberasoftware.train.controllers.ecos.messages;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Renze de Vries
 */
public class EcosMessageParameter {
    private final String name;
    private final List<Object> attributes;

    public EcosMessageParameter(String name, List<Object> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public EcosMessageParameter(String name) {
        this.name = name;
        this.attributes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Object> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "EcosMessageParameter{" +
                "name='" + name + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
