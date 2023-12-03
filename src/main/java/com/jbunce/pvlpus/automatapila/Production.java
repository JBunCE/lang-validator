package com.jbunce.pvlpus.automatapila;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Setter @Getter
public class Production {
    private String[] symbol;
    private String name;
    private List<Production[]> next;
    private boolean isTerminal;
    private boolean isASet;
    private Pattern symbolMatcher;

    public Production(String... symbol) {
        this.symbol = symbol;
    }

    public Production(Pattern symbol) {
        this.symbolMatcher = symbol;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    @Override
    public String toString() {
        return name;
    }
}