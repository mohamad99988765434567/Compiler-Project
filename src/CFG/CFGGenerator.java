package CFG;

import IR.*;
import java.util.*;

public class CFGGenerator {
    private List<CFGNode> nodes = new ArrayList<>();
    private Map<String, CFGNode> labelMap = new HashMap<>();
    private Set<String> uninitializedVars = new HashSet<>();
    private Set<String> invalid = new HashSet<>();
    private Set<String> invalidop = new HashSet<>();
    private Set<String> globalVars = new HashSet<>(); // Track global variable declarations

    public void generateCFG(List<IRcommand> irCommands) {
        System.out.print("-----------------------------------------generateCFG------------------------------\n");

        CFGNode prevNode = null;

        for (int i = 0; i < irCommands.size(); i++) {
            CFGNode node = new CFGNode(i, irCommands.get(i));
            nodes.add(node);

            if (irCommands.get(i) instanceof IRcommand_Label) {
                IRcommand_Label labelCmd = (IRcommand_Label) irCommands.get(i);
                labelMap.put(labelCmd.getLabelName(), node);
            }

            if (prevNode != null) {
                prevNode.addSuccessor(node);
            }
            prevNode = node;
        }

        for (CFGNode node : nodes) {
            IRcommand cmd = node.command;

            if (cmd instanceof IRcommand_Jump_Label) {
                IRcommand_Jump_Label jumpCmd = (IRcommand_Jump_Label) cmd;
                String targetLabel = jumpCmd.getLabelName();

                if (labelMap.containsKey(targetLabel)) {
                    node.addSuccessor(labelMap.get(targetLabel));
                }
            } 
            else if (cmd instanceof IRcommand_Jump_If_Eq_To_Zero) {
                IRcommand_Jump_If_Eq_To_Zero condJump = (IRcommand_Jump_If_Eq_To_Zero) cmd;
                String targetLabel = condJump.getLabelName();

                if (labelMap.containsKey(targetLabel)) {
                    node.addSuccessor(labelMap.get(targetLabel));
                }
                if (node.lineNumber + 1 < nodes.size()) {
                    node.addSuccessor(nodes.get(node.lineNumber + 1));
                }
            }
        }
    }

    public void computeGenAndUse() {
        for (CFGNode node : nodes) {
            node.computeGenUse();
        }
    }

    public void initializeVariables(List<IRcommand> irCommands) {
        Set<String> assignedVars = new HashSet<>();
        for (IRcommand cmd : irCommands) {
            uninitializedVars.addAll(cmd.getUninitialized());
        }
        System.out.println(" initial Uninitialized Variables list: " + uninitializedVars);
    }

    public void performChaoticIteration() {
        System.out.print("--------------------performChaoticIteration-------------------\n\n");
        boolean changed;
        do {
            changed = false;
            for (CFGNode node : nodes) {
                int invalidbit = 0;
                Set<String> newIN = new HashSet<>();

                for (CFGNode pred : node.predecessors) {
                    newIN.addAll(pred.OUT);
                }

                Set<String> newOUT = new HashSet<>(newIN);
                newOUT.addAll(node.GEN);

                for (String var : node.GEN) {
                    if (newIN.contains(var)) {
                        System.out.println(" newIN contain:" + var);
                        if (!node.command.isUninitializedYet(var)) {
                            System.out.println(" Chaotic Iteration: Removed " + var);
                            uninitializedVars.remove(var);
                        }
                    }
                }

                for (String var : node.USE) {
                    System.out.println(" Used var:" + var);
                    if (!newIN.contains(var)){ 
                        if(node.command.isUninitializedYet(var)) {
                            invalidbit++;
                            uninitializedVars.add(var);
                            invalid.add(var);
                            System.out.println(" Chaotic Iteration: Still Uninitialized " + var);
                        }
                    }
                    else{
                        if(node.command instanceof IRcommand_PrintInt){
                            System.out.println("uninitialized so far: " + node.command.getUninitialized());
                            System.out.println("uninitialized: " + uninitializedVars);
              
                            if(!node.command.isUninitializedYet(var)){
                                System.out.println(" Chaotic Iteration: Removed " + var);
                                uninitializedVars.remove(var);
                            }
                            else{
                                uninitializedVars.add(var);
                            }
                        }
                    }
                }
                
                //check if in store command we used uninitialized vars before use
                if(invalidbit != 0){
                    if(node.command instanceof IRcommand_Store){
                        IRcommand_Store storeCmd = (IRcommand_Store) node.command;
                        invalid.add(storeCmd.getVarName());
                    }
                }

                if (!newOUT.equals(node.OUT)) {
                    node.OUT.clear();
                    node.OUT.addAll(newOUT);
                    changed = true;
                }

                node.IN.clear();
                node.IN.addAll(newIN);
            }
            
        } while (changed);
    }

    public Set<String> getUninitializedVariables() {
        System.out.println("Final Uninitialized Variables Before Filtering: " + uninitializedVars);

        Set<String> actuallyUsedVars = new HashSet<>();
        for (CFGNode node : nodes) {
            actuallyUsedVars.addAll(node.USE);
        }

        Set<String> finalUninitializedVars = new HashSet<>(uninitializedVars);
        finalUninitializedVars.addAll(uninitializedVars);
        finalUninitializedVars.addAll(invalid);
        finalUninitializedVars.retainAll(actuallyUsedVars);
       
        // Ensure global uninitialized variables are included
        //finalUninitializedVars.addAll(globalVars);

        System.out.println("Final Uninitialized Variables After Filtering: " + finalUninitializedVars);
        return finalUninitializedVars;
    }

    private boolean insideMainFunction(List<IRcommand> irCommands, String varName) {
        boolean insideMain = false;
        for (IRcommand cmd : irCommands) {
            if (cmd instanceof IRcommand_Label) {
                IRcommand_Label labelCmd = (IRcommand_Label) cmd;
                if (labelCmd.getLabelName().equals("main")) {
                    insideMain = true;
                }
            }
            if (insideMain && cmd instanceof IRcommand_Declare) {
                IRcommand_Declare declareCmd = (IRcommand_Declare) cmd;
                if (declareCmd.getVarName().equals(varName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
