package oop.tracker.inputs;

import oop.tracker.exceptions.MenuOutException;
import oop.tracker.interfaces.Input;

import java.util.Scanner;
/**
 * Class ConsoleInput
 * @author Egor Novikov
 * E-mail: enovikovdev@gmail.com
 * @version 1
 * @since 14/06/2018
 */
public class ConsoleInput implements Input {

    private Scanner scanner = new Scanner(System.in);

    public String ask(String question) {
        System.out.print(question);
        return scanner.nextLine();
    }

    public int ask(String question, int[] range) {
        int key = Integer.valueOf(this.ask(question));
        boolean exists = false;
        for (int value : range) {
            if (value == key) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            throw new MenuOutException("Out of menu range");
        }
        return key;
    }
}