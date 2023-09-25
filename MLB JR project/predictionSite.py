from fuzzywuzzy import fuzz, process
import pandas as pd 
import numpy as np
import ReadInFile as read
import preprocessing as pre
import FixNamesCreatDict
import CreateOppDictionary
import Clean
import PitcherCAREERAVGinfo as avg
import PitchersEncoded as PE
import OppEncodedValue as op
import pitcherAVGSagainstgivenTeam as PAAGT
import joblib
from checPitchersforDeput import debutCheck
import unkownpitchers 
import remoevUnwantedPitchers
from sklearn.preprocessing import StandardScaler
import pickle
import mlbScrape
import accuracyResults

#need to create dataframe containing info that corresponds to data that model is trained on. 
#need to take it from the website and use pitcher season avgs. somehow need to add the features created by yourself. 
#nly values that the user will enter is the pitcher and the team theyre playing against
dataset =pd.read_excel("USESEPT04.xlsx")
#pre.preprocess(dataset)

#get pn dictionary
pnDict = FixNamesCreatDict.fixnamesCreateDict(dataset)
# print("pnDict")
# print(pnDict)
#get opponent team names and encoded value dictionaryand
oppDictionary = CreateOppDictionary.OppDict(dataset)
# print("oppDict")
# print(oppDictionary)
#DROP THE BAD FEATURES 
#, "Ground_Balls", "Fly_Balls", "Line_Drives", "PopUps", "Unkwn_out"
featuresToDrop = ['Pitcher_Name', 'year', 'Team', 'Opp', 'G_Career', 'G_team', 'Date', 'Result', 'Inngs_Played', "Unkwn_out", "Stolen_Bases_givenUP", "Caught_Stealing", "Pickoffs", "Intentional_Walks", "GrndDubPlays", "Sac_Flies", "avgchampLI", "avg_LeverageIndex", "champWinProbAdded", "Strike_Outs", "Pitcher_Name.1", "year.1"]

features = dataset.drop(featuresToDrop, axis = 1)
print(list(features.columns))
print(features.head())
newdf = Clean.clean_dataset(features)
# print(newdf.head())
# print(list(newdf.columns))


#need to create dataframe containing info that corresponds to data that model is trained on. 
#need to take it from the website and use pitcher season avgs. somehow need to add the features created by yourself. 
#nly values that the user will enter is the pitcher and the team theyre playing against
#pitchersTonight = input("Enter a list of the pitchers names that are playing tonight, separated by commas: ")
pitchersTonight, oppsTonight, homeAwayList = mlbScrape.mlbScrape()
# print(pitchersTonight)
# print(oppsTonight)
for space in pitchersTonight: 
     space.replace(" ", "")
for otherSpace in oppsTonight: 
     otherSpace.replace(" ", "")

pitchersHAmapping = dict(zip(pitchersTonight, homeAwayList))
pTeamsmapping= dict(zip(pitchersTonight, oppsTonight))
print(pTeamsmapping)
#print(pitchersHAmapping)
#userList = pitchersTonight.split(',')
#check datatypes of lists to make sure that they are strings dylan cease,shohei ohtani,adam wainwright,gerrit cole
# dylan cease,shohei ohtani
# for item in userList: 
#     item_type = type(item)
#     print(f"Item: {item}, Type: {item_type}")




# get encoded values for pitcher names from user. these are what will be added to the pitcher info df. 
# could need to convert these the col vectors??
pitcherName, pEncodes, unwanteds = PE.getPitcherEncodedVal(pitchersTonight, pnDict, homeAwayList)
# print(pitcherName)
# print(pEncodes)

Opp, oppEncodes =op.oppEncodedVals(oppsTonight, oppDictionary)   




#hould be able to call someting from PitcherSeasonAVGinfo on the list of encoded pitchers. 
#get the avgs for the career of the guys entered into the list
#this is a dataframe the same size as the models that should contain season aavgs for each  pitcher. now we want to use this dataframe to replacre the guessing vals with what wwe want to  enter. 
#like [] opponent, 'Home/Away', days rest, ]
#print("enter all of this info separated by commas")
feedmodelDF = avg.pitcherdataframe(newdf, pEncodes, pitcherName)
# print(feedmodelDF)
#So far we have all the avg values for all every feature in the df. 
#need ot replace values with waht we want to enter. 
# for p in userList:
perGivenTeamDF = PAAGT.avgsPerIndTeamDF(newdf, pEncodes, oppEncodes)
# print(perGivenTeamDF)



#now add back the pitchers that were sent to the shadow realm
if unwanteds:
     #need to add to the pnDict to account for an unknown pitcher. 
     #might be easiest to send them to get their own values calculated then combine dataframes at the end. 
     #but first send then remove them from the list. 
     unknownPdftoAdd = pd.DataFrame()
     for i in unwanteds: 
          unknownPdftoAdd_1 = unkownpitchers.shadowRealm(newdf)
          unknownPdftoAdd=pd.concat([unknownPdftoAdd, unknownPdftoAdd_1], ignore_index = True)
     feedmodelDF=pd.concat([feedmodelDF, unknownPdftoAdd], ignore_index = True)
     perGivenTeamDF=pd.concat([perGivenTeamDF, unknownPdftoAdd], ignore_index = True)
# print(feedmodelDF.head())
# print(perGivenTeamDF.head())

# print(userList)dylan cease
# print(unwanteds)
 
#get list complete with all the pitchers 
newList = []    
#before we add to this we need to remove the unwanted pitchers again. 
newList = PE.processTheList(pitchersTonight)
#have to process te lsit so it looks the same as the unwnnteds
updatedList = remoevUnwantedPitchers.removeUnwantedPitchers(unwanteds, newList)
#add them all back in their proper order
FinList= updatedList + unwanteds

home_away_list = [pitchersHAmapping.get(pitcher_name, 0) for pitcher_name in FinList]

# print(home_away_list)
# print("this is the Final List: ", FinList)
# print(len(FinList))
#iterate through the dataframes and fill in the correct info
for info in range(len(FinList)): 
     daysRest = int(input("Enter the amount of days of rest that "+FinList[info]+" has had between outings: "))
     
     #have the season df anf the per team df, need to alter values in both of these...
     #change at the correct row and column too
     
     #ALTER HOME AWAY
     feedmodelDF.at[info, 'Home/Away'] = home_away_list[info]
     perGivenTeamDF.at[info, 'Home/Away'] = home_away_list[info]
     #ALTER DAYS REST
     feedmodelDF.at[info, 'Days_Rest'] = daysRest
     perGivenTeamDF.at[info, 'Days_Rest'] = daysRest
     #ALTER THE OPPONENT
     feedmodelDF.at[info, 'Opp_Encoded'] = oppEncodes[info]
     perGivenTeamDF.at[info, 'Opp_Encoded'] = oppEncodes[info]
     
     
     
print(feedmodelDF)
print(perGivenTeamDF)
print(feedmodelDF.shape)
print(perGivenTeamDF.shape)






#need to change these dataframes into arrays for the model. 
newfeedModelasArrs = np.asarray(feedmodelDF)
#print(newfeedModelasArrs)
newperGivenTeamasArrs = np.array(perGivenTeamDF)
#print(newperGivenTeamasArrs)

with open('scalerSaved.pk2', 'rb') as scalarSaved: 
     scaler = pickle.load(scalarSaved)
     
testfeed = scaler.transform(newfeedModelasArrs)
testperGiven = scaler.transform(newperGivenTeamasArrs)

loaded_pca_model = joblib.load('pca_model.pkl')  # Load the PCA model
testfeedPCA = loaded_pca_model.transform(testfeed) 
testpergivenPCA = loaded_pca_model.transform(testperGiven) 

JohnnysLocks = joblib.load('sbmodelDNN.pk1')

careerBased = JohnnysLocks.predict(testfeedPCA)
#print("Prediction on the Career based dataframe: " , careerBased)
perTeamBased = JohnnysLocks.predict(testpergivenPCA)
player_predicts_Career = list(zip(FinList, careerBased))
player_predicts_PerTeam = list(zip(FinList, perTeamBased))

teamsInOrderList= [pTeamsmapping.get(pitcher_name, 0)for pitcher_name in FinList]
print(teamsInOrderList)
count=0
print("Career Based Predictions: ")
for player, prediction in player_predicts_Career:
    print(f"{player}: {np.round(prediction, 2)} against {teamsInOrderList[count]}")
    count =count+1
    
print()
count2=0
print("Predictinos based off performance against given team: ")
for player, prediction in player_predicts_PerTeam:
    print(f"{player}: {np.round(prediction, 2)} against {teamsInOrderList[count2]}")
    count2=count2+1


  #SHOULD CALL THE ACCURACY FUNCTION HERE TO UPDATE CSV WITH PREDICTIONS. 
     

    
