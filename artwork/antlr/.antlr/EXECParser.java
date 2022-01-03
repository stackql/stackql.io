// Generated from c:\Users\javen\OneDrive\GitRepositories\infraql\infraql-help\syntax_trees\EXEC.g4 by ANTLR 4.8
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class EXECParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		EXEC=18, AUTH=19, LOGIN=20, INTERACTIVE=21, REVOKE=22, DESC=23, METHODS=24, 
		DESCRIBE=25, EXPLAIN=26, EXTENDED=27, LIMIT=28, ORDER=29, BY=30, GROUP=31, 
		WITH=32, ROLLUP=33, HAVING=34, IN=35, AS=36, USE=37, LIKE=38, FROM=39, 
		SHOW=40, WHERE=41, SERVICES=42, RESOURCES=43, PROVIDERS=44, FIELDS=45, 
		SELECT=46, DISTINCT=47, ADD=48, ARRAY=49, AND=50, ASC=51, AUTO_INCREMENT=52, 
		BETWEEN=53, BINARY=54, CASE=55, COLLATE=56, CONVERT=57, CREATE=58, CROSS=59, 
		CUME_DIST=60, CURRENT_DATE=61, CURRENT_TIME=62, CURRENT_TIMESTAMP=63, 
		SUBSTR=64, SUBSTRING=65, DATABASE=66, DATABASES=67, DEFAULT=68, DELETE=69, 
		DENSE_RANK=70, DISTINCTROW=71, DIV=72, DROP=73, ELSE=74, END=75, ESCAPE=76, 
		EXISTS=77, FALSE=78, FIRST_VALUE=79, FOR=80, FORCE=81, GROUPING=82, GROUPS=83, 
		IF=84, IGNORE=85, INDEX=86, INNER=87, INSERT=88, INTERVAL=89, INTO=90, 
		IS=91, JOIN=92, JSON_TABLE=93, KEY=94, LAG=95, LAST_VALUE=96, LATERAL=97, 
		LEAD=98, LEFT=99, LOCALTIME=100, LOCALTIMESTAMP=101, LOCK=102, MEMBER=103, 
		MATCH=104, MAXVALUE=105, MOD=106, NATURAL=107, NEXT=108, NOT=109, NTH_VALUE=110, 
		NTILE=111, NULL=112, OF=113, OFF=114, ON=115, OR=116, OUTER=117, OVER=118, 
		PERCENT_RANK=119, RANK=120, RECURSIVE=121, REGEXP=122, RENAME=123, REPLACE=124, 
		RIGHT=125, ROW_NUMBER=126, SCHEMA=127, SEPARATOR=128, SET=129, STRAIGHT_JOIN=130, 
		SYSTEM=131, TABLE=132, THEN=133, TIMESTAMPADD=134, TIMESTAMPDIFF=135, 
		TO=136, TRUE=137, UNION=138, UNIQUE=139, UNLOCK=140, UPDATE=141, USING=142, 
		UTC_DATE=143, UTC_TIME=144, UTC_TIMESTAMP=145, VALUES=146, WHEN=147, WINDOW=148, 
		XOR=149;
	public static final int
		RULE_execStatement = 0, RULE_provider = 1, RULE_fullFieldName = 2, RULE_functionCall = 3, 
		RULE_alias = 4, RULE_number = 5, RULE_groupByItem = 6, RULE_resourceOrSubQuery = 7, 
		RULE_multipartIdentifier = 8, RULE_pattern = 9, RULE_expression = 10, 
		RULE_providerMethodName = 11, RULE_parameterName = 12, RULE_parameterValue = 13, 
		RULE_parameterExpression = 14, RULE_queryHint = 15;
	private static String[] makeRuleNames() {
		return new String[] {
			"execStatement", "provider", "fullFieldName", "functionCall", "alias", 
			"number", "groupByItem", "resourceOrSubQuery", "multipartIdentifier", 
			"pattern", "expression", "providerMethodName", "parameterName", "parameterValue", 
			"parameterExpression", "queryHint"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "';'", "'provider'", "'fullFieldName'", "'functionCall'", 
			"'alias'", "'number'", "'groupByItem'", "'resourceOrSubQuery'", "'multipartIdentifier'", 
			"'pattern'", "'expression'", "'providerMethodName'", "'parameterName'", 
			"'parameterValue'", "'parameterExpression'", "'queryHint'", "'EXEC'", 
			"'AUTH'", "'LOGIN'", "'INTERACTIVE'", "'REVOKE'", "'DESC'", "'METHODS'", 
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
			"'RIGHT'", "'ROW_NUMBER'", "'SCHEMA'", "'SEPARATOR'", "'SET'", "'STRAIGHT_JOIN'", 
			"'SYSTEM'", "'TABLE'", "'THEN'", "'TIMESTAMPADD'", "'TIMESTAMPDIFF'", 
			"'TO'", "'TRUE'", "'UNION'", "'UNIQUE'", "'UNLOCK'", "'UPDATE'", "'USING'", 
			"'UTC_DATE'", "'UTC_TIME'", "'UTC_TIMESTAMP'", "'VALUES'", "'WHEN'", 
			"'WINDOW'", "'XOR'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, "EXEC", "AUTH", "LOGIN", "INTERACTIVE", 
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
	public String getGrammarFileName() { return "EXEC.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public EXECParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ExecStatementContext extends ParserRuleContext {
		public TerminalNode EXEC() { return getToken(EXECParser.EXEC, 0); }
		public MultipartIdentifierContext multipartIdentifier() {
			return getRuleContext(MultipartIdentifierContext.class,0);
		}
		public ProviderMethodNameContext providerMethodName() {
			return getRuleContext(ProviderMethodNameContext.class,0);
		}
		public List<ParameterExpressionContext> parameterExpression() {
			return getRuleContexts(ParameterExpressionContext.class);
		}
		public ParameterExpressionContext parameterExpression(int i) {
			return getRuleContext(ParameterExpressionContext.class,i);
		}
		public QueryHintContext queryHint() {
			return getRuleContext(QueryHintContext.class,0);
		}
		public ExecStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_execStatement; }
	}

	public final ExecStatementContext execStatement() throws RecognitionException {
		ExecStatementContext _localctx = new ExecStatementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_execStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(32);
			match(EXEC);
			setState(34);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__16) {
				{
				setState(33);
				queryHint();
				}
			}

			setState(36);
			multipartIdentifier();
			setState(37);
			match(T__0);
			setState(38);
			providerMethodName();
			setState(39);
			parameterExpression();
			setState(43);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__15) {
				{
				{
				setState(40);
				parameterExpression();
				}
				}
				setState(45);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(46);
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
			setState(48);
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
		enterRule(_localctx, 4, RULE_fullFieldName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
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
		enterRule(_localctx, 6, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
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
		enterRule(_localctx, 8, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
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
		enterRule(_localctx, 10, RULE_number);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
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
		enterRule(_localctx, 12, RULE_groupByItem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
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
		enterRule(_localctx, 14, RULE_resourceOrSubQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60);
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
		enterRule(_localctx, 16, RULE_multipartIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62);
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
		enterRule(_localctx, 18, RULE_pattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
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
		enterRule(_localctx, 20, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
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
		enterRule(_localctx, 22, RULE_providerMethodName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
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
		enterRule(_localctx, 24, RULE_parameterName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(70);
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
		enterRule(_localctx, 26, RULE_parameterValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
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
		enterRule(_localctx, 28, RULE_parameterExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
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
		enterRule(_localctx, 30, RULE_queryHint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0097Q\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2\5"+
		"\2%\n\2\3\2\3\2\3\2\3\2\3\2\7\2,\n\2\f\2\16\2/\13\2\3\2\3\2\3\3\3\3\3"+
		"\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3"+
		"\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\21\2\2\22\2\4\6\b"+
		"\n\f\16\20\22\24\26\30\32\34\36 \2\2\2B\2\"\3\2\2\2\4\62\3\2\2\2\6\64"+
		"\3\2\2\2\b\66\3\2\2\2\n8\3\2\2\2\f:\3\2\2\2\16<\3\2\2\2\20>\3\2\2\2\22"+
		"@\3\2\2\2\24B\3\2\2\2\26D\3\2\2\2\30F\3\2\2\2\32H\3\2\2\2\34J\3\2\2\2"+
		"\36L\3\2\2\2 N\3\2\2\2\"$\7\24\2\2#%\5 \21\2$#\3\2\2\2$%\3\2\2\2%&\3\2"+
		"\2\2&\'\5\22\n\2\'(\7\3\2\2()\5\30\r\2)-\5\36\20\2*,\5\36\20\2+*\3\2\2"+
		"\2,/\3\2\2\2-+\3\2\2\2-.\3\2\2\2.\60\3\2\2\2/-\3\2\2\2\60\61\7\4\2\2\61"+
		"\3\3\2\2\2\62\63\7\5\2\2\63\5\3\2\2\2\64\65\7\6\2\2\65\7\3\2\2\2\66\67"+
		"\7\7\2\2\67\t\3\2\2\289\7\b\2\29\13\3\2\2\2:;\7\t\2\2;\r\3\2\2\2<=\7\n"+
		"\2\2=\17\3\2\2\2>?\7\13\2\2?\21\3\2\2\2@A\7\f\2\2A\23\3\2\2\2BC\7\r\2"+
		"\2C\25\3\2\2\2DE\7\16\2\2E\27\3\2\2\2FG\7\17\2\2G\31\3\2\2\2HI\7\20\2"+
		"\2I\33\3\2\2\2JK\7\21\2\2K\35\3\2\2\2LM\7\22\2\2M\37\3\2\2\2NO\7\23\2"+
		"\2O!\3\2\2\2\4$-";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}