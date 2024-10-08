// Generated from /mnt/c/LocalGitRepos/stackql/web-properties/stackql.io/ref/antlr/AUTH.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class AUTHParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, EXEC=24, AUTH=25, 
		LIST=26, PULL=27, LOGIN=28, INTERACTIVE=29, REVOKE=30, DESC=31, METHODS=32, 
		DESCRIBE=33, EXPLAIN=34, EXTENDED=35, LIMIT=36, ORDER=37, BY=38, GROUP=39, 
		WITH=40, ROLLUP=41, HAVING=42, IN=43, AS=44, USE=45, LIKE=46, FROM=47, 
		SHOW=48, WHERE=49, SERVICES=50, RESOURCES=51, PROVIDERS=52, FIELDS=53, 
		SELECT=54, DISTINCT=55, ADD=56, ARRAY=57, AND=58, ASC=59, AUTO_INCREMENT=60, 
		BETWEEN=61, BINARY=62, CASE=63, COLLATE=64, CONVERT=65, CREATE=66, CROSS=67, 
		CUME_DIST=68, CURRENT_DATE=69, CURRENT_TIME=70, CURRENT_TIMESTAMP=71, 
		SUBSTR=72, SUBSTRING=73, DATABASE=74, DATABASES=75, DEFAULT=76, DELETE=77, 
		DENSE_RANK=78, DISTINCTROW=79, DIV=80, DROP=81, ELSE=82, END=83, ESCAPE=84, 
		EXISTS=85, FALSE=86, FIRST_VALUE=87, FOR=88, FORCE=89, GROUPING=90, GROUPS=91, 
		IF=92, IGNORE=93, INDEX=94, INNER=95, INSERT=96, INTERVAL=97, INTO=98, 
		IS=99, JOIN=100, JSON_TABLE=101, KEY=102, LAG=103, LAST_VALUE=104, LATERAL=105, 
		LEAD=106, LEFT=107, LOCALTIME=108, LOCALTIMESTAMP=109, LOCK=110, MEMBER=111, 
		MATCH=112, MAXVALUE=113, MOD=114, NATURAL=115, NEXT=116, NOT=117, NTH_VALUE=118, 
		NTILE=119, NULL=120, OF=121, OFF=122, ON=123, OR=124, OUTER=125, OVER=126, 
		PERCENT_RANK=127, RANK=128, RECURSIVE=129, REGEXP=130, RENAME=131, REPLACE=132, 
		REGISTRY=133, RIGHT=134, ROW_NUMBER=135, SCHEMA=136, SEPARATOR=137, SET=138, 
		STRAIGHT_JOIN=139, SYSTEM=140, TABLE=141, THEN=142, TIMESTAMPADD=143, 
		TIMESTAMPDIFF=144, TO=145, TRUE=146, UNION=147, UNIQUE=148, UNLOCK=149, 
		UPDATE=150, USING=151, UTC_DATE=152, UTC_TIME=153, UTC_TIMESTAMP=154, 
		VALUES=155, VIEW=156, WHEN=157, WINDOW=158, XOR=159, MATERIALIZED=160, 
		REFRESH=161;
	public static final int
		RULE_authStatement = 0, RULE_provider = 1, RULE_version = 2, RULE_fullFieldName = 3, 
		RULE_functionCall = 4, RULE_alias = 5, RULE_number = 6, RULE_groupByItem = 7, 
		RULE_resourceOrSubQuery = 8, RULE_resource = 9, RULE_multipartIdentifier = 10, 
		RULE_pattern = 11, RULE_expression = 12, RULE_providerMethodName = 13, 
		RULE_parameterName = 14, RULE_parameterValue = 15, RULE_parameterExpression = 16, 
		RULE_queryHint = 17, RULE_fieldList = 18, RULE_selectStatement = 19, RULE_awaitQueryHint = 20, 
		RULE_viewName = 21, RULE_fieldValue = 22;
	private static String[] makeRuleNames() {
		return new String[] {
			"authStatement", "provider", "version", "fullFieldName", "functionCall", 
			"alias", "number", "groupByItem", "resourceOrSubQuery", "resource", "multipartIdentifier", 
			"pattern", "expression", "providerMethodName", "parameterName", "parameterValue", 
			"parameterExpression", "queryHint", "fieldList", "selectStatement", "awaitQueryHint", 
			"viewName", "fieldValue"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'provider'", "'version'", "'fullFieldName'", "'functionCall'", 
			"'alias'", "'number'", "'groupByItem'", "'resourceOrSubQuery'", "'resource'", 
			"'multipartIdentifier'", "'pattern'", "'expression'", "'providerMethodName'", 
			"'parameterName'", "'parameterValue'", "'parameterExpression'", "'queryHint'", 
			"'fieldList'", "'selectStatement'", "'awaitQueryHint'", "'viewName'", 
			"'fieldValue'", "'EXEC'", "'AUTH'", "'LIST'", "'PULL'", "'LOGIN'", "'INTERACTIVE'", 
			"'REVOKE'", "'DESC'", "'METHODS'", "'DESCRIBE'", "'EXPLAIN'", "'EXTENDED'", 
			"'LIMIT'", "'ORDER'", "'BY'", "'GROUP'", "'WITH'", "'ROLLUP'", "'HAVING'", 
			"'IN'", "'AS'", "'USE'", "'LIKE'", "'FROM'", "'SHOW'", "'WHERE'", "'SERVICES'", 
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
			"EXEC", "AUTH", "LIST", "PULL", "LOGIN", "INTERACTIVE", "REVOKE", "DESC", 
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
	public String getGrammarFileName() { return "AUTH.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public AUTHParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AuthStatementContext extends ParserRuleContext {
		public TerminalNode AUTH() { return getToken(AUTHParser.AUTH, 0); }
		public TerminalNode LOGIN() { return getToken(AUTHParser.LOGIN, 0); }
		public TerminalNode REVOKE() { return getToken(AUTHParser.REVOKE, 0); }
		public ProviderContext provider() {
			return getRuleContext(ProviderContext.class,0);
		}
		public TerminalNode INTERACTIVE() { return getToken(AUTHParser.INTERACTIVE, 0); }
		public AuthStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_authStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterAuthStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitAuthStatement(this);
		}
	}

	public final AuthStatementContext authStatement() throws RecognitionException {
		AuthStatementContext _localctx = new AuthStatementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_authStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			match(AUTH);
			setState(55);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LOGIN:
				{
				setState(47);
				match(LOGIN);
				{
				setState(49);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(48);
					provider();
					}
				}

				setState(52);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==INTERACTIVE) {
					{
					setState(51);
					match(INTERACTIVE);
					}
				}

				}
				}
				break;
			case REVOKE:
				{
				setState(54);
				match(REVOKE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(57);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ProviderContext extends ParserRuleContext {
		public ProviderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_provider; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterProvider(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitProvider(this);
		}
	}

	public final ProviderContext provider() throws RecognitionException {
		ProviderContext _localctx = new ProviderContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_provider);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
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

	@SuppressWarnings("CheckReturnValue")
	public static class VersionContext extends ParserRuleContext {
		public VersionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_version; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterVersion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitVersion(this);
		}
	}

	public final VersionContext version() throws RecognitionException {
		VersionContext _localctx = new VersionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_version);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
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
	public static class FullFieldNameContext extends ParserRuleContext {
		public FullFieldNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fullFieldName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterFullFieldName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitFullFieldName(this);
		}
	}

	public final FullFieldNameContext fullFieldName() throws RecognitionException {
		FullFieldNameContext _localctx = new FullFieldNameContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_fullFieldName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
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
	public static class FunctionCallContext extends ParserRuleContext {
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitFunctionCall(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
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
	public static class AliasContext extends ParserRuleContext {
		public AliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitAlias(this);
		}
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
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
	public static class NumberContext extends ParserRuleContext {
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitNumber(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_number);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
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
	public static class GroupByItemContext extends ParserRuleContext {
		public GroupByItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupByItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterGroupByItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitGroupByItem(this);
		}
	}

	public final GroupByItemContext groupByItem() throws RecognitionException {
		GroupByItemContext _localctx = new GroupByItemContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_groupByItem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
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
	public static class ResourceOrSubQueryContext extends ParserRuleContext {
		public ResourceOrSubQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resourceOrSubQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterResourceOrSubQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitResourceOrSubQuery(this);
		}
	}

	public final ResourceOrSubQueryContext resourceOrSubQuery() throws RecognitionException {
		ResourceOrSubQueryContext _localctx = new ResourceOrSubQueryContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_resourceOrSubQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
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
	public static class ResourceContext extends ParserRuleContext {
		public ResourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resource; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterResource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitResource(this);
		}
	}

	public final ResourceContext resource() throws RecognitionException {
		ResourceContext _localctx = new ResourceContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_resource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
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
	public static class MultipartIdentifierContext extends ParserRuleContext {
		public MultipartIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multipartIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterMultipartIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitMultipartIdentifier(this);
		}
	}

	public final MultipartIdentifierContext multipartIdentifier() throws RecognitionException {
		MultipartIdentifierContext _localctx = new MultipartIdentifierContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_multipartIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
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
	public static class PatternContext extends ParserRuleContext {
		public PatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitPattern(this);
		}
	}

	public final PatternContext pattern() throws RecognitionException {
		PatternContext _localctx = new PatternContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_pattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
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
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
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
	public static class ProviderMethodNameContext extends ParserRuleContext {
		public ProviderMethodNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_providerMethodName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterProviderMethodName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitProviderMethodName(this);
		}
	}

	public final ProviderMethodNameContext providerMethodName() throws RecognitionException {
		ProviderMethodNameContext _localctx = new ProviderMethodNameContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_providerMethodName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
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
	public static class ParameterNameContext extends ParserRuleContext {
		public ParameterNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterParameterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitParameterName(this);
		}
	}

	public final ParameterNameContext parameterName() throws RecognitionException {
		ParameterNameContext _localctx = new ParameterNameContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_parameterName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
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
	public static class ParameterValueContext extends ParserRuleContext {
		public ParameterValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterParameterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitParameterValue(this);
		}
	}

	public final ParameterValueContext parameterValue() throws RecognitionException {
		ParameterValueContext _localctx = new ParameterValueContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_parameterValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
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
	public static class ParameterExpressionContext extends ParserRuleContext {
		public ParameterExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterParameterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitParameterExpression(this);
		}
	}

	public final ParameterExpressionContext parameterExpression() throws RecognitionException {
		ParameterExpressionContext _localctx = new ParameterExpressionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_parameterExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
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
	public static class QueryHintContext extends ParserRuleContext {
		public QueryHintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryHint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterQueryHint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitQueryHint(this);
		}
	}

	public final QueryHintContext queryHint() throws RecognitionException {
		QueryHintContext _localctx = new QueryHintContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_queryHint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
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
	public static class FieldListContext extends ParserRuleContext {
		public FieldListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterFieldList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitFieldList(this);
		}
	}

	public final FieldListContext fieldList() throws RecognitionException {
		FieldListContext _localctx = new FieldListContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_fieldList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
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
	public static class SelectStatementContext extends ParserRuleContext {
		public SelectStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterSelectStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitSelectStatement(this);
		}
	}

	public final SelectStatementContext selectStatement() throws RecognitionException {
		SelectStatementContext _localctx = new SelectStatementContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_selectStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
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
	public static class AwaitQueryHintContext extends ParserRuleContext {
		public AwaitQueryHintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_awaitQueryHint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterAwaitQueryHint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitAwaitQueryHint(this);
		}
	}

	public final AwaitQueryHintContext awaitQueryHint() throws RecognitionException {
		AwaitQueryHintContext _localctx = new AwaitQueryHintContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_awaitQueryHint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
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
	public static class ViewNameContext extends ParserRuleContext {
		public ViewNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_viewName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterViewName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitViewName(this);
		}
	}

	public final ViewNameContext viewName() throws RecognitionException {
		ViewNameContext _localctx = new ViewNameContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_viewName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
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
	public static class FieldValueContext extends ParserRuleContext {
		public FieldValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).enterFieldValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AUTHListener ) ((AUTHListener)listener).exitFieldValue(this);
		}
	}

	public final FieldValueContext fieldValue() throws RecognitionException {
		FieldValueContext _localctx = new FieldValueContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_fieldValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
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

	public static final String _serializedATN =
		"\u0004\u0001\u00a1h\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000"+
		"2\b\u0000\u0001\u0000\u0003\u00005\b\u0000\u0001\u0000\u0003\u00008\b"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b"+
		"\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001"+
		"\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001"+
		"\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001"+
		"\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0000\u0000\u0017\u0000\u0002\u0004\u0006"+
		"\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,\u0000"+
		"\u0000S\u0000.\u0001\u0000\u0000\u0000\u0002;\u0001\u0000\u0000\u0000"+
		"\u0004=\u0001\u0000\u0000\u0000\u0006?\u0001\u0000\u0000\u0000\bA\u0001"+
		"\u0000\u0000\u0000\nC\u0001\u0000\u0000\u0000\fE\u0001\u0000\u0000\u0000"+
		"\u000eG\u0001\u0000\u0000\u0000\u0010I\u0001\u0000\u0000\u0000\u0012K"+
		"\u0001\u0000\u0000\u0000\u0014M\u0001\u0000\u0000\u0000\u0016O\u0001\u0000"+
		"\u0000\u0000\u0018Q\u0001\u0000\u0000\u0000\u001aS\u0001\u0000\u0000\u0000"+
		"\u001cU\u0001\u0000\u0000\u0000\u001eW\u0001\u0000\u0000\u0000 Y\u0001"+
		"\u0000\u0000\u0000\"[\u0001\u0000\u0000\u0000$]\u0001\u0000\u0000\u0000"+
		"&_\u0001\u0000\u0000\u0000(a\u0001\u0000\u0000\u0000*c\u0001\u0000\u0000"+
		"\u0000,e\u0001\u0000\u0000\u0000.7\u0005\u0019\u0000\u0000/1\u0005\u001c"+
		"\u0000\u000002\u0003\u0002\u0001\u000010\u0001\u0000\u0000\u000012\u0001"+
		"\u0000\u0000\u000024\u0001\u0000\u0000\u000035\u0005\u001d\u0000\u0000"+
		"43\u0001\u0000\u0000\u000045\u0001\u0000\u0000\u000058\u0001\u0000\u0000"+
		"\u000068\u0005\u001e\u0000\u00007/\u0001\u0000\u0000\u000076\u0001\u0000"+
		"\u0000\u000089\u0001\u0000\u0000\u00009:\u0005\u0001\u0000\u0000:\u0001"+
		"\u0001\u0000\u0000\u0000;<\u0005\u0002\u0000\u0000<\u0003\u0001\u0000"+
		"\u0000\u0000=>\u0005\u0003\u0000\u0000>\u0005\u0001\u0000\u0000\u0000"+
		"?@\u0005\u0004\u0000\u0000@\u0007\u0001\u0000\u0000\u0000AB\u0005\u0005"+
		"\u0000\u0000B\t\u0001\u0000\u0000\u0000CD\u0005\u0006\u0000\u0000D\u000b"+
		"\u0001\u0000\u0000\u0000EF\u0005\u0007\u0000\u0000F\r\u0001\u0000\u0000"+
		"\u0000GH\u0005\b\u0000\u0000H\u000f\u0001\u0000\u0000\u0000IJ\u0005\t"+
		"\u0000\u0000J\u0011\u0001\u0000\u0000\u0000KL\u0005\n\u0000\u0000L\u0013"+
		"\u0001\u0000\u0000\u0000MN\u0005\u000b\u0000\u0000N\u0015\u0001\u0000"+
		"\u0000\u0000OP\u0005\f\u0000\u0000P\u0017\u0001\u0000\u0000\u0000QR\u0005"+
		"\r\u0000\u0000R\u0019\u0001\u0000\u0000\u0000ST\u0005\u000e\u0000\u0000"+
		"T\u001b\u0001\u0000\u0000\u0000UV\u0005\u000f\u0000\u0000V\u001d\u0001"+
		"\u0000\u0000\u0000WX\u0005\u0010\u0000\u0000X\u001f\u0001\u0000\u0000"+
		"\u0000YZ\u0005\u0011\u0000\u0000Z!\u0001\u0000\u0000\u0000[\\\u0005\u0012"+
		"\u0000\u0000\\#\u0001\u0000\u0000\u0000]^\u0005\u0013\u0000\u0000^%\u0001"+
		"\u0000\u0000\u0000_`\u0005\u0014\u0000\u0000`\'\u0001\u0000\u0000\u0000"+
		"ab\u0005\u0015\u0000\u0000b)\u0001\u0000\u0000\u0000cd\u0005\u0016\u0000"+
		"\u0000d+\u0001\u0000\u0000\u0000ef\u0005\u0017\u0000\u0000f-\u0001\u0000"+
		"\u0000\u0000\u0003147";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}