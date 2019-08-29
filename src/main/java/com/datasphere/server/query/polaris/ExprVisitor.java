// Generated from com/datasphere/server/query/polaris/Expr.g4 by ANTLR 4.5.1
package com.datasphere.server.query.polaris;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ExprParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ExprVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code doubleExpr}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleExpr(ExprParser.DoubleExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addSubExpr}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSubExpr(ExprParser.AddSubExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code string}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(ExprParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code longExpr}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLongExpr(ExprParser.LongExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalAndOrExpr}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAndOrExpr(ExprParser.LogicalAndOrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nestedExpr}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedExpr(ExprParser.NestedExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code idArrayExpr}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdArrayExpr(ExprParser.IdArrayExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalOpExpr}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOpExpr(ExprParser.LogicalOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionExpr}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionExpr(ExprParser.FunctionExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryOpExpr}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOpExpr(ExprParser.UnaryOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalAndOrExpr2}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAndOrExpr2(ExprParser.LogicalAndOrExpr2Context ctx);
	/**
	 * Visit a parse tree produced by the {@code mulDivModuloExpr}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDivModuloExpr(ExprParser.MulDivModuloExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code powOpExpr}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPowOpExpr(ExprParser.PowOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignExpr(ExprParser.AssignExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierExpr}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierExpr(ExprParser.IdentifierExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionArgs}
	 * labeled alternative in {@link ExprParser#fnArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArgs(ExprParser.FunctionArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code idfield}
	 * labeled alternative in {@link ExprParser#field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdfield(ExprParser.IdfieldContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierArray}
	 * labeled alternative in {@link ExprParser#idArray}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierArray(ExprParser.IdentifierArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyArray}
	 * labeled alternative in {@link ExprParser#idArray}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyArray(ExprParser.EmptyArrayContext ctx);
}