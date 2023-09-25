import pandas as pd 

def READ(): 
    df = pd.read_csv("bsballsav.csv")
    return df