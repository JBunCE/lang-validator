package com.jbunce.pvlpus.automatapila;

import lombok.Getter;
import lombok.extern.java.Log;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Getter
public class Validator {

    //data types
    private final Production T;

    //Letter
    private final Production L;

    //Digit
    private final Production D;

    //Other
    private final Production G;

    //Arrow
    private final Production F;

    //Semicolon
    private final Production M2;

    // Non-terminal productions

    // N -> L N | D N | G N | L
    private final Production N;

    //FN -> F N
    private final Production FN;

    //T2 -> T FN
    private final Production T2;

    //S -> T2 M2 |
    private final Production S;

    private final Production P2;

    private final Production C;

    private final Production $;

    private Stack<Production> stack = new Stack<>();
    private int index = 0;

    private final Logger logger;

    public Validator(org.slf4j.Logger logger) {
        this.logger = logger;

        //data types
        T = new Production("int", "char", "bool", "string");
        T.setName("T");
        T.setTerminal(true);

        //Letter
        L = new Production(Pattern.compile("[A-Za-z]"));
        L.setName("L");
        L.setASet(true);
        L.setTerminal(true);

        //Digit
        D = new Production(Pattern.compile("[0-9]"));
        D.setName("D");
        D.setASet(true);
        D.setTerminal(true);

        //Other
        G = new Production("_");
        G.setName("G");
        G.setTerminal(true);

        //Arrow
        F = new Production("->");
        F.setName("F");
        F.setTerminal(true);

        Production F1 = new Production("<-");
        F1.setName("F1");
        F1.setTerminal(true);

        //logic
        Production OL = new Production("||", "&&");
        OL.setName("OL");
        OL.setTerminal(true);

        //Equals
        Production K = new Production("=");
        K.setName("K");
        K.setTerminal(true);

        // Boolean true
        Production BT = new Production("true");
        BT.setName("BT");
        BT.setTerminal(true);

        // Boolean false
        Production BF = new Production("false");
        BF.setName("BF");
        BF.setTerminal(true);

        // for
        Production B2 = new Production("for");
        B2.setName("B2");
        B2.setTerminal(true);

        // if
        Production I1 = new Production("if");
        I1.setName("I1");
        I1.setTerminal(true);

        // else
        Production E = new Production("else");
        E.setName("E");
        E.setTerminal(true);

        Production TE = new Production("then");
        TE.setName("TE");
        TE.setTerminal(true);

        Production P1 = new Production("(");
        P1.setName("P1");
        P1.setTerminal(true);

        P2 = new Production(")");
        P2.setName("P1");
        P2.setTerminal(true);

        Production C1 = new Production("{}");
        C1.setName("C1");
        C1.setTerminal(true);

        Production TO = new Production("to");
        TO.setName("TO");
        TO.setTerminal(true);

        Production R = new Production("<", ">", "==", "<=", ">=", "!=");
        R.setName("R");
        R.setTerminal(true);

        // M

        // "
        Production M = new Production("\"");
        M.setName("M");
        M.setTerminal(true);

        // '
        Production Mv = new Production(",");
        Mv.setName("Mv");
        Mv.setTerminal(true);

        Production Mi = new Production("'");
        Mi.setName("Mi");
        Mi.setTerminal(true);

        //Semicolon
        M2 = new Production(";");
        M2.setName("M2");
        M2.setTerminal(true);

        // N -> L N | D N | G N | L
        // N -> [//w*[A-Za-z0-9]+]
        N = new Production(Pattern.compile("^[A-Za-z]+[A-Za-z0-9]*$"));
        N.setName("N");
        N.setTerminal(true);
        N.setASet(true);
        /*
            N.setRecursive(true);
            N.setNext(List.of(
                new Production[]{L, N},
                new Production[]{D, N},
                new Production[]{G, N},
                new Production[]{L})
            );
        */

        // Non-terminal productions

        // P -> P1 P2
        Production P = new Production();
        P.setName("P");
        P.setTerminal(false);
        P.setNext(Stream.<Production[]>of(
                new Production[]{P1, P2}
        ).toList());

        // Mn -> M N
        Production Mn = new Production();
        Mn.setName("Mn");
        Mn.setTerminal(false);
        Mn.setNext(Stream.<Production[]>of(
                new Production[]{M, N}
        ).toList());

        // MiL -> Mi L
        Production MiL = new Production();
        MiL.setName("MiL");
        MiL.setTerminal(false);
        MiL.setNext(Stream.<Production[]>of(
                new Production[]{Mi, L}
        ).toList());

        // MiD -> Mi D
        Production MiD = new Production();
        MiD.setName("MiD");
        MiD.setTerminal(false);
        MiD.setNext(Stream.<Production[]>of(
                new Production[]{Mi, D}
        ).toList());

        // MF -> P M2
        Production MF = new Production();
        MF.setName("MF");
        MF.setTerminal(false);
        MF.setNext(Stream.<Production[]>of(
                new Production[]{P, M2}
        ).toList());

        // Ton -> TO N
        Production Ton = new Production();
        Ton.setName("Ton");
        Ton.setTerminal(false);
        Ton.setNext(Stream.<Production[]>of(
                new Production[]{TO, N}
        ).toList());

        // Tod -> TO D
        Production Tod = new Production();
        Tod.setName("Tod");
        Tod.setTerminal(false);
        Tod.setNext(Stream.<Production[]>of(
                new Production[]{TO, D}
        ).toList());

        // Dt -> D Tod | D ton
        Production Dt = new Production();
        Dt.setName("Dt");
        Dt.setTerminal(false);
        Dt.setNext(Stream.<Production[]>of(
                new Production[]{D, Tod},
                new Production[]{D, Ton}
        ).toList());

        // Nt -> N Ton | N Tod
        Production Nt = new Production();
        Nt.setName("Nt");
        Nt.setTerminal(false);
        Nt.setNext(Stream.<Production[]>of(
                new Production[]{N, Ton},
                new Production[]{N, Tod}
        ).toList());

        // NF -> N MF
        Production NF = new Production();
        NF.setName("NF");
        NF.setTerminal(false);
        NF.setNext(Stream.<Production[]>of(
                new Production[]{N, MF}
        ).toList());

        // PO -> P1 Dt | P1 Nt
        Production PO = new Production();
        PO.setName("PO");
        PO.setTerminal(false);
        PO.setNext(Stream.<Production[]>of(
                new Production[]{P1, Dt},
                new Production[]{P1, Nt}
        ).toList());

        // AF -> PO P2
        Production AF = new Production();
        AF.setName("AF");
        AF.setTerminal(false);
        AF.setNext(Stream.<Production[]>of(
                new Production[]{PO, P2}
        ).toList());

        // B1 -> B2 N
        Production B1 = new Production();
        B1.setName("B1");
        B1.setTerminal(false);
        B1.setNext(Stream.<Production[]>of(
                new Production[]{B2, N}
        ).toList());

        // B -> B1 AF
        Production B = new Production();
        B.setName("B");
        B.setTerminal(false);
        B.setNext(Stream.<Production[]>of(
                new Production[]{B1, AF}
        ).toList());

        // C -> P C1
        C = new Production();
        C.setName("C");
        C.setTerminal(false);
        C.setNext(Stream.<Production[]>of(
                new Production[]{P, C1}
        ).toList());

        // V -> Mn M | MiL Mi | MiD Mi | BT | BF
        Production V = new Production();
        V.setName("V");
        V.setTerminal(false);
        V.setNext(Stream.<Production[]>of(
                new Production[]{Mn, M},
                new Production[]{MiL, Mi},
                new Production[]{MiD, Mi},
                new Production[]{BT},
                new Production[]{BF},
                new Production[]{D}
        ).toList());

        // Vm -> V M2
        Production Vm = new Production();
        Vm.setName("Vm");
        Vm.setTerminal(false);
        Vm.setNext(Stream.<Production[]>of(
                new Production[]{V, M2}
        ).toList());

        // NR -> N R
        Production NR = new Production();
        NR.setName("NR");
        NR.setTerminal(false);
        NR.setNext(Stream.<Production[]>of(
                new Production[]{N, R}
        ).toList());

        // VR -> V R
        Production VR = new Production();
        VR.setName("VR");
        VR.setTerminal(false);
        VR.setNext(Stream.<Production[]>of(
                new Production[]{V, R}
        ).toList());

        // OP -> NR N | VR V | NR V | VR N
        Production OP = new Production();
        OP.setName("OP");
        OP.setTerminal(false);
        OP.setNext(Stream.<Production[]>of(
                new Production[]{NR, N},
                new Production[]{VR, V},
                new Production[]{NR, V},
                new Production[]{VR, N}
        ).toList());

        // O -> OP OL | OP
        Production O = new Production();
        O.setName("O");
        O.setTerminal(false);
        O.setNext(Stream.<Production[]>of(
                new Production[]{OP, OL},
                new Production[]{OP}
        ).toList());

        // POL -> P1 O
        Production POL = new Production();
        POL.setName("POL");
        POL.setTerminal(false);
        POL.setNext(Stream.<Production[]>of(
                new Production[]{P1, O}
        ).toList());

        // PO -> POL P2
        Production POi = new Production();
        POi.setName("PO2");
        POi.setTerminal(false);
        POi.setNext(Stream.<Production[]>of(
                new Production[]{POL, P2}
        ).toList());

        // I -> I1 POi
        Production I = new Production();
        I.setName("I");
        I.setTerminal(false);
        I.setNext(Stream.<Production[]>of(
                new Production[]{I1, POi}
        ).toList());

        // Kv -> K Vm
        Production Kv = new Production();
        Kv.setName("Kv");
        Kv.setTerminal(false);
        Kv.setNext(Stream.<Production[]>of(
                new Production[]{K, Vm}
        ).toList());

        // Kc -> K C
        Production Kc = new Production();
        Kc.setName("Kc");
        Kc.setTerminal(false);
        Kc.setNext(Stream.<Production[]>of(
                new Production[]{K, C}
        ).toList());

        //FN -> F N
        FN = new Production();
        FN.setName("FN");
        FN.setTerminal(false);
        FN.setNext(Stream.<Production[]>of(
                new Production[]{F, N}
        ).toList());

        //F1N -> F1 N
        Production F1N = new Production();
        F1N.setName("F1N");
        F1N.setTerminal(false);
        F1N.setNext(Stream.<Production[]>of(
                new Production[]{F1, N}
        ).toList());

        // FC -> F C
        Production FC = new Production();
        FC.setName("FC");
        FC.setTerminal(false);
        FC.setNext(Stream.<Production[]>of(
                new Production[]{F, C}
        ).toList());

        // Ts -> TE FC
        Production Ts = new Production();
        Ts.setName("Ts");
        Ts.setTerminal(false);
        Ts.setNext(Stream.<Production[]>of(
                new Production[]{TE, FC}
        ).toList());

        //T2 -> T FN
        T2 = new Production();
        T2.setName("T2");
        T2.setTerminal(false);
        T2.setNext(Stream.<Production[]>of(
                new Production[]{T, FN}
        ).toList());

        //T3 -> T F1N
        Production T3 = new Production();
        T3.setName("T3");
        T3.setTerminal(false);
        T3.setNext(Stream.<Production[]>of(
                new Production[]{T, F1N}
        ).toList());

        // Ei -> E FC
        Production Ei = new Production();
        Ei.setName("Ei");
        Ei.setTerminal(false);
        Ei.setNext(Stream.<Production[]>of(
                new Production[]{E, FC}
        ).toList());

        // MVL -> Mv Ei
        Production MVL = new Production();
        MVL.setName("MVL");
        MVL.setTerminal(false);
        MVL.setNext(Stream.<Production[]>of(
                new Production[]{Mv, Ei}
        ).toList());

        // TEi
        Production TEi = new Production();
        TEi.setName("TEi");
        TEi.setTerminal(false);
        TEi.setNext(Stream.<Production[]>of(
                new Production[]{Ts, MVL}
        ).toList());

        // MV3 -> Mv T2
        Production MV3 = new Production();
        MV3.setName("MV3");
        MV3.setTerminal(false);
        MV3.setNext(Stream.<Production[]>of(
                new Production[]{Mv, T2}
        ).toList());

        // Pf1 -> T2 MV3 | T FN
        Production Pf1 = new Production();
        Pf1.setName("Pf1");
        Pf1.setTerminal(false);
        Pf1.setNext(Stream.<Production[]>of(
                new Production[]{T2, MV3},
                new Production[]{T, FN}
        ).toList());

        // Pfp -> P1 Pf1
        Production Pfp = new Production();
        Pfp.setName("Pfp");
        Pfp.setTerminal(false);
        Pfp.setNext(Stream.<Production[]>of(
                new Production[]{P1, Pf1}
        ).toList());

        // Pfv -> Pfp P2
        Production Pfv = new Production();
        Pfv.setName("Pfv");
        Pfv.setTerminal(false);
        Pfv.setNext(Stream.<Production[]>of(
                new Production[]{Pfp, P2}
        ).toList());

        // Pf -> Pfv C1
        Production Pf = new Production();
        Pf.setName("Pf");
        Pf.setTerminal(false);
        Pf.setNext(Stream.<Production[]>of(
                new Production[]{Pfv, C1}
        ).toList());

        // Tf -> T1 Fin
        Production Tf = new Production();
        Tf.setName("Tf");
        Tf.setTerminal(false);
        Tf.setNext(Stream.<Production[]>of(
                new Production[]{T, F1N}
        ).toList());

        //S -> T2 M2 | T2 Kv | T3 Kc
        S = new Production();
        S.setName("S");
        S.setTerminal(false);
        S.setNext(Stream.<Production[]>of(
                new Production[]{T2, M2},
                new Production[]{T2, Kv},
                new Production[]{T3, Kc},
                new Production[]{B, FC},
                new Production[]{B, NF},
                new Production[]{I, Ts},
                new Production[]{I, TEi},
                new Production[]{Tf, Pf}
        ).toList());

        $ = new Production("$");
        $.setName("$");
        $.setTerminal(true);

        stack.push($);
        stack.push(S);
    }

    public boolean validate (String input) {

        while (!stack.isEmpty()) {
            System.out.println(stack);
            logger.warn(stack.toString()  + "\n");
            Production currentProduction = stack.peek();
            if (currentProduction.isTerminal()) {

                if (currentProduction.isASet()) {
                    String currentInput = getCurrentInput(index, input);
                    if (currentProduction.getSymbolMatcher().matcher(currentInput).matches()) {
                        stack.pop();
                        index++;
                    } else {
                        return false;
                    }
                } else {
                    boolean result = false;
                    String currentInput = getCurrentInput(index, input);
                    for (int i = currentProduction.getSymbol().length - 1; i >= 0; i--) {
                        if (currentProduction.getSymbol()[i].equals(currentInput)) {
                            result = true;
                            stack.pop();
                            index++;
                            i = -1;
                        }
                    }
                    if (!result) {
                        return false;
                    }
                }

            } else {
                Production nonTerminal = stack.pop();
                for (Production[] productions : nonTerminal.getNext()) {
                    logger.warn(stack.toString() + "\n");
                    System.out.println(stack);
                    int currentIndex = index;
                    Stack<Production> before = (Stack<Production>) stack.clone();
                    for (int i = productions.length - 1; i >= 0; i--) {
                        stack.push(productions[i]);
                    }

                    boolean result = validate(input);
                    if (result) {
                        return true;
                    }
                    stack = before;
                    index = currentIndex;
                }
                return false;
            }
        }

        return stack.isEmpty();
    }


    // la siguiente funcion esta horriblemente mal escrita pero ya tenia mucho sueño :c
    private String getCurrentInput(int index, String input) {
        // Eliminar los espacios iniciales
        input = input.trim();

        // Dividir la entrada en tokens basándose en los espacios
        String[] tokens = input.split("\\s+");

        // Crear una lista para almacenar los tokens finales
        List<String> finalTokens = new ArrayList<>();

        // Recorrer los tokens
        for (String token : tokens) {
            // Si el token es una cadena entre comillas
            if (token.startsWith("\"") && token.endsWith("\";")) {
                finalTokens.add("\"");
                finalTokens.add(token.substring(1, token.length() - 2)); // Agregar "\w*[A-Za-z0-9]+"
                finalTokens.add("\"");
                finalTokens.add(";");
            }
            // Si el token es un número entre comillas simples
            else if (token.startsWith("'") && token.endsWith("';")) {
                finalTokens.add("'");
                finalTokens.add(token.substring(1, token.length() - 2)); // Agregar "[0-9]+"
                finalTokens.add("'");
                finalTokens.add(";");
            }
            // Si el token coincide con el patrón "\w*[A-Za-z0-9]+\(\);"
            else if (token.matches("\\w*[A-Za-z0-9]+\\(\\);")) {
                finalTokens.add(token.substring(0, token.length() - 3)); // Agregar "\w*[A-Za-z0-9]+"
                finalTokens.add("(");
                finalTokens.add(")");
                finalTokens.add(";");
            }
            // Si el token coincide con el patrón "\w*[A-Za-z0-9]+;"
            else if (token.matches("\\w*[A-Za-z0-9]+;")) {
                finalTokens.add(token.substring(0, token.length() - 1)); // Agregar "\w*[A-Za-z0-9]+"
                finalTokens.add(";");
            }
            // Si el token es un paréntesis de apertura
            else if (token.equals("(")) {
                finalTokens.add("(");
            }
            // Si el token es un paréntesis de cierre
            else if (token.equals(")")) {
                finalTokens.add(")");
            }
            else if (token.equals("()")) {
                finalTokens.add("(");
                finalTokens.add(")");
            } else {
                finalTokens.add(token);
            }
        }

        // Comprobar si el índice es válido
        if (index < 0 || index >= finalTokens.size()) {
            return "$";
        }

        // Devolver el token en el índice especificado
        return finalTokens.get(index);
    }
}
