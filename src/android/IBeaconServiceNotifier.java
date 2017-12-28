
package com.unarin.cordova.beacon;

import org.altbeacon.beacon.Region;

/*
 * Interface for addition iOS events
 */
public interface IBeaconServiceNotifier {
    /*
     * notified when a region monitor is successfully started
     */
    public void didStartMonitoringForRegion(Region region);

    /*
     * notified when a region monitor fails to start or the region is invalid
     * NOTE: Should add service listener for when BT is turned on and off
     */
    public void monitoringDidFailForRegion(Region region, Exception exception);

    /*
     * notified when a ranging listener fails to start or the listener fails
     */
    public void rangingBeaconsDidFailForRegion(Region region, Exception exception);

    /*
     * Most likely when Bluetooth aerial is switched on or off
     */
    public void didChangeAuthorizationStatus(String status);
}

