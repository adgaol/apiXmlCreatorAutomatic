// Generated from .//production/gramatica.g4 by ANTLR 4.7.2

    import vistdsapixmlcreator.Writer;
    import vistdsapixmlcreator.Node;
    import vistdsapixmlcreator.Paso;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class gramaticaLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Number=1, Por=2, Mas=3, PuntoComa=4;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"Number", "Por", "Mas", "PuntoComa"
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


	    Writer writer = new Writer("gramatica.g4","./descendent","cadena.txt",true);


	public gramaticaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "gramatica.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 0:
			Number_action((RuleContext)_localctx, actionIndex);
			break;
		case 1:
			Por_action((RuleContext)_localctx, actionIndex);
			break;
		case 2:
			Mas_action((RuleContext)_localctx, actionIndex);
			break;
		case 3:
			PuntoComa_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void Number_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:

			        
			break;
		}
	}
	private void Por_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:


			    
			break;
		}
	}
	private void Mas_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:


			    
			break;
		}
	}
	private void PuntoComa_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:


			    
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\6\33\b\1\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\3\2\6\2\r\n\2\r\2\16\2\16\3\2\3\2\3\3\3\3\3\3"+
		"\3\4\3\4\3\4\3\5\3\5\3\5\2\2\6\3\3\5\4\7\5\t\6\3\2\2\2\33\2\3\3\2\2\2"+
		"\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\3\f\3\2\2\2\5\22\3\2\2\2\7\25\3\2"+
		"\2\2\t\30\3\2\2\2\13\r\4\62;\2\f\13\3\2\2\2\r\16\3\2\2\2\16\f\3\2\2\2"+
		"\16\17\3\2\2\2\17\20\3\2\2\2\20\21\b\2\2\2\21\4\3\2\2\2\22\23\7,\2\2\23"+
		"\24\b\3\3\2\24\6\3\2\2\2\25\26\7-\2\2\26\27\b\4\4\2\27\b\3\2\2\2\30\31"+
		"\7=\2\2\31\32\b\5\5\2\32\n\3\2\2\2\4\2\16\6\3\2\2\3\3\3\3\4\4\3\5\5";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}