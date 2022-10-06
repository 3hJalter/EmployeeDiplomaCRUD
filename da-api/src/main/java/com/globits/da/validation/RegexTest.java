package com.globits.da.validation;

public class RegexTest {
    public static void main(String[] args) {
        String[] testStrings = {"test", " test", "te st", "test ", "te   st",
                " t e s t ", " ", "", "\ttest", "   "};
        for (String string : testStrings) {
            System.out.println( "Does \"" + string + "\" contain whitespace? " +
                    string.matches(".*\\s.*"));
        }
    }
}
