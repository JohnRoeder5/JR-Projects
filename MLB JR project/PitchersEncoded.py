
from fuzzywuzzy import fuzz, process
import pandas as pd 
import numpy as np
import ReadInFile as read
import preprocessing as pre
import FixNamesCreatDict
import CreateOppDictionary
import Clean 
from checPitchersforDeput import debutCheck
from remoevUnwantedPitchers import removeUnwantedPitchers

def processTheList(pitchersTonight): 
    processedList = []
    for word in pitchersTonight: 
        nw = word.lower()
        nextW = nw.title()
        processedList.append(nextW)  
    return processedList  

#takes pitchers tonight which is list of pitcher s from user separated by commmas. 
#gets pDict which is the pitchers dictionary with corresponding encode values
#returns list of the pitchers encoded value
def getPitcherEncodedVal(pitchersTonight, pnDict, homeawayList): 
    processedList = processTheList(pitchersTonight)
    #print("pitchers tonight: ",processedList)
    # #list of pitchers we dont know
    unwanteds = []
    pns = []
    pEncodeds = []
    #go through pitchers get encoded vals 
    for guy in processedList: 
        #print(guy)
        bestmatch = process.extractOne(guy, choices=pnDict.keys())
            #will return the pitcjer name and the match scoreS
        if bestmatch[-1] > 80: 
            #print(bestmatch)
            pn = bestmatch[0]
            #print(pn)
            encodedName = pnDict[pn]
            # print(encodedName)
            # print("pitcher name: " + pn)
            # print("encoded value: ", encodedName)
            pns.append(pn)
            pEncodeds.append(encodedName)
           #need to figure out how to address multipel unknown or debuted pitchers 
        else: 
             #need to check for debuted or unknown pitchers. 
            #easiest way is to check and then if false add to the pnDict. 
            # for pitcher in processedList:
            #print(pitcher)
            debutorNot = debutCheck(guy, pnDict)
            if not debutorNot:
                unwanteds.append(guy)
                #if its true then the pitche ris in the dictionary and is accounted for
    #print("This is the unwanteds list: ", unwanteds)
            
            #second part of function
            
    #newPitchersTonight = removeUnwantedPitchers(unwanteds,processedList)
    #print("Pitchers to get encodes for: ",newPitchersTonight)
   
    return pns, pEncodeds, unwanteds