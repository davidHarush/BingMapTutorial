package com.david.haru.bingmaptutorial.util;

import android.location.Location;

import com.david.haru.bingmaptutorial.places.Place;

import java.util.Comparator;

public class PlacesDistanceComparator implements Comparator<Place.Item> {
    Location currentLoc;

    public PlacesDistanceComparator(Location current) {
        currentLoc = current;
    }

    @Override
    public int compare(final Place.Item location, final Place.Item location2) {
        double distanceToPlace1 = currentLoc.distanceTo(location.getLocation());
        double distanceToPlace2 = currentLoc.distanceTo(location2.getLocation());
        return (int) (distanceToPlace1 - distanceToPlace2);
    }
}