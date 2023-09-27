#takes both dataframes and combines them
import numpy as np
import pandas as pd
from fuzzywuzzy import fuzz, process
import sys
from datetime import datetime, date

#takes both dataframes and then pitchers????
def combine(regDF, savDF, pnDict):
    #best bet is to query info from sav and then add it to the end of the re df
    headers = savDF.columns.values
    pitchers = list(regDF['Pitcher_Name'].unique())
    completeDF = pd.DataFrame(columns=headers)
    for index, row in regDF.iterrows(): 
        pitcher = row['Pitcher_Name']
        bestMatchP = process.extractOne(pitcher, choices=pnDict.keys())
        pn = bestMatchP[0]
        print(pn)
        year = row['year']
        savDF['Similarity'] = savDF['Pitcher_Name'].apply(lambda x: fuzz.ratio(x, pn))
        closest = process.extractOne(pn, savDF['Pitcher_Name'])[0]
        thresh = 90
        print("closest match: ", closest)
        query= savDF.query("Similarity >= @thresh and year == @year")
        miniSAVquery = pd.DataFrame(query)
        #print("minisav query: ", miniSAVquery)
        print("Mini SAV query shape: ",miniSAVquery.shape)
        if miniSAVquery.empty: 
            #when this happens neeed to  get the avg for all and repalce values
            #this will grab every year for the pitcher amd append so that the df is significantly larger tahn needed
            #take avgs and add that. 
            #fails when pitcher name has foreign characters
            #print("query empty")
            nquery= savDF.query("Similarity >= @thresh")
            #print("shape of the nQuery on minisav that was empty", nquery.shape)
            if nquery.empty: 
                print("nquery empty")
                #then just make the rowvector the avg of all pitcher in the df.
                columnsToExcludeSAV = ['Pitcher_Name']
                filteredForMeanSAV = savDF.drop(columns=columnsToExcludeSAV)
                meansDF=pd.DataFrame(filteredForMeanSAV.mean())
                
                rowVectorSAV = meansDF.T.values
                
                #print("rSAV: ",rowVectorSAV)
                toADDSAV = pd.Series([pn])
                #print(toADDSAV[0])
                
                col = 'Pitcher_Name'
                transSAV = toADDSAV.T
                #rowVectorSAV =pd.concat([transSAV, pd.DataFrame(rowVectorSAV, columns=filteredForMean.columns)], axis=1)
                rowVectorSAV = pd.DataFrame([[transSAV[0]] + rowVectorSAV[0].tolist()], columns=[col] + filteredForMeanSAV.columns.tolist())
                #print("Row vector :")
                rowVectorSAV.iloc[0, 1]=year
                print("shape of rv using whole df avgs", rowVectorSAV.shape)
                rvShape = rowVectorSAV.shape
                
                if (rowVectorSAV.shape[0]>1): 
                    temporary= rowVectorSAV.iloc[0]
                else: 
                    temporary = rowVectorSAV
                completeDF=pd.concat([completeDF, temporary], ignore_index = True)
            else:
                columnsToExclude = ['Pitcher_Name']
                filteredForMean = nquery.drop(columns=columnsToExclude)
                
                #get mean
                means= pd.DataFrame(filteredForMean.mean())
                
                rowVector=means.T.values
                #print("RVECT: ",rowVector)
               # print(len(rowVector[0]))
                pname = nquery.iloc[0]['Pitcher_Name']
                colu='Pitcher_Name'
                toADD = pd.Series([pn])
                transposeTADD=toADD.T
                #print(transposeTADD[0])
                #rowVector = pd.concat([transposeTADD, pd.DataFrame(rowVector, columns=filteredForMean.columns)], axis=1)
                rowVector = pd.DataFrame([[transposeTADD[0]] + rowVector[0].tolist()], columns=[colu] + filteredForMean.columns.tolist())
                #print("rowvecotr else: ")
            
                # Add the row vector to the completeDF DataFrame
                #should have the completeed pd dataframe. now replace the year in the df with the year gathered 
                #from way up top,,,, year.
                
                rowVector.iloc[0, 1]=year
                print("nquery not empty shape: ", rowVector.shape)
                rowVecSH = rowVector.shape
                if (rowVector.shape[0] >1): 
                    temp = rowVector.iloc[0]
                else: 
                    temp = rowVector
                completeDF=pd.concat([completeDF, temp], ignore_index = True)
            
        else: 
            if (miniSAVquery.shape[0] >1):
                temp = miniSAVquery.iloc[0]
                transposeG1MFr= temp.T.values
                print("G1MFR", transposeG1MFr.shape)
            else: 
                transposeG1MFr = miniSAVquery
            completeDF=pd.concat([completeDF, transposeG1MFr], ignore_index = True)
                
        
       
    #concate the df as more are made. 
    #add this to the alarge df. 
    #once we have the large df we need to concat directly across
    #check size and shape.
    rShape = regDF.shape
    print("reg shape", rShape)
    cShape = completeDF.shape
    print("c shape", cShape)
    print("This is the c DF")
    print(completeDF.head())
    
    usedate = date.today()
    completeDF.drop(['Similarity'], axis = 1, inplace=True)
    completeDF.to_excel('comboNewSEPT3'+str(usedate)+'.xlsx', index = False)
    print("saved to excel")
    
    regDF['Pitcher_Name'] = regDF['Pitcher_Name'].astype(str)
    completeDF['Pitcher_Name'] = completeDF['Pitcher_Name'].astype(str)
    
    regDFList= list(regDF.columns)
    compDFList = list(completeDF.columns)
    comboList = regDFList +compDFList
    
    regDF.reset_index(drop=True, inplace=True)
    completeDF.reset_index(drop=True, inplace=True) 
    
    #merged = pd.merge(df, bsballSAVDF, on= 'Pitcher_Name')
    merged = pd.concat([regDF, completeDF], axis=1, ignore_index=True)
    print(merged.shape)
    
    return merged, comboList