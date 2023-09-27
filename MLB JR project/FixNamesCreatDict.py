#fix names and create encoded to pitchernames dictionary so that when user wants to predict the pitcher 
#SO totals, they can just enter the name and the dictionary automatically gets the encoded number. 
def fixnamesCreateDict(df): 
    #get encoded names 
    pnEncodes = list(df['Encoded_PitcherNames'].unique())
    #Get pitcher names and fix them 
    pns = list(df['Pitcher_Name'].unique())
    pitcherNs = []  
    for name in pns: 
        fixed = name.replace('\xa0', ' ').replace('�', '').replace('\xad', '')
        pitcherNs.append(str(fixed))
        #print(pitcherNs)
    Pdict = dict(zip(pitcherNs, pnEncodes))
    return Pdict