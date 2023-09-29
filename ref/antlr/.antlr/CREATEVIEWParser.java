// Generated from c:\LocalGitRepos\stackql\stackql.io\ref\antlr\CREATEVIEW.g4 by ANTLR 4.8
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CREATEVIEWParser extends Parser {
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
		WINDOW=157, XOR=158, MATERIALIZED=159, REFRESH=160;
	public static final int
		RULE_createViewStatement = 0, RULE_provider = 1, RULE_version = 2, RULE_fullFieldName = 3, 
		RULE_functionCall = 4, RULE_alias = 5, RULE_number = 6, RULE_groupByItem = 7, 
		RULE_resourceOrSubQuery = 8, RULE_resource = 9, RULE_multipartIdentifier = 10, 
		RULE_pattern = 11, RULE_expression = 12, RULE_providerMethodName = 13, 
		RULE_parameterName = 14, RULE_parameterValue = 15, RULE_parameterExpression = 16, 
		RULE_queryHint = 17, RULE_fieldList = 18, RULE_selectStatement = 19, RULE_awaitQueryHint = 20, 
		RULE_viewName = 21;
	private static String[] makeRuleNames() {
		return new String[] {
			"createViewStatement", "provider", "version", "fullFieldName", "functionCall", 
			"alias", "number", "groupByItem", "resourceOrSubQuery", "resource", "multipartIdentifier", 
			"pattern", "expression", "providerMethodName", "parameterName", "parameterValue", 
			"parameterExpression", "queryHint", "fieldList", "selectStatement", "awaitQueryHint", 
			"viewName"
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
	public String getGrammarFileName() { return "CREATEVIEW.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CREATEVIEWParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class CreateViewStatementContext extends ParserRuleContext {
		public TerminalNode CREATE() { return getToken(CREATEVIEWParser.CREATE, 0); }
		public TerminalNode VIEW() { return getToken(CREATEVIEWParser.VIEW, 0); }
		public ViewNameContext viewName() {
			return getRuleContext(ViewNameContext.class,0);
		}
		public TerminalNode AS() { return getToken(CREATEVIEWParser.AS, 0); }
		public List<SelectStatementContext> selectStatement() {
			return getRuleContexts(SelectStatementContext.class);
		}
		public SelectStatementContext selectStatement(int i) {
			return getRuleContext(SelectStatementContext.class,i);
		}
		public TerminalNode JOIN() { return getToken(CREATEVIEWParser.JOIN, 0); }
		public TerminalNode UNION() { return getToken(CREATEVIEWParser.UNION, 0); }
		public TerminalNode OR() { return getToken(CREATEVIEWParser.OR, 0); }
		public TerminalNode REPLACE() { return getToken(CREATEVIEWParser.REPLACE, 0); }
		public TerminalNode MATERIALIZED() { return getToken(CREATEVIEWParser.MATERIALIZED, 0); }
		public TerminalNode OUTER() { return getToken(CREATEVIEWParser.OUTER, 0); }
		public CreateViewStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createViewStatement; }
	}

	public final CreateViewStatementContext createViewStatement() throws RecognitionException {
		CreateViewStatementContext _localctx = new CreateViewStatementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_createViewStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
			match(CREATE);
			setState(48);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OR:
				{
				{
				setState(45);
				match(OR);
				setState(46);
				match(REPLACE);
				}
				}
				break;
			case MATERIALIZED:
				{
				{
				setState(47);
				match(MATERIALIZED);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(50);
			match(VIEW);
			setState(51);
			viewName();
			setState(52);
			match(AS);
			setState(53);
			selectStatement();
			setState(61);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case JOIN:
			case OUTER:
				{
				setState(55);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OUTER) {
					{
					setState(54);
					match(OUTER);
					}
				}

				setState(57);
				match(JOIN);
				setState(58);
				selectStatement();
				}
				break;
			case UNION:
				{
				setState(59);
				match(UNION);
				setState(60);
				selectStatement();
				}
				break;
			case T__0:
				break;
			default:
				break;
			}
			setState(63);
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
			setState(65);
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
			setState(67);
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
			setState(69);
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
			setState(71);
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
			setState(73);
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
			setState(75);
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
			setState(77);
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
			setState(79);
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
			setState(81);
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
		enterRule(_localctx, 20, RULE_multipartIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
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
		enterRule(_localctx, 22, RULE_pattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
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
		enterRule(_localctx, 24, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
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
		enterRule(_localctx, 26, RULE_providerMethodName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
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
			setState(91);
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
			setState(93);
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
			setState(95);
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
			setState(97);
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
			setState(99);
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
			setState(101);
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

	public static class AwaitQueryHintContext extends ParserRuleContext {
		public AwaitQueryHintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_awaitQueryHint; }
	}

	public final AwaitQueryHintContext awaitQueryHint() throws RecognitionException {
		AwaitQueryHintContext _localctx = new AwaitQueryHintContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_awaitQueryHint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
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

	public static class ViewNameContext extends ParserRuleContext {
		public ViewNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_viewName; }
	}

	public final ViewNameContext viewName() throws RecognitionException {
		ViewNameContext _localctx = new ViewNameContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_viewName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u00a2n\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\3\2\3\2\5\2"+
		"\63\n\2\3\2\3\2\3\2\3\2\3\2\5\2:\n\2\3\2\3\2\3\2\3\2\5\2@\n\2\3\2\3\2"+
		"\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13"+
		"\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22"+
		"\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\27\2\2\30\2\4\6\b"+
		"\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,\2\2\2[\2.\3\2\2\2\4C\3\2\2\2\6"+
		"E\3\2\2\2\bG\3\2\2\2\nI\3\2\2\2\fK\3\2\2\2\16M\3\2\2\2\20O\3\2\2\2\22"+
		"Q\3\2\2\2\24S\3\2\2\2\26U\3\2\2\2\30W\3\2\2\2\32Y\3\2\2\2\34[\3\2\2\2"+
		"\36]\3\2\2\2 _\3\2\2\2\"a\3\2\2\2$c\3\2\2\2&e\3\2\2\2(g\3\2\2\2*i\3\2"+
		"\2\2,k\3\2\2\2.\62\7C\2\2/\60\7}\2\2\60\63\7\u0085\2\2\61\63\7\u00a1\2"+
		"\2\62/\3\2\2\2\62\61\3\2\2\2\63\64\3\2\2\2\64\65\7\u009d\2\2\65\66\5,"+
		"\27\2\66\67\7-\2\2\67?\5(\25\28:\7~\2\298\3\2\2\29:\3\2\2\2:;\3\2\2\2"+
		";<\7e\2\2<@\5(\25\2=>\7\u0094\2\2>@\5(\25\2?9\3\2\2\2?=\3\2\2\2?@\3\2"+
		"\2\2@A\3\2\2\2AB\7\3\2\2B\3\3\2\2\2CD\7\4\2\2D\5\3\2\2\2EF\7\5\2\2F\7"+
		"\3\2\2\2GH\7\6\2\2H\t\3\2\2\2IJ\7\7\2\2J\13\3\2\2\2KL\7\b\2\2L\r\3\2\2"+
		"\2MN\7\t\2\2N\17\3\2\2\2OP\7\n\2\2P\21\3\2\2\2QR\7\13\2\2R\23\3\2\2\2"+
		"ST\7\f\2\2T\25\3\2\2\2UV\7\r\2\2V\27\3\2\2\2WX\7\16\2\2X\31\3\2\2\2YZ"+
		"\7\17\2\2Z\33\3\2\2\2[\\\7\20\2\2\\\35\3\2\2\2]^\7\21\2\2^\37\3\2\2\2"+
		"_`\7\22\2\2`!\3\2\2\2ab\7\23\2\2b#\3\2\2\2cd\7\24\2\2d%\3\2\2\2ef\7\25"+
		"\2\2f\'\3\2\2\2gh\7\26\2\2h)\3\2\2\2ij\7\27\2\2j+\3\2\2\2kl\7\30\2\2l"+
		"-\3\2\2\2\5\629?";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}