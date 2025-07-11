// Generated from c:/LocalGitRepos/stackql/web-properties/stackql.io/ref/antlr/INSERT.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link INSERTParser}.
 */
public interface INSERTListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link INSERTParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void enterInsertStatement(INSERTParser.InsertStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void exitInsertStatement(INSERTParser.InsertStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#provider}.
	 * @param ctx the parse tree
	 */
	void enterProvider(INSERTParser.ProviderContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#provider}.
	 * @param ctx the parse tree
	 */
	void exitProvider(INSERTParser.ProviderContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#version}.
	 * @param ctx the parse tree
	 */
	void enterVersion(INSERTParser.VersionContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#version}.
	 * @param ctx the parse tree
	 */
	void exitVersion(INSERTParser.VersionContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#fullFieldName}.
	 * @param ctx the parse tree
	 */
	void enterFullFieldName(INSERTParser.FullFieldNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#fullFieldName}.
	 * @param ctx the parse tree
	 */
	void exitFullFieldName(INSERTParser.FullFieldNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(INSERTParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(INSERTParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#alias}.
	 * @param ctx the parse tree
	 */
	void enterAlias(INSERTParser.AliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#alias}.
	 * @param ctx the parse tree
	 */
	void exitAlias(INSERTParser.AliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(INSERTParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(INSERTParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#groupByItem}.
	 * @param ctx the parse tree
	 */
	void enterGroupByItem(INSERTParser.GroupByItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#groupByItem}.
	 * @param ctx the parse tree
	 */
	void exitGroupByItem(INSERTParser.GroupByItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#resourceOrSubQuery}.
	 * @param ctx the parse tree
	 */
	void enterResourceOrSubQuery(INSERTParser.ResourceOrSubQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#resourceOrSubQuery}.
	 * @param ctx the parse tree
	 */
	void exitResourceOrSubQuery(INSERTParser.ResourceOrSubQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#resource}.
	 * @param ctx the parse tree
	 */
	void enterResource(INSERTParser.ResourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#resource}.
	 * @param ctx the parse tree
	 */
	void exitResource(INSERTParser.ResourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#multipartIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterMultipartIdentifier(INSERTParser.MultipartIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#multipartIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitMultipartIdentifier(INSERTParser.MultipartIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPattern(INSERTParser.PatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPattern(INSERTParser.PatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(INSERTParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(INSERTParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#providerMethodName}.
	 * @param ctx the parse tree
	 */
	void enterProviderMethodName(INSERTParser.ProviderMethodNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#providerMethodName}.
	 * @param ctx the parse tree
	 */
	void exitProviderMethodName(INSERTParser.ProviderMethodNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#parameterName}.
	 * @param ctx the parse tree
	 */
	void enterParameterName(INSERTParser.ParameterNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#parameterName}.
	 * @param ctx the parse tree
	 */
	void exitParameterName(INSERTParser.ParameterNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#parameterValue}.
	 * @param ctx the parse tree
	 */
	void enterParameterValue(INSERTParser.ParameterValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#parameterValue}.
	 * @param ctx the parse tree
	 */
	void exitParameterValue(INSERTParser.ParameterValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#parameterExpression}.
	 * @param ctx the parse tree
	 */
	void enterParameterExpression(INSERTParser.ParameterExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#parameterExpression}.
	 * @param ctx the parse tree
	 */
	void exitParameterExpression(INSERTParser.ParameterExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#queryHint}.
	 * @param ctx the parse tree
	 */
	void enterQueryHint(INSERTParser.QueryHintContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#queryHint}.
	 * @param ctx the parse tree
	 */
	void exitQueryHint(INSERTParser.QueryHintContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#fieldList}.
	 * @param ctx the parse tree
	 */
	void enterFieldList(INSERTParser.FieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#fieldList}.
	 * @param ctx the parse tree
	 */
	void exitFieldList(INSERTParser.FieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterSelectStatement(INSERTParser.SelectStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitSelectStatement(INSERTParser.SelectStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#replaceStatement}.
	 * @param ctx the parse tree
	 */
	void enterReplaceStatement(INSERTParser.ReplaceStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#replaceStatement}.
	 * @param ctx the parse tree
	 */
	void exitReplaceStatement(INSERTParser.ReplaceStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#awaitQueryHint}.
	 * @param ctx the parse tree
	 */
	void enterAwaitQueryHint(INSERTParser.AwaitQueryHintContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#awaitQueryHint}.
	 * @param ctx the parse tree
	 */
	void exitAwaitQueryHint(INSERTParser.AwaitQueryHintContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#viewName}.
	 * @param ctx the parse tree
	 */
	void enterViewName(INSERTParser.ViewNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#viewName}.
	 * @param ctx the parse tree
	 */
	void exitViewName(INSERTParser.ViewNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link INSERTParser#fieldValue}.
	 * @param ctx the parse tree
	 */
	void enterFieldValue(INSERTParser.FieldValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link INSERTParser#fieldValue}.
	 * @param ctx the parse tree
	 */
	void exitFieldValue(INSERTParser.FieldValueContext ctx);
}