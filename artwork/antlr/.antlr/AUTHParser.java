// Generated from c:\Users\javen\OneDrive\GitRepositories\infraql\docs.infraql.io\artwork\antlr\AUTH.g4 by ANTLR 4.8
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
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, EXEC=20, AUTH=21, LOGIN=22, INTERACTIVE=23, REVOKE=24, 
		DESC=25, METHODS=26, DESCRIBE=27, EXPLAIN=28, EXTENDED=29, LIMIT=30, ORDER=31, 
		BY=32, GROUP=33, WITH=34, ROLLUP=35, HAVING=36, IN=37, AS=38, USE=39, 
		LIKE=40, FROM=41, SHOW=42, WHERE=43, SERVICES=44, RESOURCES=45, PROVIDERS=46, 
		FIELDS=47, SELECT=48, DISTINCT=49, ADD=50, ARRAY=51, AND=52, ASC=53, AUTO_INCREMENT=54, 
		BETWEEN=55, BINARY=56, CASE=57, COLLATE=58, CONVERT=59, CREATE=60, CROSS=61, 
		CUME_DIST=62, CURRENT_DATE=63, CURRENT_TIME=64, CURRENT_TIMESTAMP=65, 
		SUBSTR=66, SUBSTRING=67, DATABASE=68, DATABASES=69, DEFAULT=70, DELETE=71, 
		DENSE_RANK=72, DISTINCTROW=73, DIV=74, DROP=75, ELSE=76, END=77, ESCAPE=78, 
		EXISTS=79, FALSE=80, FIRST_VALUE=81, FOR=82, FORCE=83, GROUPING=84, GROUPS=85, 
		IF=86, IGNORE=87, INDEX=88, INNER=89, INSERT=90, INTERVAL=91, INTO=92, 
		IS=93, JOIN=94, JSON_TABLE=95, KEY=96, LAG=97, LAST_VALUE=98, LATERAL=99, 
		LEAD=100, LEFT=101, LOCALTIME=102, LOCALTIMESTAMP=103, LOCK=104, MEMBER=105, 
		MATCH=106, MAXVALUE=107, MOD=108, NATURAL=109, NEXT=110, NOT=111, NTH_VALUE=112, 
		NTILE=113, NULL=114, OF=115, OFF=116, ON=117, OR=118, OUTER=119, OVER=120, 
		PERCENT_RANK=121, RANK=122, RECURSIVE=123, REGEXP=124, RENAME=125, REPLACE=126, 
		RIGHT=127, ROW_NUMBER=128, SCHEMA=129, SEPARATOR=130, SET=131, STRAIGHT_JOIN=132, 
		SYSTEM=133, TABLE=134, THEN=135, TIMESTAMPADD=136, TIMESTAMPDIFF=137, 
		TO=138, TRUE=139, UNION=140, UNIQUE=141, UNLOCK=142, UPDATE=143, USING=144, 
		UTC_DATE=145, UTC_TIME=146, UTC_TIMESTAMP=147, VALUES=148, WHEN=149, WINDOW=150, 
		XOR=151;
	public static final int
		RULE_authStatement = 0, RULE_provider = 1, RULE_fullFieldName = 2, RULE_functionCall = 3, 
		RULE_alias = 4, RULE_number = 5, RULE_groupByItem = 6, RULE_resourceOrSubQuery = 7, 
		RULE_multipartIdentifier = 8, RULE_pattern = 9, RULE_expression = 10, 
		RULE_providerMethodName = 11, RULE_parameterName = 12, RULE_parameterValue = 13, 
		RULE_parameterExpression = 14, RULE_queryHint = 15, RULE_fieldList = 16, 
		RULE_selectStatement = 17, RULE_awaitQueryHint = 18;
	private static String[] makeRuleNames() {
		return new String[] {
			"authStatement", "provider", "fullFieldName", "functionCall", "alias", 
			"number", "groupByItem", "resourceOrSubQuery", "multipartIdentifier", 
			"pattern", "expression", "providerMethodName", "parameterName", "parameterValue", 
			"parameterExpression", "queryHint", "fieldList", "selectStatement", "awaitQueryHint"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'provider'", "'fullFieldName'", "'functionCall'", "'alias'", 
			"'number'", "'groupByItem'", "'resourceOrSubQuery'", "'multipartIdentifier'", 
			"'pattern'", "'expression'", "'providerMethodName'", "'parameterName'", 
			"'parameterValue'", "'parameterExpression'", "'queryHint'", "'fieldList'", 
			"'selectStatement'", "'awaitQueryHint'", "'EXEC'", "'AUTH'", "'LOGIN'", 
			"'INTERACTIVE'", "'REVOKE'", "'DESC'", "'METHODS'", "'DESCRIBE'", "'EXPLAIN'", 
			"'EXTENDED'", "'LIMIT'", "'ORDER'", "'BY'", "'GROUP'", "'WITH'", "'ROLLUP'", 
			"'HAVING'", "'IN'", "'AS'", "'USE'", "'LIKE'", "'FROM'", "'SHOW'", "'WHERE'", 
			"'SERVICES'", "'RESOURCES'", "'PROVIDERS'", "'FIELDS'", "'SELECT'", "'DISTINCT'", 
			"'ADD'", "'ARRAY '", "'AND'", "'ASC'", "'AUTO_INCREMENT'", "'BETWEEN'", 
			"'BINARY'", "'CASE'", "'COLLATE'", "'CONVERT'", "'CREATE'", "'CROSS'", 
			"'CUME_DIST'", "'CURRENT_DATE'", "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", 
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
			null, null, null, null, null, null, null, null, "EXEC", "AUTH", "LOGIN", 
			"INTERACTIVE", "REVOKE", "DESC", "METHODS", "DESCRIBE", "EXPLAIN", "EXTENDED", 
			"LIMIT", "ORDER", "BY", "GROUP", "WITH", "ROLLUP", "HAVING", "IN", "AS", 
			"USE", "LIKE", "FROM", "SHOW", "WHERE", "SERVICES", "RESOURCES", "PROVIDERS", 
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
			setState(38);
			match(AUTH);
			setState(47);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LOGIN:
				{
				setState(39);
				match(LOGIN);
				{
				setState(41);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(40);
					provider();
					}
				}

				setState(44);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==INTERACTIVE) {
					{
					setState(43);
					match(INTERACTIVE);
					}
				}

				}
				}
				break;
			case REVOKE:
				{
				setState(46);
				match(REVOKE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(49);
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
			setState(51);
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
			setState(53);
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
			setState(55);
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
			setState(57);
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
			setState(59);
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
			setState(61);
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
			setState(63);
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
			setState(65);
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
			setState(67);
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
			setState(69);
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
			setState(71);
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
			setState(73);
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
			setState(75);
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
			setState(77);
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
			setState(79);
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
			setState(81);
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
			setState(83);
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
			setState(85);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0099Z\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\3\2\3\2\3\2\5\2,\n\2\3\2\5\2/\n\2\3\2\5\2\62\n\2"+
		"\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3"+
		"\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3"+
		"\22\3\22\3\23\3\23\3\24\3\24\3\24\2\2\25\2\4\6\b\n\f\16\20\22\24\26\30"+
		"\32\34\36 \"$&\2\2\2I\2(\3\2\2\2\4\65\3\2\2\2\6\67\3\2\2\2\b9\3\2\2\2"+
		"\n;\3\2\2\2\f=\3\2\2\2\16?\3\2\2\2\20A\3\2\2\2\22C\3\2\2\2\24E\3\2\2\2"+
		"\26G\3\2\2\2\30I\3\2\2\2\32K\3\2\2\2\34M\3\2\2\2\36O\3\2\2\2 Q\3\2\2\2"+
		"\"S\3\2\2\2$U\3\2\2\2&W\3\2\2\2(\61\7\27\2\2)+\7\30\2\2*,\5\4\3\2+*\3"+
		"\2\2\2+,\3\2\2\2,.\3\2\2\2-/\7\31\2\2.-\3\2\2\2./\3\2\2\2/\62\3\2\2\2"+
		"\60\62\7\32\2\2\61)\3\2\2\2\61\60\3\2\2\2\62\63\3\2\2\2\63\64\7\3\2\2"+
		"\64\3\3\2\2\2\65\66\7\4\2\2\66\5\3\2\2\2\678\7\5\2\28\7\3\2\2\29:\7\6"+
		"\2\2:\t\3\2\2\2;<\7\7\2\2<\13\3\2\2\2=>\7\b\2\2>\r\3\2\2\2?@\7\t\2\2@"+
		"\17\3\2\2\2AB\7\n\2\2B\21\3\2\2\2CD\7\13\2\2D\23\3\2\2\2EF\7\f\2\2F\25"+
		"\3\2\2\2GH\7\r\2\2H\27\3\2\2\2IJ\7\16\2\2J\31\3\2\2\2KL\7\17\2\2L\33\3"+
		"\2\2\2MN\7\20\2\2N\35\3\2\2\2OP\7\21\2\2P\37\3\2\2\2QR\7\22\2\2R!\3\2"+
		"\2\2ST\7\23\2\2T#\3\2\2\2UV\7\24\2\2V%\3\2\2\2WX\7\25\2\2X\'\3\2\2\2\5"+
		"+.\61";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}