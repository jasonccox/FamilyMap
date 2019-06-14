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

public class ResourceGenerator {

    static {
        Iconify.with(new FontAwesomeModule());
    }

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

    public static Drawable eventIcon(Context context, int sizeDP, int colorRes) {
        return new IconDrawable(context, FontAwesomeIcons.fa_calendar_o)
                .colorRes(colorRes)
                .sizeDp(sizeDP);
    }

    public static String genderString(String gender, Resources res) {
        if (Gender.MALE.equals(gender)) {
            return res.getString(R.string.male);
        }

        if (Gender.FEMALE.equals(gender)) {
            return res.getString(R.string.female);
        }

        return res.getString(R.string.unknown);
    }

    public static String genderStringShort(String gender, Resources res) {
        if (Gender.MALE.equals(gender)) {
            return res.getString(R.string.male_short);
        }

        if (Gender.FEMALE.equals(gender)) {
            return res.getString(R.string.female_short);
        }

        return res.getString(R.string.unknown);
    }

    public static Drawable searchIcon(Context context, int sizeDP, int colorRes) {
        return new IconDrawable(context, FontAwesomeIcons.fa_search)
                .colorRes(colorRes)
                .sizeDp(sizeDP);
    }

    public static Drawable iconActionBar(Context context, int colorRes, FontAwesomeIcons icon) {
        return new IconDrawable(context, icon).colorRes(colorRes).actionBarSize();
    }
}
