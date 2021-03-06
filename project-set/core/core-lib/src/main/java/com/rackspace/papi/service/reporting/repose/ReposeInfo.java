package com.rackspace.papi.service.reporting.repose;

public interface ReposeInfo {

    long getTotalStatusCode(int statusCode);
    void incrementStatusCodeCount(int statusCode);
    void incrementRequestCount();
    void incrementResponseCount();
    void accumulateRequestSize(long requestSize);
    void accumulateResponseSize(long responseSize);
    void updateMinMaxRequestSize(long requestSize);
    void updateMinMaxResponseSize(long responseSize);
    long getMinimumRequestSize();
    long getMaximumRequestSize();
    long getMinimumResponseSize();
    long getMaximumResponseSize();
    double getAverageRequestSize();
    double getAverageResponseSize();

    ReposeInfo copy();
}
