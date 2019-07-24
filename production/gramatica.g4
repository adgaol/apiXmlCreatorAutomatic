grammar gramatica;
@header {
    import vistdsapixmlcreator.Writer;
    import vistdsapixmlcreator.Node;
    import vistdsapixmlcreator.Paso;
}
@members {
    Writer writer = new Writer("gramatica.g4","./descendent","cadena.txt",true);
}



exp  returns [Exp expO] 
    :
    {
        Exp expO=new Exp();
        Node node=writer.addPasoNoTerminalDes("EXP", null, null, expO, false, null, null);
    } 

bO=b[node, true] aO=a[node, true, Integer.parseInt(((ExpContext)_localctx).bO.bO.getValue())] puntoComaO=puntoComa[node, false] 
    {
        writer.updateNoTerminals("EXP::= B {A.valor=B.result;} A ; {print(A.result);}", ((ExpContext)_localctx).aO.aO.getValue(), expO, ((ExpContext)_localctx).bO.bO);
        System.out.println(((ExpContext)_localctx).aO.aO.getValue());
        _localctx.expO=expO;   
        writer.writeXML();
    }
    ;
a [Node nodeAnt,Boolean haveBrother,Integer her]  returns  [A aO]
    :
    { 
        A aO=new A();
        Node node=writer.addPasoNoTerminalDes("A", "valor", "result", aO, haveBrother, her.toString(), nodeAnt);
    }
masO=mas[node, true] bO=b[node, true] aeO=a[node, false, Integer.parseInt(((AContext)_localctx).bO.bO.getValue())+her] 
    {    
        writer.updateNoTerminals("A::= + B {A1.valor=A.valor+B.result;} A1 {A.result=A1.result;}", ((AContext)_localctx).aeO.aO.getValue(), aO, ((AContext)_localctx).masO.masO);
        _localctx.aO=aO;   
    } 
    | 
    {
        A aO=new A();     
        writer.addPasoLambdaDes("A", "valor", "result", her.toString(), "{A.result=A.valor;}", aO, haveBrother, nodeAnt);
        _localctx.aO=aO;
    }
    ;
b  [Node nodeAnt,Boolean haveBrother] returns  [B bO]
    :
    {
        B bO=new B();
        Node node=writer.addPasoNoTerminalDes("B", null, "result", bO, haveBrother, null, nodeAnt);
    } 
numO=number[node, true] cO=c[node, false, Integer.parseInt(((BContext)_localctx).numO.numberO.getValue())] 
    {
        writer.updateNoTerminals("B::= num {C.valor=num.vlex;} C {B.result=C.result;}", ((BContext)_localctx).cO.cO.getValue(), bO, ((BContext)_localctx).numO.numberO);    
        _localctx.bO=bO;
    }
    ;

c [Node nodeAnt,Boolean haveBrother,Integer her]   returns  [C cO]
    :
    {
        C cO=new C();
        Node node=writer.addPasoNoTerminalDes("C", "valor", "result", cO, haveBrother, her.toString(), nodeAnt);        
    }  
porO=por[node, true] numO=number[node, true] ceO=c[node, false, her*Integer.parseInt(((CContext)_localctx).numO.numberO.getValue())] 
    {
        writer.updateNoTerminals("C::= * num {C1.valor=C.valor*num.vlex;} C1 {C.result=C1.result;}", ((CContext)_localctx).ceO.cO.getValue(), cO, ((CContext)_localctx).porO.porO);
        _localctx.cO=cO;} 
    |
    {
        C cO=new C();    
        writer.addPasoLambdaDes("C", "valor", "result", her.toString(), "{C.result=C.valor;}", cO, haveBrother, nodeAnt);
        _localctx.cO=cO;
    }
    ;


number [Node nodeAnt, Boolean haveBrother]  returns [Number numberO]
    : Number {
        
        Number numberO=new Number();
        writer.addPasoTerminalDes("number", "vlex", Integer.parseInt(this._ctx.getText()), numberO, haveBrother, nodeAnt);
        
        _localctx.numberO=numberO;
    }
    ;
por [Node nodeAnt, Boolean haveBrother]  returns [Por porO]
    : Por {
        
        Por porO=new Por();
        writer.addPasoTerminalDes(this._ctx.getText(), null, porO, haveBrother, nodeAnt);
        
        _localctx.porO=porO;
    }
    ;
mas [Node nodeAnt, Boolean haveBrother]  returns [Mas masO]
    : Mas {
        
        Mas masO=new Mas();
        writer.addPasoTerminalDes(this._ctx.getText(), null, masO, haveBrother, nodeAnt);
        
        _localctx.masO=masO;
    }
    ;
puntoComa [Node nodeAnt, Boolean haveBrother]  returns [PuntoComa puntoComaO]
    : PuntoComa {
        
        PuntoComa puntoComaO=new PuntoComa();
        writer.addPasoTerminalDes(this._ctx.getText(), null, puntoComaO, haveBrother, nodeAnt);
        
        _localctx.puntoComaO=puntoComaO;
    }
    ;
Number 
    : ('0'..'9')+ {
        }
    ;   
Por
    : '*'{

    }
    ;
Mas
    : '+'{

    }
    ;
PuntoComa
    : ';'{

    }
    ;
