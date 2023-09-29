// Generated from c:\LocalGitRepos\stackql\stackql-artwork\antlr\AUTH.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AUTHParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, EXEC=21, AUTH=22, LIST=23, PULL=24, LOGIN=25, 
		INTERACTIVE=26, REVOKE=27, DESC=28, METHODS=29, DESCRIBE=30, EXPLAIN=31, 
		EXTENDED=32, LIMIT=33, ORDER=34, BY=35, GROUP=36, WITH=37, ROLLUP=38, 
		HAVING=39, IN=40, AS=41, USE=42, LIKE=43, FROM=44, SHOW=45, WHERE=46, 
		SERVICES=47, RESOURCES=48, PROVIDERS=49, FIELDS=50, SELECT=51, DISTINCT=52, 
		ADD=53, ARRAY=54, AND=55, ASC=56, AUTO_INCREMENT=57, BETWEEN=58, BINARY=59, 
		CASE=60, COLLATE=61, CONVERT=62, CREATE=63, CROSS=64, CUME_DIST=65, CURRENT_DATE=66, 
		CURRENT_TIME=67, CURRENT_TIMESTAMP=68, SUBSTR=69, SUBSTRING=70, DATABASE=71, 
		DATABASES=72, DEFAULT=73, DELETE=74, DENSE_RANK=75, DISTINCTROW=76, DIV=77, 
		DROP=78, ELSE=79, END=80, ESCAPE=81, EXISTS=82, FALSE=83, FIRST_VALUE=84, 
		FOR=85, FORCE=86, GROUPING=87, GROUPS=88, IF=89, IGNORE=90, INDEX=91, 
		INNER=92, INSERT=93, INTERVAL=94, INTO=95, IS=96, JOIN=97, JSON_TABLE=98, 
		KEY=99, LAG=100, LAST_VALUE=101, LATERAL=102, LEAD=103, LEFT=104, LOCALTIME=105, 
		LOCALTIMESTAMP=106, LOCK=107, MEMBER=108, MATCH=109, MAXVALUE=110, MOD=111, 
		NATURAL=112, NEXT=113, NOT=114, NTH_VALUE=115, NTILE=116, NULL=117, OF=118, 
		OFF=119, ON=120, OR=121, OUTER=122, OVER=123, PERCENT_RANK=124, RANK=125, 
		RECURSIVE=126, REGEXP=127, RENAME=128, REPLACE=129, REGISTRY=130, RIGHT=131, 
		ROW_NUMBER=132, SCHEMA=133, SEPARATOR=134, SET=135, STRAIGHT_JOIN=136, 
		SYSTEM=137, TABLE=138, THEN=139, TIMESTAMPADD=140, TIMESTAMPDIFF=141, 
		TO=142, TRUE=143, UNION=144, UNIQUE=145, UNLOCK=146, UPDATE=147, USING=148, 
		UTC_DATE=149, UTC_TIME=150, UTC_TIMESTAMP=151, VALUES=152, VIEW=153, WHEN=154, 
		WINDOW=155, XOR=156;
	public static final int
		RULE_authStatement = 0, RULE_provider = 1, RULE_version = 2, RULE_fullFieldName = 3, 
		RULE_functionCall = 4, RULE_alias = 5, RULE_number = 6, RULE_groupByItem = 7, 
		RULE_resourceOrSubQuery = 8, RULE_multipartIdentifier = 9, RULE_pattern = 10, 
		RULE_expression = 11, RULE_providerMethodName = 12, RULE_parameterName = 13, 
		RULE_parameterValue = 14, RULE_parameterExpression = 15, RULE_queryHint = 16, 
		RULE_fieldList = 17, RULE_selectStatement = 18, RULE_awaitQueryHint = 19;
	private static String[] makeRuleNames() {
		return new String[] {
			"authStatement", "provider", "version", "fullFieldName", "functionCall", 
			"alias", "number", "groupByItem", "resourceOrSubQuery", "multipartIdentifier", 
			"pattern", "expression", "providerMethodName", "parameterName", "parameterValue", 
			"parameterExpression", "queryHint", "fieldList", "selectStatement", "awaitQueryHint"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'provider'", "'version'", "'fullFieldName'", "'functionCall'", 
			"'alias'", "'number'", "'groupByItem'", "'resourceOrSubQuery'", "'multipartIdentifier'", 
			"'pattern'", "'expression'", "'providerMethodName'", "'parameterName'", 
			"'parameterValue'", "'parameterExpression'", "'queryHint'", "'fieldList'", 
			"'selectStatement'", "'awaitQueryHint'", "'EXEC'", "'AUTH'", "'LIST'", 
			"'PULL'", "'LOGIN'", "'INTERACTIVE'", "'REVOKE'", "'DESC'", "'METHODS'", 
			"'DESCRIBE'", "'EXPLAIN'", "'EXTENDED'", "'LIMIT'", "'ORDER'", "'BY'", 
			"'GROUP'", "'WITH'", "'ROLLUP'", "'HAVING'", "'IN'", "'AS'", "'USE'", 
			"'LIKE'", "'FROM'", "'SHOW'", "'WHERE'", "'SERVICES'", "'RESOURCES'", 
			"'PROVIDERS'", "'FIELDS'", "'SELECT'", "'DISTINCT'", "'ADD'", "'ARRAY '", 
			"'AND'", "'ASC'", "'AUTO_INCREMENT'", "'BETWEEN'", "'BINARY'", "'CASE'", 
			"'COLLATE'", "'CONVERT'", "'CREATE'", "'CROSS'", "'CUME_DIST'", "'CURRENT_DATE'", 
			"'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", "'SUBSTR'", "'SUBSTRING'", "'DATABASE'", 
			"'DATABASES'", "'DEFAULT'", "'DELETE'", "'DENSE_RANK'", "'DISTINCTROW'", 
			"'DIV'", "'DROP'", "'ELSE'", "'END'", "'ESCAPE'", "'EXISTS'", "'FALSE'", 
			"'FIRST_VALUE'", "'FOR'", "'FORCE'", "'GROUPING'", "'GROUPS'", "'IF'", 
			"'IGNORE'", "'INDEX'", "'INNER'", "'INSERT'", "'INTERVAL'", "'INTO'", 
			"'IS'", "'JOIN'", "'JSON_TABLE'", "'KEY'", "'LAG'", "'LAST_VALUE'", "'LATERAL'", 
			"'LEAD'", "'LEFT'", "'LOCALTIME'", "'LOCALTIMESTAMP'", "'LOCK'", "'MEMBER'", 
			"'MATCH'", "'MAXVALUE'", "'MOD'", "'NATURAL'", "'NEXT'", "'NOT'", "'NTH_VALUE'", 
			"'NTILE'", "'NULL'", "'OF'", "'OFF'", "'ON'", "'OR'", "'OUTER'", "'OVER'", 
			"'PERCENT_RANK'", "'RANK'", "'RECURSIVE'", "'REGEXP'", "'RENAME'", "'REPLACE'", 
			"'REGISTRY'", "'RIGHT'", "'ROW_NUMBER'", "'SCHEMA'", "'SEPARATOR'", "'SET'", 
			"'STRAIGHT_JOIN'", "'SYSTEM'", "'TABLE'", "'THEN'", "'TIMESTAMPADD'", 
			"'TIMESTAMPDIFF'", "'TO'", "'TRUE'", "'UNION'", "'UNIQUE'", "'UNLOCK'", 
			"'UPDATE'", "'USING'", "'UTC_DATE'", "'UTC_TIME'", "'UTC_TIMESTAMP'", 
			"'VALUES'", "'VIEW'", "'WHEN'", "'WINDOW'", "'XOR'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "EXEC", "AUTH", 
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
	}

	public final AuthStatementContext authStatement() throws RecognitionException {
		AuthStatementContext _localctx = new AuthStatementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_authStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			match(AUTH);
			setState(49);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LOGIN:
				{
				setState(41);
				match(LOGIN);
				{
				setState(43);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(42);
					provider();
					}
				}

				setState(46);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==INTERACTIVE) {
					{
					setState(45);
					match(INTERACTIVE);
					}
				}

				}
				}
				break;
			case REVOKE:
				{
				setState(48);
				match(REVOKE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(51);
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
			setState(53);
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
			setState(55);
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
			setState(57);
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
			setState(59);
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
			setState(61);
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
			setState(63);
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
			setState(65);
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
			setState(67);
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
			setState(69);
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
			setState(71);
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
			setState(73);
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
			setState(75);
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
			setState(77);
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
			setState(79);
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
			setState(81);
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
			setState(83);
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
			setState(85);
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
			setState(87);
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
			setState(89);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u009e^\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\3\2\5\2.\n\2\3\2\5\2\61\n\2\3\2"+
		"\5\2\64\n\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3"+
		"\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20"+
		"\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\25\2\2\26\2\4\6\b"+
		"\n\f\16\20\22\24\26\30\32\34\36 \"$&(\2\2\2L\2*\3\2\2\2\4\67\3\2\2\2\6"+
		"9\3\2\2\2\b;\3\2\2\2\n=\3\2\2\2\f?\3\2\2\2\16A\3\2\2\2\20C\3\2\2\2\22"+
		"E\3\2\2\2\24G\3\2\2\2\26I\3\2\2\2\30K\3\2\2\2\32M\3\2\2\2\34O\3\2\2\2"+
		"\36Q\3\2\2\2 S\3\2\2\2\"U\3\2\2\2$W\3\2\2\2&Y\3\2\2\2([\3\2\2\2*\63\7"+
		"\30\2\2+-\7\33\2\2,.\5\4\3\2-,\3\2\2\2-.\3\2\2\2.\60\3\2\2\2/\61\7\34"+
		"\2\2\60/\3\2\2\2\60\61\3\2\2\2\61\64\3\2\2\2\62\64\7\35\2\2\63+\3\2\2"+
		"\2\63\62\3\2\2\2\64\65\3\2\2\2\65\66\7\3\2\2\66\3\3\2\2\2\678\7\4\2\2"+
		"8\5\3\2\2\29:\7\5\2\2:\7\3\2\2\2;<\7\6\2\2<\t\3\2\2\2=>\7\7\2\2>\13\3"+
		"\2\2\2?@\7\b\2\2@\r\3\2\2\2AB\7\t\2\2B\17\3\2\2\2CD\7\n\2\2D\21\3\2\2"+
		"\2EF\7\13\2\2F\23\3\2\2\2GH\7\f\2\2H\25\3\2\2\2IJ\7\r\2\2J\27\3\2\2\2"+
		"KL\7\16\2\2L\31\3\2\2\2MN\7\17\2\2N\33\3\2\2\2OP\7\20\2\2P\35\3\2\2\2"+
		"QR\7\21\2\2R\37\3\2\2\2ST\7\22\2\2T!\3\2\2\2UV\7\23\2\2V#\3\2\2\2WX\7"+
		"\24\2\2X%\3\2\2\2YZ\7\25\2\2Z\'\3\2\2\2[\\\7\26\2\2\\)\3\2\2\2\5-\60\63";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}