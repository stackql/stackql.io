// Generated from c:\Users\javen\OneDrive\GitRepositories\infraql\infraql-help\syntax_trees\SELECT.g4 by ANTLR 4.8
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SELECTParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, EXEC=15, AUTH=16, LOGIN=17, 
		INTERACTIVE=18, REVOKE=19, DESC=20, METHODS=21, DESCRIBE=22, EXPLAIN=23, 
		EXTENDED=24, LIMIT=25, ORDER=26, BY=27, GROUP=28, WITH=29, ROLLUP=30, 
		HAVING=31, IN=32, AS=33, USE=34, LIKE=35, FROM=36, SHOW=37, WHERE=38, 
		SERVICES=39, RESOURCES=40, PROVIDERS=41, FIELDS=42, SELECT=43, DISTINCT=44, 
		ADD=45, ARRAY=46, AND=47, ASC=48, AUTO_INCREMENT=49, BETWEEN=50, BINARY=51, 
		CASE=52, COLLATE=53, CONVERT=54, CREATE=55, CROSS=56, CUME_DIST=57, CURRENT_DATE=58, 
		CURRENT_TIME=59, CURRENT_TIMESTAMP=60, SUBSTR=61, SUBSTRING=62, DATABASE=63, 
		DATABASES=64, DEFAULT=65, DELETE=66, DENSE_RANK=67, DISTINCTROW=68, DIV=69, 
		DROP=70, ELSE=71, END=72, ESCAPE=73, EXISTS=74, FALSE=75, FIRST_VALUE=76, 
		FOR=77, FORCE=78, GROUPING=79, GROUPS=80, IF=81, IGNORE=82, INDEX=83, 
		INNER=84, INSERT=85, INTERVAL=86, INTO=87, IS=88, JOIN=89, JSON_TABLE=90, 
		KEY=91, LAG=92, LAST_VALUE=93, LATERAL=94, LEAD=95, LEFT=96, LOCALTIME=97, 
		LOCALTIMESTAMP=98, LOCK=99, MEMBER=100, MATCH=101, MAXVALUE=102, MOD=103, 
		NATURAL=104, NEXT=105, NOT=106, NTH_VALUE=107, NTILE=108, NULL=109, OF=110, 
		OFF=111, ON=112, OR=113, OUTER=114, OVER=115, PERCENT_RANK=116, RANK=117, 
		RECURSIVE=118, REGEXP=119, RENAME=120, REPLACE=121, RIGHT=122, ROW_NUMBER=123, 
		SCHEMA=124, SEPARATOR=125, SET=126, STRAIGHT_JOIN=127, SYSTEM=128, TABLE=129, 
		THEN=130, TIMESTAMPADD=131, TIMESTAMPDIFF=132, TO=133, TRUE=134, UNION=135, 
		UNIQUE=136, UNLOCK=137, UPDATE=138, USING=139, UTC_DATE=140, UTC_TIME=141, 
		UTC_TIMESTAMP=142, VALUES=143, WHEN=144, WINDOW=145, XOR=146;
	public static final int
		RULE_selectStatement = 0, RULE_provider = 1, RULE_fullFieldName = 2, RULE_functionCall = 3, 
		RULE_alias = 4, RULE_number = 5, RULE_groupByItem = 6, RULE_resourceOrSubQuery = 7, 
		RULE_multipartIdentifier = 8, RULE_pattern = 9, RULE_expression = 10, 
		RULE_providerMethodName = 11;
	private static String[] makeRuleNames() {
		return new String[] {
			"selectStatement", "provider", "fullFieldName", "functionCall", "alias", 
			"number", "groupByItem", "resourceOrSubQuery", "multipartIdentifier", 
			"pattern", "expression", "providerMethodName"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'*'", "','", "';'", "'provider'", "'fullFieldName'", "'functionCall'", 
			"'alias'", "'number'", "'groupByItem'", "'resourceOrSubQuery'", "'multipartIdentifier'", 
			"'pattern'", "'expression'", "'providerMethodName'", "'EXEC'", "'AUTH'", 
			"'LOGIN'", "'INTERACTIVE'", "'REVOKE'", "'DESC'", "'METHODS'", "'DESCRIBE'", 
			"'EXPLAIN'", "'EXTENDED'", "'LIMIT'", "'ORDER'", "'BY'", "'GROUP'", "'WITH'", 
			"'ROLLUP'", "'HAVING'", "'IN'", "'AS'", "'USE'", "'LIKE'", "'FROM'", 
			"'SHOW'", "'WHERE'", "'SERVICES'", "'RESOURCES'", "'PROVIDERS'", "'FIELDS'", 
			"'SELECT'", "'DISTINCT'", "'ADD'", "'ARRAY '", "'AND'", "'ASC'", "'AUTO_INCREMENT'", 
			"'BETWEEN'", "'BINARY'", "'CASE'", "'COLLATE'", "'CONVERT'", "'CREATE'", 
			"'CROSS'", "'CUME_DIST'", "'CURRENT_DATE'", "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", 
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
			null, null, null, "EXEC", "AUTH", "LOGIN", "INTERACTIVE", "REVOKE", "DESC", 
			"METHODS", "DESCRIBE", "EXPLAIN", "EXTENDED", "LIMIT", "ORDER", "BY", 
			"GROUP", "WITH", "ROLLUP", "HAVING", "IN", "AS", "USE", "LIKE", "FROM", 
			"SHOW", "WHERE", "SERVICES", "RESOURCES", "PROVIDERS", "FIELDS", "SELECT", 
			"DISTINCT", "ADD", "ARRAY", "AND", "ASC", "AUTO_INCREMENT", "BETWEEN", 
			"BINARY", "CASE", "COLLATE", "CONVERT", "CREATE", "CROSS", "CUME_DIST", 
			"CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "SUBSTR", "SUBSTRING", 
			"DATABASE", "DATABASES", "DEFAULT", "DELETE", "DENSE_RANK", "DISTINCTROW", 
			"DIV", "DROP", "ELSE", "END", "ESCAPE", "EXISTS", "FALSE", "FIRST_VALUE", 
			"FOR", "FORCE", "GROUPING", "GROUPS", "IF", "IGNORE", "INDEX", "INNER", 
			"INSERT", "INTERVAL", "INTO", "IS", "JOIN", "JSON_TABLE", "KEY", "LAG", 
			"LAST_VALUE", "LATERAL", "LEAD", "LEFT", "LOCALTIME", "LOCALTIMESTAMP", 
			"LOCK", "MEMBER", "MATCH", "MAXVALUE", "MOD", "NATURAL", "NEXT", "NOT", 
			"NTH_VALUE", "NTILE", "NULL", "OF", "OFF", "ON", "OR", "OUTER", "OVER", 
			"PERCENT_RANK", "RANK", "RECURSIVE", "REGEXP", "RENAME", "REPLACE", "RIGHT", 
			"ROW_NUMBER", "SCHEMA", "SEPARATOR", "SET", "STRAIGHT_JOIN", "SYSTEM", 
			"TABLE", "THEN", "TIMESTAMPADD", "TIMESTAMPDIFF", "TO", "TRUE", "UNION", 
			"UNIQUE", "UNLOCK", "UPDATE", "USING", "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", 
			"VALUES", "WHEN", "WINDOW", "XOR"
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
	public String getGrammarFileName() { return "SELECT.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SELECTParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class SelectStatementContext extends ParserRuleContext {
		public Token star;
		public ExpressionContext havingExpr;
		public TerminalNode SELECT() { return getToken(SELECTParser.SELECT, 0); }
		public TerminalNode DISTINCT() { return getToken(SELECTParser.DISTINCT, 0); }
		public List<FullFieldNameContext> fullFieldName() {
			return getRuleContexts(FullFieldNameContext.class);
		}
		public FullFieldNameContext fullFieldName(int i) {
			return getRuleContext(FullFieldNameContext.class,i);
		}
		public List<FunctionCallContext> functionCall() {
			return getRuleContexts(FunctionCallContext.class);
		}
		public FunctionCallContext functionCall(int i) {
			return getRuleContext(FunctionCallContext.class,i);
		}
		public TerminalNode FROM() { return getToken(SELECTParser.FROM, 0); }
		public List<ResourceOrSubQueryContext> resourceOrSubQuery() {
			return getRuleContexts(ResourceOrSubQueryContext.class);
		}
		public ResourceOrSubQueryContext resourceOrSubQuery(int i) {
			return getRuleContext(ResourceOrSubQueryContext.class,i);
		}
		public TerminalNode ORDER() { return getToken(SELECTParser.ORDER, 0); }
		public List<TerminalNode> BY() { return getTokens(SELECTParser.BY); }
		public TerminalNode BY(int i) {
			return getToken(SELECTParser.BY, i);
		}
		public TerminalNode LIMIT() { return getToken(SELECTParser.LIMIT, 0); }
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public TerminalNode ASC() { return getToken(SELECTParser.ASC, 0); }
		public TerminalNode DESC() { return getToken(SELECTParser.DESC, 0); }
		public List<AliasContext> alias() {
			return getRuleContexts(AliasContext.class);
		}
		public AliasContext alias(int i) {
			return getRuleContext(AliasContext.class,i);
		}
		public TerminalNode WHERE() { return getToken(SELECTParser.WHERE, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode GROUP() { return getToken(SELECTParser.GROUP, 0); }
		public List<GroupByItemContext> groupByItem() {
			return getRuleContexts(GroupByItemContext.class);
		}
		public GroupByItemContext groupByItem(int i) {
			return getRuleContext(GroupByItemContext.class,i);
		}
		public TerminalNode HAVING() { return getToken(SELECTParser.HAVING, 0); }
		public List<TerminalNode> AS() { return getTokens(SELECTParser.AS); }
		public TerminalNode AS(int i) {
			return getToken(SELECTParser.AS, i);
		}
		public TerminalNode WITH() { return getToken(SELECTParser.WITH, 0); }
		public TerminalNode ROLLUP() { return getToken(SELECTParser.ROLLUP, 0); }
		public SelectStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectStatement; }
	}

	public final SelectStatementContext selectStatement() throws RecognitionException {
		SelectStatementContext _localctx = new SelectStatementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_selectStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			match(SELECT);
			setState(26);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DISTINCT) {
				{
				setState(25);
				match(DISTINCT);
				}
			}

			setState(45);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__4) | (1L << T__5))) != 0)) {
				{
				setState(43);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__0:
					{
					setState(28);
					((SelectStatementContext)_localctx).star = match(T__0);
					}
					break;
				case T__4:
					{
					setState(29);
					fullFieldName();
					setState(34);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==T__6 || _la==AS) {
						{
						setState(31);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==AS) {
							{
							setState(30);
							match(AS);
							}
						}

						setState(33);
						alias();
						}
					}

					}
					break;
				case T__5:
					{
					setState(36);
					functionCall();
					setState(41);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==T__6 || _la==AS) {
						{
						setState(38);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==AS) {
							{
							setState(37);
							match(AS);
							}
						}

						setState(40);
						alias();
						}
					}

					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(47);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FROM) {
				{
				setState(48);
				match(FROM);
				setState(49);
				resourceOrSubQuery();
				setState(54);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(50);
					match(T__1);
					setState(51);
					resourceOrSubQuery();
					}
					}
					setState(56);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(59);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WHERE) {
					{
					setState(57);
					match(WHERE);
					setState(58);
					expression();
					}
				}

				setState(75);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GROUP) {
					{
					setState(61);
					match(GROUP);
					setState(62);
					match(BY);
					setState(63);
					groupByItem();
					setState(68);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__1) {
						{
						{
						setState(64);
						match(T__1);
						setState(65);
						groupByItem();
						}
						}
						setState(70);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(73);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WITH) {
						{
						setState(71);
						match(WITH);
						setState(72);
						match(ROLLUP);
						}
					}

					}
				}

				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==HAVING) {
					{
					setState(77);
					match(HAVING);
					setState(78);
					((SelectStatementContext)_localctx).havingExpr = expression();
					}
				}

				}
			}

			setState(93);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDER) {
				{
				setState(83);
				match(ORDER);
				setState(84);
				match(BY);
				setState(85);
				fullFieldName();
				setState(90);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(86);
					match(T__1);
					setState(87);
					fullFieldName();
					}
					}
					setState(92);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(96);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DESC || _la==ASC) {
				{
				setState(95);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==ASC) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(100);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LIMIT) {
				{
				setState(98);
				match(LIMIT);
				setState(99);
				number();
				}
			}

			setState(102);
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
			setState(104);
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
			setState(106);
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
			setState(108);
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
			setState(110);
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
			setState(112);
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
			setState(114);
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
			setState(116);
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
			setState(118);
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
			setState(120);
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
			setState(122);
			match(T__12);
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
			setState(124);
			match(T__13);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0094\u0081\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\3\2\3\2\5\2\35\n\2\3\2\3\2\3\2\5\2\"\n\2\3\2\5\2"+
		"%\n\2\3\2\3\2\5\2)\n\2\3\2\5\2,\n\2\7\2.\n\2\f\2\16\2\61\13\2\3\2\3\2"+
		"\3\2\3\2\7\2\67\n\2\f\2\16\2:\13\2\3\2\3\2\5\2>\n\2\3\2\3\2\3\2\3\2\3"+
		"\2\7\2E\n\2\f\2\16\2H\13\2\3\2\3\2\5\2L\n\2\5\2N\n\2\3\2\3\2\5\2R\n\2"+
		"\5\2T\n\2\3\2\3\2\3\2\3\2\3\2\7\2[\n\2\f\2\16\2^\13\2\5\2`\n\2\3\2\5\2"+
		"c\n\2\3\2\3\2\5\2g\n\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7"+
		"\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\2\2\16\2\4\6\b"+
		"\n\f\16\20\22\24\26\30\2\3\4\2\26\26\62\62\2\u0087\2\32\3\2\2\2\4j\3\2"+
		"\2\2\6l\3\2\2\2\bn\3\2\2\2\np\3\2\2\2\fr\3\2\2\2\16t\3\2\2\2\20v\3\2\2"+
		"\2\22x\3\2\2\2\24z\3\2\2\2\26|\3\2\2\2\30~\3\2\2\2\32\34\7-\2\2\33\35"+
		"\7.\2\2\34\33\3\2\2\2\34\35\3\2\2\2\35/\3\2\2\2\36.\7\3\2\2\37$\5\6\4"+
		"\2 \"\7#\2\2! \3\2\2\2!\"\3\2\2\2\"#\3\2\2\2#%\5\n\6\2$!\3\2\2\2$%\3\2"+
		"\2\2%.\3\2\2\2&+\5\b\5\2\')\7#\2\2(\'\3\2\2\2()\3\2\2\2)*\3\2\2\2*,\5"+
		"\n\6\2+(\3\2\2\2+,\3\2\2\2,.\3\2\2\2-\36\3\2\2\2-\37\3\2\2\2-&\3\2\2\2"+
		".\61\3\2\2\2/-\3\2\2\2/\60\3\2\2\2\60S\3\2\2\2\61/\3\2\2\2\62\63\7&\2"+
		"\2\638\5\20\t\2\64\65\7\4\2\2\65\67\5\20\t\2\66\64\3\2\2\2\67:\3\2\2\2"+
		"8\66\3\2\2\289\3\2\2\29=\3\2\2\2:8\3\2\2\2;<\7(\2\2<>\5\26\f\2=;\3\2\2"+
		"\2=>\3\2\2\2>M\3\2\2\2?@\7\36\2\2@A\7\35\2\2AF\5\16\b\2BC\7\4\2\2CE\5"+
		"\16\b\2DB\3\2\2\2EH\3\2\2\2FD\3\2\2\2FG\3\2\2\2GK\3\2\2\2HF\3\2\2\2IJ"+
		"\7\37\2\2JL\7 \2\2KI\3\2\2\2KL\3\2\2\2LN\3\2\2\2M?\3\2\2\2MN\3\2\2\2N"+
		"Q\3\2\2\2OP\7!\2\2PR\5\26\f\2QO\3\2\2\2QR\3\2\2\2RT\3\2\2\2S\62\3\2\2"+
		"\2ST\3\2\2\2T_\3\2\2\2UV\7\34\2\2VW\7\35\2\2W\\\5\6\4\2XY\7\4\2\2Y[\5"+
		"\6\4\2ZX\3\2\2\2[^\3\2\2\2\\Z\3\2\2\2\\]\3\2\2\2]`\3\2\2\2^\\\3\2\2\2"+
		"_U\3\2\2\2_`\3\2\2\2`b\3\2\2\2ac\t\2\2\2ba\3\2\2\2bc\3\2\2\2cf\3\2\2\2"+
		"de\7\33\2\2eg\5\f\7\2fd\3\2\2\2fg\3\2\2\2gh\3\2\2\2hi\7\5\2\2i\3\3\2\2"+
		"\2jk\7\6\2\2k\5\3\2\2\2lm\7\7\2\2m\7\3\2\2\2no\7\b\2\2o\t\3\2\2\2pq\7"+
		"\t\2\2q\13\3\2\2\2rs\7\n\2\2s\r\3\2\2\2tu\7\13\2\2u\17\3\2\2\2vw\7\f\2"+
		"\2w\21\3\2\2\2xy\7\r\2\2y\23\3\2\2\2z{\7\16\2\2{\25\3\2\2\2|}\7\17\2\2"+
		"}\27\3\2\2\2~\177\7\20\2\2\177\31\3\2\2\2\24\34!$(+-/8=FKMQS\\_bf";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}