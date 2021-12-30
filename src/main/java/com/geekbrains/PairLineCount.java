package com.geekbrains;

public class PairLineCount {
    public int line;
    public int count;
    public LineEnum lineDirection;

    public PairLineCount(LineEnum horizontal) {
        this(0, 0, horizontal);
    }

    public PairLineCount(int line, int count, LineEnum horizontal) {
        this.line=line;
        this.count=count;
        this.lineDirection =horizontal;
    }

}
