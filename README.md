[![Build Status](https://dev.azure.com/LiamPringle/LiamPringle/_apis/build/status/pringlel.experiment-annotations?branchName=master)](https://dev.azure.com/LiamPringle/LiamPringle/_build/latest?definitionId=1&branchName=master) [![codecov](https://codecov.io/gh/pringlel/experiment-annotations/branch/master/graph/badge.svg)](https://codecov.io/gh/pringlel/experiment-annotations)

# What is this?
This is an additional annotation for spring boot that allows you to configure AB Experiments to spring bean methods
It is comprised of one annotation '@ExperimentOf' that takes 3 required parameters 

- id
- property
- variant

**Id** is a unique number to identify a specific experiment

**Property** is an application property/toggle to manage switching the experiment on and off

**Variant** is a singular letter to determine which side of the experiment the method is

```java
    @Service
    public class ExampleServiceImpl implements ExampleService {
    
    @ExperimentOf(id = 1, property = "experiment.toggle.property", variant = ExperimentVariant.A)
    public int addition(int number, int otherNumber) {
        return number + otherNumber;
       }
        
     @ExperimentOf(id = 1, property = "experiment.toggle.property", variant = ExperimentVariant.B)
     public int subtraction(int number, int otherNumber) { 
        return number - otherNumber;
       }
    }

```
> This example shows how you would create an experiment on the method addition using the property `experiment.toggle.property` to toggle the experiment, an experiment id of 1 and which methods correspond to which variants.

# Getting Started

### ---Under Construction---

