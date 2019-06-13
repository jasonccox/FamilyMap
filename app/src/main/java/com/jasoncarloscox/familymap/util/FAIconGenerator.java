package com.jasoncarloscox.familymap.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Gender;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class FAIconGenerator {

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
}
