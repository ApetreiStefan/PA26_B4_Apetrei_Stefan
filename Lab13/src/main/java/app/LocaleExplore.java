package app;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import com.DisplayLocales;
import com.SetLocale;
import com.Info;

public class LocaleExplore {
    private Locale locale;
    private ResourceBundle messages;

    public LocaleExplore() {
        setLocale(Locale.getDefault());
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        try {
            this.messages = ResourceBundle.getBundle("res.Messages", locale);
        } catch (Exception e) {
            this.messages = ResourceBundle.getBundle("res.Messages", Locale.getDefault());
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public ResourceBundle getMessages() {
        return messages;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(messages.getString("prompt") + " ");
            if (!scanner.hasNextLine()) break;
            String command = scanner.nextLine().trim();
            if (command.equals("exit")) {
                break;
            } else if (command.equals("display locales")) {
                DisplayLocales.execute(messages);
            } else if (command.startsWith("set locale ")) {
                String languageTag = command.substring(11).trim();
                SetLocale.execute(languageTag, this);
            } else if (command.equals("info")) {
                Info.execute(locale, messages);
            } else if (command.startsWith("info ")) {
                String languageTag = command.substring(5).trim();
                Info.execute(Locale.forLanguageTag(languageTag), messages);
            } else if (!command.isEmpty()) {
                System.out.println(messages.getString("invalid"));
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        new LocaleExplore().start();
    }
}
