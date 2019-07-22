grammar gramaticaSin;


exp  returns [Integer expO] 
    :{
        
        Node node=writer.addPasoNoTerminalDes("EXP", expO, false, null, null, false, null, null);
        
        
    } 

    b[node,true]  aO=a[((ExpContext)_localctx).bO.bO,node,true,false] PuntoComa {
        writer.updateNoTerminals("EXP::= B A ;", ((ExpContext)_localctx).aO.aO, expO, ((ExpContext)_localctx).bO.bO);
        System.out.println(((ExpContext)_localctx).aO.aO);
        _localctx.expO=((ExpContext)_localctx).aO.aO;   
        writer.writeXML();
    }
    ;
a [Integer her,Node nodeAnt,Boolean haveBrother,Boolean a1] returns [Integer aO]
    :{ 
    
        Node node=writer.addPasoNoTerminalDes("A", aO, haveBrother, her.toString(), nodeAnt, a1, "valor", "result");
        
    }
    Mas bO=b[node,true]  aeO=a[((AContext)_localctx).bO.bO + her,node,false,true] {
        
        writer.updateNoTerminals("A::= + B A1", ((AContext)_localctx).aeO.aO, aO, ((AContext)_localctx).masO.m);

        
        _localctx.aO=((AContext)_localctx).aeO.aO;
        
    } 
    
    | 
    {
        
        writer.addPasoLambdaDes("A", "valor", "result", her.toString(), aO, haveBrother, nodeAnt, a1);
        _localctx.aO=her;
        
        }
    ;
b [Node nodeAnt,Boolean haveBrother]  returns [Integer bO]
    :{
        
        Node node=writer.addPasoNoTerminalDes("B", bO, haveBrother, null, nodeAnt, false, null, "result");
      
    } 
    numO=Number cO=c[ Integer.parseInt(((BContext)_localctx).numO.getText()),node,false,false] {
        writer.updateNoTerminals("B::= num C", ((BContext)_localctx).cO.cO, bO, ((BContext)_localctx).numO.numO);
        
        _localctx.bO=((BContext)_localctx).cO.cO;}
    ;

c [Integer her,Node nodeAnt,Boolean haveBrother,Boolean c1]   returns [Integer cO]
    :{
       
        Node node=writer.addPasoNoTerminalDes("C", cO, haveBrother, her.toString(), nodeAnt, c1, "valor", "result");        
    }  
    Por numO=Number ceO=c[her*Integer.parseInt(((CContext)_localctx).numO.getText()),node,false,true] {
        writer.updateNoTerminals("C::= * num C1", ((CContext)_localctx).ceO.cO, cO, ((CContext)_localctx).porO.pr);

        _localctx.cO=((CContext)_localctx).ceO.cO;} 
    |{
        
        
        writer.addPasoLambdaDes("C", "valor", "result", her.toString(), cO, haveBrother, nodeAnt, c1);
        _localctx.cO=her;}

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