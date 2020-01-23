package com.microsoft.azure.helium.health.ietf;

public class CosmosDbError {
    public String status = "down";
    public HealthzErrorDetails details = new HealthzErrorDetails();
}
