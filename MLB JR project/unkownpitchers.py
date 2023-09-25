#gets the unknown pitchers and creates dataframes based off of results for pitchers with 99 days rest, so only grabs opening day guys or debuting guys. 
import pandas as pd 
import numpy as np

def shadowRealm(df):
     #we need to create a dataframe based off of the avgs of the whole entire dataset. 
            #act like any pitcher with a 99 days rest is either a rookie or making their debut. so grab all rows with 99 and 
            #get the avg. call this the avg for that pitcher. 
            headers = df.columns.values
            createDF = pd.DataFrame(columns=headers)
            valWEwant = 99
            debutPitchers = df.query("Days_Rest == @valWEwant")
            #print(debutPitchers)
            debutAVGs = pd.DataFrame(debutPitchers.mean())
            rowVector=debutAVGs[0].values.reshape(1, -1)
            #print(len(rowVector[0]))
            createDF.loc[len(createDF)] = rowVector[0]
            #pname index used as a counter type thing to keep track of indices
          #   print("dataframe for debuts")
          #   print(createDF)
            return createDF
           