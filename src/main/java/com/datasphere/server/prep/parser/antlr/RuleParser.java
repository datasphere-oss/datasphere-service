// Generated from com/datasphere/datalayer/prep/parser/antlr/Rule.g4 by ANTLR 4.5.1
package com.datasphere.server.prep.parser.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class RuleParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, WS=7, RULE_NAME=8, ARG_NAME=9, 
		BOOLEAN=10, IDENTIFIER=11, NULL=12, LONG=13, DOUBLE=14, STRING=15, REGEX=16, 
		MINUS=17, NOT=18, POW=19, MUL=20, DIV=21, MODULO=22, PLUS=23, LT=24, LEQ=25, 
		GT=26, GEQ=27, EQ=28, NEQ=29, AND=30, OR=31, ASSIGN=32;
	public static final int
		RULE_ruleset = 0, RULE_args = 1, RULE_expr = 2, RULE_fn = 3, RULE_fnArgs = 4;
	public static final String[] ruleNames = {
		"ruleset", "args", "expr", "fn", "fnArgs"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "':'", "'&'", "'|'", "'('", "')'", "','", null, null, null, null, 
		null, "'null'", null, null, null, null, "'-'", "'!'", "'^'", "'*'", "'/'", 
		"'%'", "'+'", "'<'", "'<='", "'>'", "'>='", "'=='", "'!='", "'&&'", "'||'", 
		"'='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, "WS", "RULE_NAME", "ARG_NAME", 
		"BOOLEAN", "IDENTIFIER", "NULL", "LONG", "DOUBLE", "STRING", "REGEX", 
		"MINUS", "NOT", "POW", "MUL", "DIV", "MODULO", "PLUS", "LT", "LEQ", "GT", 
		"GEQ", "EQ", "NEQ", "AND", "OR", "ASSIGN"
	};
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
	public String getGrammarFileName() { return "Rule.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public RuleParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class RulesetContext extends ParserRuleContext {
		public TerminalNode RULE_NAME() { return getToken(RuleParser.RULE_NAME, 0); }
		public List<ArgsContext> args() {
			return getRuleContexts(ArgsContext.class);
		}
		public ArgsContext args(int i) {
			return getRuleContext(ArgsContext.class,i);
		}
		public RulesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleset; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitRuleset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RulesetContext ruleset() throws RecognitionException {
		RulesetContext _localctx = new RulesetContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_ruleset);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(10);
			match(RULE_NAME);
			setState(14);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ARG_NAME) {
				{
				{
				setState(11);
				args();
				}
				}
				setState(16);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class ArgsContext extends ParserRuleContext {
		public TerminalNode ARG_NAME() { return getToken(RuleParser.ARG_NAME, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_args; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgsContext args() throws RecognitionException {
		ArgsContext _localctx = new ArgsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_args);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(17);
			match(ARG_NAME);
			setState(18);
			match(T__0);
			setState(19);
			expr(0);
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

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FunctionArrayExprContext extends ExprContext {
		public List<FnContext> fn() {
			return getRuleContexts(FnContext.class);
		}
		public FnContext fn(int i) {
			return getRuleContext(FnContext.class,i);
		}
		public FunctionArrayExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitFunctionArrayExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DoubleExprContext extends ExprContext {
		public TerminalNode DOUBLE() { return getToken(RuleParser.DOUBLE, 0); }
		public DoubleExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitDoubleExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RegularExprContext extends ExprContext {
		public TerminalNode REGEX() { return getToken(RuleParser.REGEX, 0); }
		public RegularExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitRegularExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AddSubExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public AddSubExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitAddSubExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullExprContext extends ExprContext {
		public TerminalNode NULL() { return getToken(RuleParser.NULL, 0); }
		public NullExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitNullExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LongExprContext extends ExprContext {
		public TerminalNode LONG() { return getToken(RuleParser.LONG, 0); }
		public LongExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitLongExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LogicalAndOrExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public LogicalAndOrExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitLogicalAndOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanExprContext extends ExprContext {
		public TerminalNode BOOLEAN() { return getToken(RuleParser.BOOLEAN, 0); }
		public BooleanExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitBooleanExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NestedExprContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public NestedExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitNestedExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DoubleArrayExprContext extends ExprContext {
		public List<TerminalNode> DOUBLE() { return getTokens(RuleParser.DOUBLE); }
		public TerminalNode DOUBLE(int i) {
			return getToken(RuleParser.DOUBLE, i);
		}
		public DoubleArrayExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitDoubleArrayExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringArrayExprContext extends ExprContext {
		public List<TerminalNode> STRING() { return getTokens(RuleParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(RuleParser.STRING, i);
		}
		public StringArrayExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitStringArrayExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LongArrayExprContext extends ExprContext {
		public List<TerminalNode> LONG() { return getTokens(RuleParser.LONG); }
		public TerminalNode LONG(int i) {
			return getToken(RuleParser.LONG, i);
		}
		public LongArrayExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitLongArrayExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LogicalOpExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public LogicalOpExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitLogicalOpExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionExprContext extends ExprContext {
		public FnContext fn() {
			return getRuleContext(FnContext.class,0);
		}
		public FunctionExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitFunctionExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringExprContext extends ExprContext {
		public TerminalNode STRING() { return getToken(RuleParser.STRING, 0); }
		public StringExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitStringExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryOpExprContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public UnaryOpExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitUnaryOpExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdentifierArrayExprContext extends ExprContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(RuleParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(RuleParser.IDENTIFIER, i);
		}
		public IdentifierArrayExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitIdentifierArrayExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LogicalAndOrExpr2Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public LogicalAndOrExpr2Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitLogicalAndOrExpr2(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MulDivModuloExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public MulDivModuloExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitMulDivModuloExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PowOpExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public PowOpExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitPowOpExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AssignExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public AssignExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitAssignExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdentifierExprContext extends ExprContext {
		public TerminalNode IDENTIFIER() { return getToken(RuleParser.IDENTIFIER, 0); }
		public IdentifierExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitIdentifierExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				_localctx = new UnaryOpExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(22);
				_la = _input.LA(1);
				if ( !(_la==MINUS || _la==NOT) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(23);
				expr(22);
				}
				break;
			case 2:
				{
				_localctx = new NestedExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(24);
				match(T__3);
				setState(25);
				expr(0);
				setState(26);
				match(T__4);
				}
				break;
			case 3:
				{
				_localctx = new FunctionExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(28);
				fn();
				}
				break;
			case 4:
				{
				_localctx = new FunctionArrayExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(29);
				fn();
				setState(32); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(30);
						match(T__5);
						setState(31);
						fn();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(34); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 5:
				{
				_localctx = new IdentifierExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(36);
				match(IDENTIFIER);
				}
				break;
			case 6:
				{
				_localctx = new NullExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(37);
				match(NULL);
				}
				break;
			case 7:
				{
				_localctx = new DoubleExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(38);
				match(DOUBLE);
				}
				break;
			case 8:
				{
				_localctx = new LongExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(39);
				match(LONG);
				}
				break;
			case 9:
				{
				_localctx = new BooleanExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(40);
				match(BOOLEAN);
				}
				break;
			case 10:
				{
				_localctx = new StringExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(41);
				match(STRING);
				}
				break;
			case 11:
				{
				_localctx = new RegularExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(42);
				match(REGEX);
				}
				break;
			case 12:
				{
				_localctx = new IdentifierArrayExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(43);
				match(IDENTIFIER);
				setState(46); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(44);
						match(T__5);
						setState(45);
						match(IDENTIFIER);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(48); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 13:
				{
				_localctx = new StringArrayExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(50);
				match(STRING);
				setState(53); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(51);
						match(T__5);
						setState(52);
						match(STRING);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(55); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 14:
				{
				_localctx = new LongArrayExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(57);
				match(LONG);
				setState(60); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(58);
						match(T__5);
						setState(59);
						match(LONG);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(62); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 15:
				{
				_localctx = new DoubleArrayExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(64);
				match(DOUBLE);
				setState(67); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(65);
						match(T__5);
						setState(66);
						match(DOUBLE);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(69); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(96);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(94);
					switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
					case 1:
						{
						_localctx = new PowOpExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(73);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(74);
						match(POW);
						setState(75);
						expr(21);
						}
						break;
					case 2:
						{
						_localctx = new MulDivModuloExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(76);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(77);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUL) | (1L << DIV) | (1L << MODULO))) != 0)) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(78);
						expr(21);
						}
						break;
					case 3:
						{
						_localctx = new AddSubExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(79);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(80);
						_la = _input.LA(1);
						if ( !(_la==MINUS || _la==PLUS) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(81);
						expr(20);
						}
						break;
					case 4:
						{
						_localctx = new LogicalOpExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(82);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(83);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << LEQ) | (1L << GT) | (1L << GEQ) | (1L << EQ) | (1L << NEQ) | (1L << ASSIGN))) != 0)) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(84);
						expr(19);
						}
						break;
					case 5:
						{
						_localctx = new LogicalAndOrExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(85);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(86);
						_la = _input.LA(1);
						if ( !(_la==AND || _la==OR) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(87);
						expr(18);
						}
						break;
					case 6:
						{
						_localctx = new LogicalAndOrExpr2Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(88);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(89);
						_la = _input.LA(1);
						if ( !(_la==T__1 || _la==T__2) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(90);
						expr(17);
						}
						break;
					case 7:
						{
						_localctx = new AssignExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(91);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(92);
						match(ASSIGN);
						setState(93);
						expr(16);
						}
						break;
					}
					} 
				}
				setState(98);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class FnContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(RuleParser.IDENTIFIER, 0); }
		public FnArgsContext fnArgs() {
			return getRuleContext(FnArgsContext.class,0);
		}
		public FnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fn; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitFn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FnContext fn() throws RecognitionException {
		FnContext _localctx = new FnContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_fn);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			match(IDENTIFIER);
			setState(100);
			match(T__3);
			setState(102);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << BOOLEAN) | (1L << IDENTIFIER) | (1L << NULL) | (1L << LONG) | (1L << DOUBLE) | (1L << STRING) | (1L << REGEX) | (1L << MINUS) | (1L << NOT))) != 0)) {
				{
				setState(101);
				fnArgs();
				}
			}

			setState(104);
			match(T__4);
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

	public static class FnArgsContext extends ParserRuleContext {
		public FnArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fnArgs; }
	 
		public FnArgsContext() { }
		public void copyFrom(FnArgsContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FunctionArgsContext extends FnArgsContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public FunctionArgsContext(FnArgsContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleVisitor ) return ((RuleVisitor<? extends T>)visitor).visitFunctionArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FnArgsContext fnArgs() throws RecognitionException {
		FnArgsContext _localctx = new FnArgsContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_fnArgs);
		int _la;
		try {
			_localctx = new FunctionArgsContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			expr(0);
			setState(111);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(107);
				match(T__5);
				setState(108);
				expr(0);
				}
				}
				setState(113);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 21);
		case 1:
			return precpred(_ctx, 20);
		case 2:
			return precpred(_ctx, 19);
		case 3:
			return precpred(_ctx, 18);
		case 4:
			return precpred(_ctx, 17);
		case 5:
			return precpred(_ctx, 16);
		case 6:
			return precpred(_ctx, 15);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\"u\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\3\2\7\2\17\n\2\f\2\16\2\22\13\2\3\3\3\3"+
		"\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\6\4#\n\4\r\4\16\4"+
		"$\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\6\4\61\n\4\r\4\16\4\62\3\4\3"+
		"\4\3\4\6\48\n\4\r\4\16\49\3\4\3\4\3\4\6\4?\n\4\r\4\16\4@\3\4\3\4\3\4\6"+
		"\4F\n\4\r\4\16\4G\5\4J\n\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4a\n\4\f\4\16\4d\13\4\3\5\3"+
		"\5\3\5\5\5i\n\5\3\5\3\5\3\6\3\6\3\6\7\6p\n\6\f\6\16\6s\13\6\3\6\2\3\6"+
		"\7\2\4\6\b\n\2\b\3\2\23\24\3\2\26\30\4\2\23\23\31\31\4\2\32\37\"\"\3\2"+
		" !\3\2\4\5\u008c\2\f\3\2\2\2\4\23\3\2\2\2\6I\3\2\2\2\be\3\2\2\2\nl\3\2"+
		"\2\2\f\20\7\n\2\2\r\17\5\4\3\2\16\r\3\2\2\2\17\22\3\2\2\2\20\16\3\2\2"+
		"\2\20\21\3\2\2\2\21\3\3\2\2\2\22\20\3\2\2\2\23\24\7\13\2\2\24\25\7\3\2"+
		"\2\25\26\5\6\4\2\26\5\3\2\2\2\27\30\b\4\1\2\30\31\t\2\2\2\31J\5\6\4\30"+
		"\32\33\7\6\2\2\33\34\5\6\4\2\34\35\7\7\2\2\35J\3\2\2\2\36J\5\b\5\2\37"+
		"\"\5\b\5\2 !\7\b\2\2!#\5\b\5\2\" \3\2\2\2#$\3\2\2\2$\"\3\2\2\2$%\3\2\2"+
		"\2%J\3\2\2\2&J\7\r\2\2\'J\7\16\2\2(J\7\20\2\2)J\7\17\2\2*J\7\f\2\2+J\7"+
		"\21\2\2,J\7\22\2\2-\60\7\r\2\2./\7\b\2\2/\61\7\r\2\2\60.\3\2\2\2\61\62"+
		"\3\2\2\2\62\60\3\2\2\2\62\63\3\2\2\2\63J\3\2\2\2\64\67\7\21\2\2\65\66"+
		"\7\b\2\2\668\7\21\2\2\67\65\3\2\2\289\3\2\2\29\67\3\2\2\29:\3\2\2\2:J"+
		"\3\2\2\2;>\7\17\2\2<=\7\b\2\2=?\7\17\2\2><\3\2\2\2?@\3\2\2\2@>\3\2\2\2"+
		"@A\3\2\2\2AJ\3\2\2\2BE\7\20\2\2CD\7\b\2\2DF\7\20\2\2EC\3\2\2\2FG\3\2\2"+
		"\2GE\3\2\2\2GH\3\2\2\2HJ\3\2\2\2I\27\3\2\2\2I\32\3\2\2\2I\36\3\2\2\2I"+
		"\37\3\2\2\2I&\3\2\2\2I\'\3\2\2\2I(\3\2\2\2I)\3\2\2\2I*\3\2\2\2I+\3\2\2"+
		"\2I,\3\2\2\2I-\3\2\2\2I\64\3\2\2\2I;\3\2\2\2IB\3\2\2\2Jb\3\2\2\2KL\f\27"+
		"\2\2LM\7\25\2\2Ma\5\6\4\27NO\f\26\2\2OP\t\3\2\2Pa\5\6\4\27QR\f\25\2\2"+
		"RS\t\4\2\2Sa\5\6\4\26TU\f\24\2\2UV\t\5\2\2Va\5\6\4\25WX\f\23\2\2XY\t\6"+
		"\2\2Ya\5\6\4\24Z[\f\22\2\2[\\\t\7\2\2\\a\5\6\4\23]^\f\21\2\2^_\7\"\2\2"+
		"_a\5\6\4\22`K\3\2\2\2`N\3\2\2\2`Q\3\2\2\2`T\3\2\2\2`W\3\2\2\2`Z\3\2\2"+
		"\2`]\3\2\2\2ad\3\2\2\2b`\3\2\2\2bc\3\2\2\2c\7\3\2\2\2db\3\2\2\2ef\7\r"+
		"\2\2fh\7\6\2\2gi\5\n\6\2hg\3\2\2\2hi\3\2\2\2ij\3\2\2\2jk\7\7\2\2k\t\3"+
		"\2\2\2lq\5\6\4\2mn\7\b\2\2np\5\6\4\2om\3\2\2\2ps\3\2\2\2qo\3\2\2\2qr\3"+
		"\2\2\2r\13\3\2\2\2sq\3\2\2\2\r\20$\629@GI`bhq";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}