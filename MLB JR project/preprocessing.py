import numpy as np
import pandas as pd
from sklearn.preprocessing import LabelEncoder
import sys

#do all preprocessing for the dataset
def preprocess(df): 
    #PROCESSING TO FIX NAN VALUES IN HOMEAWAY COLUMN. ALSO CHANGES VALUES TO 0 and 1 SO MODEL CAN USE 
    #make the column name Home(1)/Away(0)....) 
        #manual encoding
    #print("initial", df.shape)
    df.drop(df[df['Inngs_Pitched']< 1.0].index, inplace = True)
    df.replace([np.inf, -np.inf], np.nan, inplace=True)
# #
    
    pns = list(df['Pitcher_Name'])
    pns_as_strings = [str(item) for item in pns]
    pitcherNs = []  
    for name in pns_as_strings: 
        fixed = name.replace('\xa0', ' ').replace('�', '').replace('\xad', '')
        
        pitcherNs.append(str(fixed))
    #print(pitcherNs)
    
    df['Pitcher_Name'] = pitcherNs
    #print(df['Pitcher_Name'].unique())
    
    df['Home/Away'] = df['Home/Away'].replace('@', 0)
    df['Home/Away'] = df['Home/Away'].fillna(1)
    #print(df['Home/Away'])
    
    
    
    df['year'] = df['Date'].str.split(',').str[-1].astype(int)
    getCol = df.pop('year')
    df.insert(1, 'year', getCol)
    #print(df['year'].head())
    

    #PROCESSING FOR DAYS REST TO FIX 99 OUTLIERS
    #99 days rest is an outlier and could affect the effectiveness f the models use of Days_rest. 
        #replace 99 with the mean days rest value. 
    #mean could be affected by the 99's ?????
    # df['Days_Rest'] = df['Days_Rest'].replace(99, np.nan)
    # mean = df['Days_Rest'].mean()
    # df['Days_Rest'] = df['Days_Rest'].fillna(mean)
    # #print(df['Days_Rest'])


        #PROCESSING FOR MISSING VALUES USING MEAN OF THE COLUMNs
    df['Pitches_Thrwn']=df['Pitches_Thrwn'].fillna(df['Pitches_Thrwn'].mean())
    df['Strikes_Thrown'] = df['Strikes_Thrown'].fillna( df['Strikes_Thrown'].mean())
    df['Strikes_Looking'] = df['Strikes_Looking'].fillna( df['Strikes_Looking'].mean())
    df['Strikes_Swinging'] = df['Strikes_Swinging'].fillna( df['Strikes_Swinging'].mean())
    df['Ground_Balls'] = df['Ground_Balls'].fillna( df['Ground_Balls'].mean())
    df['Fly_Balls'] = df['Fly_Balls'].fillna( df['Fly_Balls'].mean())
    df['Line_Drives'] = df['Line_Drives'].fillna( df['Line_Drives'].mean())
    df['PopUps'] = df['PopUps'].fillna( df['PopUps'].mean())
    df['Unkwn_out'] = df['Unkwn_out'].fillna( df['Unkwn_out'].mean())
    df['avg_LeverageIndex'] = df['avg_LeverageIndex'].fillna( df['avg_LeverageIndex'].mean())
    df['WinProbAdded'] = df['WinProbAdded'].fillna( df['WinProbAdded'].mean())
    df['avgchampLI'] = df['avgchampLI'].fillna( df['avgchampLI'].mean())
    df['champWinProbAdded'] = df['champWinProbAdded'].fillna( df['champWinProbAdded'].mean())
    df['Base_out_Runs_saved'] = df['Base_out_Runs_saved'].fillna( df['Base_out_Runs_saved'].mean())
    df['Bases_Balls/Walks'] = df['Bases_Balls/Walks'].fillna(df['Bases_Balls/Walks'].mean())
    df['Inngs_Pitched'] = df['Inngs_Pitched'].fillna(df['Inngs_Pitched'].mean())
    df['Hits_Allowed'] = df['Hits_Allowed'].fillna(df['Hits_Allowed'].mean())
    
    
    df['ERA'] = df['ERA'].replace('---', np.nan)
    df['ERA'] = df['ERA'].astype(float)
    #df["ERA"] = df['ERA'].replace([np.inf, -np.inf], np.nan)
    df['ERA'] = df['ERA'].fillna(df['ERA'].mean())
    # print("ERA")
    #print(df['ERA'])

    
    
    
    
    #['Fielding_Independent_Pitching']
    df['Fielding_Independent_Pitching'] = df['Fielding_Independent_Pitching'].replace('---', np.nan)
    df['Fielding_Independent_Pitching'] = df['Fielding_Independent_Pitching'].astype(float)
    #df["Fielding_Independent_Pitching"] = df['Fielding_Independent_Pitching'].replace([np.inf,-np.inf], np.nan)
    df['Fielding_Independent_Pitching'] = df['Fielding_Independent_Pitching'].fillna(df['Fielding_Independent_Pitching'].mean())
    # print("Fielding_Independent_Pitching")
    # print(df['Fielding_Independent_Pitching'])

    #PROCESSING FOR TOTAL SCORE TO PREDICT O/U 
    homeVals = df['Result'].str.split('[,-]').str[1]
    homeVals = pd.to_numeric(homeVals)
    awayVals= df['Result'].str[4:]
    awayVals= pd.to_numeric(awayVals)
    #need to convert these score to numbers
    df['Total_Score']= homeVals+awayVals
    #print(df['Total_Score'])

    
    df['Strike_Rate'] = df['Strike_Outs'] / df['Batters_Faced']
    df['Strike_Rate'] = df['Strike_Rate'].fillna(df['Strike_Rate'].mean())
    
    
    df['Swinging_Strikes_percentage'] = (df['Strikes_Swinging'] / df['Pitches_Thrwn'])  
    df['Swinging_Strikes_percentage']= df['Swinging_Strikes_percentage'].fillna(df['Swinging_Strikes_percentage'].mean())
    #print(df['Swinging_Strikes_percentage'])

    #ENCODING PITCHER NAMES SO THEY CAN BE USED BY MODEL
    pitcherNames = df['Pitcher_Name'].unique()
    #print(pitcherNames)
    encode = LabelEncoder()
    encodedPN = encode.fit_transform(df['Pitcher_Name'])
    #print(encodedPN)
    df['Encoded_PitcherNames'] = encodedPN
    #print(df['Encoded_PitcherNames'])
   # print("uniques", list(df['Encoded_PitcherNames'].unique()))
    #print(max(list(df['Encoded_PitcherNames'].unique())))


    #ENCODING OPPONENTS SO THEY CAN BE USED BY MODEL. 
    opp = df['Opp' ].unique()
    #print(opp)
    ohEncoder = LabelEncoder()
    opp_encoded = ohEncoder.fit_transform(df['Opp'])
    df['Opp_Encoded'] = opp_encoded
    # print("opp_encodeds", opp_encoded)
    # print(df['Opp_Encoded'])

    #walk rate 
    df['WalkRate']= (df['Bases_Balls/Walks'] / df['Inngs_Pitched'])*9
    df["WalkRate"] = df['WalkRate'].replace([np.inf, -np.inf], np.nan)
    df['WalkRate'] = df['WalkRate'].fillna( df['WalkRate'].mean())
    # print("WalkR")
    # print(df["WalkRate"])
    
    df['PPA'] = df['Pitches_Thrwn'] / df['Batters_Faced']
    df['PPA'] = df['PPA'].replace([np.inf, -np.inf], np.nan)
    df['PPA']= df['PPA'].fillna(df['PPA'].mean())
    
    
    #NEW VARIABLES ADDED: SO/9, Opp_BATTING_AVG/GAME, 
    #Create variable, StrikeOutsPer9 for individual games. 
    df['SO9'] = (df['Strike_Outs'] / df['Inngs_Pitched']) * 9
    #for empty cells within 
    df['SO9'] = df['SO9'].fillna( df['SO9'].mean())
    #print(df['SO9'])
     #OBA, hits allowed by batters faced... 
    df['Opp_batting_avg/game'] = df['Hits_Allowed'] / df['At_Bats']
    #for empty vals wtihtn
    df['Opp_batting_avg/game'] = df['Opp_batting_avg/game'].fillna( df['Opp_batting_avg/game'].mean())
    #print(df['Opp_batting_avg/game'])
    df['Strike_percentage'] = df['Strikes_Thrown'] / df['Pitches_Thrwn']
    df['Strike_percentage'] = df['Strike_percentage'].fillna( df['Strike_percentage'].mean())
    
     #CANNOT DO THIS RN SOMEHOW BREAKS THE MODEL
    # df.drop(df[df['Inngs_Pitched']< 1].index)
    # print("df shape ", df.shape)
    # # #walks + hits / innings pitched
    df['WHIP'] = (df['Bases_Balls/Walks'] + df['Hits_Allowed']) / (df['Inngs_Pitched'])
    df['WHIP'] = df['WHIP'].replace([np.inf, -np.inf], np.nan)
    df['WHIP'] = df['WHIP'].fillna(df['WHIP'].mean())
    # print("WHIP: ")
    # print(df['WHIP'])
    #print(df['Strike_percentage'])
    #need original So9 so that we can get the avg for each pitcher 
    # for pitcherguyEncoded in list(df['Encoded_PitcherNames'].unique()): 
    #     #print(pitcherguyEncoded)
    #     #get query of rows with individual pitchers encodeds
    #     ind = df.query("Encoded_PitcherNames == @pitcherguyEncoded")
    #     #print(indSO9)
        
    #     eraAdjusted = df['ERA'].mean() 
       
    #     #get the mean of the query
    #     indSO9Mean = ind['SO9'].mean()
    #     indOBAVG = ind['Opp_batting_avg/game'].mean()
    #     indSP = ind['Strike_percentage'].mean()
    #     #print(indSO9Mean)
    #     #get the row indexes of the rows in the query. 
    #     listIndexes = ind.index.tolist()
    #     #print(listIndexes)
    #     #figure out a way to replace all values for SO9 for a pitcher
    #     df.loc[listIndexes, 'SO9']= df.loc[listIndexes, 'SO9'] = np.nan
    #     df.loc[listIndexes, 'SO9']= df.loc[listIndexes].replace(0, np.nan)
    #     df['SO9'].fillna(indSO9Mean, inplace = True)
        
    #     # #also for the opp batting avgs
    #     df.loc[listIndexes, 'Opp_batting_avg/game']= df.loc[listIndexes, 'Opp_batting_avg/game'] = np.nan
    #     df.loc[listIndexes, 'Opp_batting_avg/game']= df.loc[listIndexes].replace(0, np.nan)
    #     df['Opp_batting_avg/game'].fillna(indOBAVG, inplace = True)
    #     # #for strike out percentage
    #     df.loc[listIndexes, 'Strike_percentage']= df.loc[listIndexes, 'Strike_percentage'] = np.nan
    #     df.loc[listIndexes, 'Strike_percentage']= df.loc[listIndexes].replace(0, np.nan)
    #     df['Strike_percentage'].fillna(indSP, inplace = True)
        
        
    #     df.loc[listIndexes, 'WalkRate']= df.loc[listIndexes, 'WalkRate'] = np.nan
    #     df.loc[listIndexes, 'WalkRate']= df.loc[listIndexes].replace(0, np.nan)
    #     df['WalkRate'].fillna(indSP, inplace = True)
        
    #     df.loc[listIndexes, 'WHIP']= df.loc[listIndexes, 'WHIP'] = np.nan
    #     df.loc[listIndexes, 'WHIP']= df.loc[listIndexes].replace(0, np.nan)
    #     df['WHIP'].fillna(indSP, inplace = True)
        #print("done")
    # print("check for SO9, Opp_batting_avg/game")
    # print(df)
        

    # rows_with_inf = df[df.isin([np.inf, -np.inf]).any(axis=1)]

    # # Remove rows with inf values
    # df = df[~df.index.isin(rows_with_inf.index)]
    #print(df.columns.values)
    
    #Drop rows with NaN values
    #df.dropna(inplace=True)
    #print("before dropping NAN", df.shape)
    df.dropna(inplace=True)
    #print("after dropping nan", df.shape )
    return df

    
    