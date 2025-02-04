[![](https://jitpack.io/v/buvnswrn/SOGBOFA.svg)](https://jitpack.io/#buvnswrn/SOGBOFA)

-------------------------------------------------------------------------
OVERVIEW

SOGBOFA is a state-of-the-art stochastic online planning algorithm that works well in MDPs that have large state and action spaces. It 
uses the idea of "Aggregate Simulation" to build a computation graph which symbolically approximates the Q value of actions given the 
current state, and searches the action space very efficiently using gradient updates. It supports various action constraints defined in 
first-order predicates. It directly works with MDP domains defined in RDDL, and is implemented based on the original code of the RDDL 
simulator (https://github.com/ssanner/rddlsim). 
   
Please cite our paper if you are using the planner as a baseline, and/or star the project if you like it ^_^

-------------------------------------------------------------------------
NEWS

* We fixed some bugs and added support for more action constraints after IPC2018. For more details, see our most recent ICAPS 2019 paper
   http://homes.sice.indiana.edu/rkhardon/PUB/icaps2019-LCsogbofa.pdf
  We are then able to outperform the winner of IPC2018/2014/2011 with a large margin.

* The latest version of SOGBOFA with the new extensions of "lifting" and "conformant" is the runner-up of the most recent IPC2018 
competition. For more information, see the competition web page 
   https://ipc2018-probabilistic.bitbucket.io/ 
  Note that since SOGBOFA did not fully support all the competition constraints by then, it crashed on quite some problems. The new 
code supports all the constraints from the competition.

-------------------------------------------------------------------------
RELATED PAPERS

* H. Cui, T. Keller and R. Khardon, Stochastic Planning with Lifted Symbolic Trajectory Optimization, Proceedings of the International 
Conference on Automated Planning and Scheduling (ICAPS), 2019.

* H. Cui and R. Khardon, Lifted Stochastic Planning, Belief Propagation and Marginal MAP, The AAAI-18 Workshop on Planning and Inference,
held with the AAAI Conference on Artificial Intelligence (AAAI), 2018

* H. Cui and R. Khardon, Online Symbolic Gradient-Based Optimization for Factored Action MDPs, International Joint Conference on 
Artificial Intelligence (IJCAI), 2016 

* H. Cui, R. Khardon, A. Fern, and P. Tadepalli., Factored MCTS for Large Scale Stochastic Planning. , Proceedings of the AAAI Conference
on Artificial Intelligence (AAAI), 2015

You can find other papers of interest at Hao Cui's personal page
   https://sites.google.com/view/hao-cui/home
   
-------------------------------------------------------------------------
HOW TO USE

* Prerequisites: Java SE 1.8 or higher
* Compile
  In the SOGBOFA/ directory, type command ./compile
* Run the server
  One can either run a server from the RDDLSim Project, or run the Server class compiled from our source code with the following command
  ./run_server rddlfilename-or-dir portnumber num-rounds random-seed timeout 
  For example: 
  ./run_server Domains 2323 100 1 10000
  Note that the time out is the total time (in seconds) allowed to any client connects to it.
* Run SOGBOFA
  Type the following command
  ./run_sogbofa instance-name portnumber
  example:
  ./run_sogbofa traffic_inst_mdp__10 2323
* Results
  After finishing the requested number of runs, 6 files will be recorded in the bin/ folder, with the following names
  L_C_SOGBOFA_timeout_instance-name_Score
  L_C_SOGBOFA_timeout_instance-name_depthCounter
  L_C_SOGBOFA_timeout_instance-name_sizeCounter
  L_C_SOGBOFA_timeout_instance-name_rrCounter
  L_C_SOGBOFA_timeout_instance-name_seenCounter
  L_C_SOGBOFA_timeout_instance-name_updatesCounter
  which record respectively the average score of the runs, the average depth of the search, the average size of the computation graph, 
  the average number of radnom restarts, the average number of action evaluated, the average number of gradient updates.
  The Score file has three numbers, respectively the instance index, the average cumulative reward, and the standard deviation. The other
  files each has only one number inside.

-------------------------------------------------------------------------
RDDL FILES READY TO USE

Please find all domain and instance files that we used in the ICAPS 2019 paper in the Domains/ folder. It includes all IPC2018 
competition problems, plus the elevators, traffic, skill_teaching and tamarisk from IPC2014. Of ourse you can alwalys define your own 
domains using RDDL and run our planner with them.

-------------------------------------------------------------------------
IJCAI2016 VERSION

We have a copy of the source code used in the experiments of our IJCAI2016 paper (the very first SOGBOFA paper), but for consistency we 
no longer maintain github page for that version. If you are interested, please contact us and we'll directly send you the code and 
instructions. Alternatively, you can run the latest code with no lifting or conformant, which is conceptually the same as the 2016 
version, with many bugs fixed and new constraints supported. To do so, you can type
   ./run_sogbofa_original instance-name portnumber
In this way, the names of the results files should begin with "SOGBOFA_Original" instead of "L_C_SOGBOFA".

-------------------------------------------------------------------------
OTHER WORK

Checkout the other project that uses the similar idea of "Aggregate Simulation", but work on Bayesian Inference. 
  Link (https://github.com/hcui01/AGS)
  
Checkout the POMDP planner based on aggregate simulation.
  Link (https://github.com/hcui01/SNAP)

