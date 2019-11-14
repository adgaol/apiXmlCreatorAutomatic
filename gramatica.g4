grammar gramatica;
 

exp returns [Exp expO] 
    :
    {
        Exp expO=new Exp();
        Node node=writer.addPasoNoTerminal("EXP", null, null, expO, "null");
    } 

    bO=b aO=a[Integer.parseInt(((ExpContext)_localctx).bO.bO.getValue())] PuntoComa 
    {
        writer.updateNoTerminals("EXP::= B {A.valor=B.result;} A ; {print(A.result);}", ((ExpContext)_localctx).aO.aO.getValue(), expO, ((ExpContext)_localctx).bO.bO);
        System.out.println(((ExpContext)_localctx).aO.aO.getValue());
        _localctx.expO=expO;   
        writer.writeXML();
    }
    ;
a [Integer her] returns [A aO]
    :
    { 
        A aO=new A();
        Node node=writer.addPasoNoTerminal("A", "valor", "result", aO, her);
    
    }
    Mas bO=b  aeO=a[Integer.parseInt(((AContext)_localctx).bO.bO.getValue()) + her] 
    {    
        writer.updateNoTerminals("A::= + B {A1.valor=A.valor+B.result;} A {A.result=A1.result;}", ((AContext)_localctx).aeO.aO.getValue(), aO, ((AContext)_localctx).masO.masO);
        _localctx.aO=aO;   
    } 
    
    | 
    {
        A aO=new A();     
        writer.addPasoLambda("A", "valor", "result", her, "{A.result=A.valor;}", aO);
        _localctx.aO=aO;
    }
    ;
b returns [B bO]
    :
    {
        B bO=new B();
        Node node=writer.addPasoNoTerminal("B", null, "result", bO, "null");
    } 
    numO=Number cO=c[Integer.parseInt(((BContext)_localctx).numO.numberO.getValue())] 
    {
        writer.updateNoTerminals("B::= number {C.valor=num.vlex;} C {B.result=C.result;}", ((BContext)_localctx).cO.cO.getValue(), bO, ((BContext)_localctx).numO.numberO);    
        _localctx.bO=bO;
    }
    ;

c [Integer her] returns [C cO]
    :
    {
        C cO=new C();
        Node node=writer.addPasoNoTerminal("C", "valor", "result", cO, her);
    }  
    Por numO=Number  ceO=c[her*Integer.parseInt(((CContext)_localctx).numO.numberO.getValue())] 
    {
        writer.updateNoTerminals("C::= * number {C1.valor=C.valor*number.vlex;} C {C.result=C1.result;}", ((CContext)_localctx).ceO.cO.getValue(), cO, ((CContext)_localctx).porO.porO);
        _localctx.cO=cO;
    } 
    |
    {
        C cO=new C();    
        writer.addPasoLambda("C", "valor", "result", her, "{C.result=C.valor;}", cO);
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