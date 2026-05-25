package com;

import app.LocaleExplore;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class SetLocale {
    public static void execute(String languageTag, LocaleExplore app) {
        Locale locale = Locale.forLanguageTag(languageTag);
        app.setLocale(locale);
        ResourceBundle messages = app.getMessages();
        System.out.println(MessageFormat.format(messages.getString("locale.set"), locale.toString()));
    }
}
