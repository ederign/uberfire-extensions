package org.uberfire.ext.layout.editor.client.novo.template.research.layout.screens;

import java.util.concurrent.ThreadLocalRandom;

public enum Screens {

    AnotherScreen,
    HelloWorldScreen,
    HomeScreen,
    HomeScreen1,
    HomeScreen2,
    HomeScreen3,
    HomeScreen4,
    HomeScreen5,
    MoodScreen,
    MoodScreen2,
    MoodScreen3;

    public static int next = -1;

    public static Screens next() {
        next = next + 1;
        return Screens.values()[next];
    }
}
