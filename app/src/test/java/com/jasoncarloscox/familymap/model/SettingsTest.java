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
        settings.setShouldShowLifeLines(!settings.shouldShowLifeLines());
        assertTrue(settings.isAltered());
        assertTrue(settings.areLinesAltered());
        assertTrue(settings.isLifeLineVisibilityAltered());
        settings.setShouldShowLifeLines(!settings.shouldShowLifeLines());
        assertFalse(settings.isAltered());
        assertFalse(settings.areLinesAltered());
        assertFalse(settings.isLifeLineVisibilityAltered());

        LineSetting.Color originalColor = settings.getLifeLineColor();
        settings.setLifeLineColor(LineSetting.Color.BLACK);
        assertTrue(settings.isAltered());
        assertTrue(settings.areLinesAltered());
        assertTrue(settings.isLifeLineColorAltered());
        settings.setLifeLineColor(originalColor);
        assertFalse(settings.isAltered());
        assertFalse(settings.areLinesAltered());
        assertFalse(settings.isLifeLineColorAltered());

        settings.setShouldShowSpouseLines(!settings.shouldShowSpouseLines());
        assertTrue(settings.isAltered());
        assertTrue(settings.areLinesAltered());
        assertTrue(settings.isSpouseLineVisibilityAltered());
        settings.setShouldShowSpouseLines(!settings.shouldShowSpouseLines());
        assertFalse(settings.isAltered());
        assertFalse(settings.areLinesAltered());
        assertFalse(settings.isSpouseLineVisibilityAltered());

        originalColor = settings.getSpouseLineColor();
        settings.setSpouseLineColor(LineSetting.Color.BLACK);
        assertTrue(settings.isAltered());
        assertTrue(settings.areLinesAltered());
        assertTrue(settings.isSpouseLineColorAltered());
        settings.setSpouseLineColor(originalColor);
        assertFalse(settings.isAltered());
        assertFalse(settings.areLinesAltered());
        assertFalse(settings.isSpouseLineColorAltered());

        settings.setShouldShowTreeLines(!settings.shouldShowTreeLines());
        assertTrue(settings.isAltered());
        assertTrue(settings.areLinesAltered());
        assertTrue(settings.isTreeLineVisibilityAltered());
        settings.setShouldShowTreeLines(!settings.shouldShowTreeLines());
        assertFalse(settings.isAltered());
        assertFalse(settings.areLinesAltered());
        assertFalse(settings.isTreeLineVisibilityAltered());

        originalColor = settings.getTreeLineColor();
        settings.setTreeLineColor(LineSetting.Color.BLACK);
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
        settings.setShouldShowLifeLines(!settings.shouldShowLifeLines());
        settings.setLifeLineColor(LineSetting.Color.BLACK);
        settings.setShouldShowSpouseLines(!settings.shouldShowSpouseLines());
        settings.setSpouseLineColor(LineSetting.Color.BLACK);
        settings.setShouldShowTreeLines(!settings.shouldShowTreeLines());
        settings.setTreeLineColor(LineSetting.Color.BLACK);
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