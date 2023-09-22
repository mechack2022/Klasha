package com.fagile.Klasha;

public enum Countries {

        ITALY("Italy"),
        NEW_ZEALAND("New Zealand"),
        GHANA("Ghana");

        private final String name;

        Countries(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


