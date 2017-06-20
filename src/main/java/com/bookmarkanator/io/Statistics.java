package com.bookmarkanator.io;

import java.util.Properties;

public interface Statistics {

    /**
     * This method will load a list of properties from a designated location defined by the implementing class.
     *
     * @return a list of properties
     */
    Properties loadStatistics();

    /**
     * This method will override the current statistics being used.
     * Preferred method is to update each property individually rather than the whole file at once
     * @param statistics the properties to be written over.
     */
    void setStatistics(Properties statistics);


    /**
     * Returns the requested property
     *
     * @param property requested
     * @return property if present, null otherwise
     */
    String getStatistic(String property);

    /**
     * Sets the appropriate property, and saves it
     *
     * @param property the key of the property being set
     * @param value the value of the property
     */
    void setStatistic(String property, String value);
}
