// Generated from c:\LocalGitRepos\stackql\stackql.io\ref\antlr\common.g4 by ANTLR 4.8
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class commonParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, EXEC=22, AUTH=23, LIST=24, PULL=25, 
		LOGIN=26, INTERACTIVE=27, REVOKE=28, DESC=29, METHODS=30, DESCRIBE=31, 
		EXPLAIN=32, EXTENDED=33, LIMIT=34, ORDER=35, BY=36, GROUP=37, WITH=38, 
		ROLLUP=39, HAVING=40, IN=41, AS=42, USE=43, LIKE=44, FROM=45, SHOW=46, 
		WHERE=47, SERVICES=48, RESOURCES=49, PROVIDERS=50, FIELDS=51, SELECT=52, 
		DISTINCT=53, ADD=54, ARRAY=55, AND=56, ASC=57, AUTO_INCREMENT=58, BETWEEN=59, 
		BINARY=60, CASE=61, COLLATE=62, CONVERT=63, CREATE=64, CROSS=65, CUME_DIST=66, 
		CURRENT_DATE=67, CURRENT_TIME=68, CURRENT_TIMESTAMP=69, SUBSTR=70, SUBSTRING=71, 
		DATABASE=72, DATABASES=73, DEFAULT=74, DELETE=75, DENSE_RANK=76, DISTINCTROW=77, 
		DIV=78, DROP=79, ELSE=80, END=81, ESCAPE=82, EXISTS=83, FALSE=84, FIRST_VALUE=85, 
		FOR=86, FORCE=87, GROUPING=88, GROUPS=89, IF=90, IGNORE=91, INDEX=92, 
		INNER=93, INSERT=94, INTERVAL=95, INTO=96, IS=97, JOIN=98, JSON_TABLE=99, 
		KEY=100, LAG=101, LAST_VALUE=102, LATERAL=103, LEAD=104, LEFT=105, LOCALTIME=106, 
		LOCALTIMESTAMP=107, LOCK=108, MEMBER=109, MATCH=110, MAXVALUE=111, MOD=112, 
		NATURAL=113, NEXT=114, NOT=115, NTH_VALUE=116, NTILE=117, NULL=118, OF=119, 
		OFF=120, ON=121, OR=122, OUTER=123, OVER=124, PERCENT_RANK=125, RANK=126, 
		RECURSIVE=127, REGEXP=128, RENAME=129, REPLACE=130, REGISTRY=131, RIGHT=132, 
		ROW_NUMBER=133, SCHEMA=134, SEPARATOR=135, SET=136, STRAIGHT_JOIN=137, 
		SYSTEM=138, TABLE=139, THEN=140, TIMESTAMPADD=141, TIMESTAMPDIFF=142, 
		TO=143, TRUE=144, UNION=145, UNIQUE=146, UNLOCK=147, UPDATE=148, USING=149, 
		UTC_DATE=150, UTC_TIME=151, UTC_TIMESTAMP=152, VALUES=153, VIEW=154, WHEN=155, 
		WINDOW=156, XOR=157, MATERIALIZED=158, REFRESH=159;
	public static final int
		RULE_provider = 0, RULE_version = 1, RULE_fullFieldName = 2, RULE_functionCall = 3, 
		RULE_alias = 4, RULE_number = 5, RULE_groupByItem = 6, RULE_resourceOrSubQuery = 7, 
		RULE_resource = 8, RULE_multipartIdentifier = 9, RULE_pattern = 10, RULE_expression = 11, 
		RULE_providerMethodName = 12, RULE_parameterName = 13, RULE_parameterValue = 14, 
		RULE_parameterExpression = 15, RULE_queryHint = 16, RULE_fieldList = 17, 
		RULE_selectStatement = 18, RULE_awaitQueryHint = 19, RULE_viewName = 20;
	private static String[] makeRuleNames() {
		return new String[] {
			"provider", "version", "fullFieldName", "functionCall", "alias", "number", 
			"groupByItem", "resourceOrSubQuery", "resource", "multipartIdentifier", 
			"pattern", "expression", "providerMethodName", "parameterName", "parameterValue", 
			"parameterExpression", "queryHint", "fieldList", "selectStatement", "awaitQueryHint", 
			"viewName"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'provider'", "'version'", "'fullFieldName'", "'functionCall'", 
			"'alias'", "'number'", "'groupByItem'", "'resourceOrSubQuery'", "'resource'", 
			"'multipartIdentifier'", "'pattern'", "'expression'", "'providerMethodName'", 
			"'parameterName'", "'parameterValue'", "'parameterExpression'", "'queryHint'", 
			"'fieldList'", "'selectStatement'", "'awaitQueryHint'", "'viewName'", 
			"'EXEC'", "'AUTH'", "'LIST'", "'PULL'", "'LOGIN'", "'INTERACTIVE'", "'REVOKE'", 
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
			null, null, null, null, null, null, null, null, null, null, "EXEC", "AUTH", 
			"LIST", "PULL", "LOGIN", "INTERACTIVE", "REVOKE", "DESC", "METHODS", 
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
			"VIEW", "WHEN", "WINDOW", "XOR", "MATERIALIZED", "REFRESH"
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
	public String getGrammarFileName() { return "common.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public commonParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProviderContext extends ParserRuleContext {
		public ProviderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_provider; }
	}

	public final ProviderContext provider() throws RecognitionException {
		ProviderContext _localctx = new ProviderContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_provider);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
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

	public static class VersionContext extends ParserRuleContext {
		public VersionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_version; }
	}

	public final VersionContext version() throws RecognitionException {
		VersionContext _localctx = new VersionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_version);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
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
			setState(46);
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
			setState(48);
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
			setState(50);
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
			setState(52);
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
			setState(54);
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
			setState(56);
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

	public static class ResourceContext extends ParserRuleContext {
		public ResourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resource; }
	}

	public final ResourceContext resource() throws RecognitionException {
		ResourceContext _localctx = new ResourceContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_resource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
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

	public static class MultipartIdentifierContext extends ParserRuleContext {
		public MultipartIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multipartIdentifier; }
	}

	public final MultipartIdentifierContext multipartIdentifier() throws RecognitionException {
		MultipartIdentifierContext _localctx = new MultipartIdentifierContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_multipartIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60);
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

	public static class PatternContext extends ParserRuleContext {
		public PatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pattern; }
	}

	public final PatternContext pattern() throws RecognitionException {
		PatternContext _localctx = new PatternContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_pattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62);
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

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
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

	public static class ProviderMethodNameContext extends ParserRuleContext {
		public ProviderMethodNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_providerMethodName; }
	}

	public final ProviderMethodNameContext providerMethodName() throws RecognitionException {
		ProviderMethodNameContext _localctx = new ProviderMethodNameContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_providerMethodName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
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

	public static class ParameterNameContext extends ParserRuleContext {
		public ParameterNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterName; }
	}

	public final ParameterNameContext parameterName() throws RecognitionException {
		ParameterNameContext _localctx = new ParameterNameContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_parameterName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
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

	public static class ParameterValueContext extends ParserRuleContext {
		public ParameterValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterValue; }
	}

	public final ParameterValueContext parameterValue() throws RecognitionException {
		ParameterValueContext _localctx = new ParameterValueContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_parameterValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(70);
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

	public static class ParameterExpressionContext extends ParserRuleContext {
		public ParameterExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterExpression; }
	}

	public final ParameterExpressionContext parameterExpression() throws RecognitionException {
		ParameterExpressionContext _localctx = new ParameterExpressionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_parameterExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
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

	public static class QueryHintContext extends ParserRuleContext {
		public QueryHintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryHint; }
	}

	public final QueryHintContext queryHint() throws RecognitionException {
		QueryHintContext _localctx = new QueryHintContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_queryHint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
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

	public static class FieldListContext extends ParserRuleContext {
		public FieldListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldList; }
	}

	public final FieldListContext fieldList() throws RecognitionException {
		FieldListContext _localctx = new FieldListContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_fieldList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
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

	public static class SelectStatementContext extends ParserRuleContext {
		public SelectStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectStatement; }
	}

	public final SelectStatementContext selectStatement() throws RecognitionException {
		SelectStatementContext _localctx = new SelectStatementContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_selectStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
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
			setState(80);
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
			setState(82);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u00a1W\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3"+
		"\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3"+
		"\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3"+
		"\25\3\25\3\26\3\26\3\26\2\2\27\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36"+
		" \"$&(*\2\2\2A\2,\3\2\2\2\4.\3\2\2\2\6\60\3\2\2\2\b\62\3\2\2\2\n\64\3"+
		"\2\2\2\f\66\3\2\2\2\168\3\2\2\2\20:\3\2\2\2\22<\3\2\2\2\24>\3\2\2\2\26"+
		"@\3\2\2\2\30B\3\2\2\2\32D\3\2\2\2\34F\3\2\2\2\36H\3\2\2\2 J\3\2\2\2\""+
		"L\3\2\2\2$N\3\2\2\2&P\3\2\2\2(R\3\2\2\2*T\3\2\2\2,-\7\3\2\2-\3\3\2\2\2"+
		"./\7\4\2\2/\5\3\2\2\2\60\61\7\5\2\2\61\7\3\2\2\2\62\63\7\6\2\2\63\t\3"+
		"\2\2\2\64\65\7\7\2\2\65\13\3\2\2\2\66\67\7\b\2\2\67\r\3\2\2\289\7\t\2"+
		"\29\17\3\2\2\2:;\7\n\2\2;\21\3\2\2\2<=\7\13\2\2=\23\3\2\2\2>?\7\f\2\2"+
		"?\25\3\2\2\2@A\7\r\2\2A\27\3\2\2\2BC\7\16\2\2C\31\3\2\2\2DE\7\17\2\2E"+
		"\33\3\2\2\2FG\7\20\2\2G\35\3\2\2\2HI\7\21\2\2I\37\3\2\2\2JK\7\22\2\2K"+
		"!\3\2\2\2LM\7\23\2\2M#\3\2\2\2NO\7\24\2\2O%\3\2\2\2PQ\7\25\2\2Q\'\3\2"+
		"\2\2RS\7\26\2\2S)\3\2\2\2TU\7\27\2\2U+\3\2\2\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}