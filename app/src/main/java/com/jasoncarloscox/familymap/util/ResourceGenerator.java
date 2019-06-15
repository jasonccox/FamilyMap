package com.jasoncarloscox.familymap.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Gender;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

/**
 * Helper methods to generate resources.
 */
public class ResourceGenerator {

    static {
        Iconify.with(new FontAwesomeModule());
    }

    /**
     * Creates and returns an icon based on the given gender.
     *
     * @param context the context
     * @param gender the gender the icon will represent
     * @param sizeDP the size in dp of the icon
     * @return a gender icon
     */
    public static Drawable genderIcon(Context context, String gender, int sizeDP) {
        IconDrawable icon;

        if (Gender.MALE.equals(gender)) {
            icon = new IconDrawable(context, FontAwesomeIcons.fa_male);
            icon.colorRes(R.color.colorMale);
        } else {
            icon = new IconDrawable(context, FontAwesomeIcons.fa_female);
            icon.colorRes(R.color.colorFemale);
        }

        icon.sizeDp(sizeDP);

        return icon;
    }

    /**
     * Creates and returns an event icon.
     *
     * @param context the context
     * @param sizeDP the size in dp of the icon
     * @param colorRes the resource id of the color to make the icon
     * @return an event icon
     */
    public static Drawable eventIcon(Context context, int sizeDP, int colorRes) {
        return new IconDrawable(context, FontAwesomeIcons.fa_calendar_o)
                .colorRes(colorRes)
                .sizeDp(sizeDP);
    }

    /**
     * @param gender the gender to be represented
     * @param res a Resources object
     * @return a string representing the gender
     */
    public static String genderString(String gender, Resources res) {
        if (Gender.MALE.equals(gender)) {
            return res.getString(R.string.male);
        }

        if (Gender.FEMALE.equals(gender)) {
            return res.getString(R.string.female);
        }

        return res.getString(R.string.unknown);
    }

    /**
     * @param gender the gender to be represented
     * @param res a Resources object
     * @return an abbreviated string representing the gender
     */
    public static String genderStringShort(String gender, Resources res) {
        if (Gender.MALE.equals(gender)) {
            return res.getString(R.string.male_short);
        }

        if (Gender.FEMALE.equals(gender)) {
            return res.getString(R.string.female_short);
        }

        return res.getString(R.string.unknown);
    }


    /**
     * Creates and returns a search icon.
     *
     * @param context the context
     * @param sizeDP the size in dp of the icon
     * @param colorRes the resource id of the color to make the icon
     * @return a search icon
     */
    public static Drawable searchIcon(Context context, int sizeDP, int colorRes) {
        return new IconDrawable(context, FontAwesomeIcons.fa_search)
                .colorRes(colorRes)
                .sizeDp(sizeDP);
    }

    /**
     * Creates and returns an icon to go in the action bar.
     *
     * @param context the context
     * @param colorRes the resource id of the color to make the icon
     * @param icon the FontAwesome icon to be created
     * @return an icon that fits in the action bar
     */
    public static Drawable iconActionBar(Context context, int colorRes, FontAwesomeIcons icon) {
        return new IconDrawable(context, icon).colorRes(colorRes).actionBarSize();
    }
}
