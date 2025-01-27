package com.oracle.dragon.util;

import java.time.Duration;

/**
 * Console related helper methods.
 */
public class Console {
    /**
     * Enable or disable console output ANSI color coding usage.
     */
    public static boolean ENABLE_COLORS = true;

    public final static int MAX_COLUMNS = 80;

    public static class Color {
        public final int r,g,b;

        public Color(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    // https://docs.microsoft.com/en-us/windows/console/console-virtual-terminal-sequences#screen-colors
    public enum Style {
        ANSI_RESET("\u001B[0m"),
        ANSI_UNDERLINE("\u001B[4;1m"),
        ANSI_INVERT("\u001B[7m"),
        ANSI_BRIGHT("\u001B[1m"),
        ANSI_BLACK("\u001B[30m"),
        ANSI_RED("\u001B[31;1m"),
        ANSI_BRIGHT_GREEN("\u001B[92;1m"),
        ANSI_BRIGHT_RED("\u001B[91;1m"),
        ANSI_BRIGHT_WHITE("\u001B[97;1m"),
        ANSI_YELLOW("\u001B[33;1m"),
        ANSI_BLUE("\u001B[34m"),
        ANSI_PURPLE("\u001B[35m"),
        ANSI_CYAN("\u001B[36m"),
        ANSI_WHITE("\u001B[37m"),
        ANSI_BRIGHT_CYAN("\u001B[96m"),
        ANSI_BLACK_BACKGROUND("\u001B[40m"),
        ANSI_RED_BACKGROUND("\u001B[41m"),
        ANSI_GREEN_BACKGROUND("\u001B[42m"),
        ANSI_YELLOW_BACKGROUND("\u001B[43m"),
        ANSI_BLUE_BACKGROUND("\u001B[44m"),
        ANSI_PURPLE_BACKGROUND("\u001B[45m"),
        ANSI_CYAN_BACKGROUND("\u001B[46m"),
        ANSI_WHITE_BACKGROUND("\u001B[47m"),
        ANSI_TITLE("\u001B[33;4;1m"),
        ANSI_VSC_DASH("\u001B[38;2;249;38;114m"),
        ANSI_VSC_BLUE("\u001B[38;2;97;202;220m");

        private final String pattern;

        Style(final String pattern) {
            this.pattern = pattern;
        }

        @Override
        public String toString() {
            return ENABLE_COLORS ? pattern : "";
        }
    }

    public static void printBounded(final String section, final String msg) {
        final int total = section.length() + msg.length() + 2 + 1;
        int spaces = MAX_COLUMNS - total + 1;
        final StringBuilder sb = new StringBuilder("> ").append(section);

        for (int i = 0; i < spaces; i++) {
            if (i == 0 || i == spaces - 1) {
                sb.append(' ');
            } else {
                sb.append('.');
            }
        }

        sb.append(msg);

        print(sb.toString());
    }

    public static void printBoundedln(final String section, final String msg) {
        printBounded(section, msg);
        println();
    }

    /**
     * Print a carriage return in the terminal.
     */
    public static void println() {
        System.out.print(Style.ANSI_RESET);
        System.out.println();
    }

    /**
     * Print a message to the terminal including a carriage return.
     *
     * @param msg the message to display
     */
    public static void println(final String msg) {
        System.out.print(msg);
        System.out.print(Style.ANSI_RESET);
        System.out.println();
    }

    /**
     * Print a message to the terminal erasing the previous line.
     *
     * @param msg the message to display
     */
    public static void print(final String msg) {
        System.out.print("\r");
        System.out.print(msg);
        System.out.print(Style.ANSI_RESET);
        System.out.flush();
    }

    public static String getDurationSince(long startTime) {
        final long durationMillis = System.currentTimeMillis() - startTime;
        if (durationMillis < 1000) {
            return String.format("0.%03ds", durationMillis);
        } else {
            final Duration duration = Duration.ofMillis(durationMillis);
            return duration.toString().substring(2).replaceAll("(\\d[HMS])(?!$)", "$1 ").replaceAll("\\.\\d+", "").toLowerCase();
        }
    }

    public static void printGradient(Color from, Color to, String msg, boolean bright, boolean underline) {
        final StringBuilder s = new StringBuilder();
        s.append(Style.ANSI_RESET);
        if (bright) {
            s.append(Style.ANSI_BRIGHT);
        }
        if (underline) {
            s.append(Style.ANSI_UNDERLINE);
        }

        final int length = msg.length();

        int r = from.r;
        int g = from.g;
        int b = from.b;

        double rd = r;
        double gd = g;
        double bd = b;

        final double rInc = (to.r - from.r) / (double)length;
        final double gInc = (to.g - from.g) / (double)length;
        final double bInc = (to.b - from.b) / (double)length;

        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                s.append(String.format("\u001B[38;2;%d;%d;%dm", to.r, to.g, to.b));
            } else {
                s.append(String.format("\u001B[38;2;%d;%d;%dm", r, g, b));
                rd += rInc;
                gd += gInc;
                bd += bInc;

                r = (int)rd;
                g = (int)gd;
                b = (int)bd;
            }

            s.append(msg.charAt(i));
        }


        s.append(Style.ANSI_RESET);
        print(s.toString());
    }
}
