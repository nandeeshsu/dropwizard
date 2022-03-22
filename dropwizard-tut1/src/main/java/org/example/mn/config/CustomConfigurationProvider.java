package org.example.mn.config;

import io.dropwizard.configuration.ConfigurationSourceProvider;
import org.example.mn.AppConstants;

import java.io.IOException;
import java.io.InputStream;

public class CustomConfigurationProvider implements ConfigurationSourceProvider {
    /**
     * Returns an {@link InputStream} that contains the source of the configuration for the
     * application. The caller is responsible for closing the result.
     *
     * @param path the path to the configuration
     * @return an {@link InputStream}
     * @throws IOException if there is an error reading the data at {@code path}
     */
    @Override
    public InputStream open(String path) throws IOException {
        return CustomConfigurationProvider.class
                .getClassLoader()
                .getResourceAsStream(
                        ConfigYaml.getconfigYml()
                );
    }
}
