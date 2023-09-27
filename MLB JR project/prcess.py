import numpy as np 
import pandas as pd
from sklearn.preprocessing import LabelEncoder


def processdatBih(df): 
    colList = list(df.columns[2:])
    for column in colList: 
        if column in ['last_name', 'first_name', 'pitch_hand']:
            continue
        else: 
            #do nothing 
            df[column] = df[column].fillna(df[column].mean())
        
    

    #remove spacres from first name: 
    df[' first_name'] = df[' first_name'].str.replace(' ', '')
    #print(df[' first_name'])
    
    handed = df['pitch_hand'].unique()
    #(handed)
    ohEncoder = LabelEncoder()
    opp_encoded = ohEncoder.fit_transform(df['pitch_hand'])
    df['HandednessEncoded'] = opp_encoded
    #print(df['HandednessEncoded'])
    
    # df['pitch_hand']= df['pitch_hand'].replace("L", 1).replace("R", 2)
    # print("pitch hand column: ", df["pitch_hand"])
    
    df['Pitcher_Name'] = df[' first_name'] + ' '+ df['last_name']
    #print(df['PitcherName'])
    getCol = df.pop('Pitcher_Name')
    df.insert(0, 'Pitcher_Name', getCol)
    return df 

