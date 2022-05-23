import gen.MyGrammarBaseListener;
import gen.MyGrammarLexer;
import gen.MyGrammarParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.*;

class MyListener extends MyGrammarBaseListener
{
	private final Stack<Integer> numberStack = new Stack<>();
	private final Map<String,Integer> varibleMap = new HashMap<>();

	@Override public void enterMyStart(MyGrammarParser.MyStartContext ctx)
	{
		System.err.println("enterMyStart()");
	}

	@Override public void exitMyStart(MyGrammarParser.MyStartContext ctx)
	{
		System.err.println("exitMyStart()");
	}

	@Override public void visitTerminal(TerminalNode node)
	{
		System.err.println("terminal-node: '" + node.getText() + "'");
	}

	@Override
	public void exitValueNumber(MyGrammarParser.ValueNumberContext ctx) {
		int i = Integer.parseInt(ctx.INT().getText());
		numberStack.push(i);
	}

	@Override
	public void exitOtherExpr(MyGrammarParser.OtherExprContext ctx) {
		int result = numberStack.pop();
		System.err.println("Final result is: " + result);
	}

	@Override
	public void exitAssign(MyGrammarParser.AssignContext ctx) {
		String id = ctx.ID().getText();
		int value = numberStack.pop();
		varibleMap.put(id, value);
		System.err.println("memory put: " + id + "=" + value);
	}

	@Override
	public void exitPrintExpr(MyGrammarParser.PrintExprContext ctx) {
		int value = numberStack.pop();
		System.err.println("print "+ctx.expr().getText()+ " = "+ value);
	}

	@Override
	public void exitValueVariable(MyGrammarParser.ValueVariableContext ctx) {
		String id = ctx.ID().getText();
		int number= varibleMap.get(id);
		numberStack.push(number);
		System.err.println("Added id to letterstack: "+id+" meaning adding "+number+" to numberstack");
	}

	@Override
	public void exitFact(MyGrammarParser.FactContext ctx) {
		int number = numberStack.pop();
		int fact = 1;
		for (int i = 2; i <= number; i++) {
			fact = fact * i;
		}
		numberStack.push(fact);
		System.err.println("Factorial of "+number+ " is "+ fact);

	}

	@Override
	public void exitPow(MyGrammarParser.PowContext ctx) {
		int right = numberStack.pop();
		int left = numberStack.pop();
		int result = 0;
		if (ctx.op.getType() == MyGrammarParser.POW) {
			result = (int) Math.pow(left, right);
			System.err.println(left + " to the power of " + right);
			numberStack.push(result);
		}


	}

	@Override
	public void exitAdd(MyGrammarParser.AddContext ctx) {
		int right = numberStack.pop();
		int left = numberStack.pop();
		int result = left + right;
		System.err.println("Added " + left + " with " + right);
		numberStack.push(result);
	}

	@Override
	public void exitSub(MyGrammarParser.SubContext ctx) {
		int right = numberStack.pop();
		int left = numberStack.pop();
		int result = left - right;
		System.err.println("Subtracted " + left + " with " + right);
		numberStack.push(result);
	}

	@Override
	public void exitMul(MyGrammarParser.MulContext ctx) {
		int right = numberStack.pop();
		int left = numberStack.pop();
		int result = left * right;
		System.err.println("Multiplied " + left + " with " + right);
		numberStack.push(result);
	}

	@Override
	public void exitDiv(MyGrammarParser.DivContext ctx) {
		int right = numberStack.pop();
		int left = numberStack.pop();
		if (right == 0 )
		{
			throw new ArithmeticException("Cannot divide by zero!");
		}
		int result = left / right;
		System.err.println("Divided " + left + " with " + right);
		numberStack.push(result);
	}
}

public class Main
{
	public static void main(String[] args) throws Exception
	{
		CharStream input = CharStreams.fromStream(System.in);
		MyGrammarLexer lexer = new MyGrammarLexer(input);

		CommonTokenStream tokens = new CommonTokenStream(lexer);


		MyGrammarParser parser = new MyGrammarParser(tokens);

		ParseTree tree = parser.myStart();

		MyListener m = new MyListener();
		ParseTreeWalker.DEFAULT.walk(m, tree);
	}
}

//import gen.MyGrammarBaseListener;
//import gen.MyGrammarParser;
//import org.antlr.v4.runtime.*;
//import org.antlr.v4.runtime.tree.*;
//import java.util.*;
//
//class MyListener extends MyGrammarBaseListener
//{
//	private Stack<Integer> numberStack = new Stack<Integer>();
//
//	@Override public void enterMyStart(MyGrammarParser.MyStartContext ctx)
//	{
//		System.err.println("enterMyStart()");
//	}
//
//	@Override public void exitMyStart(MyGrammarParser.MyStartContext ctx)
//	{
//		System.err.println("exitMyStart()");
//	}
//
//	@Override public void visitTerminal(TerminalNode node)
//	{
//		System.err.println("terminal-node: '" + node.getText() + "'");
//	}
//
//	@Override
//    public void exitAddSub(MyGrammarParser.AddSubContext ctx) {
//		int right = numberStack.pop();
//		int left = numberStack.pop();
//
//        int result;
//        if (ctx.op.getType() == MyGrammarParser.ADD) {
//            result = left + right;
//			System.err.println("-------------------------------------------------");
//			System.err.println("added "+left+ " with "+right + " = " + result);
//		} else {
//            result = left - right;
//			System.err.println("-------------------------------------------------");
//			System.err.println("subtracted "+left+ " with "+right + " = " + result);
//		}
//		System.err.println("-------------------------------------------------");
//		numberStack.push(result);
//
//    }
//
//	@Override
//    public void exitMulDiv(MyGrammarParser.MulDivContext ctx) {
//        int right = numberStack.pop();
//		int left = numberStack.pop();
//
//        int result;
//        if (ctx.op.getType() == MyGrammarParser.MUL) {
//            result = left * right;
//			System.err.println("-------------------------------------------------");
//			System.err.println("multiplied "+left+ " with "+right + " = " + result);
//		} else {
//            result = left / right;
//			System.err.println("-------------------------------------------------");
//			System.err.println("divided "+left+ " by "+right + " = " + result);
//		}
//		System.err.println("-------------------------------------------------");
//		numberStack.push(result);
//    }
//
//
//	@Override
//	public void exitFact(MyGrammarParser.FactContext ctx) {
//		int number = numberStack.pop();
//		int i,fact=1;
//		for(i=1;i<=number;i++){
//			fact=fact*i;
//		}
//		System.err.println("-------------------------------------------------");
//		System.err.println("Factorial of "+number+" is: "+fact);
//		System.err.println("-------------------------------------------------");
//        numberStack.push(fact);
//	}
//
//	@Override
//	public void exitPow(MyGrammarParser.PowContext ctx) {
//		int right = numberStack.pop();
//		int left = numberStack.pop();
//		int result = 0;
//		if (ctx.op.getType() == MyGrammarParser.POW) {
//			result = (int) Math.pow(left,right);
//			System.err.println(left+ " to the power of "+ right);
//			numberStack.push(result);
//		}
//	}
//
//	@Override
//	public void exitInt(MyGrammarParser.IntContext ctx) {
//		String integ=ctx.INT().getText();
//		numberStack.push(Integer.valueOf(integ));
//		System.err.println("Added integ to numberStack: "+integ);
//	}
//
//
//}
//
//public class Main
//{
//    public static <MyGrammarLexer> void main(String[] args) throws Exception {
//		CharStream input = CharStreams.fromStream(System.in);
//		gen.MyGrammarLexer lexer = new gen.MyGrammarLexer(input);
//
//		CommonTokenStream tokens = new CommonTokenStream(lexer);
//
//
//		MyGrammarParser parser = new MyGrammarParser(tokens);
//
//		ParseTree tree = parser.myStart();
//
//		MyListener m = new MyListener();
//		ParseTreeWalker.DEFAULT.walk(m, tree);
//    }
//}