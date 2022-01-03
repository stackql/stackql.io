// Generated from c:\Users\javen\OneDrive\GitRepositories\infraql\docs.infraql.io\artwork\antlr\ABS.g4 by ANTLR 4.8
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ABSParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, ABS=20, EXEC=21, AUTH=22, LOGIN=23, INTERACTIVE=24, 
		REVOKE=25, DESC=26, METHODS=27, DESCRIBE=28, EXPLAIN=29, EXTENDED=30, 
		LIMIT=31, ORDER=32, BY=33, GROUP=34, WITH=35, ROLLUP=36, HAVING=37, IN=38, 
		AS=39, USE=40, LIKE=41, FROM=42, SHOW=43, WHERE=44, SERVICES=45, RESOURCES=46, 
		PROVIDERS=47, FIELDS=48, SELECT=49, DISTINCT=50, ADD=51, ARRAY=52, AND=53, 
		ASC=54, AUTO_INCREMENT=55, BETWEEN=56, BINARY=57, CASE=58, COLLATE=59, 
		CONVERT=60, CREATE=61, CROSS=62, CUME_DIST=63, CURRENT_DATE=64, CURRENT_TIME=65, 
		CURRENT_TIMESTAMP=66, SUBSTR=67, SUBSTRING=68, DATABASE=69, DATABASES=70, 
		DEFAULT=71, DELETE=72, DENSE_RANK=73, DISTINCTROW=74, DIV=75, DROP=76, 
		ELSE=77, END=78, ESCAPE=79, EXISTS=80, FALSE=81, FIRST_VALUE=82, FOR=83, 
		FORCE=84, GROUPING=85, GROUPS=86, IF=87, IGNORE=88, INDEX=89, INNER=90, 
		INSERT=91, INTERVAL=92, INTO=93, IS=94, JOIN=95, JSON_TABLE=96, KEY=97, 
		LAG=98, LAST_VALUE=99, LATERAL=100, LEAD=101, LEFT=102, LOCALTIME=103, 
		LOCALTIMESTAMP=104, LOCK=105, MEMBER=106, MATCH=107, MAXVALUE=108, MOD=109, 
		NATURAL=110, NEXT=111, NOT=112, NTH_VALUE=113, NTILE=114, NULL=115, OF=116, 
		OFF=117, ON=118, OR=119, OUTER=120, OVER=121, PERCENT_RANK=122, RANK=123, 
		RECURSIVE=124, REGEXP=125, RENAME=126, REPLACE=127, RIGHT=128, ROW_NUMBER=129, 
		SCHEMA=130, SEPARATOR=131, SET=132, STRAIGHT_JOIN=133, SYSTEM=134, TABLE=135, 
		THEN=136, TIMESTAMPADD=137, TIMESTAMPDIFF=138, TO=139, TRUE=140, UNION=141, 
		UNIQUE=142, UNLOCK=143, UPDATE=144, USING=145, UTC_DATE=146, UTC_TIME=147, 
		UTC_TIMESTAMP=148, VALUES=149, WHEN=150, WINDOW=151, XOR=152;
	public static final int
		RULE_absFunction = 0, RULE_provider = 1, RULE_fullFieldName = 2, RULE_functionCall = 3, 
		RULE_alias = 4, RULE_number = 5, RULE_groupByItem = 6, RULE_resourceOrSubQuery = 7, 
		RULE_multipartIdentifier = 8, RULE_pattern = 9, RULE_expression = 10, 
		RULE_providerMethodName = 11, RULE_parameterName = 12, RULE_parameterValue = 13, 
		RULE_parameterExpression = 14, RULE_queryHint = 15, RULE_fieldList = 16, 
		RULE_selectStatement = 17, RULE_awaitQueryHint = 18;
	private static String[] makeRuleNames() {
		return new String[] {
			"absFunction", "provider", "fullFieldName", "functionCall", "alias", 
			"number", "groupByItem", "resourceOrSubQuery", "multipartIdentifier", 
			"pattern", "expression", "providerMethodName", "parameterName", "parameterValue", 
			"parameterExpression", "queryHint", "fieldList", "selectStatement", "awaitQueryHint"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'(x , y )'", "'provider'", "'fullFieldName'", "'functionCall'", 
			"'alias'", "'number'", "'groupByItem'", "'resourceOrSubQuery'", "'multipartIdentifier'", 
			"'pattern'", "'expression'", "'providerMethodName'", "'parameterName'", 
			"'parameterValue'", "'parameterExpression'", "'queryHint'", "'fieldList'", 
			"'selectStatement'", "'awaitQueryHint'", "'ABS'", "'EXEC'", "'AUTH'", 
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
			null, null, null, null, null, null, null, null, "ABS", "EXEC", "AUTH", 
			"LOGIN", "INTERACTIVE", "REVOKE", "DESC", "METHODS", "DESCRIBE", "EXPLAIN", 
			"EXTENDED", "LIMIT", "ORDER", "BY", "GROUP", "WITH", "ROLLUP", "HAVING", 
			"IN", "AS", "USE", "LIKE", "FROM", "SHOW", "WHERE", "SERVICES", "RESOURCES", 
			"PROVIDERS", "FIELDS", "SELECT", "DISTINCT", "ADD", "ARRAY", "AND", "ASC", 
			"AUTO_INCREMENT", "BETWEEN", "BINARY", "CASE", "COLLATE", "CONVERT", 
			"CREATE", "CROSS", "CUME_DIST", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", 
			"SUBSTR", "SUBSTRING", "DATABASE", "DATABASES", "DEFAULT", "DELETE", 
			"DENSE_RANK", "DISTINCTROW", "DIV", "DROP", "ELSE", "END", "ESCAPE", 
			"EXISTS", "FALSE", "FIRST_VALUE", "FOR", "FORCE", "GROUPING", "GROUPS", 
			"IF", "IGNORE", "INDEX", "INNER", "INSERT", "INTERVAL", "INTO", "IS", 
			"JOIN", "JSON_TABLE", "KEY", "LAG", "LAST_VALUE", "LATERAL", "LEAD", 
			"LEFT", "LOCALTIME", "LOCALTIMESTAMP", "LOCK", "MEMBER", "MATCH", "MAXVALUE", 
			"MOD", "NATURAL", "NEXT", "NOT", "NTH_VALUE", "NTILE", "NULL", "OF", 
			"OFF", "ON", "OR", "OUTER", "OVER", "PERCENT_RANK", "RANK", "RECURSIVE", 
			"REGEXP", "RENAME", "REPLACE", "RIGHT", "ROW_NUMBER", "SCHEMA", "SEPARATOR", 
			"SET", "STRAIGHT_JOIN", "SYSTEM", "TABLE", "THEN", "TIMESTAMPADD", "TIMESTAMPDIFF", 
			"TO", "TRUE", "UNION", "UNIQUE", "UNLOCK", "UPDATE", "USING", "UTC_DATE", 
			"UTC_TIME", "UTC_TIMESTAMP", "VALUES", "WHEN", "WINDOW", "XOR"
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
	public String getGrammarFileName() { return "ABS.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ABSParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class AbsFunctionContext extends ParserRuleContext {
		public TerminalNode ABS() { return getToken(ABSParser.ABS, 0); }
		public AbsFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_absFunction; }
	}

	public final AbsFunctionContext absFunction() throws RecognitionException {
		AbsFunctionContext _localctx = new AbsFunctionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_absFunction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			match(ABS);
			setState(39);
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
			setState(41);
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
			setState(43);
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
			setState(45);
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
			setState(47);
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
			setState(49);
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
			setState(51);
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
			setState(53);
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
			setState(55);
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
			setState(57);
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
			setState(59);
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
			setState(61);
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

	public static class ParameterNameContext extends ParserRuleContext {
		public ParameterNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterName; }
	}

	public final ParameterNameContext parameterName() throws RecognitionException {
		ParameterNameContext _localctx = new ParameterNameContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_parameterName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
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

	public static class ParameterValueContext extends ParserRuleContext {
		public ParameterValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterValue; }
	}

	public final ParameterValueContext parameterValue() throws RecognitionException {
		ParameterValueContext _localctx = new ParameterValueContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_parameterValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
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

	public static class ParameterExpressionContext extends ParserRuleContext {
		public ParameterExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterExpression; }
	}

	public final ParameterExpressionContext parameterExpression() throws RecognitionException {
		ParameterExpressionContext _localctx = new ParameterExpressionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_parameterExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
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

	public static class QueryHintContext extends ParserRuleContext {
		public QueryHintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryHint; }
	}

	public final QueryHintContext queryHint() throws RecognitionException {
		QueryHintContext _localctx = new QueryHintContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_queryHint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
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

	public static class FieldListContext extends ParserRuleContext {
		public FieldListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldList; }
	}

	public final FieldListContext fieldList() throws RecognitionException {
		FieldListContext _localctx = new FieldListContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_fieldList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
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

	public static class SelectStatementContext extends ParserRuleContext {
		public SelectStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectStatement; }
	}

	public final SelectStatementContext selectStatement() throws RecognitionException {
		SelectStatementContext _localctx = new SelectStatementContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_selectStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
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

	public static class AwaitQueryHintContext extends ParserRuleContext {
		public AwaitQueryHintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_awaitQueryHint; }
	}

	public final AwaitQueryHintContext awaitQueryHint() throws RecognitionException {
		AwaitQueryHintContext _localctx = new AwaitQueryHintContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_awaitQueryHint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u009aP\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3"+
		"\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3"+
		"\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\24\2\2\25\2\4"+
		"\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&\2\2\2<\2(\3\2\2\2\4+\3\2\2\2"+
		"\6-\3\2\2\2\b/\3\2\2\2\n\61\3\2\2\2\f\63\3\2\2\2\16\65\3\2\2\2\20\67\3"+
		"\2\2\2\229\3\2\2\2\24;\3\2\2\2\26=\3\2\2\2\30?\3\2\2\2\32A\3\2\2\2\34"+
		"C\3\2\2\2\36E\3\2\2\2 G\3\2\2\2\"I\3\2\2\2$K\3\2\2\2&M\3\2\2\2()\7\26"+
		"\2\2)*\7\3\2\2*\3\3\2\2\2+,\7\4\2\2,\5\3\2\2\2-.\7\5\2\2.\7\3\2\2\2/\60"+
		"\7\6\2\2\60\t\3\2\2\2\61\62\7\7\2\2\62\13\3\2\2\2\63\64\7\b\2\2\64\r\3"+
		"\2\2\2\65\66\7\t\2\2\66\17\3\2\2\2\678\7\n\2\28\21\3\2\2\29:\7\13\2\2"+
		":\23\3\2\2\2;<\7\f\2\2<\25\3\2\2\2=>\7\r\2\2>\27\3\2\2\2?@\7\16\2\2@\31"+
		"\3\2\2\2AB\7\17\2\2B\33\3\2\2\2CD\7\20\2\2D\35\3\2\2\2EF\7\21\2\2F\37"+
		"\3\2\2\2GH\7\22\2\2H!\3\2\2\2IJ\7\23\2\2J#\3\2\2\2KL\7\24\2\2L%\3\2\2"+
		"\2MN\7\25\2\2N\'\3\2\2\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}