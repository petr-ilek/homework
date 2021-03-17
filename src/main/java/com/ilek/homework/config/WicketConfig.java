package com.ilek.homework.config;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import org.apache.wicket.protocol.http.WebApplication;

@ApplicationInitExtension
public class WicketConfig implements WicketApplicationInitConfiguration {

    @Override
    public void init(WebApplication webApplication) {
        Bootstrap.install(webApplication);
        BootstrapSettings settings = new BootstrapSettings();
        Bootstrap.install(webApplication, settings);
    }
}
