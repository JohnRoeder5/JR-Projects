import requests
import csv
import pandas as pd 
import re
from bs4 import BeautifulSoup
import time
import random 
import openpyxl
from datetime import datetime, date
import os
from urllib.parse import urljoin

def cleanTextPlayer(text):
    fixed = text.replace('\n', '').replace('�', '').replace('\xad', '') 
    return fixed

def cleanTextTeam(text): 
    new =text.replace('\n', '')
    textVals = new.split(" ")
    returnTxt=textVals[0]
    #print(returnTxt)
    return returnTxt

def remove_duplicates(input_list):
    unique_list = []
    for item in input_list:
        if item not in unique_list:
            unique_list.append(item)
    return unique_list

def mlbScrape(): 
    r = requests.get("https://www.mlb.com/starting-lineups")
    page = r.text
    #print(page)
    souptoday =BeautifulSoup(page, 'lxml')
    #print(souptoday)
    listOfPitchersToday=[]
    listofCorrTeams = []
    pCount = 0
    players = souptoday.find_all("div", class_="starting-lineups__pitcher-name")
    #print("players", players)
    teams = souptoday.find_all("div", class_=["starting-lineups__teams--away-head", "starting-lineups__teams--home-head"])
 
    
    
    #print(teams)
    for player in players: 
        #print("printedOut", player)
        textPlayer = player.get_text()
        #print(text)
        fixed = cleanTextPlayer(textPlayer)
        listOfPitchersToday.append(fixed)
        
    for team in teams: 
        #print(team)
        teamText = team.get_text()
        #print(teamText)
        stripT=teamText.strip()
        fixedTName = cleanTextTeam(stripT)
        listofCorrTeams.append(fixedTName)
        
        
    if "TBD" in listOfPitchersToday:
        listOfPitchersToday.remove("TBD")
    
    
    updatedTeamsToday= remove_duplicates(listofCorrTeams)
    #print(listOfPitchersToday)
    #print(updatedTeamsToday)
    #flip the indices in pairs 
    for i in range(0, len(updatedTeamsToday) - 1, 2):
        updatedTeamsToday[i], updatedTeamsToday[i + 1] = updatedTeamsToday[i + 1], updatedTeamsToday[i]
        
    #print("changed list ", updatedTeamsToday)
       
    #1 IS HOME AND 0 IS AWAY
    length = len(listOfPitchersToday)
    alternating_list = [i % 2 for i in range(length)]
    #print(alternating_list)
        
    return listOfPitchersToday, updatedTeamsToday, alternating_list



start = mlbScrape()