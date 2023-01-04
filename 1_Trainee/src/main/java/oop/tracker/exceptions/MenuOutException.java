package oop.tracker.exceptions;
/**
 * Class MenuOutException.
 * Error when selecting the wrong menu item.
 * @author Egor Novikov
 * E-mail: enovikovdev@gmail.com
 * @version 1
 * @since 14/06/2018
 */
public class MenuOutException extends RuntimeException {
    public MenuOutException(String msg) {
        super(msg);
    }
}