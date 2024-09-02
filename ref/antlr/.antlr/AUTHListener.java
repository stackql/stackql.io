// Generated from /mnt/c/LocalGitRepos/stackql/web-properties/stackql.io/ref/antlr/AUTH.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AUTHParser}.
 */
public interface AUTHListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AUTHParser#authStatement}.
	 * @param ctx the parse tree
	 */
	void enterAuthStatement(AUTHParser.AuthStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#authStatement}.
	 * @param ctx the parse tree
	 */
	void exitAuthStatement(AUTHParser.AuthStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#provider}.
	 * @param ctx the parse tree
	 */
	void enterProvider(AUTHParser.ProviderContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#provider}.
	 * @param ctx the parse tree
	 */
	void exitProvider(AUTHParser.ProviderContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#version}.
	 * @param ctx the parse tree
	 */
	void enterVersion(AUTHParser.VersionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#version}.
	 * @param ctx the parse tree
	 */
	void exitVersion(AUTHParser.VersionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#fullFieldName}.
	 * @param ctx the parse tree
	 */
	void enterFullFieldName(AUTHParser.FullFieldNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#fullFieldName}.
	 * @param ctx the parse tree
	 */
	void exitFullFieldName(AUTHParser.FullFieldNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(AUTHParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(AUTHParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#alias}.
	 * @param ctx the parse tree
	 */
	void enterAlias(AUTHParser.AliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#alias}.
	 * @param ctx the parse tree
	 */
	void exitAlias(AUTHParser.AliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(AUTHParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(AUTHParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#groupByItem}.
	 * @param ctx the parse tree
	 */
	void enterGroupByItem(AUTHParser.GroupByItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#groupByItem}.
	 * @param ctx the parse tree
	 */
	void exitGroupByItem(AUTHParser.GroupByItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#resourceOrSubQuery}.
	 * @param ctx the parse tree
	 */
	void enterResourceOrSubQuery(AUTHParser.ResourceOrSubQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#resourceOrSubQuery}.
	 * @param ctx the parse tree
	 */
	void exitResourceOrSubQuery(AUTHParser.ResourceOrSubQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#resource}.
	 * @param ctx the parse tree
	 */
	void enterResource(AUTHParser.ResourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#resource}.
	 * @param ctx the parse tree
	 */
	void exitResource(AUTHParser.ResourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#multipartIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterMultipartIdentifier(AUTHParser.MultipartIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#multipartIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitMultipartIdentifier(AUTHParser.MultipartIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPattern(AUTHParser.PatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPattern(AUTHParser.PatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(AUTHParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(AUTHParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#providerMethodName}.
	 * @param ctx the parse tree
	 */
	void enterProviderMethodName(AUTHParser.ProviderMethodNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#providerMethodName}.
	 * @param ctx the parse tree
	 */
	void exitProviderMethodName(AUTHParser.ProviderMethodNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#parameterName}.
	 * @param ctx the parse tree
	 */
	void enterParameterName(AUTHParser.ParameterNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#parameterName}.
	 * @param ctx the parse tree
	 */
	void exitParameterName(AUTHParser.ParameterNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#parameterValue}.
	 * @param ctx the parse tree
	 */
	void enterParameterValue(AUTHParser.ParameterValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#parameterValue}.
	 * @param ctx the parse tree
	 */
	void exitParameterValue(AUTHParser.ParameterValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#parameterExpression}.
	 * @param ctx the parse tree
	 */
	void enterParameterExpression(AUTHParser.ParameterExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#parameterExpression}.
	 * @param ctx the parse tree
	 */
	void exitParameterExpression(AUTHParser.ParameterExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#queryHint}.
	 * @param ctx the parse tree
	 */
	void enterQueryHint(AUTHParser.QueryHintContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#queryHint}.
	 * @param ctx the parse tree
	 */
	void exitQueryHint(AUTHParser.QueryHintContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#fieldList}.
	 * @param ctx the parse tree
	 */
	void enterFieldList(AUTHParser.FieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#fieldList}.
	 * @param ctx the parse tree
	 */
	void exitFieldList(AUTHParser.FieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterSelectStatement(AUTHParser.SelectStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitSelectStatement(AUTHParser.SelectStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#awaitQueryHint}.
	 * @param ctx the parse tree
	 */
	void enterAwaitQueryHint(AUTHParser.AwaitQueryHintContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#awaitQueryHint}.
	 * @param ctx the parse tree
	 */
	void exitAwaitQueryHint(AUTHParser.AwaitQueryHintContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#viewName}.
	 * @param ctx the parse tree
	 */
	void enterViewName(AUTHParser.ViewNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#viewName}.
	 * @param ctx the parse tree
	 */
	void exitViewName(AUTHParser.ViewNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link AUTHParser#fieldValue}.
	 * @param ctx the parse tree
	 */
	void enterFieldValue(AUTHParser.FieldValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link AUTHParser#fieldValue}.
	 * @param ctx the parse tree
	 */
	void exitFieldValue(AUTHParser.FieldValueContext ctx);
}