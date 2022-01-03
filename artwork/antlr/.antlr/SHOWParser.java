// Generated from c:\Users\javen\OneDrive\GitRepositories\infraql\infraql-help\syntax_trees\SHOW.g4 by ANTLR 4.8
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SHOWParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, EXEC=13, AUTH=14, LOGIN=15, INTERACTIVE=16, 
		REVOKE=17, DESC=18, DESCRIBE=19, EXPLAIN=20, EXTENDED=21, LIMIT=22, ORDER=23, 
		BY=24, GROUP=25, WITH=26, ROLLUP=27, HAVING=28, IN=29, AS=30, USE=31, 
		LIKE=32, FROM=33, SHOW=34, WHERE=35, SERVICES=36, RESOURCES=37, PROVIDERS=38, 
		FIELDS=39, SELECT=40, DISTINCT=41, ADD=42, ARRAY=43, AND=44, ASC=45, AUTO_INCREMENT=46, 
		BETWEEN=47, BINARY=48, CASE=49, COLLATE=50, CONVERT=51, CREATE=52, CROSS=53, 
		CUME_DIST=54, CURRENT_DATE=55, CURRENT_TIME=56, CURRENT_TIMESTAMP=57, 
		SUBSTR=58, SUBSTRING=59, DATABASE=60, DATABASES=61, DEFAULT=62, DELETE=63, 
		DENSE_RANK=64, DISTINCTROW=65, DIV=66, DROP=67, ELSE=68, END=69, ESCAPE=70, 
		EXISTS=71, FALSE=72, FIRST_VALUE=73, FOR=74, FORCE=75, GROUPING=76, GROUPS=77, 
		IF=78, IGNORE=79, INDEX=80, INNER=81, INSERT=82, INTERVAL=83, INTO=84, 
		IS=85, JOIN=86, JSON_TABLE=87, KEY=88, LAG=89, LAST_VALUE=90, LATERAL=91, 
		LEAD=92, LEFT=93, LOCALTIME=94, LOCALTIMESTAMP=95, LOCK=96, MEMBER=97, 
		MATCH=98, MAXVALUE=99, MOD=100, NATURAL=101, NEXT=102, NOT=103, NTH_VALUE=104, 
		NTILE=105, NULL=106, OF=107, OFF=108, ON=109, OR=110, OUTER=111, OVER=112, 
		PERCENT_RANK=113, RANK=114, RECURSIVE=115, REGEXP=116, RENAME=117, REPLACE=118, 
		RIGHT=119, ROW_NUMBER=120, SCHEMA=121, SEPARATOR=122, SET=123, STRAIGHT_JOIN=124, 
		SYSTEM=125, TABLE=126, THEN=127, TIMESTAMPADD=128, TIMESTAMPDIFF=129, 
		TO=130, TRUE=131, UNION=132, UNIQUE=133, UNLOCK=134, UPDATE=135, USING=136, 
		UTC_DATE=137, UTC_TIME=138, UTC_TIMESTAMP=139, VALUES=140, WHEN=141, WINDOW=142, 
		XOR=143, METHODS=144;
	public static final int
		RULE_showStatement = 0, RULE_provider = 1, RULE_fullFieldName = 2, RULE_functionCall = 3, 
		RULE_alias = 4, RULE_number = 5, RULE_groupByItem = 6, RULE_resourceOrSubQuery = 7, 
		RULE_multipartIdentifier = 8, RULE_pattern = 9, RULE_expression = 10, 
		RULE_providerMethodName = 11;
	private static String[] makeRuleNames() {
		return new String[] {
			"showStatement", "provider", "fullFieldName", "functionCall", "alias", 
			"number", "groupByItem", "resourceOrSubQuery", "multipartIdentifier", 
			"pattern", "expression", "providerMethodName"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'provider'", "'fullFieldName'", "'functionCall'", "'alias'", 
			"'number'", "'groupByItem'", "'resourceOrSubQuery'", "'multipartIdentifier'", 
			"'pattern'", "'expression'", "'providerMethodName'", "'EXEC'", "'AUTH'", 
			"'LOGIN'", "'INTERACTIVE'", "'REVOKE'", "'DESC'", "'DESCRIBE'", "'EXPLAIN'", 
			"'EXTENDED'", "'LIMIT'", "'ORDER'", "'BY'", "'GROUP'", "'WITH'", "'ROLLUP'", 
			"'HAVING'", "'IN'", "'AS'", "'USE'", "'LIKE'", "'FROM'", "'SHOW'", "'WHERE'", 
			"'SERVICES'", "'RESOURCES'", "'PROVIDERS'", "'FIELDS'", "'SELECT'", "'DISTINCT'", 
			"'ADD'", "'ARRAY '", "'AND'", "'ASC'", "'AUTO_INCREMENT'", "'BETWEEN'", 
			"'BINARY'", "'CASE'", "'COLLATE'", "'CONVERT'", "'CREATE'", "'CROSS'", 
			"'CUME_DIST'", "'CURRENT_DATE'", "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", 
			"'SUBSTR'", "'SUBSTRING'", "'DATABASE'", "'DATABASES'", "'DEFAULT'", 
			"'DELETE'", "'DENSE_RANK'", "'DISTINCTROW'", "'DIV'", "'DROP'", "'ELSE'", 
			"'END'", "'ESCAPE'", "'EXISTS'", "'FALSE'", "'FIRST_VALUE'", "'FOR'", 
			"'FORCE'", "'GROUPING'", "'GROUPS'", "'IF'", "'IGNORE'", "'INDEX'", "'INNER'", 
			"'INSERT'", "'INTERVAL'", "'INTO'", "'IS'", "'JOIN'", "'JSON_TABLE'", 
			"'KEY'", "'LAG'", "'LAST_VALUE'", "'LATERAL'", "'LEAD'", "'LEFT'", "'LOCALTIME'", 
			"'LOCALTIMESTAMP'", "'LOCK'", "'MEMBER'", "'MATCH'", "'MAXVALUE'", "'MOD'", 
			"'NATURAL'", "'NEXT'", "'NOT'", "'NTH_VALUE'", "'NTILE'", "'NULL'", "'OF'", 
			"'OFF'", "'ON'", "'OR'", "'OUTER'", "'OVER'", "'PERCENT_RANK'", "'RANK'", 
			"'RECURSIVE'", "'REGEXP'", "'RENAME'", "'REPLACE'", "'RIGHT'", "'ROW_NUMBER'", 
			"'SCHEMA'", "'SEPARATOR'", "'SET'", "'STRAIGHT_JOIN'", "'SYSTEM'", "'TABLE'", 
			"'THEN'", "'TIMESTAMPADD'", "'TIMESTAMPDIFF'", "'TO'", "'TRUE'", "'UNION'", 
			"'UNIQUE'", "'UNLOCK'", "'UPDATE'", "'USING'", "'UTC_DATE'", "'UTC_TIME'", 
			"'UTC_TIMESTAMP'", "'VALUES'", "'WHEN'", "'WINDOW'", "'XOR'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, "EXEC", "AUTH", "LOGIN", "INTERACTIVE", "REVOKE", "DESC", "DESCRIBE", 
			"EXPLAIN", "EXTENDED", "LIMIT", "ORDER", "BY", "GROUP", "WITH", "ROLLUP", 
			"HAVING", "IN", "AS", "USE", "LIKE", "FROM", "SHOW", "WHERE", "SERVICES", 
			"RESOURCES", "PROVIDERS", "FIELDS", "SELECT", "DISTINCT", "ADD", "ARRAY", 
			"AND", "ASC", "AUTO_INCREMENT", "BETWEEN", "BINARY", "CASE", "COLLATE", 
			"CONVERT", "CREATE", "CROSS", "CUME_DIST", "CURRENT_DATE", "CURRENT_TIME", 
			"CURRENT_TIMESTAMP", "SUBSTR", "SUBSTRING", "DATABASE", "DATABASES", 
			"DEFAULT", "DELETE", "DENSE_RANK", "DISTINCTROW", "DIV", "DROP", "ELSE", 
			"END", "ESCAPE", "EXISTS", "FALSE", "FIRST_VALUE", "FOR", "FORCE", "GROUPING", 
			"GROUPS", "IF", "IGNORE", "INDEX", "INNER", "INSERT", "INTERVAL", "INTO", 
			"IS", "JOIN", "JSON_TABLE", "KEY", "LAG", "LAST_VALUE", "LATERAL", "LEAD", 
			"LEFT", "LOCALTIME", "LOCALTIMESTAMP", "LOCK", "MEMBER", "MATCH", "MAXVALUE", 
			"MOD", "NATURAL", "NEXT", "NOT", "NTH_VALUE", "NTILE", "NULL", "OF", 
			"OFF", "ON", "OR", "OUTER", "OVER", "PERCENT_RANK", "RANK", "RECURSIVE", 
			"REGEXP", "RENAME", "REPLACE", "RIGHT", "ROW_NUMBER", "SCHEMA", "SEPARATOR", 
			"SET", "STRAIGHT_JOIN", "SYSTEM", "TABLE", "THEN", "TIMESTAMPADD", "TIMESTAMPDIFF", 
			"TO", "TRUE", "UNION", "UNIQUE", "UNLOCK", "UPDATE", "USING", "UTC_DATE", 
			"UTC_TIME", "UTC_TIMESTAMP", "VALUES", "WHEN", "WINDOW", "XOR", "METHODS"
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
	public String getGrammarFileName() { return "SHOW.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SHOWParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ShowStatementContext extends ParserRuleContext {
		public TerminalNode SHOW() { return getToken(SHOWParser.SHOW, 0); }
		public TerminalNode PROVIDERS() { return getToken(SHOWParser.PROVIDERS, 0); }
		public TerminalNode AUTH() { return getToken(SHOWParser.AUTH, 0); }
		public TerminalNode SERVICES() { return getToken(SHOWParser.SERVICES, 0); }
		public TerminalNode RESOURCES() { return getToken(SHOWParser.RESOURCES, 0); }
		public TerminalNode METHODS() { return getToken(SHOWParser.METHODS, 0); }
		public TerminalNode EXTENDED() { return getToken(SHOWParser.EXTENDED, 0); }
		public MultipartIdentifierContext multipartIdentifier() {
			return getRuleContext(MultipartIdentifierContext.class,0);
		}
		public TerminalNode FROM() { return getToken(SHOWParser.FROM, 0); }
		public TerminalNode IN() { return getToken(SHOWParser.IN, 0); }
		public TerminalNode LIKE() { return getToken(SHOWParser.LIKE, 0); }
		public PatternContext pattern() {
			return getRuleContext(PatternContext.class,0);
		}
		public TerminalNode WHERE() { return getToken(SHOWParser.WHERE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ShowStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_showStatement; }
	}

	public final ShowStatementContext showStatement() throws RecognitionException {
		ShowStatementContext _localctx = new ShowStatementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_showStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			match(SHOW);
			setState(41);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EXTENDED:
			case SERVICES:
			case RESOURCES:
			case METHODS:
				{
				setState(26);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(25);
					match(EXTENDED);
					}
				}

				setState(28);
				_la = _input.LA(1);
				if ( !(_la==SERVICES || _la==RESOURCES || _la==METHODS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(37);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IN || _la==FROM) {
					{
					setState(29);
					_la = _input.LA(1);
					if ( !(_la==IN || _la==FROM) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(30);
					multipartIdentifier();
					setState(35);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LIKE:
						{
						setState(31);
						match(LIKE);
						setState(32);
						pattern();
						}
						break;
					case WHERE:
						{
						setState(33);
						match(WHERE);
						setState(34);
						expression();
						}
						break;
					case T__0:
						break;
					default:
						break;
					}
					}
				}

				}
				break;
			case PROVIDERS:
				{
				setState(39);
				match(PROVIDERS);
				}
				break;
			case AUTH:
				{
				setState(40);
				match(AUTH);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(43);
			match(T__0);
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

	public static class ProviderContext extends ParserRuleContext {
		public ProviderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_provider; }
	}

	public final ProviderContext provider() throws RecognitionException {
		ProviderContext _localctx = new ProviderContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_provider);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			match(T__1);
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

	public static class FullFieldNameContext extends ParserRuleContext {
		public FullFieldNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fullFieldName; }
	}

	public final FullFieldNameContext fullFieldName() throws RecognitionException {
		FullFieldNameContext _localctx = new FullFieldNameContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_fullFieldName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			match(T__2);
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

	public static class FunctionCallContext extends ParserRuleContext {
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			match(T__3);
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

	public static class AliasContext extends ParserRuleContext {
		public AliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alias; }
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(51);
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

	public static class NumberContext extends ParserRuleContext {
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_number);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(53);
			match(T__5);
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

	public static class GroupByItemContext extends ParserRuleContext {
		public GroupByItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupByItem; }
	}

	public final GroupByItemContext groupByItem() throws RecognitionException {
		GroupByItemContext _localctx = new GroupByItemContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_groupByItem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			match(T__6);
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

	public static class ResourceOrSubQueryContext extends ParserRuleContext {
		public ResourceOrSubQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resourceOrSubQuery; }
	}

	public final ResourceOrSubQueryContext resourceOrSubQuery() throws RecognitionException {
		ResourceOrSubQueryContext _localctx = new ResourceOrSubQueryContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_resourceOrSubQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			match(T__7);
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

	public static class MultipartIdentifierContext extends ParserRuleContext {
		public MultipartIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multipartIdentifier; }
	}

	public final MultipartIdentifierContext multipartIdentifier() throws RecognitionException {
		MultipartIdentifierContext _localctx = new MultipartIdentifierContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_multipartIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			match(T__8);
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

	public static class PatternContext extends ParserRuleContext {
		public PatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pattern; }
	}

	public final PatternContext pattern() throws RecognitionException {
		PatternContext _localctx = new PatternContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_pattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			match(T__9);
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

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			match(T__10);
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

	public static class ProviderMethodNameContext extends ParserRuleContext {
		public ProviderMethodNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_providerMethodName; }
	}

	public final ProviderMethodNameContext providerMethodName() throws RecognitionException {
		ProviderMethodNameContext _localctx = new ProviderMethodNameContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_providerMethodName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			match(T__11);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0092F\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\3\2\3\2\5\2\35\n\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2"+
		"&\n\2\5\2(\n\2\3\2\3\2\5\2,\n\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3"+
		"\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\2\2\16"+
		"\2\4\6\b\n\f\16\20\22\24\26\30\2\4\4\2&\'\u0092\u0092\4\2\37\37##\2?\2"+
		"\32\3\2\2\2\4/\3\2\2\2\6\61\3\2\2\2\b\63\3\2\2\2\n\65\3\2\2\2\f\67\3\2"+
		"\2\2\169\3\2\2\2\20;\3\2\2\2\22=\3\2\2\2\24?\3\2\2\2\26A\3\2\2\2\30C\3"+
		"\2\2\2\32+\7$\2\2\33\35\7\27\2\2\34\33\3\2\2\2\34\35\3\2\2\2\35\36\3\2"+
		"\2\2\36\'\t\2\2\2\37 \t\3\2\2 %\5\22\n\2!\"\7\"\2\2\"&\5\24\13\2#$\7%"+
		"\2\2$&\5\26\f\2%!\3\2\2\2%#\3\2\2\2%&\3\2\2\2&(\3\2\2\2\'\37\3\2\2\2\'"+
		"(\3\2\2\2(,\3\2\2\2),\7(\2\2*,\7\20\2\2+\34\3\2\2\2+)\3\2\2\2+*\3\2\2"+
		"\2,-\3\2\2\2-.\7\3\2\2.\3\3\2\2\2/\60\7\4\2\2\60\5\3\2\2\2\61\62\7\5\2"+
		"\2\62\7\3\2\2\2\63\64\7\6\2\2\64\t\3\2\2\2\65\66\7\7\2\2\66\13\3\2\2\2"+
		"\678\7\b\2\28\r\3\2\2\29:\7\t\2\2:\17\3\2\2\2;<\7\n\2\2<\21\3\2\2\2=>"+
		"\7\13\2\2>\23\3\2\2\2?@\7\f\2\2@\25\3\2\2\2AB\7\r\2\2B\27\3\2\2\2CD\7"+
		"\16\2\2D\31\3\2\2\2\6\34%\'+";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}