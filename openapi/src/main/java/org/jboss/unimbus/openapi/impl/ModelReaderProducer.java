package org.jboss.unimbus.openapi.impl;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.OASConfig;
import org.eclipse.microprofile.openapi.OASModelReader;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.jboss.unimbus.UNimbus;

/**
 * @author Ken Finnigan
 */
@ApplicationScoped
public class ModelReaderProducer {

    @Inject
    @ConfigProperty(name = OASConfig.MODEL_READER)
    private Optional<String> readerClassName;

    @Inject
    private UNimbus uNimbus;

    @Produces
    @OpenApiModel(OpenApiModel.ModelType.READER)
    public OpenAPI modelReader() {
        if (!readerClassName.isPresent()) {
            return null;
        }

        try {
            Class<?> c = uNimbus.getClassLoader().loadClass(readerClassName.get());
            OASModelReader reader = (OASModelReader) c.newInstance();
            return reader.buildModel();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
