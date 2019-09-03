# AVATAR 
mAchine learning pipeline eVAluaTion in Automatic pipeline composition and optimisation using suRrogate model




## Experimental results


| Dataset      | Total Evaluated Pipeline | Invalid Pipelines | AVATAR - Average Pipeline Evaluation Time (ms) | T-method - Average Pipeline Evaluation Time (ms) |
|--------------|--------------------------|-------------------|--------------------------------------------|------------------------------------------------|
| abalone      | 1,780                     | 683               | 4                                          | 24,267                                         |
| car          | 11,204                    | 4387              | 9                                          | 3,845                                          |
| convex       | 680                      | 250               | 221                                        | 64,093                                         |
| germancredit | 11,765                    | 4557              | 12                                         | 3,659                                          |
| winequality  | 3,227                     | 1276              | 13                                         | 13,373                                         |

The above table compares the average pipeline evaluation time between the AVATAR and the traditional method (T-method) which evaluates pipelines by executing them. We generate random pipelines with a random number of components from 1 to 6. The predictors are always put at the end of the pipelines. The time budget for each run with one dataset is 12 hours. Each random pipeline is evaluated with both the AVATAR and T-method. The results show that the AVATAR significantly reduces the evaluation time of ML pipelines in comparison with T-method.

## Contributors
The AVATAR is developed at Advanced Analytics Institute (AAi), University of Technology Sydney (UTS), Australia.

* Tien-Dung Nguyen (TienDung.Nguyen-2@student.uts.edu.au)
* Professor Bogdan Gabrys (Bogdan.Gabrys@uts.edu.au) 
* Associate Professor Katarzyna Musial (Katarzyna.Musial-Gabrys@uts.edu.au)
* Dr. Tomasz Maszczyk (Tomasz.Maszczyk@uts.edu.au)

