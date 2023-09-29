// Generated from c:\LocalGitRepos\stackql\stackql-artwork\antlr\SELECT.g4 by ANTLR 4.8
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
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, EXEC=23, AUTH=24, LIST=25, 
		PULL=26, LOGIN=27, INTERACTIVE=28, REVOKE=29, DESC=30, METHODS=31, DESCRIBE=32, 
		EXPLAIN=33, EXTENDED=34, LIMIT=35, ORDER=36, BY=37, GROUP=38, WITH=39, 
		ROLLUP=40, HAVING=41, IN=42, AS=43, USE=44, LIKE=45, FROM=46, SHOW=47, 
		WHERE=48, SERVICES=49, RESOURCES=50, PROVIDERS=51, FIELDS=52, SELECT=53, 
		DISTINCT=54, ADD=55, ARRAY=56, AND=57, ASC=58, AUTO_INCREMENT=59, BETWEEN=60, 
		BINARY=61, CASE=62, COLLATE=63, CONVERT=64, CREATE=65, CROSS=66, CUME_DIST=67, 
		CURRENT_DATE=68, CURRENT_TIME=69, CURRENT_TIMESTAMP=70, SUBSTR=71, SUBSTRING=72, 
		DATABASE=73, DATABASES=74, DEFAULT=75, DELETE=76, DENSE_RANK=77, DISTINCTROW=78, 
		DIV=79, DROP=80, ELSE=81, END=82, ESCAPE=83, EXISTS=84, FALSE=85, FIRST_VALUE=86, 
		FOR=87, FORCE=88, GROUPING=89, GROUPS=90, IF=91, IGNORE=92, INDEX=93, 
		INNER=94, INSERT=95, INTERVAL=96, INTO=97, IS=98, JOIN=99, JSON_TABLE=100, 
		KEY=101, LAG=102, LAST_VALUE=103, LATERAL=104, LEAD=105, LEFT=106, LOCALTIME=107, 
		LOCALTIMESTAMP=108, LOCK=109, MEMBER=110, MATCH=111, MAXVALUE=112, MOD=113, 
		NATURAL=114, NEXT=115, NOT=116, NTH_VALUE=117, NTILE=118, NULL=119, OF=120, 
		OFF=121, ON=122, OR=123, OUTER=124, OVER=125, PERCENT_RANK=126, RANK=127, 
		RECURSIVE=128, REGEXP=129, RENAME=130, REPLACE=131, REGISTRY=132, RIGHT=133, 
		ROW_NUMBER=134, SCHEMA=135, SEPARATOR=136, SET=137, STRAIGHT_JOIN=138, 
		SYSTEM=139, TABLE=140, THEN=141, TIMESTAMPADD=142, TIMESTAMPDIFF=143, 
		TO=144, TRUE=145, UNION=146, UNIQUE=147, UNLOCK=148, UPDATE=149, USING=150, 
		UTC_DATE=151, UTC_TIME=152, UTC_TIMESTAMP=153, VALUES=154, VIEW=155, WHEN=156, 
		WINDOW=157, XOR=158;
	public static final int
		RULE_selectStatement = 0, RULE_provider = 1, RULE_version = 2, RULE_fullFieldName = 3, 
		RULE_functionCall = 4, RULE_alias = 5, RULE_number = 6, RULE_groupByItem = 7, 
		RULE_resourceOrSubQuery = 8, RULE_resource = 9, RULE_multipartIdentifier = 10, 
		RULE_pattern = 11, RULE_expression = 12, RULE_providerMethodName = 13, 
		RULE_parameterName = 14, RULE_parameterValue = 15, RULE_parameterExpression = 16, 
		RULE_queryHint = 17, RULE_fieldList = 18, RULE_awaitQueryHint = 19;
	private static String[] makeRuleNames() {
		return new String[] {
			"selectStatement", "provider", "version", "fullFieldName", "functionCall", 
			"alias", "number", "groupByItem", "resourceOrSubQuery", "resource", "multipartIdentifier", 
			"pattern", "expression", "providerMethodName", "parameterName", "parameterValue", 
			"parameterExpression", "queryHint", "fieldList", "awaitQueryHint"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'*'", "','", "';'", "'provider'", "'version'", "'fullFieldName'", 
			"'functionCall'", "'alias'", "'number'", "'groupByItem'", "'resourceOrSubQuery'", 
			"'resource'", "'multipartIdentifier'", "'pattern'", "'expression'", "'providerMethodName'", 
			"'parameterName'", "'parameterValue'", "'parameterExpression'", "'queryHint'", 
			"'fieldList'", "'awaitQueryHint'", "'EXEC'", "'AUTH'", "'LIST'", "'PULL'", 
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
			"'RECURSIVE'", "'REGEXP'", "'RENAME'", "'REPLACE'", "'REGISTRY'", "'RIGHT'", 
			"'ROW_NUMBER'", "'SCHEMA'", "'SEPARATOR'", "'SET'", "'STRAIGHT_JOIN'", 
			"'SYSTEM'", "'TABLE'", "'THEN'", "'TIMESTAMPADD'", "'TIMESTAMPDIFF'", 
			"'TO'", "'TRUE'", "'UNION'", "'UNIQUE'", "'UNLOCK'", "'UPDATE'", "'USING'", 
			"'UTC_DATE'", "'UTC_TIME'", "'UTC_TIMESTAMP'", "'VALUES'", "'VIEW'", 
			"'WHEN'", "'WINDOW'", "'XOR'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, "EXEC", 
			"AUTH", "LIST", "PULL", "LOGIN", "INTERACTIVE", "REVOKE", "DESC", "METHODS", 
			"DESCRIBE", "EXPLAIN", "EXTENDED", "LIMIT", "ORDER", "BY", "GROUP", "WITH", 
			"ROLLUP", "HAVING", "IN", "AS", "USE", "LIKE", "FROM", "SHOW", "WHERE", 
			"SERVICES", "RESOURCES", "PROVIDERS", "FIELDS", "SELECT", "DISTINCT", 
			"ADD", "ARRAY", "AND", "ASC", "AUTO_INCREMENT", "BETWEEN", "BINARY", 
			"CASE", "COLLATE", "CONVERT", "CREATE", "CROSS", "CUME_DIST", "CURRENT_DATE", 
			"CURRENT_TIME", "CURRENT_TIMESTAMP", "SUBSTR", "SUBSTRING", "DATABASE", 
			"DATABASES", "DEFAULT", "DELETE", "DENSE_RANK", "DISTINCTROW", "DIV", 
			"DROP", "ELSE", "END", "ESCAPE", "EXISTS", "FALSE", "FIRST_VALUE", "FOR", 
			"FORCE", "GROUPING", "GROUPS", "IF", "IGNORE", "INDEX", "INNER", "INSERT", 
			"INTERVAL", "INTO", "IS", "JOIN", "JSON_TABLE", "KEY", "LAG", "LAST_VALUE", 
			"LATERAL", "LEAD", "LEFT", "LOCALTIME", "LOCALTIMESTAMP", "LOCK", "MEMBER", 
			"MATCH", "MAXVALUE", "MOD", "NATURAL", "NEXT", "NOT", "NTH_VALUE", "NTILE", 
			"NULL", "OF", "OFF", "ON", "OR", "OUTER", "OVER", "PERCENT_RANK", "RANK", 
			"RECURSIVE", "REGEXP", "RENAME", "REPLACE", "REGISTRY", "RIGHT", "ROW_NUMBER", 
			"SCHEMA", "SEPARATOR", "SET", "STRAIGHT_JOIN", "SYSTEM", "TABLE", "THEN", 
			"TIMESTAMPADD", "TIMESTAMPDIFF", "TO", "TRUE", "UNION", "UNIQUE", "UNLOCK", 
			"UPDATE", "USING", "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", "VALUES", 
			"VIEW", "WHEN", "WINDOW", "XOR"
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
		public List<ResourceContext> resource() {
			return getRuleContexts(ResourceContext.class);
		}
		public ResourceContext resource(int i) {
			return getRuleContext(ResourceContext.class,i);
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
		public TerminalNode JOIN() { return getToken(SELECTParser.JOIN, 0); }
		public TerminalNode ON() { return getToken(SELECTParser.ON, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode WHERE() { return getToken(SELECTParser.WHERE, 0); }
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
		public TerminalNode OUTER() { return getToken(SELECTParser.OUTER, 0); }
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
			setState(40);
			match(SELECT);
			setState(42);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DISTINCT) {
				{
				setState(41);
				match(DISTINCT);
				}
			}

			setState(61);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__5) | (1L << T__6))) != 0)) {
				{
				setState(59);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__0:
					{
					setState(44);
					((SelectStatementContext)_localctx).star = match(T__0);
					}
					break;
				case T__5:
					{
					setState(45);
					fullFieldName();
					setState(50);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==T__7 || _la==AS) {
						{
						setState(47);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==AS) {
							{
							setState(46);
							match(AS);
							}
						}

						setState(49);
						alias();
						}
					}

					}
					break;
				case T__6:
					{
					setState(52);
					functionCall();
					setState(57);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==T__7 || _la==AS) {
						{
						setState(54);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==AS) {
							{
							setState(53);
							match(AS);
							}
						}

						setState(56);
						alias();
						}
					}

					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(63);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(100);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FROM) {
				{
				setState(64);
				match(FROM);
				setState(65);
				resource();
				setState(74);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==JOIN || _la==OUTER) {
					{
					setState(67);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==OUTER) {
						{
						setState(66);
						match(OUTER);
						}
					}

					setState(69);
					match(JOIN);
					setState(70);
					resource();
					setState(71);
					match(ON);
					setState(72);
					expression();
					}
				}

				setState(78);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WHERE) {
					{
					setState(76);
					match(WHERE);
					setState(77);
					expression();
					}
				}

				setState(94);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GROUP) {
					{
					setState(80);
					match(GROUP);
					setState(81);
					match(BY);
					setState(82);
					groupByItem();
					setState(87);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__1) {
						{
						{
						setState(83);
						match(T__1);
						setState(84);
						groupByItem();
						}
						}
						setState(89);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(92);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WITH) {
						{
						setState(90);
						match(WITH);
						setState(91);
						match(ROLLUP);
						}
					}

					}
				}

				setState(98);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==HAVING) {
					{
					setState(96);
					match(HAVING);
					setState(97);
					((SelectStatementContext)_localctx).havingExpr = expression();
					}
				}

				}
			}

			setState(112);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDER) {
				{
				setState(102);
				match(ORDER);
				setState(103);
				match(BY);
				setState(104);
				fullFieldName();
				setState(109);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(105);
					match(T__1);
					setState(106);
					fullFieldName();
					}
					}
					setState(111);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(115);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DESC || _la==ASC) {
				{
				setState(114);
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

			setState(119);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LIMIT) {
				{
				setState(117);
				match(LIMIT);
				setState(118);
				number();
				}
			}

			setState(121);
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
			setState(123);
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

	public static class VersionContext extends ParserRuleContext {
		public VersionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_version; }
	}

	public final VersionContext version() throws RecognitionException {
		VersionContext _localctx = new VersionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_version);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(125);
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

	public static class FullFieldNameContext extends ParserRuleContext {
		public FullFieldNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fullFieldName; }
	}

	public final FullFieldNameContext fullFieldName() throws RecognitionException {
		FullFieldNameContext _localctx = new FullFieldNameContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_fullFieldName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
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

	public static class FunctionCallContext extends ParserRuleContext {
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
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

	public static class AliasContext extends ParserRuleContext {
		public AliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alias; }
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(131);
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

	public static class NumberContext extends ParserRuleContext {
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_number);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
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

	public static class GroupByItemContext extends ParserRuleContext {
		public GroupByItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupByItem; }
	}

	public final GroupByItemContext groupByItem() throws RecognitionException {
		GroupByItemContext _localctx = new GroupByItemContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_groupByItem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135);
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

	public static class ResourceOrSubQueryContext extends ParserRuleContext {
		public ResourceOrSubQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resourceOrSubQuery; }
	}

	public final ResourceOrSubQueryContext resourceOrSubQuery() throws RecognitionException {
		ResourceOrSubQueryContext _localctx = new ResourceOrSubQueryContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_resourceOrSubQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(137);
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

	public static class ResourceContext extends ParserRuleContext {
		public ResourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resource; }
	}

	public final ResourceContext resource() throws RecognitionException {
		ResourceContext _localctx = new ResourceContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_resource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
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

	public static class MultipartIdentifierContext extends ParserRuleContext {
		public MultipartIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multipartIdentifier; }
	}

	public final MultipartIdentifierContext multipartIdentifier() throws RecognitionException {
		MultipartIdentifierContext _localctx = new MultipartIdentifierContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_multipartIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
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

	public static class PatternContext extends ParserRuleContext {
		public PatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pattern; }
	}

	public final PatternContext pattern() throws RecognitionException {
		PatternContext _localctx = new PatternContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_pattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
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

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			match(T__14);
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
		enterRule(_localctx, 26, RULE_providerMethodName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			match(T__15);
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

	public static class ParameterNameContext extends ParserRuleContext {
		public ParameterNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterName; }
	}

	public final ParameterNameContext parameterName() throws RecognitionException {
		ParameterNameContext _localctx = new ParameterNameContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_parameterName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			match(T__16);
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

	public static class ParameterValueContext extends ParserRuleContext {
		public ParameterValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterValue; }
	}

	public final ParameterValueContext parameterValue() throws RecognitionException {
		ParameterValueContext _localctx = new ParameterValueContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_parameterValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			match(T__17);
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

	public static class ParameterExpressionContext extends ParserRuleContext {
		public ParameterExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterExpression; }
	}

	public final ParameterExpressionContext parameterExpression() throws RecognitionException {
		ParameterExpressionContext _localctx = new ParameterExpressionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_parameterExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(153);
			match(T__18);
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

	public static class QueryHintContext extends ParserRuleContext {
		public QueryHintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryHint; }
	}

	public final QueryHintContext queryHint() throws RecognitionException {
		QueryHintContext _localctx = new QueryHintContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_queryHint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			match(T__19);
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

	public static class FieldListContext extends ParserRuleContext {
		public FieldListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldList; }
	}

	public final FieldListContext fieldList() throws RecognitionException {
		FieldListContext _localctx = new FieldListContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_fieldList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			match(T__20);
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

	public static class AwaitQueryHintContext extends ParserRuleContext {
		public AwaitQueryHintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_awaitQueryHint; }
	}

	public final AwaitQueryHintContext awaitQueryHint() throws RecognitionException {
		AwaitQueryHintContext _localctx = new AwaitQueryHintContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_awaitQueryHint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(159);
			match(T__21);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u00a0\u00a4\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\5\2-\n\2\3\2\3\2\3\2\5\2\62\n\2"+
		"\3\2\5\2\65\n\2\3\2\3\2\5\29\n\2\3\2\5\2<\n\2\7\2>\n\2\f\2\16\2A\13\2"+
		"\3\2\3\2\3\2\5\2F\n\2\3\2\3\2\3\2\3\2\3\2\5\2M\n\2\3\2\3\2\5\2Q\n\2\3"+
		"\2\3\2\3\2\3\2\3\2\7\2X\n\2\f\2\16\2[\13\2\3\2\3\2\5\2_\n\2\5\2a\n\2\3"+
		"\2\3\2\5\2e\n\2\5\2g\n\2\3\2\3\2\3\2\3\2\3\2\7\2n\n\2\f\2\16\2q\13\2\5"+
		"\2s\n\2\3\2\5\2v\n\2\3\2\3\2\5\2z\n\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5"+
		"\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16"+
		"\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25"+
		"\3\25\3\25\2\2\26\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(\2\3\4\2"+
		"  <<\2\u00a3\2*\3\2\2\2\4}\3\2\2\2\6\177\3\2\2\2\b\u0081\3\2\2\2\n\u0083"+
		"\3\2\2\2\f\u0085\3\2\2\2\16\u0087\3\2\2\2\20\u0089\3\2\2\2\22\u008b\3"+
		"\2\2\2\24\u008d\3\2\2\2\26\u008f\3\2\2\2\30\u0091\3\2\2\2\32\u0093\3\2"+
		"\2\2\34\u0095\3\2\2\2\36\u0097\3\2\2\2 \u0099\3\2\2\2\"\u009b\3\2\2\2"+
		"$\u009d\3\2\2\2&\u009f\3\2\2\2(\u00a1\3\2\2\2*,\7\67\2\2+-\78\2\2,+\3"+
		"\2\2\2,-\3\2\2\2-?\3\2\2\2.>\7\3\2\2/\64\5\b\5\2\60\62\7-\2\2\61\60\3"+
		"\2\2\2\61\62\3\2\2\2\62\63\3\2\2\2\63\65\5\f\7\2\64\61\3\2\2\2\64\65\3"+
		"\2\2\2\65>\3\2\2\2\66;\5\n\6\2\679\7-\2\28\67\3\2\2\289\3\2\2\29:\3\2"+
		"\2\2:<\5\f\7\2;8\3\2\2\2;<\3\2\2\2<>\3\2\2\2=.\3\2\2\2=/\3\2\2\2=\66\3"+
		"\2\2\2>A\3\2\2\2?=\3\2\2\2?@\3\2\2\2@f\3\2\2\2A?\3\2\2\2BC\7\60\2\2CL"+
		"\5\24\13\2DF\7~\2\2ED\3\2\2\2EF\3\2\2\2FG\3\2\2\2GH\7e\2\2HI\5\24\13\2"+
		"IJ\7|\2\2JK\5\32\16\2KM\3\2\2\2LE\3\2\2\2LM\3\2\2\2MP\3\2\2\2NO\7\62\2"+
		"\2OQ\5\32\16\2PN\3\2\2\2PQ\3\2\2\2Q`\3\2\2\2RS\7(\2\2ST\7\'\2\2TY\5\20"+
		"\t\2UV\7\4\2\2VX\5\20\t\2WU\3\2\2\2X[\3\2\2\2YW\3\2\2\2YZ\3\2\2\2Z^\3"+
		"\2\2\2[Y\3\2\2\2\\]\7)\2\2]_\7*\2\2^\\\3\2\2\2^_\3\2\2\2_a\3\2\2\2`R\3"+
		"\2\2\2`a\3\2\2\2ad\3\2\2\2bc\7+\2\2ce\5\32\16\2db\3\2\2\2de\3\2\2\2eg"+
		"\3\2\2\2fB\3\2\2\2fg\3\2\2\2gr\3\2\2\2hi\7&\2\2ij\7\'\2\2jo\5\b\5\2kl"+
		"\7\4\2\2ln\5\b\5\2mk\3\2\2\2nq\3\2\2\2om\3\2\2\2op\3\2\2\2ps\3\2\2\2q"+
		"o\3\2\2\2rh\3\2\2\2rs\3\2\2\2su\3\2\2\2tv\t\2\2\2ut\3\2\2\2uv\3\2\2\2"+
		"vy\3\2\2\2wx\7%\2\2xz\5\16\b\2yw\3\2\2\2yz\3\2\2\2z{\3\2\2\2{|\7\5\2\2"+
		"|\3\3\2\2\2}~\7\6\2\2~\5\3\2\2\2\177\u0080\7\7\2\2\u0080\7\3\2\2\2\u0081"+
		"\u0082\7\b\2\2\u0082\t\3\2\2\2\u0083\u0084\7\t\2\2\u0084\13\3\2\2\2\u0085"+
		"\u0086\7\n\2\2\u0086\r\3\2\2\2\u0087\u0088\7\13\2\2\u0088\17\3\2\2\2\u0089"+
		"\u008a\7\f\2\2\u008a\21\3\2\2\2\u008b\u008c\7\r\2\2\u008c\23\3\2\2\2\u008d"+
		"\u008e\7\16\2\2\u008e\25\3\2\2\2\u008f\u0090\7\17\2\2\u0090\27\3\2\2\2"+
		"\u0091\u0092\7\20\2\2\u0092\31\3\2\2\2\u0093\u0094\7\21\2\2\u0094\33\3"+
		"\2\2\2\u0095\u0096\7\22\2\2\u0096\35\3\2\2\2\u0097\u0098\7\23\2\2\u0098"+
		"\37\3\2\2\2\u0099\u009a\7\24\2\2\u009a!\3\2\2\2\u009b\u009c\7\25\2\2\u009c"+
		"#\3\2\2\2\u009d\u009e\7\26\2\2\u009e%\3\2\2\2\u009f\u00a0\7\27\2\2\u00a0"+
		"\'\3\2\2\2\u00a1\u00a2\7\30\2\2\u00a2)\3\2\2\2\25,\61\648;=?ELPY^`dfo"+
		"ruy";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}