// Generated from com/datasphere/datalayer/prep/parser/antlr/Rule.g4 by ANTLR 4.5.1
package com.datasphere.server.prep.parser.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link RuleParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface RuleVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link RuleParser#ruleset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleset(RuleParser.RulesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParser#args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgs(RuleParser.ArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionArrayExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArrayExpr(RuleParser.FunctionArrayExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code doubleExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleExpr(RuleParser.DoubleExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code regularExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegularExpr(RuleParser.RegularExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addSubExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSubExpr(RuleParser.AddSubExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nullExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullExpr(RuleParser.NullExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code longExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLongExpr(RuleParser.LongExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalAndOrExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAndOrExpr(RuleParser.LogicalAndOrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code booleanExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanExpr(RuleParser.BooleanExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nestedExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedExpr(RuleParser.NestedExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code doubleArrayExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleArrayExpr(RuleParser.DoubleArrayExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringArrayExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringArrayExpr(RuleParser.StringArrayExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code longArrayExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLongArrayExpr(RuleParser.LongArrayExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalOpExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOpExpr(RuleParser.LogicalOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionExpr(RuleParser.FunctionExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringExpr(RuleParser.StringExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryOpExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOpExpr(RuleParser.UnaryOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierArrayExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierArrayExpr(RuleParser.IdentifierArrayExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalAndOrExpr2}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAndOrExpr2(RuleParser.LogicalAndOrExpr2Context ctx);
	/**
	 * Visit a parse tree produced by the {@code mulDivModuloExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDivModuloExpr(RuleParser.MulDivModuloExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code powOpExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPowOpExpr(RuleParser.PowOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignExpr(RuleParser.AssignExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierExpr}
	 * labeled alternative in {@link RuleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierExpr(RuleParser.IdentifierExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParser#fn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFn(RuleParser.FnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionArgs}
	 * labeled alternative in {@link RuleParser#fnArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArgs(RuleParser.FunctionArgsContext ctx);
}