package com.group4;

/**
 * Class to print the statements and errors
 * @author Akshat Soni
 */
public class CustomPrint {
    /**
     * get - shouldPrintComments
     */
    static final boolean shouldPrintComments = true;

    /**
     * @param obj Object to print on same line
     * @author Akshat Soni
     */
    static void print(Object obj) {
        if (!shouldPrintComments)
            return;
        System.out.print(obj);
    }

    /**
     * @param TAG Tag to the print
     * @param obj Object to print on same line
     * @author Akshat Soni
     */
    static void print(String TAG, Object obj) {
        if (!shouldPrintComments)
            return;
        System.out.print(STR."[\{TAG}] :: \{obj}");
    }

    /**
     * @param obj Object to print on new line
     * @author Akshat Soni
     */
    static void println(Object obj) {
        if (!shouldPrintComments)
            return;
        System.out.println(obj);
    }

    /**
     * @param TAG Tag to the print
     * @param obj Object to print on new line
     * @author Akshat Soni
     */
    static void println(String TAG, Object obj) {
        if (!shouldPrintComments)
            return;
        System.out.println(STR."[\{TAG}] :: \{obj}");
    }

    /**
     * @param obj Object to print error on new line
     * @author Akshat Soni
     */
    static void printError(Object obj) {
        if (!shouldPrintComments)
            return;
        System.err.println(obj);
    }

    /**
     * @param TAG Tag to the print
     * @param obj Object to print error on new line
     * @author Akshat Soni
     */
    static void printError(String TAG, Object obj) {
        if (!shouldPrintComments)
            return;
        System.err.println(STR."[\{TAG}] :: \{obj}");
    }
}
