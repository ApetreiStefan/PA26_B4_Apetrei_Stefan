package com;

import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public class Info {
    public static void execute(Locale locale, ResourceBundle messages) {
        System.out.println(MessageFormat.format(messages.getString("info"), locale.toString()));

        String displayCountry = locale.getDisplayCountry();
        String displayCountryLocale = locale.getDisplayCountry(locale);
        if (displayCountry.isEmpty()) {
            displayCountry = "N/A";
            displayCountryLocale = "N/A";
        }
        System.out.println("Country: " + displayCountry + " (" + displayCountryLocale + ")");

        String displayLanguage = locale.getDisplayLanguage();
        String displayLanguageLocale = locale.getDisplayLanguage(locale);
        if (displayLanguage.isEmpty()) {
            displayLanguage = "N/A";
            displayLanguageLocale = "N/A";
        }
        System.out.println("Language: " + displayLanguage + " (" + displayLanguageLocale + ")");

        try {
            Currency currency = Currency.getInstance(locale);
            System.out.println("Currency: " + currency.getCurrencyCode() + " (" + currency.getDisplayName() + ")");
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println("Currency: N/A");
        }

        System.out.print("Week Days: ");
        for (int i = 1; i <= 7; i++) {
            System.out.print(DayOfWeek.of(i).getDisplayName(TextStyle.FULL, locale));
            if (i < 7) {
                System.out.print(", ");
            }
        }
        System.out.println();

        System.out.print("Months: ");
        for (int i = 1; i <= 12; i++) {
            System.out.print(Month.of(i).getDisplayName(TextStyle.FULL, locale));
            if (i < 12) {
                System.out.print(", ");
            }
        }
        System.out.println();

        LocalDate today = LocalDate.now();
        DateTimeFormatter defaultFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.getDefault());
        DateTimeFormatter localeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale);

        System.out.println("Today: " + today.format(defaultFormatter) + " (" + today.format(localeFormatter) + ")");
    }
}
