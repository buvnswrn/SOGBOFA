package rddl.policy;

import java.util.*;

import rddl.EState;
import rddl.EvalException;
import rddl.RDDL;
import rddl.RDDL.*;
import rddl.State;
import rddl.parser.parser;
import rddl.viz.GenericScreenDisplay;
import rddl.viz.NullScreenDisplay;
import rddl.viz.StateViz;
import rddl.RDDL.INSTANCE;
import rddl.RDDL.ENUM_TYPE_DEF;
import rddl.RDDL.LCONST;
import rddl.RDDL.LVAR;
import rddl.RDDL.PVARIABLE_ACTION_DEF;
import rddl.RDDL.PVARIABLE_DEF;
import rddl.RDDL.PVAR_INST_DEF;
import rddl.RDDL.PVAR_NAME;
import util.Permutation;

/**
 * RDDL: Implements a random policy for a domain with concurrent actions
 *       (allows mixed action types)
 * 
 * @author Tom Walsh (thomasjwalsh@gmail.com)
 * @author Scott Saner (ssanner@gmail.com)
 * @version 11/7/10
 *
 **/

public class AALLPolicy extends Policy {
		
	//public int NUM_CONCURRENT_ACTIONS = 8; // Max number of non-default concurrent actions
	public int MAX_INT_VALUE = 5; // Max int value to use when selecting random action
	public double MAX_REAL_VALUE = 5.0d; // Max real value to use when selecting random action
	
	public AALLPolicy () { 
		super();
	}
	
	public AALLPolicy(String instance_name) {
		super(instance_name);
	}

	
	
	public void setActionMaxIntValue(int max_int_value) {
		MAX_INT_VALUE = max_int_value;
	}
	
	public void setActionMaxRealValue(double max_real_value) {
		MAX_REAL_VALUE = max_real_value; 
	}
	
	public ArrayList<PVAR_INST_DEF> getActions(State s) throws EvalException {
		
		throw new EvalException("ALLPolicy-getActions: only support aggregate state");
	}
	
	public ArrayList<PVAR_INST_DEF> getActions(EState s) throws EvalException {
		
		HashMap<PVAR_NAME, HashMap<ArrayList<LCONST>, Double>> availability = new HashMap<PVAR_NAME, HashMap<ArrayList<LCONST>, Double>>();
		
		//all set to 0
		for(PVAR_NAME act : s._alActionNames){
			ArrayList<ArrayList<LCONST>> terms = s.generateAtoms(act);
			availability.put(act, new HashMap<ArrayList<LCONST>, Double>());
			for(ArrayList<LCONST> term : terms){
				availability.get(act).put(term, 0.0);
			}
		}
		
		//check availability singly
		double passed_constraints = 1;
		double sumOfAail = 0;
		ArrayList<PVAR_INST_DEF>  possibleSingleActions = new ArrayList<PVAR_INST_DEF>();
		for(PVAR_NAME actionName : s._alActionNames){
			ArrayList<ArrayList<LCONST>> terms = s.generateAtoms(actionName);
			for(ArrayList<LCONST> term : terms){
				ArrayList<PVAR_INST_DEF> singleAction = new ArrayList<PVAR_INST_DEF>();
				PVAR_INST_DEF theSingleAction = new PVAR_INST_DEF(actionName._sPVarName, true, term);
				singleAction.add(theSingleAction);
				passed_constraints = s.checkStateActionConstraints(singleAction);
				HashMap<ArrayList<LCONST>, Double> pred_assign = availability.get(actionName);
				pred_assign.put(term, passed_constraints);
				sumOfAail += passed_constraints;
				if( passed_constraints != 0 ){
					possibleSingleActions.add(theSingleAction);
				}
			}
		}
		
		if(s._nMaxNondefActions > 1){
			//put them into array
			PVAR_INST_DEF arraySigAct[] = new PVAR_INST_DEF[possibleSingleActions.size()];
			for(int i = 0; i < possibleSingleActions.size(); i ++ ){
				arraySigAct[i] = possibleSingleActions.get(i);
			}
			
			//find all possible combinations
			ArrayList<List<PVAR_INST_DEF[]>> actionList = new ArrayList<List<PVAR_INST_DEF[]>>();
			for(int m = 1; m <= s._nMaxNondefActions; m ++){  
				AssemblyDemo c = new AssemblyDemo();
		        actionList.add(c.combination(arraySigAct, m));
			}
			
			//check availability of combinatorial actions
			//add availability to single action involved
	        for(int i = 1; i < s._nMaxNondefActions; i ++){
	        	List<PVAR_INST_DEF[]> list = actionList.get(i);
	        	for(int j = 0; j < list.size(); j ++){
	        		PVAR_INST_DEF[] caa = list.get(j);
	        		ArrayList<PVAR_INST_DEF> combinatorialAction = new ArrayList<PVAR_INST_DEF>();
	        		for(int t = 0; t < caa.length; t ++){
	        			combinatorialAction.add(caa[t]);
	        		}
	        		double avail = s.checkStateActionConstraints(combinatorialAction);
	        		for(int t = 0; t < caa.length; t ++){
	        			HashMap<ArrayList<LCONST>, Double> pred_assign = availability.get(caa[t]._sPredName);
	        			double originalAvail = pred_assign.get(caa[t]._alTerms);
	        			originalAvail += avail;
	        			pred_assign.put(caa[t]._alTerms, originalAvail);
	        			sumOfAail += avail;
	        		}
	        	}
	        }
		}
	        
		// normalize at single action level
		for (PVAR_NAME act : s._alActionNames) {
			ArrayList<ArrayList<LCONST>> terms = s.generateAtoms(act);
			for (ArrayList<LCONST> term : terms) {
				double ori = availability.get(act).get(term);
				availability.get(act).put(term, ori / sumOfAail);
			}
		}
		
		ArrayList<PVAR_INST_DEF> finalActions = new ArrayList<PVAR_INST_DEF>();
		for (PVAR_NAME act : s._alActionNames) {
			ArrayList<ArrayList<LCONST>> terms = s.generateAtoms(act);
			for (ArrayList<LCONST> term : terms) {
				if(availability.get(act).get(term) != 0){
					finalActions.add(new PVAR_INST_DEF(act._sPVarName, (Object)availability.get(act).get(term), term));
				}
			}
		}
		
		return finalActions;
	}

}


class AssemblyDemo {
    /**  
     * @param a:�������  
     * @param m:������ϳ���  
     * @return :���п��ܵ���������б�  
     */
    List<PVAR_INST_DEF[]> combination(PVAR_INST_DEF[] a, int m) {
        AssemblyDemo c = new AssemblyDemo();
        List<PVAR_INST_DEF[]> list = new ArrayList<PVAR_INST_DEF[]>();
        int n = a.length;
        if( n == 0){
        	return list;
        }
        if(m == n){
        	list.add(a);
        	return list;
        }
        boolean end = false; // �Ƿ������һ����ϵı��   
        // ���ɸ������顣���ȳ�ʼ����������ǰn��Ԫ����1����ʾ��һ�����Ϊǰn������   
        int[] tempNum = new int[n];
        for (int i = 0; i < n; i++) {
            if (i < m) {
                tempNum[i] = 1;
  
            } else {
                tempNum[i] = 0;
            }
        }
       // printVir(tempNum);// ��ӡ�׸���������   
        list.add(c.createResult(a, tempNum, m));// ��ӡ��һ��Ĭ�����   
        int k = 0;//���λ   
        while (!end) {
            boolean findFirst = false;
            boolean swap = false;
            // Ȼ�������ɨ������Ԫ��ֵ��"10"��ϣ��ҵ���һ��"10"��Ϻ����Ϊ"01"   
            for (int i = 0; i < n; i++) {
                int l = 0;
                if (!findFirst && tempNum[i] == 1) {
                    k = i;
                    findFirst = true;
                }
                if (tempNum[i] == 1 && tempNum[i + 1] == 0) {
                    tempNum[i] = 0;
                    tempNum[i + 1] = 1;
                    swap = true;
                    for (l = 0; l < i - k; l++) { // ͬʱ������ߵ�����"1"ȫ���ƶ������������ˡ�   
                        tempNum[l] = tempNum[k + l];
                    }
                    for (l = i - k; l < i; l++) {
                        tempNum[l] = 0;
                    }
                    if (k == i && i + 1 == n - m) {//�����һ��"1"�ո��ƶ�����n-m+1��λ��,����ֹ����Ѱ��   
                        end = true;
                    }
                }
                if (swap) {
                    break;
                }
            }
           // printVir(tempNum);// ��ӡ��������   
            list.add(c.createResult(a, tempNum, m));// �����һ��Ĭ�����   
        }
        return list;
    }
  
    // ���ݸ��������ԭʼ�������ɽ������   
    public PVAR_INST_DEF[] createResult(PVAR_INST_DEF[] a, int[] temp, int m) {
        PVAR_INST_DEF[] result = new PVAR_INST_DEF[m];
        int j = 0;
        for (int i = 0; i < a.length; i++) {
            if (temp[i] == 1) {
                result[j] = a[i];
                //System.out.println("result[" + j + "]:" + result[j]);
                j++;
            }
        }
        return result;
    }
  
    // ��ӡ��������   
    public void print(List<int[]> list) {
        //System.out.println("������Ͻ��Ϊ:");
        for (int i = 0; i < list.size(); i++) {
            int[] temp = (int[]) list.get(i);
            for (int j = 0; j < temp.length; j++) {
                java.text.DecimalFormat df = new java.text.DecimalFormat("00");//�������ʽ��   
                //System.out.print(df.format(temp[j]) + " ");
            }
           // System.out.println();
        }
    }
  
    // ��ӡ��������ķ���   
    public void printVir(int[] a) {
        //System.out.println("���ɵĸ�������Ϊ��");
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i]);
        }
        //System.out.println();
    }
}

