import pandas as pd 
import numpy as np
import ReadInFile as read
import preprocessing as pre
import FixNamesCreatDict
import CreateOppDictionary
import Clean
from checPitchersforDeput import debutCheck

#GOAL 
#goal here is to get the same sized pitcher dataframes but only with information and avgs 
#against the corresponding teams they are playing. 

#assemble to pitcher dataframe to be fed to the model to represent a game and a prediction...
#takes inputs dataframe and the list of encoded pitcher values. 
def avgsPerIndTeamDF(df, pitchersList, oppList): 
    #starting w first pitcher value..
    headers = df.columns.values
    #print(len(headers))
    pAVGsVSIndTeams = pd.DataFrame(columns= headers)
    #print(pitcherName)
    pnameIndex = 0
    #see if u can do thisidk 
    for pitcher in pitchersList: 
        #first check we can do here is to see if the pitcher is in the dict, cause if not we have no info to create his avgs...
    
        opposition = oppList[pnameIndex]
        #print(opposition)
        #if a pitcher has never played a team before just put his avg stats. 
        #query of pitchers individual game losgs. So this is the average across their careers. 
        pitcherIndividualRows = df.query("Encoded_PitcherNames == @pitcher and Opp_Encoded == @opposition")
        pitcherAVGS = pd.DataFrame(pitcherIndividualRows.mean())
        if pitcherIndividualRows.isna().all().all(): 
            #all the values are NaN
             pitcherIndividualRows =df.query("Encoded_PitcherNames == @pitcher")
             pitcherAVGS = pd.DataFrame(pitcherIndividualRows.mean())
            
        
             
            #pitcher avgs is a dataframe that contains headers and pavgs. I want to turn these into rows of the form 
            # H E A D E R S
        # shohei ohtani : stat stat, sta, stat, 
        # dylan cease : stat, stasa, stat, 
        # # -
        #need to access the pitcher column names and transpose it. 
        rowVector=pitcherAVGS[0].values.reshape(1, -1)
        #print(len(rowVector[0]))
        pAVGsVSIndTeams.loc[len(pAVGsVSIndTeams)] = rowVector[0]
        #pname index used as a counter type thing to keep track of indices
        pnameIndex = pnameIndex+1
        #print(pAVGsVSIndTeams)
    return pAVGsVSIndTeams