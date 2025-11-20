package CFG;

import IR.*;
import java.util.*;

public class CFGNode {
    int lineNumber;
    IRcommand command;
    List<CFGNode> successors;
    List<CFGNode> predecessors;
    Set<String> IN;
    Set<String> OUT;
    Set<String> GEN;
    Set<String> USE;
    static Set<String> globallyUsedVars = new HashSet<>();  //Track globally used variables
    static Set<String> declaredVars = new HashSet<>();  //  Track declared variables globally

    public CFGNode(int lineNumber, IRcommand command) {
        this.lineNumber = lineNumber;
        this.command = command;
        this.successors = new ArrayList<>();
        this.predecessors = new ArrayList<>();
        this.IN = new HashSet<>();
        this.OUT = new HashSet<>();
        this.GEN = new HashSet<>();
        this.USE = new HashSet<>();
    }

    public void addSuccessor(CFGNode succ) {
        if (!successors.contains(succ)) {
            successors.add(succ);
            succ.predecessors.add(this);
        }
    }

    public void computeGenUse() {
        Set<String> dependencies = getDependencies(command);
        Set<String> definedVars = getDefinedVariables(command);

        for (String dep : dependencies) {
            boolean isUnInit = command.isUninitializedYet(dep);//check if var is initialized in this stage
            System.out.println("CFGNode.computeGenUse() -> Checking: " + dep + " -> Initialized: " + !isUnInit);

            //if (!GEN.contains(dep) && isUnInit) {
            if (!GEN.contains(dep)) {
                USE.add(dep);
                globallyUsedVars.add(dep);  //  Track globally
                System.out.println(" USE Set: Adding Possibly Uninitialized " + dep);
            }
        }

        for (String var : definedVars) {
            GEN.add(var);
            declaredVars.add(var);  //  Track declared variables globally
            System.out.println(" GEN Set: Assigned Variable " + var);
        }
    }

    private Set<String> getDependencies(IRcommand command) {
        Set<String> dependencies = new HashSet<>();

        if (command instanceof IRcommand_Store) {
            IRcommand_Store storeCmd = (IRcommand_Store) command;
            dependencies.addAll(storeCmd.getSrc().getName());
            //dependencies.add(storeCmd.getVarName());
            System.out.println(" Dependency Found (Store): " + dependencies);
        } 
        else if (command instanceof IRcommand_Load) {
            IRcommand_Load loadCmd = (IRcommand_Load) command;
            dependencies.add(loadCmd.getVarName());
            System.out.println(" Dependency Found (Load): " + loadCmd.getVarName());
        }
        else if (command instanceof IRcommand_Binop_EQ_Integers) { 
            IRcommand_Binop_EQ_Integers binopCmd = (IRcommand_Binop_EQ_Integers) command;
            dependencies.addAll(binopCmd.getLeftOperand().getName()); 
            dependencies.addAll(binopCmd.getRightOperand().getName()); 
            System.out.println(" Dependency Found (Binop EQ): " + binopCmd.getLeftOperand().getName() + "=" + binopCmd.getRightOperand().getName());
        }
        else if (command instanceof IRcommand_Binop_Mul_Integers) { 
            IRcommand_Binop_Mul_Integers binopCmd = (IRcommand_Binop_Mul_Integers) command;
            dependencies.addAll(binopCmd.getLeftOperand().getName()); 
            dependencies.addAll(binopCmd.getRightOperand().getName()); 
            System.out.println(" Dependency Found (Binop Mul): " + binopCmd.getLeftOperand().getName() + "*" + binopCmd.getRightOperand().getName());
        }
        else if (command instanceof IRcommand_Binop_LT_Integers) { 
            IRcommand_Binop_LT_Integers binopCmd = (IRcommand_Binop_LT_Integers) command;
            dependencies.addAll(binopCmd.getLeftOperand().getName()); 
            dependencies.addAll(binopCmd.getRightOperand().getName()); 
            System.out.println(" Dependency Found (Binop LT): " + binopCmd.getLeftOperand().getName() + "<" + binopCmd.getRightOperand().getName());
        }
        else if (command instanceof IRcommand_Binop_GT_Integers) { 
            IRcommand_Binop_GT_Integers binopCmd = (IRcommand_Binop_GT_Integers) command;
            dependencies.addAll(binopCmd.getLeftOperand().getName()); 
            dependencies.addAll(binopCmd.getRightOperand().getName()); 
            System.out.println(" Dependency Found (Binop GT): " + binopCmd.getLeftOperand().getName() + "> " + binopCmd.getRightOperand().getName());
        }
        else if (command instanceof IRcommand_PrintInt) { 
            IRcommand_PrintInt printcmd = (IRcommand_PrintInt) command;
            dependencies.addAll(printcmd.getSrc().getName());
            System.out.println(" Dependency Found (printint): " + printcmd.getSrc().getName());
        }
        else {
            System.out.println(" No Dependency Found for command: " + command.getClass().getSimpleName());
        }
        System.out.println("uninitialized so far: " + command.getUninitialized());

        return dependencies;
    }

    private Set<String> getDefinedVariables(IRcommand command) {
        Set<String> definedVars = new HashSet<>();

        if (command instanceof IRcommand_Store) {
            IRcommand_Store storeCmd = (IRcommand_Store) command;
            definedVars.add(storeCmd.getVarName());
        }
        return definedVars;
    }
}
