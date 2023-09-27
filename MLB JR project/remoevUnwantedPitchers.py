import pandas as pd 
import numpy as np

def removeUnwantedPitchers(unwanteds, listofPitchers): 
    for i in unwanteds: 
        if i in listofPitchers: 
            listofPitchers.remove(i)
    return listofPitchers