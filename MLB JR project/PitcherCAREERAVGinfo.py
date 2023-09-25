import pandas as pd 
import numpy as np
import ReadInFile as read
import preprocessing as pre
import FixNamesCreatDict
import CreateOppDictionary
import Clean
from checPitchersforDeput import debutCheck
    

#get dataframe that is used by model. find all game logs of a certain pitcher. 
#then find the season avgs for that pitcher for the feature inputs. 
#use as the values to test on game to game. 

#will be given pitcher encoded values. need to get the career avgs for them. 
#  What will be manually entered by user?? 
# manual entries : [Pitcher name, opponent, 'Home/Away', days rest, ]
#first make the season averages. then the next step is to find the averages against certain teams or over a pitchers career. 

#assemble to pitcher dataframe to be fed to the model to represent a game and a prediction...
#takes inputs dataframe and the list of encoded pitcher values. 
def pitcherdataframe(df, pitchersList, pitcherName): 
    #starting w first pitcher value..
    headers = df.columns.values
    #print(len(headers))
    overallSeasonAVGsDataframe = pd.DataFrame(columns= headers)
    #print(pitcherName)
    pnameIndex = 0
    for pitcher in pitchersList: 
            #query of pitchers individual game losgs. So this is the average across their careers. 
            pitcherIndividualRows = df.query("Encoded_PitcherNames == @pitcher")
            
            pitcherAVGS = pd.DataFrame(pitcherIndividualRows.mean())
        
                #pitcher avgs is a dataframe that contains headers and pavgs. I want to turn these into rows of the form 
                # H E A D E R S
            # shohei ohtani : stat stat, sta, stat, 
            # dylan cease : stat, stasa, stat, 
            # # -
            #need to access the pitcher column names and transpose it. 
            rowVector=pitcherAVGS[0].values.reshape(1, -1)
            #print(len(rowVector[0]))
            overallSeasonAVGsDataframe.loc[len(overallSeasonAVGsDataframe)] = rowVector[0]
            #pname index used as a counter type thing to keep track of indices
            pnameIndex = pnameIndex+1
            #print(overallSeasonAVGsDataframe)
    return overallSeasonAVGsDataframe
        
        