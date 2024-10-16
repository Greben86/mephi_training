package mephi.exercise;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ColorPlayer {

    WHITE("White"), BLACK("Black");

    private final String color;

    public String getSymbol() {
        return color.substring(0, 1).toLowerCase();
    }
}
