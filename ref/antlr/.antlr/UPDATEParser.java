// Generated from /mnt/c/LocalGitRepos/stackql/web-properties/stackql.io/ref/antlr/UPDATE.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class UPDATEParser extends Parser {
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
		RULE_updateStatement = 0, RULE_fullFieldAssignment = 1, RULE_fieldValue = 2, 
		RULE_provider = 3, RULE_version = 4, RULE_fullFieldName = 5, RULE_functionCall = 6, 
		RULE_alias = 7, RULE_number = 8, RULE_groupByItem = 9, RULE_resourceOrSubQuery = 10, 
		RULE_resource = 11, RULE_multipartIdentifier = 12, RULE_pattern = 13, 
		RULE_expression = 14, RULE_providerMethodName = 15, RULE_parameterName = 16, 
		RULE_parameterValue = 17, RULE_parameterExpression = 18, RULE_queryHint = 19, 
		RULE_fieldList = 20, RULE_selectStatement = 21, RULE_awaitQueryHint = 22, 
		RULE_viewName = 23;
	private static String[] makeRuleNames() {
		return new String[] {
			"updateStatement", "fullFieldAssignment", "fieldValue", "provider", "version", 
			"fullFieldName", "functionCall", "alias", "number", "groupByItem", "resourceOrSubQuery", 
			"resource", "multipartIdentifier", "pattern", "expression", "providerMethodName", 
			"parameterName", "parameterValue", "parameterExpression", "queryHint", 
			"fieldList", "selectStatement", "awaitQueryHint", "viewName"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "','", "';'", "'='", "'provider'", "'version'", "'fullFieldName'", 
			"'functionCall'", "'alias'", "'number'", "'groupByItem'", "'resourceOrSubQuery'", 
			"'resource'", "'multipartIdentifier'", "'pattern'", "'expression'", "'providerMethodName'", 
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
	public String getGrammarFileName() { return "UPDATE.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public UPDATEParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UpdateStatementContext extends ParserRuleContext {
		public TerminalNode UPDATE() { return getToken(UPDATEParser.UPDATE, 0); }
		public MultipartIdentifierContext multipartIdentifier() {
			return getRuleContext(MultipartIdentifierContext.class,0);
		}
		public TerminalNode SET() { return getToken(UPDATEParser.SET, 0); }
		public List<FullFieldAssignmentContext> fullFieldAssignment() {
			return getRuleContexts(FullFieldAssignmentContext.class);
		}
		public FullFieldAssignmentContext fullFieldAssignment(int i) {
			return getRuleContext(FullFieldAssignmentContext.class,i);
		}
		public TerminalNode WHERE() { return getToken(UPDATEParser.WHERE, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(UPDATEParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(UPDATEParser.AND, i);
		}
		public List<TerminalNode> OR() { return getTokens(UPDATEParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(UPDATEParser.OR, i);
		}
		public UpdateStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_updateStatement; }
	}

	public final UpdateStatementContext updateStatement() throws RecognitionException {
		UpdateStatementContext _localctx = new UpdateStatementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_updateStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			match(UPDATE);
			setState(49);
			multipartIdentifier();
			setState(50);
			match(SET);
			setState(51);
			fullFieldAssignment();
			setState(56);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(52);
				match(T__0);
				setState(53);
				fullFieldAssignment();
				}
				}
				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(59);
			match(WHERE);
			setState(60);
			expression();
			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND || _la==OR) {
				{
				{
				setState(61);
				_la = _input.LA(1);
				if ( !(_la==AND || _la==OR) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(62);
				expression();
				}
				}
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(68);
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
	public static class FullFieldAssignmentContext extends ParserRuleContext {
		public FullFieldNameContext fullFieldName() {
			return getRuleContext(FullFieldNameContext.class,0);
		}
		public FieldValueContext fieldValue() {
			return getRuleContext(FieldValueContext.class,0);
		}
		public FullFieldAssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fullFieldAssignment; }
	}

	public final FullFieldAssignmentContext fullFieldAssignment() throws RecognitionException {
		FullFieldAssignmentContext _localctx = new FullFieldAssignmentContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_fullFieldAssignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(70);
			fullFieldName();
			setState(71);
			match(T__2);
			setState(72);
			fieldValue();
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
		enterRule(_localctx, 4, RULE_fieldValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
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
		enterRule(_localctx, 6, RULE_provider);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
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
		enterRule(_localctx, 8, RULE_version);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
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
		enterRule(_localctx, 10, RULE_fullFieldName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
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
		enterRule(_localctx, 12, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
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
		enterRule(_localctx, 14, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
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
		enterRule(_localctx, 16, RULE_number);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
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
		enterRule(_localctx, 18, RULE_groupByItem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
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
		enterRule(_localctx, 20, RULE_resourceOrSubQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
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
		enterRule(_localctx, 22, RULE_resource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
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
		enterRule(_localctx, 24, RULE_multipartIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
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
		enterRule(_localctx, 26, RULE_pattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
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
		enterRule(_localctx, 28, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
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
		enterRule(_localctx, 30, RULE_providerMethodName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
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
		enterRule(_localctx, 32, RULE_parameterName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(102);
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
		enterRule(_localctx, 34, RULE_parameterValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
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
		enterRule(_localctx, 36, RULE_parameterExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
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
		enterRule(_localctx, 38, RULE_queryHint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
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
		enterRule(_localctx, 40, RULE_fieldList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
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
		enterRule(_localctx, 42, RULE_selectStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
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
	public static class AwaitQueryHintContext extends ParserRuleContext {
		public AwaitQueryHintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_awaitQueryHint; }
	}

	public final AwaitQueryHintContext awaitQueryHint() throws RecognitionException {
		AwaitQueryHintContext _localctx = new AwaitQueryHintContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_awaitQueryHint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
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
	public static class ViewNameContext extends ParserRuleContext {
		public ViewNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_viewName; }
	}

	public final ViewNameContext viewName() throws RecognitionException {
		ViewNameContext _localctx = new ViewNameContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_viewName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
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
		"\u0004\u0001\u00a2w\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u00007\b\u0000"+
		"\n\u0000\f\u0000:\t\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0005\u0000@\b\u0000\n\u0000\f\u0000C\t\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013"+
		"\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0016"+
		"\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0000\u0000\u0018\u0000"+
		"\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c"+
		"\u001e \"$&(*,.\u0000\u0001\u0002\u0000;;}}`\u00000\u0001\u0000\u0000"+
		"\u0000\u0002F\u0001\u0000\u0000\u0000\u0004J\u0001\u0000\u0000\u0000\u0006"+
		"L\u0001\u0000\u0000\u0000\bN\u0001\u0000\u0000\u0000\nP\u0001\u0000\u0000"+
		"\u0000\fR\u0001\u0000\u0000\u0000\u000eT\u0001\u0000\u0000\u0000\u0010"+
		"V\u0001\u0000\u0000\u0000\u0012X\u0001\u0000\u0000\u0000\u0014Z\u0001"+
		"\u0000\u0000\u0000\u0016\\\u0001\u0000\u0000\u0000\u0018^\u0001\u0000"+
		"\u0000\u0000\u001a`\u0001\u0000\u0000\u0000\u001cb\u0001\u0000\u0000\u0000"+
		"\u001ed\u0001\u0000\u0000\u0000 f\u0001\u0000\u0000\u0000\"h\u0001\u0000"+
		"\u0000\u0000$j\u0001\u0000\u0000\u0000&l\u0001\u0000\u0000\u0000(n\u0001"+
		"\u0000\u0000\u0000*p\u0001\u0000\u0000\u0000,r\u0001\u0000\u0000\u0000"+
		".t\u0001\u0000\u0000\u000001\u0005\u0097\u0000\u000012\u0003\u0018\f\u0000"+
		"23\u0005\u008b\u0000\u000038\u0003\u0002\u0001\u000045\u0005\u0001\u0000"+
		"\u000057\u0003\u0002\u0001\u000064\u0001\u0000\u0000\u00007:\u0001\u0000"+
		"\u0000\u000086\u0001\u0000\u0000\u000089\u0001\u0000\u0000\u00009;\u0001"+
		"\u0000\u0000\u0000:8\u0001\u0000\u0000\u0000;<\u00052\u0000\u0000<A\u0003"+
		"\u001c\u000e\u0000=>\u0007\u0000\u0000\u0000>@\u0003\u001c\u000e\u0000"+
		"?=\u0001\u0000\u0000\u0000@C\u0001\u0000\u0000\u0000A?\u0001\u0000\u0000"+
		"\u0000AB\u0001\u0000\u0000\u0000BD\u0001\u0000\u0000\u0000CA\u0001\u0000"+
		"\u0000\u0000DE\u0005\u0002\u0000\u0000E\u0001\u0001\u0000\u0000\u0000"+
		"FG\u0003\n\u0005\u0000GH\u0005\u0003\u0000\u0000HI\u0003\u0004\u0002\u0000"+
		"I\u0003\u0001\u0000\u0000\u0000JK\u0001\u0000\u0000\u0000K\u0005\u0001"+
		"\u0000\u0000\u0000LM\u0005\u0004\u0000\u0000M\u0007\u0001\u0000\u0000"+
		"\u0000NO\u0005\u0005\u0000\u0000O\t\u0001\u0000\u0000\u0000PQ\u0005\u0006"+
		"\u0000\u0000Q\u000b\u0001\u0000\u0000\u0000RS\u0005\u0007\u0000\u0000"+
		"S\r\u0001\u0000\u0000\u0000TU\u0005\b\u0000\u0000U\u000f\u0001\u0000\u0000"+
		"\u0000VW\u0005\t\u0000\u0000W\u0011\u0001\u0000\u0000\u0000XY\u0005\n"+
		"\u0000\u0000Y\u0013\u0001\u0000\u0000\u0000Z[\u0005\u000b\u0000\u0000"+
		"[\u0015\u0001\u0000\u0000\u0000\\]\u0005\f\u0000\u0000]\u0017\u0001\u0000"+
		"\u0000\u0000^_\u0005\r\u0000\u0000_\u0019\u0001\u0000\u0000\u0000`a\u0005"+
		"\u000e\u0000\u0000a\u001b\u0001\u0000\u0000\u0000bc\u0005\u000f\u0000"+
		"\u0000c\u001d\u0001\u0000\u0000\u0000de\u0005\u0010\u0000\u0000e\u001f"+
		"\u0001\u0000\u0000\u0000fg\u0005\u0011\u0000\u0000g!\u0001\u0000\u0000"+
		"\u0000hi\u0005\u0012\u0000\u0000i#\u0001\u0000\u0000\u0000jk\u0005\u0013"+
		"\u0000\u0000k%\u0001\u0000\u0000\u0000lm\u0005\u0014\u0000\u0000m\'\u0001"+
		"\u0000\u0000\u0000no\u0005\u0015\u0000\u0000o)\u0001\u0000\u0000\u0000"+
		"pq\u0005\u0016\u0000\u0000q+\u0001\u0000\u0000\u0000rs\u0005\u0017\u0000"+
		"\u0000s-\u0001\u0000\u0000\u0000tu\u0005\u0018\u0000\u0000u/\u0001\u0000"+
		"\u0000\u0000\u00028A";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}