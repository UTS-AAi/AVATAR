# AVATAR
mAchine learning pipeline eVAluaTion in Automatic pipeline composition and optimisation using suRrogate model 

The evaluation of machine learning (ML) pipelines is essential during automatic ML pipeline composition and optimisation. The previous methods such as Bayesian-based and genetic-based optimisation, which are implemented in Auto-Weka, Auto-sklearn and TPOT, evaluate pipelines by executing them. Therefore, the pipeline composition and optimisation of these methods require a tremendous amount of time that prevents them from exploring complex pipelines to find better predictive models. 
To further explore this research challenge, we have conducted experiments that showed that many of the generated pipelines are invalid and it is unnecessary to execute them to find out whether they are good pipelines.    
To address this issue, we propose a novel method to evaluate the validity of ML pipelines using a surrogate model to accelerate automatic ML pipeline composition and optimisation (AVATAR). 



![Mapping a machine learning pipeline to its surrogate model](https://github.com/UTS-AAi/AVATAR/blob/master/docs/images/mapping_surrogate.png "Mapping a machine learning pipeline to its surrogate model")
<p align="center"><strong>Figure 1: Mapping a machine learning pipeline to its surrogate model</strong></p>

The AVATAR evaluates a ML pipeline by mapping this pipeline to its surrogate pipeline and evaluating this surrogate pipeline. Figure 1 illustrates how a machine learning pipeline is mapped to its surrogate model (Petri net).

![Machine learning pipeline construction using surrogate model](https://github.com/UTS-AAi/AVATAR/blob/master/docs/images/illustrate_pipeline_construction.png "Machine learning pipeline construction using surrogate model")
<p align="center"><strong>Figure 2: Machine learning pipeline construction using surrogate model</strong></p>

The AVATAR evaluates a ML pipeline by mapping this pipeline to its surrogate pipeline and evaluating this surrogate pipeline. Figure 2 illustrates how to construct valid pipelines using the AVATAR.

