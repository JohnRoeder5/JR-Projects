import pandas as pd
#read in files and use to send elsewhere

def readItOnIN(): 
    df = pd.read_excel('csvPitchD02023-08-28.xlsx')
    #print(list(df.columns))
    #print(df.info())
    return df