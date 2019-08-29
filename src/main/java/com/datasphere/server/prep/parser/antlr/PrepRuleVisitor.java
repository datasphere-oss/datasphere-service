// Generated from com/datasphere/datalayer/prep/parser/antlr/PrepRule.g4 by ANTLR 4.5.1
package com.datasphere.server.prep.parser.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PrepRuleParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PrepRuleVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#test_window_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTest_window_expression(PrepRuleParser.Test_window_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#test_aggregate_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTest_aggregate_expression(PrepRuleParser.Test_aggregate_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#test_condition_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTest_condition_expression(PrepRuleParser.Test_condition_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#test_left_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTest_left_value(PrepRuleParser.Test_left_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#test_right_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTest_right_value(PrepRuleParser.Test_right_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#sub_condition_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSub_condition_expression(PrepRuleParser.Sub_condition_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#sub_condition_complete_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSub_condition_complete_expression(PrepRuleParser.Sub_condition_complete_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#sub_condition_expression_argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSub_condition_expression_argument(PrepRuleParser.Sub_condition_expression_argumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#sub_value_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSub_value_expression(PrepRuleParser.Sub_value_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#function_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_expression(PrepRuleParser.Function_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#function_param_0}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_param_0(PrepRuleParser.Function_param_0Context ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#function_param_1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_param_1(PrepRuleParser.Function_param_1Context ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#function_param_2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_param_2(PrepRuleParser.Function_param_2Context ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#function_param_3}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_param_3(PrepRuleParser.Function_param_3Context ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#function_param_n}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_param_n(PrepRuleParser.Function_param_nContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#function_name_aggregate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_name_aggregate(PrepRuleParser.Function_name_aggregateContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#function_name_0}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_name_0(PrepRuleParser.Function_name_0Context ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#function_name_1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_name_1(PrepRuleParser.Function_name_1Context ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#function_name_2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_name_2(PrepRuleParser.Function_name_2Context ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#function_name_3}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_name_3(PrepRuleParser.Function_name_3Context ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#function_name_n}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_name_n(PrepRuleParser.Function_name_nContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#rule_header}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRule_header(PrepRuleParser.Rule_headerContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#rule_keep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRule_keep(PrepRuleParser.Rule_keepContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#arg_row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg_row(PrepRuleParser.Arg_rowContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#arg_rownum}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg_rownum(PrepRuleParser.Arg_rownumContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#parameter_row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_row(PrepRuleParser.Parameter_rowContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#parameter_rownum}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_rownum(PrepRuleParser.Parameter_rownumContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#expression_row_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_row_condition(PrepRuleParser.Expression_row_conditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#expression_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_condition(PrepRuleParser.Expression_conditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#expression_condition_argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_condition_argument(PrepRuleParser.Expression_condition_argumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#expression_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_value(PrepRuleParser.Expression_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#function_form}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_form(PrepRuleParser.Function_formContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_name(PrepRuleParser.Column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#ruleset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleset(PrepRuleParser.RulesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgs(PrepRuleParser.ArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionArrayExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArrayExpr(PrepRuleParser.FunctionArrayExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code doubleExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleExpr(PrepRuleParser.DoubleExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code regularExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegularExpr(PrepRuleParser.RegularExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addSubExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSubExpr(PrepRuleParser.AddSubExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nullExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullExpr(PrepRuleParser.NullExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code longExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLongExpr(PrepRuleParser.LongExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalAndOrExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAndOrExpr(PrepRuleParser.LogicalAndOrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code booleanExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanExpr(PrepRuleParser.BooleanExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nestedExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedExpr(PrepRuleParser.NestedExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code doubleArrayExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleArrayExpr(PrepRuleParser.DoubleArrayExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringArrayExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringArrayExpr(PrepRuleParser.StringArrayExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code longArrayExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLongArrayExpr(PrepRuleParser.LongArrayExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalOpExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOpExpr(PrepRuleParser.LogicalOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionExpr(PrepRuleParser.FunctionExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringExpr(PrepRuleParser.StringExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryOpExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOpExpr(PrepRuleParser.UnaryOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierArrayExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierArrayExpr(PrepRuleParser.IdentifierArrayExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalAndOrExpr2}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAndOrExpr2(PrepRuleParser.LogicalAndOrExpr2Context ctx);
	/**
	 * Visit a parse tree produced by the {@code mulDivModuloExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDivModuloExpr(PrepRuleParser.MulDivModuloExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code powOpExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPowOpExpr(PrepRuleParser.PowOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignExpr(PrepRuleParser.AssignExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierExpr}
	 * labeled alternative in {@link PrepRuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierExpr(PrepRuleParser.IdentifierExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrepRuleParser#fn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFn(PrepRuleParser.FnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionArgs}
	 * labeled alternative in {@link PrepRuleParser#fnArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArgs(PrepRuleParser.FunctionArgsContext ctx);
}