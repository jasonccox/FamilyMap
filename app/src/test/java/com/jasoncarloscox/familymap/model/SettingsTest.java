package com.jasoncarloscox.familymap.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SettingsTest {

    private Settings settings;

    @Before
    public void setup() {
        settings = new Settings();
    }

    @Test
    public void detectsAlteration() {
        settings.setShowLifeLines(!settings.showLifeLines());
        assertTrue(settings.isAltered());
        assertTrue(settings.areLinesAltered());
        assertTrue(settings.isLifeLineVisibilityAltered());
        settings.setShowLifeLines(!settings.showLifeLines());
        assertFalse(settings.isAltered());
        assertFalse(settings.areLinesAltered());
        assertFalse(settings.isLifeLineVisibilityAltered());

        Color originalColor = settings.getLifeLineColor();
        settings.setLifeLineColor(Color.BLACK);
        assertTrue(settings.isAltered());
        assertTrue(settings.areLinesAltered());
        assertTrue(settings.isLifeLineColorAltered());
        settings.setLifeLineColor(originalColor);
        assertFalse(settings.isAltered());
        assertFalse(settings.areLinesAltered());
        assertFalse(settings.isLifeLineColorAltered());

        settings.setShowSpouseLines(!settings.showSpouseLines());
        assertTrue(settings.isAltered());
        assertTrue(settings.areLinesAltered());
        assertTrue(settings.isSpouseLineVisibilityAltered());
        settings.setShowSpouseLines(!settings.showSpouseLines());
        assertFalse(settings.isAltered());
        assertFalse(settings.areLinesAltered());
        assertFalse(settings.isSpouseLineVisibilityAltered());

        originalColor = settings.getSpouseLineColor();
        settings.setSpouseLineColor(Color.BLACK);
        assertTrue(settings.isAltered());
        assertTrue(settings.areLinesAltered());
        assertTrue(settings.isSpouseLineColorAltered());
        settings.setSpouseLineColor(originalColor);
        assertFalse(settings.isAltered());
        assertFalse(settings.areLinesAltered());
        assertFalse(settings.isSpouseLineColorAltered());

        settings.setShowTreeLines(!settings.showTreeLines());
        assertTrue(settings.isAltered());
        assertTrue(settings.areLinesAltered());
        assertTrue(settings.isTreeLineVisibilityAltered());
        settings.setShowTreeLines(!settings.showTreeLines());
        assertFalse(settings.isAltered());
        assertFalse(settings.areLinesAltered());
        assertFalse(settings.isTreeLineVisibilityAltered());

        originalColor = settings.getTreeLineColor();
        settings.setTreeLineColor(Color.BLACK);
        assertTrue(settings.isAltered());
        assertTrue(settings.areLinesAltered());
        assertTrue(settings.isTreeLineColorAltered());
        settings.setTreeLineColor(originalColor);
        assertFalse(settings.isAltered());
        assertFalse(settings.areLinesAltered());
        assertFalse(settings.isTreeLineColorAltered());

        MapType originalType = settings.getMapType();
        settings.setMapType(MapType.TERRAIN);
        assertTrue(settings.isAltered());
        assertFalse(settings.areLinesAltered());
        assertTrue(settings.isMapTypeAltered());
        settings.setMapType(originalType);
        assertFalse(settings.isAltered());
        assertFalse(settings.areLinesAltered());
        assertFalse(settings.isMapTypeAltered());
    }

    @Test
    public void resetAlteredResets() {
        settings.setShowLifeLines(!settings.showLifeLines());
        settings.setLifeLineColor(Color.BLACK);
        settings.setShowSpouseLines(!settings.showSpouseLines());
        settings.setSpouseLineColor(Color.BLACK);
        settings.setShowTreeLines(!settings.showTreeLines());
        settings.setTreeLineColor(Color.BLACK);
        settings.setMapType(MapType.TERRAIN);

        settings.resetAltered();
        assertFalse(settings.isAltered());
        assertFalse(settings.areLinesAltered());
        assertFalse(settings.isLifeLineVisibilityAltered());
        assertFalse(settings.isLifeLineColorAltered());
        assertFalse(settings.isSpouseLineVisibilityAltered());
        assertFalse(settings.isSpouseLineColorAltered());
        assertFalse(settings.isTreeLineVisibilityAltered());
        assertFalse(settings.isTreeLineColorAltered());
        assertFalse(settings.isMapTypeAltered());
    }
}