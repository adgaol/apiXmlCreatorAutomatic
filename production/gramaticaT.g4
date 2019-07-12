grammar gramatica;
@header {
    import vistdsapixmlcreator.Writer;
    import vistdsapixmlcreator.Node;
    import vistdsapixmlcreator.Paso;
}
@members {
    Writer writer = new Writer("./gramatica.txt","./descendent","cadenaDePrueba.txt",true);
}



exp  returns [Exp expO] 
    :{
        Exp expO=new Exp();
        Node node=writer.addPasoNoTerminalDes("EXP", expO, false, null, null, false, null, null);
    } 

    bO=b[node,true]  aO=a[((ExpContext)_localctx).bO.bO.getValue(),node,true,false] puntocoma[node,false] {
        ((ExpContext)_localctx).bO.bO.getPaso().setRegla("EXP::= B A ;");
        expO.setValue(((ExpContext)_localctx).aO.aO.getValue());
        System.out.println(((ExpContext)_localctx).aO.aO.getValue());
        _localctx.expO=expO;   
        writer.writeXML();
        }
    ;
a [Integer her,Node nodeAnt,Boolean haveBrother,Boolean a1] returns [A aO]
    :{ 
        A aO=new A();
        Node node=writer.addPasoNoTerminalDes("A", aO, haveBrother, her.toString(), nodeAnt, a1, "valor", "result");
    }
    masO=mas[node,true] bO=b[node,true]  aeO=a[((AContext)_localctx).bO.bO.getValue()+her,node,false,true] {
        ((AContext)_localctx).masO.m.getPaso().setRegla("A::= + B A1");
        aO.setValue(((AContext)_localctx).aeO.aO.getValue());

        updateSymbol(((AContext)_localctx).masO.m,"A::= + B A1",((AContext)_localctx).aeO.aO,aO);
        _localctx.aO=aO;
    } 
    | 
    {
        A aO=new A();
        aO.setValue(her);        
        writer.addPasoLambdaDes("A", aO, haveBrother, her.toString(), nodeAnt, a1, "valor", "result");
        _localctx.aO=aO;
        }
    ;
b [Node nodeAnt,Boolean haveBrother]  returns [B bO]
    :{
        B bO=new B();
        Node node=writer.addPasoNoTerminalDes("B", bO, haveBrother, null, nodeAnt, false, null, "result");
    } 
    numO=number[node,true] cO=c[((BContext)_localctx).numO.numO.getValue(),node,false,false] {
        ((BContext)_localctx).numO.numO.getPaso().setRegla("B::= num C");
        bO.setValue(((BContext)_localctx).cO.cO.getValue());
        _localctx.bO=bO;}
    ;

c [Integer her,Node nodeAnt,Boolean haveBrother,Boolean c1]   returns [C cO]
    :{
        C cO=new C();
        Node node=writer.addPasoNoTerminalDes("C", cO, haveBrother, her.toString(), nodeAnt, c1, "valor", "result");        
    }  
    porO=por[node,true] numO=number[node,true]  ceO=c[her*((CContext)_localctx).numO.numO.getValue(),node,false,true] {
        ((CContext)_localctx).porO.pr.getPaso().setRegla("C::= * num C1");      
        cO.setValue(((CContext)_localctx).ceO.cO.getValue());
        _localctx.cO=cO;} 
    | 
        {
        C cO=new C();
        cO.setValue(her);
        writer.addPasoLambdaDes("C", cO, haveBrother, her.toString(), nodeAnt, c1, "valor", "result");
        _localctx.cO=cO;}

    ;


number [Node nodeAnt,Boolean haveBrother]  returns [Number numberO]
    : Number {
        
        Number numberO=new Number();
        numberO.setValue(Integer.parseInt(this._ctx.getText()));
        writer.addPasoTerminalDes("number", "vlex", numberO, haveBrother, nodeAnt);
        
        _localctx.numberO=numberO;
    }
    ;
por [Node nodeAnt,Boolean haveBrother]  returns [Por porO]
    : Por {
        
        Por porO=new Por();
        porO.setValue(Integer.parseInt(this._ctx.getText()));
        writer.addPasoTerminalDes("por", "vlex", porO, haveBrother, nodeAnt);
        
        _localctx.porO=porO;
    }
    ;
mas [Node nodeAnt,Boolean haveBrother]  returns [Mas masO]
    : Mas {
        
        Mas masO=new Mas();
        masO.setValue(Integer.parseInt(this._ctx.getText()));
        writer.addPasoTerminalDes("mas", "vlex", masO, haveBrother, nodeAnt);
        
        _localctx.masO=masO;
    }
    ;
puntoComa [Node nodeAnt,Boolean haveBrother]  returns [PuntoComa puntoComaO]
    : PuntoComa {
        
        PuntoComa puntoComaO=new PuntoComa();
        puntoComaO.setValue(Integer.parseInt(this._ctx.getText()));
        writer.addPasoTerminalDes("puntoComa", "vlex", puntoComaO, haveBrother, nodeAnt);
        
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
