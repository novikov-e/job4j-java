package oop.chess.figure.black;

import oop.chess.figure.Cell;
import oop.chess.figure.Figure;

/**
 * @author Egor Novikov (enovikovdev@gmail.com)
 * @version $Id$
 * @since 22/06/2018
 * Черная фигура - Король
 */
public class KingBlack implements Figure {
    private final Cell position;

    public KingBlack(final Cell position) {
        this.position = position;
    }

    @Override
    public Cell position() {
        return this.position;
    }

    @Override
    public Cell[] way(Cell source, Cell dest) {
        Cell[] steps = new Cell[0];
        for (int i = 1; i > -2; i--) {
            for (int j = 1; j > -2; j--) {
                if (source.y == dest.y + i && source.x == dest.x + j) {
                    steps = new Cell[] {dest};
                }
            }
        }
        return steps;
    }

    @Override
    public Figure copy(Cell dest) {
        return new KingBlack(dest);
    }
}