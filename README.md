# AVATAR
mAchine learning pipeline eVAluaTion in Automatic pipeline composition and optimisation using suRrogate model 

The evaluation of machine learning (ML) pipelines is essential during automatic ML pipeline composition and optimisation. The previous methods such as Bayesian-based and genetic-based optimisation, which are implemented in Auto-Weka, Auto-sklearn and TPOT, evaluate pipelines by executing them. Therefore, the pipeline composition and optimisation of these methods require a tremendous amount of time that prevents them from exploring complex pipelines to find better predictive models. 
To further explore this research challenge, we have conducted experiments that showed that many of the generated pipelines are invalid and it is unnecessary to execute them to find out whether they are good pipelines.    
To address this issue, we propose a novel method to evaluate the validity of ML pipelines using a surrogate model to accelerate automatic ML pipeline composition and optimisation (AVATAR). 


<iframe src="UTS-AAi/AVATAR/blob/master/docs/images/mapping_surrogate.pdf" width="100%" height="500px">