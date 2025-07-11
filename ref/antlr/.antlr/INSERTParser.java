// Generated from c:/LocalGitRepos/stackql/web-properties/stackql.io/ref/antlr/INSERT.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class INSERTParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, EXEC=27, AUTH=28, LIST=29, PULL=30, LOGIN=31, INTERACTIVE=32, 
		REVOKE=33, DESC=34, METHODS=35, DESCRIBE=36, EXPLAIN=37, EXTENDED=38, 
		LIMIT=39, ORDER=40, BY=41, GROUP=42, WITH=43, ROLLUP=44, HAVING=45, IN=46, 
		AS=47, USE=48, LIKE=49, FROM=50, SHOW=51, WHERE=52, SERVICES=53, RESOURCES=54, 
		PROVIDERS=55, FIELDS=56, SELECT=57, DISTINCT=58, ADD=59, ARRAY=60, AND=61, 
		ASC=62, AUTO_INCREMENT=63, BETWEEN=64, BINARY=65, CASE=66, COLLATE=67, 
		CONVERT=68, CREATE=69, CROSS=70, CUME_DIST=71, CURRENT_DATE=72, CURRENT_TIME=73, 
		CURRENT_TIMESTAMP=74, SUBSTR=75, SUBSTRING=76, DATABASE=77, DATABASES=78, 
		DEFAULT=79, DELETE=80, DENSE_RANK=81, DISTINCTROW=82, DIV=83, DROP=84, 
		ELSE=85, END=86, ESCAPE=87, EXISTS=88, FALSE=89, FIRST_VALUE=90, FOR=91, 
		FORCE=92, GROUPING=93, GROUPS=94, IF=95, IGNORE=96, INDEX=97, INNER=98, 
		INSERT=99, INTERVAL=100, INTO=101, IS=102, JOIN=103, JSON_TABLE=104, KEY=105, 
		LAG=106, LAST_VALUE=107, LATERAL=108, LEAD=109, LEFT=110, LOCALTIME=111, 
		LOCALTIMESTAMP=112, LOCK=113, MEMBER=114, MATCH=115, MAXVALUE=116, MOD=117, 
		NATURAL=118, NEXT=119, NOT=120, NTH_VALUE=121, NTILE=122, NULL=123, OF=124, 
		OFF=125, ON=126, OR=127, OUTER=128, OVER=129, PERCENT_RANK=130, RANK=131, 
		RECURSIVE=132, REGEXP=133, RENAME=134, REPLACE=135, REGISTRY=136, RIGHT=137, 
		ROW_NUMBER=138, SCHEMA=139, SEPARATOR=140, SET=141, STRAIGHT_JOIN=142, 
		SYSTEM=143, TABLE=144, THEN=145, TIMESTAMPADD=146, TIMESTAMPDIFF=147, 
		TO=148, TRUE=149, UNION=150, UNIQUE=151, UNLOCK=152, UPDATE=153, USING=154, 
		UTC_DATE=155, UTC_TIME=156, UTC_TIMESTAMP=157, VALUES=158, VIEW=159, WHEN=160, 
		WINDOW=161, XOR=162, MATERIALIZED=163, REFRESH=164, RETURNING=165;
	public static final int
		RULE_insertStatement = 0, RULE_provider = 1, RULE_version = 2, RULE_fullFieldName = 3, 
		RULE_functionCall = 4, RULE_alias = 5, RULE_number = 6, RULE_groupByItem = 7, 
		RULE_resourceOrSubQuery = 8, RULE_resource = 9, RULE_multipartIdentifier = 10, 
		RULE_pattern = 11, RULE_expression = 12, RULE_providerMethodName = 13, 
		RULE_parameterName = 14, RULE_parameterValue = 15, RULE_parameterExpression = 16, 
		RULE_queryHint = 17, RULE_fieldList = 18, RULE_selectStatement = 19, RULE_replaceStatement = 20, 
		RULE_awaitQueryHint = 21, RULE_viewName = 22, RULE_fieldValue = 23;
	private static String[] makeRuleNames() {
		return new String[] {
			"insertStatement", "provider", "version", "fullFieldName", "functionCall", 
			"alias", "number", "groupByItem", "resourceOrSubQuery", "resource", "multipartIdentifier", 
			"pattern", "expression", "providerMethodName", "parameterName", "parameterValue", 
			"parameterExpression", "queryHint", "fieldList", "selectStatement", "replaceStatement", 
			"awaitQueryHint", "viewName", "fieldValue"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "';'", "'provider'", "'version'", "'fullFieldName'", 
			"'functionCall'", "'alias'", "'number'", "'groupByItem'", "'resourceOrSubQuery'", 
			"'resource'", "'multipartIdentifier'", "'pattern'", "'expression'", "'providerMethodName'", 
			"'parameterName'", "'parameterValue'", "'parameterExpression'", "'queryHint'", 
			"'fieldList'", "'selectStatement'", "'replaceStatement'", "'awaitQueryHint'", 
			"'viewName'", "'fieldValue'", "'EXEC'", "'AUTH'", "'LIST'", "'PULL'", 
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
			"'WHEN'", "'WINDOW'", "'XOR'", "'MATERIALIZED'", "'REFRESH'", "'RETURNING'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, "EXEC", "AUTH", "LIST", "PULL", "LOGIN", "INTERACTIVE", 
			"REVOKE", "DESC", "METHODS", "DESCRIBE", "EXPLAIN", "EXTENDED", "LIMIT", 
			"ORDER", "BY", "GROUP", "WITH", "ROLLUP", "HAVING", "IN", "AS", "USE", 
			"LIKE", "FROM", "SHOW", "WHERE", "SERVICES", "RESOURCES", "PROVIDERS", 
			"FIELDS", "SELECT", "DISTINCT", "ADD", "ARRAY", "AND", "ASC", "AUTO_INCREMENT", 
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
			"REFRESH", "RETURNING"
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
	public String getGrammarFileName() { return "INSERT.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public INSERTParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InsertStatementContext extends ParserRuleContext {
		public TerminalNode INSERT() { return getToken(INSERTParser.INSERT, 0); }
		public MultipartIdentifierContext multipartIdentifier() {
			return getRuleContext(MultipartIdentifierContext.class,0);
		}
		public List<FieldListContext> fieldList() {
			return getRuleContexts(FieldListContext.class);
		}
		public FieldListContext fieldList(int i) {
			return getRuleContext(FieldListContext.class,i);
		}
		public SelectStatementContext selectStatement() {
			return getRuleContext(SelectStatementContext.class,0);
		}
		public AwaitQueryHintContext awaitQueryHint() {
			return getRuleContext(AwaitQueryHintContext.class,0);
		}
		public TerminalNode IGNORE() { return getToken(INSERTParser.IGNORE, 0); }
		public TerminalNode INTO() { return getToken(INSERTParser.INTO, 0); }
		public TerminalNode RETURNING() { return getToken(INSERTParser.RETURNING, 0); }
		public TerminalNode VALUES() { return getToken(INSERTParser.VALUES, 0); }
		public InsertStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insertStatement; }
	}

	public final InsertStatementContext insertStatement() throws RecognitionException {
		InsertStatementContext _localctx = new InsertStatementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_insertStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			match(INSERT);
			setState(50);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__23) {
				{
				setState(49);
				awaitQueryHint();
				}
			}

			setState(53);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IGNORE) {
				{
				setState(52);
				match(IGNORE);
				}
			}

			setState(56);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INTO) {
				{
				setState(55);
				match(INTO);
				}
			}

			setState(58);
			multipartIdentifier();
			{
			setState(59);
			match(T__0);
			setState(60);
			fieldList();
			setState(61);
			match(T__1);
			}
			setState(69);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__21:
				{
				setState(63);
				selectStatement();
				}
				break;
			case VALUES:
				{
				{
				setState(64);
				match(VALUES);
				setState(65);
				match(T__0);
				setState(66);
				fieldList();
				setState(67);
				match(T__1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(73);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==RETURNING) {
				{
				setState(71);
				match(RETURNING);
				setState(72);
				fieldList();
				}
			}

			setState(75);
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
			setState(77);
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
			setState(79);
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
			setState(81);
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
			setState(83);
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
			setState(85);
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
			setState(87);
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
			setState(89);
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
			setState(91);
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
			setState(93);
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
			setState(95);
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
			setState(97);
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
			setState(99);
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
			setState(101);
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
			setState(103);
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
			setState(105);
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
			setState(107);
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
			setState(109);
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
			setState(111);
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
	public static class SelectStatementContext extends ParserRuleContext {
		public SelectStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectStatement; }
	}

	public final SelectStatementContext selectStatement() throws RecognitionException {
		SelectStatementContext _localctx = new SelectStatementContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_selectStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
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
	public static class ReplaceStatementContext extends ParserRuleContext {
		public ReplaceStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_replaceStatement; }
	}

	public final ReplaceStatementContext replaceStatement() throws RecognitionException {
		ReplaceStatementContext _localctx = new ReplaceStatementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_replaceStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
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
	public static class AwaitQueryHintContext extends ParserRuleContext {
		public AwaitQueryHintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_awaitQueryHint; }
	}

	public final AwaitQueryHintContext awaitQueryHint() throws RecognitionException {
		AwaitQueryHintContext _localctx = new AwaitQueryHintContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_awaitQueryHint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ViewNameContext extends ParserRuleContext {
		public ViewNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_viewName; }
	}

	public final ViewNameContext viewName() throws RecognitionException {
		ViewNameContext _localctx = new ViewNameContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_viewName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			match(T__24);
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
		enterRule(_localctx, 46, RULE_fieldValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			match(T__25);
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
		"\u0004\u0001\u00a5|\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0001\u0000\u0001\u0000"+
		"\u0003\u00003\b\u0000\u0001\u0000\u0003\u00006\b\u0000\u0001\u0000\u0003"+
		"\u00009\b\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0003\u0000F\b\u0000\u0001\u0000\u0001\u0000\u0003\u0000J\b\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013"+
		"\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0016"+
		"\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0000\u0000\u0018\u0000"+
		"\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c"+
		"\u001e \"$&(*,.\u0000\u0000h\u00000\u0001\u0000\u0000\u0000\u0002M\u0001"+
		"\u0000\u0000\u0000\u0004O\u0001\u0000\u0000\u0000\u0006Q\u0001\u0000\u0000"+
		"\u0000\bS\u0001\u0000\u0000\u0000\nU\u0001\u0000\u0000\u0000\fW\u0001"+
		"\u0000\u0000\u0000\u000eY\u0001\u0000\u0000\u0000\u0010[\u0001\u0000\u0000"+
		"\u0000\u0012]\u0001\u0000\u0000\u0000\u0014_\u0001\u0000\u0000\u0000\u0016"+
		"a\u0001\u0000\u0000\u0000\u0018c\u0001\u0000\u0000\u0000\u001ae\u0001"+
		"\u0000\u0000\u0000\u001cg\u0001\u0000\u0000\u0000\u001ei\u0001\u0000\u0000"+
		"\u0000 k\u0001\u0000\u0000\u0000\"m\u0001\u0000\u0000\u0000$o\u0001\u0000"+
		"\u0000\u0000&q\u0001\u0000\u0000\u0000(s\u0001\u0000\u0000\u0000*u\u0001"+
		"\u0000\u0000\u0000,w\u0001\u0000\u0000\u0000.y\u0001\u0000\u0000\u0000"+
		"02\u0005c\u0000\u000013\u0003*\u0015\u000021\u0001\u0000\u0000\u00002"+
		"3\u0001\u0000\u0000\u000035\u0001\u0000\u0000\u000046\u0005`\u0000\u0000"+
		"54\u0001\u0000\u0000\u000056\u0001\u0000\u0000\u000068\u0001\u0000\u0000"+
		"\u000079\u0005e\u0000\u000087\u0001\u0000\u0000\u000089\u0001\u0000\u0000"+
		"\u00009:\u0001\u0000\u0000\u0000:;\u0003\u0014\n\u0000;<\u0005\u0001\u0000"+
		"\u0000<=\u0003$\u0012\u0000=>\u0005\u0002\u0000\u0000>E\u0001\u0000\u0000"+
		"\u0000?F\u0003&\u0013\u0000@A\u0005\u009e\u0000\u0000AB\u0005\u0001\u0000"+
		"\u0000BC\u0003$\u0012\u0000CD\u0005\u0002\u0000\u0000DF\u0001\u0000\u0000"+
		"\u0000E?\u0001\u0000\u0000\u0000E@\u0001\u0000\u0000\u0000FI\u0001\u0000"+
		"\u0000\u0000GH\u0005\u00a5\u0000\u0000HJ\u0003$\u0012\u0000IG\u0001\u0000"+
		"\u0000\u0000IJ\u0001\u0000\u0000\u0000JK\u0001\u0000\u0000\u0000KL\u0005"+
		"\u0003\u0000\u0000L\u0001\u0001\u0000\u0000\u0000MN\u0005\u0004\u0000"+
		"\u0000N\u0003\u0001\u0000\u0000\u0000OP\u0005\u0005\u0000\u0000P\u0005"+
		"\u0001\u0000\u0000\u0000QR\u0005\u0006\u0000\u0000R\u0007\u0001\u0000"+
		"\u0000\u0000ST\u0005\u0007\u0000\u0000T\t\u0001\u0000\u0000\u0000UV\u0005"+
		"\b\u0000\u0000V\u000b\u0001\u0000\u0000\u0000WX\u0005\t\u0000\u0000X\r"+
		"\u0001\u0000\u0000\u0000YZ\u0005\n\u0000\u0000Z\u000f\u0001\u0000\u0000"+
		"\u0000[\\\u0005\u000b\u0000\u0000\\\u0011\u0001\u0000\u0000\u0000]^\u0005"+
		"\f\u0000\u0000^\u0013\u0001\u0000\u0000\u0000_`\u0005\r\u0000\u0000`\u0015"+
		"\u0001\u0000\u0000\u0000ab\u0005\u000e\u0000\u0000b\u0017\u0001\u0000"+
		"\u0000\u0000cd\u0005\u000f\u0000\u0000d\u0019\u0001\u0000\u0000\u0000"+
		"ef\u0005\u0010\u0000\u0000f\u001b\u0001\u0000\u0000\u0000gh\u0005\u0011"+
		"\u0000\u0000h\u001d\u0001\u0000\u0000\u0000ij\u0005\u0012\u0000\u0000"+
		"j\u001f\u0001\u0000\u0000\u0000kl\u0005\u0013\u0000\u0000l!\u0001\u0000"+
		"\u0000\u0000mn\u0005\u0014\u0000\u0000n#\u0001\u0000\u0000\u0000op\u0005"+
		"\u0015\u0000\u0000p%\u0001\u0000\u0000\u0000qr\u0005\u0016\u0000\u0000"+
		"r\'\u0001\u0000\u0000\u0000st\u0005\u0017\u0000\u0000t)\u0001\u0000\u0000"+
		"\u0000uv\u0005\u0018\u0000\u0000v+\u0001\u0000\u0000\u0000wx\u0005\u0019"+
		"\u0000\u0000x-\u0001\u0000\u0000\u0000yz\u0005\u001a\u0000\u0000z/\u0001"+
		"\u0000\u0000\u0000\u0005258EI";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}