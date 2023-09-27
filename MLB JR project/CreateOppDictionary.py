#SAME idea create dictionary for OPP inputs so that user can input team name and it can be replaced with corresponding encoded value
def OppDict(df): 
    #get encoded names 
    oppEncodes = list(df['Opp_Encoded'].unique())
    #Get pitcher names and fix them 
    opps = list(df['Opp'].unique())
    oppsNames= []  
    for opponent in opps: 
        fixed = opponent.replace('\xa0', ' ').replace('�', '').replace('\xad', '')
        oppsNames.append(str(fixed))
    oppDict = dict(zip(oppsNames, oppEncodes))
    return oppDict