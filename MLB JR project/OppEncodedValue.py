from fuzzywuzzy import fuzz, process
import pandas as pd 
import numpy as np
import ReadInFile as read
import preprocessing as pre
import FixNamesCreatDict
import CreateOppDictionary
import Clean

def processOPPList(list): 
    oppEncodeds = []
    for opp in list: 
        nw = opp.upper()
        oppEncodeds.append(nw)  
    return oppEncodeds

#gets pitcheropponetnes which is a lsit of the OPPS
#gets oppDictionary whihc is opponent dicitonary
#returns lit of teheopposing teams encoded values
def oppEncodedVals(PitcherOpponents, oppDictionary): 
    userOppList = processOPPList(PitcherOpponents)
    #print(userOppList)
    opponents = []
    oppList = []
    #go through opponents get encoded vals. 
    for opp in userOppList: 
        #print(opp)
        bestOppmatch = process.extractOne(opp,choices=oppDictionary.keys())
        #print(bestOppmatch)
        if bestOppmatch: 
            opponent = bestOppmatch[0]
            encodedOppName = oppDictionary[opponent]
            # print("pitcher name: "+ opponent)
            # print("encoded value: " ,  encodedOppName)
        opponents.append(opponent)
        oppList.append(encodedOppName)
    return opponents, oppList
    