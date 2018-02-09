package fr.unice.polytech.qgl.qda;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by Aloysius_tim on 10/03/2016.
 */
public class Debug {
    public static void println(String x) {
        PrintStream out=System.out;
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        System.out.println(x);
        System.setOut(out);
    }
}