package org.uberfire.ext.layout.editor.client.novo.template.research.layout.screens;

public enum Screens {

    AnotherScreen,
    HelloWorldScreen,
    HomeScreen,
    HomeScreen1,
    HomeScreen2,
    HomeScreen3,
    HomeScreen4,
    HomeScreen5,
    MoodScreen;

    public static int next = -1;

    public static Screens next() {
        next = next + 1;
        return Screens.values()[next];
    }
}
