%%
%class analizador_lexico
%cup
/*%debug*/
%%
([+-]? [0-9]+ | "0" [+-]? [0-7]+ | "0x" [+-]? [A-F0-9]+) {
return new java_cup.runtime.Symbol(sym.number,yytext());
/*System.out.println("<constint,"+yytext()+">");*/
}
"+" {
return new java_cup.runtime.Symbol(sym.mas,yytext());
}

"*" {
return new java_cup.runtime.Symbol(sym.por, yytext());
}

";" {
return new java_cup.runtime.Symbol(sym.puntoComa,yytext());
}
[^] {
}