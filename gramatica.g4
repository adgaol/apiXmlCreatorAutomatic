grammar gramatica;


exp  returns [Exp expO] 
    :
    {
        Exp expO=new Exp();
        Node node=writer.addPasoNoTerminalDes("EXP", null, null, expO, false, null, null);
    } 

    bO=b aO=a[Integer.parseInt(((ExpContext)_localctx).bO.bO.getValue())] PuntoComa 
    {
        writer.updateNoTerminals("EXP::= B {A.valor=B.result;} A ; {print(A.result);}", ((ExpContext)_localctx).aO.aO.getValue(), expO, ((ExpContext)_localctx).bO.bO);
        System.out.println(((ExpContext)_localctx).aO.aO.getValue());
        _localctx.expO=expO;   
        writer.writeXML();
    }
    ;
a [Integer her]  returns [A aO]
    :
    { 
        A aO=new A();
        Node node=writer.addPasoNoTerminalDes("A", "valor", "result", aO, haveBrother, her.toString(), nodeAnt);
    }
    Mas bO=b  aeO=a[Integer.parseInt(((AContext)_localctx).bO.bO.getValue())+her] 
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
b  returns [B bO]
    :
    {
        B bO=new B();
        Node node=writer.addPasoNoTerminalDes("B", null, "result", bO, haveBrother, null, nodeAnt);
    } 
    numO=Number cO=c[Integer.parseInt(((BContext)_localctx).numO.numberO.getValue())] 
    {
        writer.updateNoTerminals("B::= num {C.valor=num.vlex;} C {B.result=C.result;}", ((BContext)_localctx).cO.cO.getValue(), bO, ((BContext)_localctx).numO.numberO);    
        _localctx.bO=bO;
    }
    ;

c [Integer her]   returns [C cO]
    :
    {
        C cO=new C();
        Node node=writer.addPasoNoTerminalDes("C", "valor", "result", cO, haveBrother, her.toString(), nodeAnt);        
    }  
    Por numO=Number  ceO=c[her*Integer.parseInt(((CContext)_localctx).numO.numberO.getValue())] 
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