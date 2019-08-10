# AVATAR 
mAchine learning pipeline eVAluaTion in Automatic pipeline composition and optimisation using suRrogate model

## Introduction

The evaluation of machine learning (ML) pipelines is essential during automatic ML pipeline composition and optimisation. The previous methods such as Bayesian-based and genetic-based optimisation, which are implemented in Auto-Weka, Auto-sklearn and TPOT, evaluate pipelines by executing them. Therefore, the pipeline composition and optimisation of these methods require a tremendous amount of time that prevents them from exploring complex pipelines to find better predictive models. 
To further explore this research challenge, we have conducted experiments that showed that many of the generated pipelines are invalid and it is unnecessary to execute them to find out whether they are good pipelines.    
To address this issue, we propose a novel method to evaluate the validity of ML pipelines using a surrogate model to accelerate automatic ML pipeline composition and optimisation (AVATAR). 


## The concepts of the AVATAR

![Mapping a machine learning pipeline to its surrogate model](https://github.com/UTS-AAi/AVATAR/blob/master/docs/images/mapping_surrogate.png "Mapping a machine learning pipeline to its surrogate model")
<p align="center"><strong>Figure 1: Mapping a machine learning pipeline to its surrogate model</strong></p>

The AVATAR evaluates a ML pipeline by mapping this pipeline to its surrogate pipeline and evaluating this surrogate pipeline. Figure 1 illustrates how a machine learning pipeline is mapped to its surrogate model (a Petri net). The reason is that it is fast to verify the validity of a Petri-net based simplified ML pipeline.

![Machine learning pipeline construction using surrogate model](https://github.com/UTS-AAi/AVATAR/blob/master/docs/images/illustrate_pipeline_construction.png "Machine learning pipeline construction using surrogate model")
<p align="center"><strong>Figure 2: Machine learning pipeline construction using surrogate model</strong></p>

The AVATAR evaluates a ML pipeline by mapping this pipeline to its surrogate pipeline and evaluating this surrogate pipeline. Figure 2 illustrates how to construct valid pipelines using the AVATAR.


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

* [Tien-Dung Nguyen](https://www.researchgate.net/profile/Tien_Dung_Nguyen5) (TienDung.Nguyen-2@student.uts.edu.au)
* [Professor Bogdan Gabrys](http://bogdan-gabrys.com/) (Bogdan.Gabrys@uts.edu.au) 
* [Associate Professor Katarzyna Musial](http://katarzyna-musial.com/) (Katarzyna.Musial-Gabrys@uts.edu.au)
* Tomasz Maszczyk (Tomasz.Maszczyk@uts.edu.au)

