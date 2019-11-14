// Generated from ./ANTLR/production/gramatica.g4 by ANTLR 4.7.2

    import vistdsapixmlcreator.Writer;
    import vistdsapixmlcreator.Node;
    import vistdsapixmlcreator.Paso;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class gramaticaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Number=1, Por=2, Mas=3, PuntoComa=4;
	public static final int
		RULE_exp = 0, RULE_a = 1, RULE_b = 2, RULE_c = 3, RULE_number = 4, RULE_por = 5, 
		RULE_mas = 6, RULE_puntoComa = 7;
	private static String[] makeRuleNames() {
		return new String[] {
			"exp", "a", "b", "c", "number", "por", "mas", "puntoComa"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'*'", "'+'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "Number", "Por", "Mas", "PuntoComa"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "gramatica.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


	    Writer writer = new Writer("gramatica.g4","./gramaticaXML",EjemploANTLR.getChain(),true);

	public gramaticaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ExpContext extends ParserRuleContext {
		public Exp expO;
		public BContext bO;
		public AContext aO;
		public PuntoComaContext puntoComaO;
		public BContext b() {
			return getRuleContext(BContext.class,0);
		}
		public AContext a() {
			return getRuleContext(AContext.class,0);
		}
		public PuntoComaContext puntoComa() {
			return getRuleContext(PuntoComaContext.class,0);
		}
		public ExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).enterExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).exitExp(this);
		}
	}

	public final ExpContext exp() throws RecognitionException {
		ExpContext _localctx = new ExpContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_exp);
		try {
			enterOuterAlt(_localctx, 1);
			{

			        Exp expO=new Exp();
			        Node node=writer.addPasoNoTerminal("EXP",  null,  null,  expO,  "null", null, false);
			    
			setState(17);
			((ExpContext)_localctx).bO = b(node, true);
			setState(18);
			((ExpContext)_localctx).aO = a(node, true, Integer.parseInt(((ExpContext)_localctx).bO.bO.getValue()));
			setState(19);
			((ExpContext)_localctx).puntoComaO = puntoComa(node, false);

			        writer.updateNoTerminals("EXP::= B {A.valor=B.result;} A ; {print(A.result);}", ((ExpContext)_localctx).aO.aO.getValue(), expO, ((ExpContext)_localctx).bO.bO);
			        System.out.println(((ExpContext)_localctx).aO.aO.getValue());
			        _localctx.expO=expO;   
			        writer.writeXML();
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AContext extends ParserRuleContext {
		public Node nodeAnt;
		public Boolean haveBrother;
		public Integer her;
		public A aO;
		public MasContext masO;
		public BContext bO;
		public AContext aeO;
		public MasContext mas() {
			return getRuleContext(MasContext.class,0);
		}
		public BContext b() {
			return getRuleContext(BContext.class,0);
		}
		public AContext a() {
			return getRuleContext(AContext.class,0);
		}
		public AContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public AContext(ParserRuleContext parent, int invokingState, Node nodeAnt, Boolean haveBrother, Integer her) {
			super(parent, invokingState);
			this.nodeAnt = nodeAnt;
			this.haveBrother = haveBrother;
			this.her = her;
		}
		@Override public int getRuleIndex() { return RULE_a; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).enterA(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).exitA(this);
		}
	}

	public final AContext a(Node nodeAnt,Boolean haveBrother,Integer her) throws RecognitionException {
		AContext _localctx = new AContext(_ctx, getState(), nodeAnt, haveBrother, her);
		enterRule(_localctx, 2, RULE_a);
		try {
			setState(29);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Mas:
				enterOuterAlt(_localctx, 1);
				{
				 
				        A aO=new A();
				        Node node=writer.addPasoNoTerminal("A",  "valor",  "result",  aO,  her, nodeAnt, haveBrother);
				    
				setState(23);
				((AContext)_localctx).masO = mas(node, true);
				setState(24);
				((AContext)_localctx).bO = b(node, true);
				setState(25);
				((AContext)_localctx).aeO = a(node, false, Integer.parseInt(((AContext)_localctx).bO.bO.getValue())+her);
				    
				        writer.updateNoTerminals("A::= + B {A1.valor=A.valor+B.result;} A {A.result=A1.result;}", ((AContext)_localctx).aeO.aO.getValue(), aO, ((AContext)_localctx).masO.masO);
				        _localctx.aO=aO;   
				    
				}
				break;
			case PuntoComa:
				enterOuterAlt(_localctx, 2);
				{

				        A aO=new A();     
				        writer.addPasoLambda("A",  "valor",  "result",  her,  "{A.result=A.valor;}",  aO, nodeAnt, haveBrother);
				        _localctx.aO=aO;
				    
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BContext extends ParserRuleContext {
		public Node nodeAnt;
		public Boolean haveBrother;
		public B bO;
		public NumberContext numO;
		public CContext cO;
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public CContext c() {
			return getRuleContext(CContext.class,0);
		}
		public BContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public BContext(ParserRuleContext parent, int invokingState, Node nodeAnt, Boolean haveBrother) {
			super(parent, invokingState);
			this.nodeAnt = nodeAnt;
			this.haveBrother = haveBrother;
		}
		@Override public int getRuleIndex() { return RULE_b; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).enterB(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).exitB(this);
		}
	}

	public final BContext b(Node nodeAnt,Boolean haveBrother) throws RecognitionException {
		BContext _localctx = new BContext(_ctx, getState(), nodeAnt, haveBrother);
		enterRule(_localctx, 4, RULE_b);
		try {
			enterOuterAlt(_localctx, 1);
			{

			        B bO=new B();
			        Node node=writer.addPasoNoTerminal("B",  null,  "result",  bO,  "null", nodeAnt, haveBrother);
			    
			setState(32);
			((BContext)_localctx).numO = number(node, true);
			setState(33);
			((BContext)_localctx).cO = c(node, false, Integer.parseInt(((BContext)_localctx).numO.numberO.getValue()));

			        writer.updateNoTerminals("B::= number {C.valor=num.vlex;} C {B.result=C.result;}", ((BContext)_localctx).cO.cO.getValue(), bO, ((BContext)_localctx).numO.numberO);    
			        _localctx.bO=bO;
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CContext extends ParserRuleContext {
		public Node nodeAnt;
		public Boolean haveBrother;
		public Integer her;
		public C cO;
		public PorContext porO;
		public NumberContext numO;
		public CContext ceO;
		public PorContext por() {
			return getRuleContext(PorContext.class,0);
		}
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public CContext c() {
			return getRuleContext(CContext.class,0);
		}
		public CContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public CContext(ParserRuleContext parent, int invokingState, Node nodeAnt, Boolean haveBrother, Integer her) {
			super(parent, invokingState);
			this.nodeAnt = nodeAnt;
			this.haveBrother = haveBrother;
			this.her = her;
		}
		@Override public int getRuleIndex() { return RULE_c; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).enterC(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).exitC(this);
		}
	}

	public final CContext c(Node nodeAnt,Boolean haveBrother,Integer her) throws RecognitionException {
		CContext _localctx = new CContext(_ctx, getState(), nodeAnt, haveBrother, her);
		enterRule(_localctx, 6, RULE_c);
		try {
			setState(43);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Por:
				enterOuterAlt(_localctx, 1);
				{

				        C cO=new C();
				        Node node=writer.addPasoNoTerminal("C",  "valor",  "result",  cO,  her, nodeAnt, haveBrother);
				    
				setState(37);
				((CContext)_localctx).porO = por(node, true);
				setState(38);
				((CContext)_localctx).numO = number(node, true);
				setState(39);
				((CContext)_localctx).ceO = c(node, false, her*Integer.parseInt(((CContext)_localctx).numO.numberO.getValue()));

				        writer.updateNoTerminals("C::= * number {C1.valor=C.valor*number.vlex;} C {C.result=C1.result;}", ((CContext)_localctx).ceO.cO.getValue(), cO, ((CContext)_localctx).porO.porO);
				        _localctx.cO=cO;
				}
				break;
			case Mas:
			case PuntoComa:
				enterOuterAlt(_localctx, 2);
				{

				        C cO=new C();    
				        writer.addPasoLambda("C",  "valor",  "result",  her,  "{C.result=C.valor;}",  cO, nodeAnt, haveBrother);
				        _localctx.cO=cO;
				    
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumberContext extends ParserRuleContext {
		public Node nodeAnt;
		public Boolean haveBrother;
		public Number numberO;
		public TerminalNode Number() { return getToken(gramaticaParser.Number, 0); }
		public NumberContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public NumberContext(ParserRuleContext parent, int invokingState, Node nodeAnt, Boolean haveBrother) {
			super(parent, invokingState);
			this.nodeAnt = nodeAnt;
			this.haveBrother = haveBrother;
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).exitNumber(this);
		}
	}

	public final NumberContext number(Node nodeAnt,Boolean haveBrother) throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState(), nodeAnt, haveBrother);
		enterRule(_localctx, 8, RULE_number);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			match(Number);

			        
			        Number numberO=new Number();
			        writer.addPasoTerminal("number", "vlex", Integer.parseInt(this._ctx.getText()), numberO, haveBrother, nodeAnt);
			        
			        _localctx.numberO=numberO;
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PorContext extends ParserRuleContext {
		public Node nodeAnt;
		public Boolean haveBrother;
		public Por porO;
		public TerminalNode Por() { return getToken(gramaticaParser.Por, 0); }
		public PorContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public PorContext(ParserRuleContext parent, int invokingState, Node nodeAnt, Boolean haveBrother) {
			super(parent, invokingState);
			this.nodeAnt = nodeAnt;
			this.haveBrother = haveBrother;
		}
		@Override public int getRuleIndex() { return RULE_por; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).enterPor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).exitPor(this);
		}
	}

	public final PorContext por(Node nodeAnt,Boolean haveBrother) throws RecognitionException {
		PorContext _localctx = new PorContext(_ctx, getState(), nodeAnt, haveBrother);
		enterRule(_localctx, 10, RULE_por);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			match(Por);

			        
			        Por porO=new Por();
			        writer.addPasoTerminal(this._ctx.getText(), null, porO, haveBrother, nodeAnt);
			        
			        _localctx.porO=porO;
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MasContext extends ParserRuleContext {
		public Node nodeAnt;
		public Boolean haveBrother;
		public Mas masO;
		public TerminalNode Mas() { return getToken(gramaticaParser.Mas, 0); }
		public MasContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public MasContext(ParserRuleContext parent, int invokingState, Node nodeAnt, Boolean haveBrother) {
			super(parent, invokingState);
			this.nodeAnt = nodeAnt;
			this.haveBrother = haveBrother;
		}
		@Override public int getRuleIndex() { return RULE_mas; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).enterMas(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).exitMas(this);
		}
	}

	public final MasContext mas(Node nodeAnt,Boolean haveBrother) throws RecognitionException {
		MasContext _localctx = new MasContext(_ctx, getState(), nodeAnt, haveBrother);
		enterRule(_localctx, 12, RULE_mas);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(51);
			match(Mas);

			        
			        Mas masO=new Mas();
			        writer.addPasoTerminal(this._ctx.getText(), null, masO, haveBrother, nodeAnt);
			        
			        _localctx.masO=masO;
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PuntoComaContext extends ParserRuleContext {
		public Node nodeAnt;
		public Boolean haveBrother;
		public PuntoComa puntoComaO;
		public TerminalNode PuntoComa() { return getToken(gramaticaParser.PuntoComa, 0); }
		public PuntoComaContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public PuntoComaContext(ParserRuleContext parent, int invokingState, Node nodeAnt, Boolean haveBrother) {
			super(parent, invokingState);
			this.nodeAnt = nodeAnt;
			this.haveBrother = haveBrother;
		}
		@Override public int getRuleIndex() { return RULE_puntoComa; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).enterPuntoComa(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof gramaticaListener ) ((gramaticaListener)listener).exitPuntoComa(this);
		}
	}

	public final PuntoComaContext puntoComa(Node nodeAnt,Boolean haveBrother) throws RecognitionException {
		PuntoComaContext _localctx = new PuntoComaContext(_ctx, getState(), nodeAnt, haveBrother);
		enterRule(_localctx, 14, RULE_puntoComa);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			match(PuntoComa);

			        
			        PuntoComa puntoComaO=new PuntoComa();
			        writer.addPasoTerminal(this._ctx.getText(), null, puntoComaO, haveBrother, nodeAnt);
			        
			        _localctx.puntoComaO=puntoComaO;
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\6<\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3 \n\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\5\5.\n\5\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3\t"+
		"\3\t\3\t\3\t\2\2\n\2\4\6\b\n\f\16\20\2\2\2\65\2\22\3\2\2\2\4\37\3\2\2"+
		"\2\6!\3\2\2\2\b-\3\2\2\2\n/\3\2\2\2\f\62\3\2\2\2\16\65\3\2\2\2\208\3\2"+
		"\2\2\22\23\b\2\1\2\23\24\5\6\4\2\24\25\5\4\3\2\25\26\5\20\t\2\26\27\b"+
		"\2\1\2\27\3\3\2\2\2\30\31\b\3\1\2\31\32\5\16\b\2\32\33\5\6\4\2\33\34\5"+
		"\4\3\2\34\35\b\3\1\2\35 \3\2\2\2\36 \b\3\1\2\37\30\3\2\2\2\37\36\3\2\2"+
		"\2 \5\3\2\2\2!\"\b\4\1\2\"#\5\n\6\2#$\5\b\5\2$%\b\4\1\2%\7\3\2\2\2&\'"+
		"\b\5\1\2\'(\5\f\7\2()\5\n\6\2)*\5\b\5\2*+\b\5\1\2+.\3\2\2\2,.\b\5\1\2"+
		"-&\3\2\2\2-,\3\2\2\2.\t\3\2\2\2/\60\7\3\2\2\60\61\b\6\1\2\61\13\3\2\2"+
		"\2\62\63\7\4\2\2\63\64\b\7\1\2\64\r\3\2\2\2\65\66\7\5\2\2\66\67\b\b\1"+
		"\2\67\17\3\2\2\289\7\6\2\29:\b\t\1\2:\21\3\2\2\2\4\37-";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}