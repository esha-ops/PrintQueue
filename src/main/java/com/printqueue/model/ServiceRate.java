package com.printqueue.model;

public class ServiceRate {
    private final String serviceName;
    private final double baseRate;
    private final double a3Multiplier;

    public ServiceRate(String serviceName, double baseRate, double a3Multiplier) {
        this.serviceName = serviceName;
        this.baseRate = baseRate;
        this.a3Multiplier = a3Multiplier;
    }

    public String getServiceName() { return serviceName; }
    public double getBaseRate() { return baseRate; }
    public double getA3Multiplier() { return a3Multiplier; }
}
