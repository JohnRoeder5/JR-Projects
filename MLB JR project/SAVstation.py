import numpy as np
import pandas as pd
from sklearn.linear_model import LinearRegression, ElasticNetCV, SGDRegressor, LogisticRegression
from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import GradientBoostingRegressor, HistGradientBoostingRegressor, RandomForestRegressor, HistGradientBoostingClassifier
from sklearn.model_selection import train_test_split, cross_val_score, GridSearchCV, KFold
from sklearn.neural_network import MLPRegressor
from sklearn.metrics import mean_squared_error, r2_score, mean_absolute_error
from sklearn.preprocessing import StandardScaler,  LabelEncoder
from math import sqrt
import joblib
import matplotlib.pyplot as plt
from sklearn.decomposition import PCA
import read 
import prcess 


#this is going to do all the preprocessing of the SAV df then return it to the model page for the sbModel

def presentSAVDataFrame(): 

    df= read.READ()

    #replace all empty values in savantDF with the avg of the column. 
    savantDF = prcess.processdatBih(df)
    #print(list(savantDF.columns))


    savantDF.drop(['last_name', ' first_name','player_id','p_game','p_formatted_ip','pa','ab','hit','home_run',	'strikeout','walk',	'k_percent','bb_percent','batting_avg', 'pitch_hand', 'p_earned_run', 'p_out', 'p_run', 'p_era', 'p_opp_batting_avg', 'p_total_strike', 'p_total_swinging_strike'], axis=1, inplace=True)
    
    columns_with_inf = savantDF.columns[savantDF.isin([np.inf]).any()]
    columns_with_neginf = savantDF.columns[savantDF.isin([-np.inf]).any()]
    # print("cols w infs: ",list(columns_with_inf))
    # print("cols w neg infs: ",list(columns_with_neginf))
    colswNan = savantDF.columns[savantDF.isna().any()].tolist()
    #print("cols w nan", colswNan)
    
   #print(savantDF.head())
    #empty cell count 
    emptyCells = savantDF.isna().sum()
    #print(emptyCells)

    number_inf = savantDF[savantDF == np.inf].count()
    print("Contains inf: ")
    #print(number_inf)
    numFeatures = list(savantDF.columns)
    #print(numFeatures)

#everything past this is to be used later
    # featuresToDrop = ['strikeout', 'PitcherName', 'pitch_hand']


    
    #at very end retunr the df to be concat w the former df
    return savantDF

