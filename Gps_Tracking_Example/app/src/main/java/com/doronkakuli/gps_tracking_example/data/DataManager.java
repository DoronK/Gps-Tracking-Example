package com.doronkakuli.gps_tracking_example.data;

import android.location.Location;

import java.util.concurrent.ConcurrentLinkedQueue;

public class DataManager {
    public static final String TAG = DataManager.class.getSimpleName();
    private ConcurrentLinkedQueue<Location> queue = new ConcurrentLinkedQueue<Location>();

    private DataManager() {
    }

    public static DataManager getInstance() {
        return DataManagerHolder.INSTANCE;
    }

    private static class DataManagerHolder {
        public static final DataManager INSTANCE = new DataManager();
    }


    public void addLocation(Location location) {
        queue.add(location);
    }

    public void initializeLocations() {
        queue = new ConcurrentLinkedQueue<Location>();
    }

    /* ****************************************************************************************** */
    /* GETTERS SETTERS                                                                                 */
    /* ****************************************************************************************** */

    public ConcurrentLinkedQueue<Location> getQueue() {
        return queue;
    }

}
