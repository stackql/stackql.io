// Generated from /mnt/c/LocalGitRepos/stackql/web-properties/stackql.io/ref/antlr/common.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link commonParser}.
 */
public interface commonListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link commonParser#provider}.
	 * @param ctx the parse tree
	 */
	void enterProvider(commonParser.ProviderContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#provider}.
	 * @param ctx the parse tree
	 */
	void exitProvider(commonParser.ProviderContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#version}.
	 * @param ctx the parse tree
	 */
	void enterVersion(commonParser.VersionContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#version}.
	 * @param ctx the parse tree
	 */
	void exitVersion(commonParser.VersionContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#fullFieldName}.
	 * @param ctx the parse tree
	 */
	void enterFullFieldName(commonParser.FullFieldNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#fullFieldName}.
	 * @param ctx the parse tree
	 */
	void exitFullFieldName(commonParser.FullFieldNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(commonParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(commonParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#alias}.
	 * @param ctx the parse tree
	 */
	void enterAlias(commonParser.AliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#alias}.
	 * @param ctx the parse tree
	 */
	void exitAlias(commonParser.AliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(commonParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(commonParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#groupByItem}.
	 * @param ctx the parse tree
	 */
	void enterGroupByItem(commonParser.GroupByItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#groupByItem}.
	 * @param ctx the parse tree
	 */
	void exitGroupByItem(commonParser.GroupByItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#resourceOrSubQuery}.
	 * @param ctx the parse tree
	 */
	void enterResourceOrSubQuery(commonParser.ResourceOrSubQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#resourceOrSubQuery}.
	 * @param ctx the parse tree
	 */
	void exitResourceOrSubQuery(commonParser.ResourceOrSubQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#resource}.
	 * @param ctx the parse tree
	 */
	void enterResource(commonParser.ResourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#resource}.
	 * @param ctx the parse tree
	 */
	void exitResource(commonParser.ResourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#multipartIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterMultipartIdentifier(commonParser.MultipartIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#multipartIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitMultipartIdentifier(commonParser.MultipartIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPattern(commonParser.PatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPattern(commonParser.PatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(commonParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(commonParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#providerMethodName}.
	 * @param ctx the parse tree
	 */
	void enterProviderMethodName(commonParser.ProviderMethodNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#providerMethodName}.
	 * @param ctx the parse tree
	 */
	void exitProviderMethodName(commonParser.ProviderMethodNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#parameterName}.
	 * @param ctx the parse tree
	 */
	void enterParameterName(commonParser.ParameterNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#parameterName}.
	 * @param ctx the parse tree
	 */
	void exitParameterName(commonParser.ParameterNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#parameterValue}.
	 * @param ctx the parse tree
	 */
	void enterParameterValue(commonParser.ParameterValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#parameterValue}.
	 * @param ctx the parse tree
	 */
	void exitParameterValue(commonParser.ParameterValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#parameterExpression}.
	 * @param ctx the parse tree
	 */
	void enterParameterExpression(commonParser.ParameterExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#parameterExpression}.
	 * @param ctx the parse tree
	 */
	void exitParameterExpression(commonParser.ParameterExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#queryHint}.
	 * @param ctx the parse tree
	 */
	void enterQueryHint(commonParser.QueryHintContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#queryHint}.
	 * @param ctx the parse tree
	 */
	void exitQueryHint(commonParser.QueryHintContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#fieldList}.
	 * @param ctx the parse tree
	 */
	void enterFieldList(commonParser.FieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#fieldList}.
	 * @param ctx the parse tree
	 */
	void exitFieldList(commonParser.FieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterSelectStatement(commonParser.SelectStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitSelectStatement(commonParser.SelectStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#awaitQueryHint}.
	 * @param ctx the parse tree
	 */
	void enterAwaitQueryHint(commonParser.AwaitQueryHintContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#awaitQueryHint}.
	 * @param ctx the parse tree
	 */
	void exitAwaitQueryHint(commonParser.AwaitQueryHintContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#viewName}.
	 * @param ctx the parse tree
	 */
	void enterViewName(commonParser.ViewNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#viewName}.
	 * @param ctx the parse tree
	 */
	void exitViewName(commonParser.ViewNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link commonParser#fieldValue}.
	 * @param ctx the parse tree
	 */
	void enterFieldValue(commonParser.FieldValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link commonParser#fieldValue}.
	 * @param ctx the parse tree
	 */
	void exitFieldValue(commonParser.FieldValueContext ctx);
}