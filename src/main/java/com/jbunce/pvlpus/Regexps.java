package com.jbunce.pvlpus;

public class Regexps {

    public static final String TYPES = "int|String|bool|char|string";
    public static final String N = "\\w+";
    public static final String N_VAR = "(\\w*[A-Za-z0-9]+)";
    public static final String BOOLEAN_VALUE = "true|false";
    public static final String OPERATIONS = "[+]|-|[*]|/|[%]|\\^";
    public static final String VALUE = "|"+ N_VAR +"|\".*?\"\\s*([+]\\s*("+ N_VAR +"|\".*?\"))*|'"+ N +"'" + "|" + BOOLEAN_VALUE +"|[0-9]";
    public static final String COMMENT = "^\\s*//.*$";
    public static final String ASSIGNMENT = "\\s*=\\s*("+ N_VAR + VALUE +");";
    public static final String VARIABLE_DECLARATION = "("+ TYPES +")\\s*->\\s*" + N_VAR;
    public static final String VARIABLE_D_A = "^\\s*("+VARIABLE_DECLARATION + ";" + "|" + VARIABLE_DECLARATION + ASSIGNMENT+")$";
    public static final String VARIABLE_ASSIGNMENT = "^(\\s*\\w+" + ASSIGNMENT+ ")$";
    public static final String ARG_DECLARATION = VARIABLE_DECLARATION + "(,\\s+" + VARIABLE_DECLARATION + ")*";
    public static final String ARGUMENTS = "("+ VALUE +")([,]("+ VALUE +"))*";
    public static final String FUNCTION_SIGNATURE = "(([(]\\s*"+ ARG_DECLARATION +"\\s*[)])|([(][)]))";
    public static final String FUNCTION ="^(\\s*("+ TYPES +"|void)\\s*<-\\s*"+ N_VAR +"(("+ FUNCTION_SIGNATURE +")|(\\s*[=]\\s*"+ FUNCTION_SIGNATURE +"))(\\s*[{]\\s*[}]*))$";
    public static final String FUNCTION_CALL = "(\\s*("+ N_VAR +"(([(]("+ ARGUMENTS +")[)])|[(][)]);))";
    public static final String FUNCTION_ASSIGNMENT = "^(\\s*"+ N_VAR +"\\s*[=]\\s*"+ FUNCTION_SIGNATURE +"\\s*[{][}]*)$";
    public static final String RETURN = "^\\s*return\\s+(("+ VALUE +")|"+ N_VAR +");$";
    public static final String VARIABLE_OPERATION = "(\\s*((("+ TYPES +")\\s+"+ N_VAR +"\\s+)|("+ N_VAR +"\\s+))->\\s+("+ N_VAR +"|([0-9]+))\\s*("+ OPERATIONS +")\\s*("+ N_VAR +"|[0-9]*);)";
    public static final String FOR_LOOP = "^((\\s*for\\s+"+ N_VAR +"\\s+[(]\\w+\\s+to\\s+\\w+[)]\\s+(->)\\s+)(("+ FUNCTION_CALL +")|([(][)][{]\\s*[}]*)))$";
    public static final String IF = "^\\s*if\\s+([(](("+ N_VAR + VALUE +")\\s+(==|<=|=>|>|<|!=)\\s+("+ N_VAR + VALUE +"))\\s*((&&|[|][|])\\s*("+ N_VAR + VALUE +")\\s+(==|<=|=>|>|<)\\s+("+ N_VAR + VALUE +"))*[)]\\s+then\\s+->\\s*[(][)]\\s*[{][}]*)$";
    public static final String ELSE = "^\\s*[}],\\s+(else)\\s+->\\s+[(][)]\\s*[{][}]*$";
    public static final String ALL = FUNCTION + "|" + VARIABLE_D_A + "|" + VARIABLE_ASSIGNMENT + "|" + COMMENT + "|" + FOR_LOOP + "|" + IF + "|" + ELSE + "|" + VARIABLE_OPERATION + "|^" + FUNCTION_CALL + "|" + RETURN + "|" + FUNCTION_ASSIGNMENT + "|^(\\s*[}]*)$|^(\\s*)$";

}
