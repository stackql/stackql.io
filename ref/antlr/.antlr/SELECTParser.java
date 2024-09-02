// Generated from /mnt/c/LocalGitRepos/stackql/web-properties/stackql.io/ref/antlr/SELECT.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class SELECTParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		EXEC=25, AUTH=26, LIST=27, PULL=28, LOGIN=29, INTERACTIVE=30, REVOKE=31, 
		DESC=32, METHODS=33, DESCRIBE=34, EXPLAIN=35, EXTENDED=36, LIMIT=37, ORDER=38, 
		BY=39, GROUP=40, WITH=41, ROLLUP=42, HAVING=43, IN=44, AS=45, USE=46, 
		LIKE=47, FROM=48, SHOW=49, WHERE=50, SERVICES=51, RESOURCES=52, PROVIDERS=53, 
		FIELDS=54, SELECT=55, DISTINCT=56, ADD=57, ARRAY=58, AND=59, ASC=60, AUTO_INCREMENT=61, 
		BETWEEN=62, BINARY=63, CASE=64, COLLATE=65, CONVERT=66, CREATE=67, CROSS=68, 
		CUME_DIST=69, CURRENT_DATE=70, CURRENT_TIME=71, CURRENT_TIMESTAMP=72, 
		SUBSTR=73, SUBSTRING=74, DATABASE=75, DATABASES=76, DEFAULT=77, DELETE=78, 
		DENSE_RANK=79, DISTINCTROW=80, DIV=81, DROP=82, ELSE=83, END=84, ESCAPE=85, 
		EXISTS=86, FALSE=87, FIRST_VALUE=88, FOR=89, FORCE=90, GROUPING=91, GROUPS=92, 
		IF=93, IGNORE=94, INDEX=95, INNER=96, INSERT=97, INTERVAL=98, INTO=99, 
		IS=100, JOIN=101, JSON_TABLE=102, KEY=103, LAG=104, LAST_VALUE=105, LATERAL=106, 
		LEAD=107, LEFT=108, LOCALTIME=109, LOCALTIMESTAMP=110, LOCK=111, MEMBER=112, 
		MATCH=113, MAXVALUE=114, MOD=115, NATURAL=116, NEXT=117, NOT=118, NTH_VALUE=119, 
		NTILE=120, NULL=121, OF=122, OFF=123, ON=124, OR=125, OUTER=126, OVER=127, 
		PERCENT_RANK=128, RANK=129, RECURSIVE=130, REGEXP=131, RENAME=132, REPLACE=133, 
		REGISTRY=134, RIGHT=135, ROW_NUMBER=136, SCHEMA=137, SEPARATOR=138, SET=139, 
		STRAIGHT_JOIN=140, SYSTEM=141, TABLE=142, THEN=143, TIMESTAMPADD=144, 
		TIMESTAMPDIFF=145, TO=146, TRUE=147, UNION=148, UNIQUE=149, UNLOCK=150, 
		UPDATE=151, USING=152, UTC_DATE=153, UTC_TIME=154, UTC_TIMESTAMP=155, 
		VALUES=156, VIEW=157, WHEN=158, WINDOW=159, XOR=160, MATERIALIZED=161, 
		REFRESH=162;
	public static final int
		RULE_selectStatement = 0, RULE_provider = 1, RULE_version = 2, RULE_fullFieldName = 3, 
		RULE_functionCall = 4, RULE_alias = 5, RULE_number = 6, RULE_groupByItem = 7, 
		RULE_resourceOrSubQuery = 8, RULE_resource = 9, RULE_multipartIdentifier = 10, 
		RULE_pattern = 11, RULE_expression = 12, RULE_providerMethodName = 13, 
		RULE_parameterName = 14, RULE_parameterValue = 15, RULE_parameterExpression = 16, 
		RULE_queryHint = 17, RULE_fieldList = 18, RULE_awaitQueryHint = 19, RULE_viewName = 20, 
		RULE_fieldValue = 21;
	private static String[] makeRuleNames() {
		return new String[] {
			"selectStatement", "provider", "version", "fullFieldName", "functionCall", 
			"alias", "number", "groupByItem", "resourceOrSubQuery", "resource", "multipartIdentifier", 
			"pattern", "expression", "providerMethodName", "parameterName", "parameterValue", 
			"parameterExpression", "queryHint", "fieldList", "awaitQueryHint", "viewName", 
			"fieldValue"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'*'", "','", "';'", "'provider'", "'version'", "'fullFieldName'", 
			"'functionCall'", "'alias'", "'number'", "'groupByItem'", "'resourceOrSubQuery'", 
			"'resource'", "'multipartIdentifier'", "'pattern'", "'expression'", "'providerMethodName'", 
			"'parameterName'", "'parameterValue'", "'parameterExpression'", "'queryHint'", 
			"'fieldList'", "'awaitQueryHint'", "'viewName'", "'fieldValue'", "'EXEC'", 
			"'AUTH'", "'LIST'", "'PULL'", "'LOGIN'", "'INTERACTIVE'", "'REVOKE'", 
			"'DESC'", "'METHODS'", "'DESCRIBE'", "'EXPLAIN'", "'EXTENDED'", "'LIMIT'", 
			"'ORDER'", "'BY'", "'GROUP'", "'WITH'", "'ROLLUP'", "'HAVING'", "'IN'", 
			"'AS'", "'USE'", "'LIKE'", "'FROM'", "'SHOW'", "'WHERE'", "'SERVICES'", 
			"'RESOURCES'", "'PROVIDERS'", "'FIELDS'", "'SELECT'", "'DISTINCT'", "'ADD'", 
			"'ARRAY '", "'AND'", "'ASC'", "'AUTO_INCREMENT'", "'BETWEEN'", "'BINARY'", 
			"'CASE'", "'COLLATE'", "'CONVERT'", "'CREATE'", "'CROSS'", "'CUME_DIST'", 
			"'CURRENT_DATE'", "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", "'SUBSTR'", 
			"'SUBSTRING'", "'DATABASE'", "'DATABASES'", "'DEFAULT'", "'DELETE'", 
			"'DENSE_RANK'", "'DISTINCTROW'", "'DIV'", "'DROP'", "'ELSE'", "'END'", 
			"'ESCAPE'", "'EXISTS'", "'FALSE'", "'FIRST_VALUE'", "'FOR'", "'FORCE'", 
			"'GROUPING'", "'GROUPS'", "'IF'", "'IGNORE'", "'INDEX'", "'INNER'", "'INSERT'", 
			"'INTERVAL'", "'INTO'", "'IS'", "'JOIN'", "'JSON_TABLE'", "'KEY'", "'LAG'", 
			"'LAST_VALUE'", "'LATERAL'", "'LEAD'", "'LEFT'", "'LOCALTIME'", "'LOCALTIMESTAMP'", 
			"'LOCK'", "'MEMBER'", "'MATCH'", "'MAXVALUE'", "'MOD'", "'NATURAL'", 
			"'NEXT'", "'NOT'", "'NTH_VALUE'", "'NTILE'", "'NULL'", "'OF'", "'OFF'", 
			"'ON'", "'OR'", "'OUTER'", "'OVER'", "'PERCENT_RANK'", "'RANK'", "'RECURSIVE'", 
			"'REGEXP'", "'RENAME'", "'REPLACE'", "'REGISTRY'", "'RIGHT'", "'ROW_NUMBER'", 
			"'SCHEMA'", "'SEPARATOR'", "'SET'", "'STRAIGHT_JOIN'", "'SYSTEM'", "'TABLE'", 
			"'THEN'", "'TIMESTAMPADD'", "'TIMESTAMPDIFF'", "'TO'", "'TRUE'", "'UNION'", 
			"'UNIQUE'", "'UNLOCK'", "'UPDATE'", "'USING'", "'UTC_DATE'", "'UTC_TIME'", 
			"'UTC_TIMESTAMP'", "'VALUES'", "'VIEW'", "'WHEN'", "'WINDOW'", "'XOR'", 
			"'MATERIALIZED'", "'REFRESH'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, "EXEC", "AUTH", "LIST", "PULL", "LOGIN", "INTERACTIVE", "REVOKE", 
			"DESC", "METHODS", "DESCRIBE", "EXPLAIN", "EXTENDED", "LIMIT", "ORDER", 
			"BY", "GROUP", "WITH", "ROLLUP", "HAVING", "IN", "AS", "USE", "LIKE", 
			"FROM", "SHOW", "WHERE", "SERVICES", "RESOURCES", "PROVIDERS", "FIELDS", 
			"SELECT", "DISTINCT", "ADD", "ARRAY", "AND", "ASC", "AUTO_INCREMENT", 
			"BETWEEN", "BINARY", "CASE", "COLLATE", "CONVERT", "CREATE", "CROSS", 
			"CUME_DIST", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "SUBSTR", 
			"SUBSTRING", "DATABASE", "DATABASES", "DEFAULT", "DELETE", "DENSE_RANK", 
			"DISTINCTROW", "DIV", "DROP", "ELSE", "END", "ESCAPE", "EXISTS", "FALSE", 
			"FIRST_VALUE", "FOR", "FORCE", "GROUPING", "GROUPS", "IF", "IGNORE", 
			"INDEX", "INNER", "INSERT", "INTERVAL", "INTO", "IS", "JOIN", "JSON_TABLE", 
			"KEY", "LAG", "LAST_VALUE", "LATERAL", "LEAD", "LEFT", "LOCALTIME", "LOCALTIMESTAMP", 
			"LOCK", "MEMBER", "MATCH", "MAXVALUE", "MOD", "NATURAL", "NEXT", "NOT", 
			"NTH_VALUE", "NTILE", "NULL", "OF", "OFF", "ON", "OR", "OUTER", "OVER", 
			"PERCENT_RANK", "RANK", "RECURSIVE", "REGEXP", "RENAME", "REPLACE", "REGISTRY", 
			"RIGHT", "ROW_NUMBER", "SCHEMA", "SEPARATOR", "SET", "STRAIGHT_JOIN", 
			"SYSTEM", "TABLE", "THEN", "TIMESTAMPADD", "TIMESTAMPDIFF", "TO", "TRUE", 
			"UNION", "UNIQUE", "UNLOCK", "UPDATE", "USING", "UTC_DATE", "UTC_TIME", 
			"UTC_TIMESTAMP", "VALUES", "VIEW", "WHEN", "WINDOW", "XOR", "MATERIALIZED", 
			"REFRESH"
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(44);
			match(SELECT);
			setState(46);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DISTINCT) {
				{
				setState(45);
				match(DISTINCT);
				}
			}

			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 194L) != 0)) {
				{
				setState(63);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__0:
					{
					setState(48);
					((SelectStatementContext)_localctx).star = match(T__0);
					}
					break;
				case T__5:
					{
					setState(49);
					fullFieldName();
					setState(54);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==T__7 || _la==AS) {
						{
						setState(51);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==AS) {
							{
							setState(50);
							match(AS);
							}
						}

						setState(53);
						alias();
						}
					}

					}
					break;
				case T__6:
					{
					setState(56);
					functionCall();
					setState(61);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==T__7 || _la==AS) {
						{
						setState(58);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==AS) {
							{
							setState(57);
							match(AS);
							}
						}

						setState(60);
						alias();
						}
					}

					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(104);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FROM) {
				{
				setState(68);
				match(FROM);
				setState(69);
				resource();
				setState(78);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==JOIN || _la==OUTER) {
					{
					setState(71);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==OUTER) {
						{
						setState(70);
						match(OUTER);
						}
					}

					setState(73);
					match(JOIN);
					setState(74);
					resource();
					setState(75);
					match(ON);
					setState(76);
					expression();
					}
				}

				setState(82);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WHERE) {
					{
					setState(80);
					match(WHERE);
					setState(81);
					expression();
					}
				}

				setState(98);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GROUP) {
					{
					setState(84);
					match(GROUP);
					setState(85);
					match(BY);
					setState(86);
					groupByItem();
					setState(91);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__1) {
						{
						{
						setState(87);
						match(T__1);
						setState(88);
						groupByItem();
						}
						}
						setState(93);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(96);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WITH) {
						{
						setState(94);
						match(WITH);
						setState(95);
						match(ROLLUP);
						}
					}

					}
				}

				setState(102);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==HAVING) {
					{
					setState(100);
					match(HAVING);
					setState(101);
					((SelectStatementContext)_localctx).havingExpr = expression();
					}
				}

				}
			}

			setState(116);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDER) {
				{
				setState(106);
				match(ORDER);
				setState(107);
				match(BY);
				setState(108);
				fullFieldName();
				setState(113);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(109);
					match(T__1);
					setState(110);
					fullFieldName();
					}
					}
					setState(115);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(119);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DESC || _la==ASC) {
				{
				setState(118);
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

			setState(123);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LIMIT) {
				{
				setState(121);
				match(LIMIT);
				setState(122);
				number();
				}
			}

			setState(125);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(127);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(129);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(131);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(133);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(135);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(137);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(139);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(141);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(143);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(145);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(147);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(149);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(151);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(153);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(155);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(157);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(159);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(161);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(163);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ViewNameContext extends ParserRuleContext {
		public ViewNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_viewName; }
	}

	public final ViewNameContext viewName() throws RecognitionException {
		ViewNameContext _localctx = new ViewNameContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_viewName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(165);
			match(T__22);
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

	@SuppressWarnings("CheckReturnValue")
	public static class FieldValueContext extends ParserRuleContext {
		public FieldValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldValue; }
	}

	public final FieldValueContext fieldValue() throws RecognitionException {
		FieldValueContext _localctx = new FieldValueContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_fieldValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167);
			match(T__23);
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
		"\u0004\u0001\u00a2\u00aa\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0001\u0000\u0001\u0000\u0003\u0000/\b\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0003\u00004\b\u0000\u0001\u0000\u0003\u00007\b\u0000"+
		"\u0001\u0000\u0001\u0000\u0003\u0000;\b\u0000\u0001\u0000\u0003\u0000"+
		">\b\u0000\u0005\u0000@\b\u0000\n\u0000\f\u0000C\t\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0003\u0000H\b\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0003\u0000O\b\u0000\u0001\u0000\u0001"+
		"\u0000\u0003\u0000S\b\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0005\u0000Z\b\u0000\n\u0000\f\u0000]\t\u0000\u0001"+
		"\u0000\u0001\u0000\u0003\u0000a\b\u0000\u0003\u0000c\b\u0000\u0001\u0000"+
		"\u0001\u0000\u0003\u0000g\b\u0000\u0003\u0000i\b\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u0000p\b\u0000\n\u0000"+
		"\f\u0000s\t\u0000\u0003\u0000u\b\u0000\u0001\u0000\u0003\u0000x\b\u0000"+
		"\u0001\u0000\u0001\u0000\u0003\u0000|\b\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0011"+
		"\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0014"+
		"\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0000\u0000\u0016\u0000"+
		"\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c"+
		"\u001e \"$&(*\u0000\u0001\u0002\u0000  <<\u00a7\u0000,\u0001\u0000\u0000"+
		"\u0000\u0002\u007f\u0001\u0000\u0000\u0000\u0004\u0081\u0001\u0000\u0000"+
		"\u0000\u0006\u0083\u0001\u0000\u0000\u0000\b\u0085\u0001\u0000\u0000\u0000"+
		"\n\u0087\u0001\u0000\u0000\u0000\f\u0089\u0001\u0000\u0000\u0000\u000e"+
		"\u008b\u0001\u0000\u0000\u0000\u0010\u008d\u0001\u0000\u0000\u0000\u0012"+
		"\u008f\u0001\u0000\u0000\u0000\u0014\u0091\u0001\u0000\u0000\u0000\u0016"+
		"\u0093\u0001\u0000\u0000\u0000\u0018\u0095\u0001\u0000\u0000\u0000\u001a"+
		"\u0097\u0001\u0000\u0000\u0000\u001c\u0099\u0001\u0000\u0000\u0000\u001e"+
		"\u009b\u0001\u0000\u0000\u0000 \u009d\u0001\u0000\u0000\u0000\"\u009f"+
		"\u0001\u0000\u0000\u0000$\u00a1\u0001\u0000\u0000\u0000&\u00a3\u0001\u0000"+
		"\u0000\u0000(\u00a5\u0001\u0000\u0000\u0000*\u00a7\u0001\u0000\u0000\u0000"+
		",.\u00057\u0000\u0000-/\u00058\u0000\u0000.-\u0001\u0000\u0000\u0000."+
		"/\u0001\u0000\u0000\u0000/A\u0001\u0000\u0000\u00000@\u0005\u0001\u0000"+
		"\u000016\u0003\u0006\u0003\u000024\u0005-\u0000\u000032\u0001\u0000\u0000"+
		"\u000034\u0001\u0000\u0000\u000045\u0001\u0000\u0000\u000057\u0003\n\u0005"+
		"\u000063\u0001\u0000\u0000\u000067\u0001\u0000\u0000\u00007@\u0001\u0000"+
		"\u0000\u00008=\u0003\b\u0004\u00009;\u0005-\u0000\u0000:9\u0001\u0000"+
		"\u0000\u0000:;\u0001\u0000\u0000\u0000;<\u0001\u0000\u0000\u0000<>\u0003"+
		"\n\u0005\u0000=:\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000\u0000>@\u0001"+
		"\u0000\u0000\u0000?0\u0001\u0000\u0000\u0000?1\u0001\u0000\u0000\u0000"+
		"?8\u0001\u0000\u0000\u0000@C\u0001\u0000\u0000\u0000A?\u0001\u0000\u0000"+
		"\u0000AB\u0001\u0000\u0000\u0000Bh\u0001\u0000\u0000\u0000CA\u0001\u0000"+
		"\u0000\u0000DE\u00050\u0000\u0000EN\u0003\u0012\t\u0000FH\u0005~\u0000"+
		"\u0000GF\u0001\u0000\u0000\u0000GH\u0001\u0000\u0000\u0000HI\u0001\u0000"+
		"\u0000\u0000IJ\u0005e\u0000\u0000JK\u0003\u0012\t\u0000KL\u0005|\u0000"+
		"\u0000LM\u0003\u0018\f\u0000MO\u0001\u0000\u0000\u0000NG\u0001\u0000\u0000"+
		"\u0000NO\u0001\u0000\u0000\u0000OR\u0001\u0000\u0000\u0000PQ\u00052\u0000"+
		"\u0000QS\u0003\u0018\f\u0000RP\u0001\u0000\u0000\u0000RS\u0001\u0000\u0000"+
		"\u0000Sb\u0001\u0000\u0000\u0000TU\u0005(\u0000\u0000UV\u0005\'\u0000"+
		"\u0000V[\u0003\u000e\u0007\u0000WX\u0005\u0002\u0000\u0000XZ\u0003\u000e"+
		"\u0007\u0000YW\u0001\u0000\u0000\u0000Z]\u0001\u0000\u0000\u0000[Y\u0001"+
		"\u0000\u0000\u0000[\\\u0001\u0000\u0000\u0000\\`\u0001\u0000\u0000\u0000"+
		"][\u0001\u0000\u0000\u0000^_\u0005)\u0000\u0000_a\u0005*\u0000\u0000`"+
		"^\u0001\u0000\u0000\u0000`a\u0001\u0000\u0000\u0000ac\u0001\u0000\u0000"+
		"\u0000bT\u0001\u0000\u0000\u0000bc\u0001\u0000\u0000\u0000cf\u0001\u0000"+
		"\u0000\u0000de\u0005+\u0000\u0000eg\u0003\u0018\f\u0000fd\u0001\u0000"+
		"\u0000\u0000fg\u0001\u0000\u0000\u0000gi\u0001\u0000\u0000\u0000hD\u0001"+
		"\u0000\u0000\u0000hi\u0001\u0000\u0000\u0000it\u0001\u0000\u0000\u0000"+
		"jk\u0005&\u0000\u0000kl\u0005\'\u0000\u0000lq\u0003\u0006\u0003\u0000"+
		"mn\u0005\u0002\u0000\u0000np\u0003\u0006\u0003\u0000om\u0001\u0000\u0000"+
		"\u0000ps\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000qr\u0001\u0000"+
		"\u0000\u0000ru\u0001\u0000\u0000\u0000sq\u0001\u0000\u0000\u0000tj\u0001"+
		"\u0000\u0000\u0000tu\u0001\u0000\u0000\u0000uw\u0001\u0000\u0000\u0000"+
		"vx\u0007\u0000\u0000\u0000wv\u0001\u0000\u0000\u0000wx\u0001\u0000\u0000"+
		"\u0000x{\u0001\u0000\u0000\u0000yz\u0005%\u0000\u0000z|\u0003\f\u0006"+
		"\u0000{y\u0001\u0000\u0000\u0000{|\u0001\u0000\u0000\u0000|}\u0001\u0000"+
		"\u0000\u0000}~\u0005\u0003\u0000\u0000~\u0001\u0001\u0000\u0000\u0000"+
		"\u007f\u0080\u0005\u0004\u0000\u0000\u0080\u0003\u0001\u0000\u0000\u0000"+
		"\u0081\u0082\u0005\u0005\u0000\u0000\u0082\u0005\u0001\u0000\u0000\u0000"+
		"\u0083\u0084\u0005\u0006\u0000\u0000\u0084\u0007\u0001\u0000\u0000\u0000"+
		"\u0085\u0086\u0005\u0007\u0000\u0000\u0086\t\u0001\u0000\u0000\u0000\u0087"+
		"\u0088\u0005\b\u0000\u0000\u0088\u000b\u0001\u0000\u0000\u0000\u0089\u008a"+
		"\u0005\t\u0000\u0000\u008a\r\u0001\u0000\u0000\u0000\u008b\u008c\u0005"+
		"\n\u0000\u0000\u008c\u000f\u0001\u0000\u0000\u0000\u008d\u008e\u0005\u000b"+
		"\u0000\u0000\u008e\u0011\u0001\u0000\u0000\u0000\u008f\u0090\u0005\f\u0000"+
		"\u0000\u0090\u0013\u0001\u0000\u0000\u0000\u0091\u0092\u0005\r\u0000\u0000"+
		"\u0092\u0015\u0001\u0000\u0000\u0000\u0093\u0094\u0005\u000e\u0000\u0000"+
		"\u0094\u0017\u0001\u0000\u0000\u0000\u0095\u0096\u0005\u000f\u0000\u0000"+
		"\u0096\u0019\u0001\u0000\u0000\u0000\u0097\u0098\u0005\u0010\u0000\u0000"+
		"\u0098\u001b\u0001\u0000\u0000\u0000\u0099\u009a\u0005\u0011\u0000\u0000"+
		"\u009a\u001d\u0001\u0000\u0000\u0000\u009b\u009c\u0005\u0012\u0000\u0000"+
		"\u009c\u001f\u0001\u0000\u0000\u0000\u009d\u009e\u0005\u0013\u0000\u0000"+
		"\u009e!\u0001\u0000\u0000\u0000\u009f\u00a0\u0005\u0014\u0000\u0000\u00a0"+
		"#\u0001\u0000\u0000\u0000\u00a1\u00a2\u0005\u0015\u0000\u0000\u00a2%\u0001"+
		"\u0000\u0000\u0000\u00a3\u00a4\u0005\u0016\u0000\u0000\u00a4\'\u0001\u0000"+
		"\u0000\u0000\u00a5\u00a6\u0005\u0017\u0000\u0000\u00a6)\u0001\u0000\u0000"+
		"\u0000\u00a7\u00a8\u0005\u0018\u0000\u0000\u00a8+\u0001\u0000\u0000\u0000"+
		"\u0013.36:=?AGNR[`bfhqtw{";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}