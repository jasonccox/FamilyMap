package com.jasoncarloscox.familymap.model;

import com.google.android.gms.maps.GoogleMap;

public enum MapType {

    // IMPORTANT: the order of the enum values must stay in sync with the
    // map_types_array in strings.xml

    NORMAL(GoogleMap.MAP_TYPE_NORMAL),
    SATELLITE(GoogleMap.MAP_TYPE_SATELLITE),
    TERRAIN(GoogleMap.MAP_TYPE_TERRAIN),
    HYBRID(GoogleMap.MAP_TYPE_HYBRID);

    private int type;

    public int getType() {
        return type;
    }

    private MapType(int type) {
        this.type = type;
    }
}
