grammar gramaticabasica;
 

exp returns [Integer expO] 
    :
    {
        Node node=writer.addPasoNoTerminal("EXP", null, null, "null");
    } 

    bO=b aO=a[((ExpContext)_localctx).bO.bO] PuntoComa 
    {
        writer.updateNoTerminals("EXP::= B {A.valor=B.result;} A ; {print(A.result);}", ((ExpContext)_localctx).aO.aO, ((ExpContext)_localctx).bO.bO);
        System.out.println(((ExpContext)_localctx).aO.aO);
        _localctx.expO=((ExpContext)_localctx).aO.aO;   
        writer.writeXML();
    }
    ;
a [Integer her] returns [Integer aO]
    :
    { 
        Node node=writer.addPasoNoTerminal("A", "valor", "result", her);
    
    }
    Mas bO=b  aeO=a[((AContext)_localctx).bO.bO + her] 
    {    
        writer.updateNoTerminals("A::= + B {A1.valor=A.valor+B.result;} A1 {A.result=A1.result;}", ((AContext)_localctx).aeO.aO, ((AContext)_localctx).masO.masO);
        _localctx.aO=((AContext)_localctx).aeO.aO;   
    } 
    
    | 
    {
        writer.addPasoLambda("A", "valor", "result", her, "{A.result=A.valor;}");
        _localctx.aO=her;
    }
    ;
b returns [Integer bO]
    :
    {
        Node node=writer.addPasoNoTerminal("B", null, "result", "null");
    } 
    numO=Number cO=c[Integer.parseInt(((BContext)_localctx).numO.numberO]
    {
        writer.updateNoTerminals("B::= number {C.valor=num.vlex;} C {B.result=C.result;}", ((BContext)_localctx).cO.cO, ((BContext)_localctx).numO.numberO);
        _localctx.bO=((BContext)_localctx).cO.cO;
    }
    ;

c [Integer her] returns [Integer cO]
    :
    {
        Node node=writer.addPasoNoTerminal("C", "valor", "result", her);
    }  
    Por numO=Number  ceO=c[her * Integer.parseInt(((CContext)_localctx).numO.numberO]
    {
       writer.updateNoTerminals("C::= * number {C1.valor=C.valor*number.vlex;} C1 {C.result=C1.result;}", ((CContext)_localctx).ceO.cO, ((CContext)_localctx).porO.porO);
        _localctx.cO=((CContext)_localctx).ceO.cO;
    } 
    |
    {
        writer.addPasoLambda("C", "valor", "result", her, "{C.result=C.valor;}");
        _localctx.cO=her;
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