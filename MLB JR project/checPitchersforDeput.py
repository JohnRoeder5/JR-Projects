#should just check the list of pitchers to see if they are in the larger list of pitchers. 
#if not then just use the all time avgs for their stats. 
import pandas as pd 
import numpy as np

#should recieve list of pitcher names and then also the disctionary to check the corresponfging names. 
def debutCheck(pitcher, pitcherDictionary): 
    #jsut want to check and see if the names are in the dictionary so check the keys 
    if pitcher in pitcherDictionary: 
        #this would evaulate to true so do nothing
        return True
    else : 
        return False
   
    