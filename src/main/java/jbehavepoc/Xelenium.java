package jbehavepoc;

import org.testng.TestNG;

public class Xelenium {

    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[] { "res/testng.xml" };
        }
        TestNG.main(args);
    }
}
