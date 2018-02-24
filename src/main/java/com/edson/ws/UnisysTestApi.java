package com.edson.ws;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class UnisysTestApi extends ResourceConfig {

    public UnisysTestApi() {
        packages(getClass().getPackage().getName() + ".resources");

    }

}