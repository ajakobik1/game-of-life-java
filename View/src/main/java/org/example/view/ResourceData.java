package org.example.view;

import java.util.ListResourceBundle;

public class ResourceData extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"authors", new String[] {"Lech Czochra", "Aleksandra Jakóbik"}},
                {"version", "1.0.0"}
        };
    }
}
