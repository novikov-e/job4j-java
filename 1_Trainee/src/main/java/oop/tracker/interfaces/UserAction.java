package oop.tracker.interfaces;

import oop.tracker.storage.Tracker;

/**
 * Interface UserAction.
 * @author Egor Novikov
 * E-mail: enovikovdev@gmail.com
 * @version 1
 * @since 14/06/2018
 */
public interface UserAction {

    int key();

    void execute(Input input, Tracker tracker);

    String info();
}