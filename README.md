gaknn
=====
Run the OptimizedKNN main methods with the arguments given below.
Optimizing
In optimizing task get the data set and divide it into two parts randomly as test and training and run the knn algorithm with different k value and weight values and get the optimal solution using genetic algorithm
Predicting
Get the test data file and run the knn algorithm with k value and weights values predicted in the optimizing task( or params file given) and predict the values for test dataset.
Parameter values
Every argument has two parts type and value. Arguments are given type followed by appropriate value
Here first two arguments are mandatory others are optional
Argument type	Parameter value	Description
-datafile	Path of training data file in csv or arff format	
-testfile	Path of test data file in csv or arff format	This file need to have same number of attributes as in training data set , so attribute need to predict also need given.
-params	Path of the parameter file which is a xml file	This is needed when predicting(p)
Parameter file need to have weight  tag which has the weight values for each attribute and k tag to have k value
Default value is given as the file created in optimizing (o) which has the name of training data set name(.prm)

-k	K value	Integer value which is the initial value to take as number of neighbors 
Default value is 1
-clsindex	Attribute index to predict value between 0 to number of attribute in dataset	Indicate which attribute need to predict
Default value is 0
-task 	o or p	O: indicate optimizing the function and get optimized values for k and weights
Default value is o
-model	c or r	C: classifier model classifying nominal values
R: regression model predict a continuous value 
Default value is c
-ptype	n or k	N : normal which use ad-hoc knn algorithm (which is good for small datasets)
K : kdtree which use kdtree data structure knn algorithm implementation (this is good for large data  sets)
Default value is n
-mutations	Integer value for number of mutations	This is used for genetic algorithm number of mutations 
Default value is 100
-population	Integer value for population size	This is used for genetic algorithm to optimizing population size to use
Default value is 20
-evals	Integer value for number of evaluations	This is used for genetic algorithm to optimize , number of evaluations to use
Default value is 1
		


