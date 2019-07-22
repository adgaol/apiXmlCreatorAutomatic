grammar gramatica;


exp  returns [Exp expO] 
    :
    {
        Exp expO=new Exp();
        Node node=writer.addPasoNoTerminalDes("EXP", expO, false, null, null, null, null);
    } 

    bO=b aO=a[Integer.parseInt(((ExpContext)_localctx).bO.bO.getValue())] PuntoComa 
    {
        writer.updateNoTerminals("EXP::= B A ;", ((ExpContext)_localctx).aO.aO.getValue(), expO, ((ExpContext)_localctx).bO.bO);
        System.out.println(((ExpContext)_localctx).aO.aO.getValue());
        _localctx.expO=expO;   
        writer.writeXML();
    }
    ;
a  returns [A aO]
    :
    { 
        A aO=new A();
        Node node=writer.addPasoNoTerminalDes("A", aO, haveBrother, her.toString(), nodeAnt, "valor", "result");
    }
    Mas bO=b  aeO=a[Integer.parseInt(((AContext)_localctx).bO.bO.getValue())+her] 
    {    
        writer.updateNoTerminals("A::= + B A1", ((AContext)_localctx).aeO.aO.getValue(), aO, ((AContext)_localctx).masO.m);
        _localctx.aO=aO;   
    } 
    
    | 
    {
        A aO=new A();     
        writer.addPasoLambdaDes("A", "valor", "result", her.toString(), aO, haveBrother, nodeAnt);
        _localctx.aO=aO;
    }
    ;
b  returns [B bO]
    :
    {
        B bO=new B();
        Node node=writer.addPasoNoTerminalDes("B", bO, haveBrother, null, nodeAnt, null, "result");
    } 
    numO=Number cO=c[Integer.parseInt(((BContext)_localctx).numO.numO.getValue())] 
    {
        writer.updateNoTerminals("B::= num C", ((BContext)_localctx).cO.cO.getValue(), bO, ((BContext)_localctx).numO.numO);    
        _localctx.bO=bO;
    }
    ;

c [Integer her,Boolean c1]   returns [C cO]
    :
    {
        C cO=new C();
        Node node=writer.addPasoNoTerminalDes("C", cO, haveBrother, her.toString(), nodeAnt, "valor", "result");        
    }  
    Por numO=Number  ceO=c[her*Integer.parseInt(((CContext)_localctx).numO.numO.getValue())] 
    {
        writer.updateNoTerminals("C::= * num C1", ((CContext)_localctx).ceO.cO.getValue(), cO, ((CContext)_localctx).porO.pr);
        _localctx.cO=cO;} 
    |
    {
        C cO=new C();    
        writer.addPasoLambdaDes("C", "valor", "result", her.toString(), cO, haveBrother, nodeAnt);
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