// Generated from c:\Users\javen\OneDrive\GitRepositories\infraql\infraql-help\syntax_trees\DELETE.g4 by ANTLR 4.8
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DELETEParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, EXEC=17, 
		AUTH=18, LOGIN=19, INTERACTIVE=20, REVOKE=21, DESC=22, METHODS=23, DESCRIBE=24, 
		EXPLAIN=25, EXTENDED=26, LIMIT=27, ORDER=28, BY=29, GROUP=30, WITH=31, 
		ROLLUP=32, HAVING=33, IN=34, AS=35, USE=36, LIKE=37, FROM=38, SHOW=39, 
		WHERE=40, SERVICES=41, RESOURCES=42, PROVIDERS=43, FIELDS=44, SELECT=45, 
		DISTINCT=46, ADD=47, ARRAY=48, AND=49, ASC=50, AUTO_INCREMENT=51, BETWEEN=52, 
		BINARY=53, CASE=54, COLLATE=55, CONVERT=56, CREATE=57, CROSS=58, CUME_DIST=59, 
		CURRENT_DATE=60, CURRENT_TIME=61, CURRENT_TIMESTAMP=62, SUBSTR=63, SUBSTRING=64, 
		DATABASE=65, DATABASES=66, DEFAULT=67, DELETE=68, DENSE_RANK=69, DISTINCTROW=70, 
		DIV=71, DROP=72, ELSE=73, END=74, ESCAPE=75, EXISTS=76, FALSE=77, FIRST_VALUE=78, 
		FOR=79, FORCE=80, GROUPING=81, GROUPS=82, IF=83, IGNORE=84, INDEX=85, 
		INNER=86, INSERT=87, INTERVAL=88, INTO=89, IS=90, JOIN=91, JSON_TABLE=92, 
		KEY=93, LAG=94, LAST_VALUE=95, LATERAL=96, LEAD=97, LEFT=98, LOCALTIME=99, 
		LOCALTIMESTAMP=100, LOCK=101, MEMBER=102, MATCH=103, MAXVALUE=104, MOD=105, 
		NATURAL=106, NEXT=107, NOT=108, NTH_VALUE=109, NTILE=110, NULL=111, OF=112, 
		OFF=113, ON=114, OR=115, OUTER=116, OVER=117, PERCENT_RANK=118, RANK=119, 
		RECURSIVE=120, REGEXP=121, RENAME=122, REPLACE=123, RIGHT=124, ROW_NUMBER=125, 
		SCHEMA=126, SEPARATOR=127, SET=128, STRAIGHT_JOIN=129, SYSTEM=130, TABLE=131, 
		THEN=132, TIMESTAMPADD=133, TIMESTAMPDIFF=134, TO=135, TRUE=136, UNION=137, 
		UNIQUE=138, UNLOCK=139, UPDATE=140, USING=141, UTC_DATE=142, UTC_TIME=143, 
		UTC_TIMESTAMP=144, VALUES=145, WHEN=146, WINDOW=147, XOR=148;
	public static final int
		RULE_deleteStatement = 0, RULE_provider = 1, RULE_fullFieldName = 2, RULE_functionCall = 3, 
		RULE_alias = 4, RULE_number = 5, RULE_groupByItem = 6, RULE_resourceOrSubQuery = 7, 
		RULE_multipartIdentifier = 8, RULE_pattern = 9, RULE_expression = 10, 
		RULE_providerMethodName = 11, RULE_parameterName = 12, RULE_parameterValue = 13, 
		RULE_parameterExpression = 14, RULE_queryHint = 15;
	private static String[] makeRuleNames() {
		return new String[] {
			"deleteStatement", "provider", "fullFieldName", "functionCall", "alias", 
			"number", "groupByItem", "resourceOrSubQuery", "multipartIdentifier", 
			"pattern", "expression", "providerMethodName", "parameterName", "parameterValue", 
			"parameterExpression", "queryHint"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'provider'", "'fullFieldName'", "'functionCall'", "'alias'", 
			"'number'", "'groupByItem'", "'resourceOrSubQuery'", "'multipartIdentifier'", 
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
			null, null, null, null, null, "EXEC", "AUTH", "LOGIN", "INTERACTIVE", 
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
	public String getGrammarFileName() { return "DELETE.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public DELETEParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class DeleteStatementContext extends ParserRuleContext {
		public TerminalNode DELETE() { return getToken(DELETEParser.DELETE, 0); }
		public TerminalNode FROM() { return getToken(DELETEParser.FROM, 0); }
		public MultipartIdentifierContext multipartIdentifier() {
			return getRuleContext(MultipartIdentifierContext.class,0);
		}
		public TerminalNode WHERE() { return getToken(DELETEParser.WHERE, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public QueryHintContext queryHint() {
			return getRuleContext(QueryHintContext.class,0);
		}
		public DeleteStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteStatement; }
	}

	public final DeleteStatementContext deleteStatement() throws RecognitionException {
		DeleteStatementContext _localctx = new DeleteStatementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_deleteStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(32);
			match(DELETE);
			setState(34);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__15) {
				{
				setState(33);
				queryHint();
				}
			}

			setState(36);
			match(FROM);
			setState(37);
			multipartIdentifier();
			setState(38);
			match(WHERE);
			setState(39);
			expression();
			setState(41);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(40);
				expression();
				}
			}

			setState(43);
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
			setState(45);
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
			setState(47);
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
			setState(49);
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
			setState(51);
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
			setState(53);
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
			setState(55);
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
			setState(57);
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
			setState(59);
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
			setState(61);
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
			setState(63);
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
			setState(65);
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
			setState(67);
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
			setState(69);
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
			setState(71);
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
			setState(73);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0096N\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2\5"+
		"\2%\n\2\3\2\3\2\3\2\3\2\3\2\5\2,\n\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3"+
		"\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16"+
		"\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\21\2\2\22\2\4\6\b\n\f\16\20\22\24"+
		"\26\30\32\34\36 \2\2\2?\2\"\3\2\2\2\4/\3\2\2\2\6\61\3\2\2\2\b\63\3\2\2"+
		"\2\n\65\3\2\2\2\f\67\3\2\2\2\169\3\2\2\2\20;\3\2\2\2\22=\3\2\2\2\24?\3"+
		"\2\2\2\26A\3\2\2\2\30C\3\2\2\2\32E\3\2\2\2\34G\3\2\2\2\36I\3\2\2\2 K\3"+
		"\2\2\2\"$\7F\2\2#%\5 \21\2$#\3\2\2\2$%\3\2\2\2%&\3\2\2\2&\'\7(\2\2\'("+
		"\5\22\n\2()\7*\2\2)+\5\26\f\2*,\5\26\f\2+*\3\2\2\2+,\3\2\2\2,-\3\2\2\2"+
		"-.\7\3\2\2.\3\3\2\2\2/\60\7\4\2\2\60\5\3\2\2\2\61\62\7\5\2\2\62\7\3\2"+
		"\2\2\63\64\7\6\2\2\64\t\3\2\2\2\65\66\7\7\2\2\66\13\3\2\2\2\678\7\b\2"+
		"\28\r\3\2\2\29:\7\t\2\2:\17\3\2\2\2;<\7\n\2\2<\21\3\2\2\2=>\7\13\2\2>"+
		"\23\3\2\2\2?@\7\f\2\2@\25\3\2\2\2AB\7\r\2\2B\27\3\2\2\2CD\7\16\2\2D\31"+
		"\3\2\2\2EF\7\17\2\2F\33\3\2\2\2GH\7\20\2\2H\35\3\2\2\2IJ\7\21\2\2J\37"+
		"\3\2\2\2KL\7\22\2\2L!\3\2\2\2\4$+";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}