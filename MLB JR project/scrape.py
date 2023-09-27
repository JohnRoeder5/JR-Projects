
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
from selenium import webdriver

def get_game_log_URLS(pitcherLink):
    #gets the link to the pitchers individual website
    #finds the game log link in their page 
    useThisLink = "https://www.baseball-reference.com" + pitcherLink
    print(useThisLink)
    response = requests.get(useThisLink)
    nwPage = response.text
    soup2 = BeautifulSoup(nwPage, 'lxml')
    #finds all the game log links in the page.
    # then use all the other game logs found at the bottom of the page (in this case all game logs as far back as 2010)
    gameLog =soup2.find_all('a', href=re.compile('p&year'),  string=['2010', '2011', '2012', '2013', '2014', '2015', '2016', '2017', '2018', '2019', '2020', '2021', '2022', '2023', 'Postseason'])
    #make it a set so that there are no duplicates 
    glSet = set(gameLog)
    #need to add the sites base URL to the urls we have in glSet
    #this wil be the list of useable URLS to iterate through 
    useableURL = []
    ytextList = []
    baseURL = 'https://www.baseball-reference.com'
    for relativeURL in glSet:
        link = relativeURL['href']
        ytext = relativeURL.get_text()
        #print(ytext)
        ytextList.append(ytext)
        newURL = urljoin(baseURL, link)
        #print(newURL)
        useableURL.append(newURL)     
    #sends the link to the game log for a given year to get_GL_table... 
    #should return the table it has gathered for a given years game log...
    #which should be returned to the original function. 
    results = []
    urlCount = 0
    for url in useableURL: 
        yearText = ytextList[urlCount]
        glTable = get_GL_table(url, yearText)
        urlCount = urlCount+1
        results.append(glTable)
        delay = random.uniform(1, 4)
        time.sleep(delay)
    return results



         
def get_GL_table(goToTable, yearText):
    #open up that years gameLog
    response = requests.get(goToTable)
    
    glPage = response.text
    soup3 = BeautifulSoup(glPage, 'lxml')
    #get that years game log table
    glt = soup3.find('table', {'id':'pitching_gamelogs'})
    if glt is None: 
        return []
    #find all the rows 
    table_rows = glt.find_all('tr')
    #get the row headers 
    HEADERS = [th.get_text() for th in table_rows[0].find_all('th')[1:]]
    #print(HEADERS)
    rowList = []
    for row in table_rows:
            #if the row does not have a class then use it 
            # the rows rhat have a class contain the months as well as the headers again so we do not want to process those. 
        if row.find(class_=False):
            vals = row.find_all('td')
            #use this to remove the draftking and fanduel values from the list of rows, stats not included in many of the older tables so get rid of it. 
            for value in vals: 
                check = value['data-stat']
                if "draftkings" not in check and "fanduel" not in check and "game_score" not in check and "inherited_runners" not in check and "inherited_score" not in check and "player_game_result" not in check and "pitcher_situation_in" not in check and "pitcher_situation_out" not in check: 
                    if "date_game" in check: 
                        data= value.get_text()
                        dateee = data+", " +yearText
                        #print(dateee)
                        rowList.append(dateee)
                        #print(value)
                    else:
                        data = value.get_text()
                        rowList.append(data)
                        #print(rowList)
    #print("Row List", rowList)                        
    return rowList
        





def scrape():
#make the beautiful soup and open the file
    # Initialize a headless browser using Selenium
    options = webdriver.ChromeOptions()
    options.add_argument('--headless')  # Run Chrome in headless mode
    driver = webdriver.Chrome(options=options)

    url = "https://www.baseball-reference.com/leagues/majors/2023-starter-pitching.shtml"
    driver.get(url)
    driver.implicitly_wait(10)
    page_source = driver.page_source

    driver.quit()

    # Now you can create a BeautifulSoup object and parse the page_source
    soup = BeautifulSoup(page_source, 'lxml')



    # r = requests.get("https://www.baseball-reference.com/leagues/majors/2023-starter-pitching.shtml")
    # page = r.text
    # soup= BeautifulSoup(page, 'lxml')
    
    
    # with open("pitchers.html", 'r') as html_file: 
    #     soup = BeautifulSoup(html_file, 'lxml')
    #     #print(soup.prettify())   
    pitchers=[]
    pitcherCount = 0
    # Find the table and find the rows 
    table = soup.find('table', {'id': 'players_starter_pitching'})
    #print("THis is the table", table)
    rows = table.find_all('tr', class_='full_table')
    for row in rows: 
            #get the pitcher individual link
            #pitcherLink creates a list of found links with these requirements
                pitcherLink = row.find('td').find_all('a', href= re.compile('/players/'))
            #access the link within the list. 
                link = pitcherLink[0]
                pitcherName = link.get_text()
                pitcherCount = pitcherCount+1
                print(pitcherName, ": ", pitcherCount)
                href = link['href']
                print(href)
            #call next function to begin the search through that pitchers individual page 
                pitcherGameLogs = get_game_log_URLS(href)
                pitchers.append({'name': pitcherName, 'game_logs': pitcherGameLogs})
                #print(pitchers)
                delay = random.uniform(1, 4)  
                time.sleep(delay)
    toWrite = pitchersSort(pitchers)
    #used to get the dates for the files to stay up to date.
    
    #CODE TO WRITE TO FILE INCLUDING DATE> PUT +STR()+ INSIDE OF FILENAME
    useDate = date.today()
    print(date)
    writeCSV(toWrite, "csvPitchD1"+str(useDate)+".csv")
    return "Program is finished!"
    

#need to access the values of the dictionary and create one large 
#List consisting of smaller lists. each list should have the pitcher name 
#and then the corresponding game log which should be 53 variables long
def pitchersSort(pitchers):
    pBigList = []
    #this should be the number of dictionaries in the list. 
    #numOfDicts = len(pitchers)
    #Since Pitchers is a list containing many dictionaries
    #need to grab the dictionary from the larger overarching list.
    #for each dictionary in the list pitchers...
    for dictionary in pitchers:
            #get the pitcher name from the current dictionary, get current career gamelogs below.
            pName = dictionary['name']
            #print(pName)
            #get the pitchers game logs. These will be stored in a large overarching list with smaller lists inside representing 
            #each year the pitcher has played. 
            pGameLogs = dictionary['game_logs']
            #print(pGameLogs)
            #iterate over the smaller lists in the larger list. larger list is grabbed by the variable pGameLogs. 
            #smaller lists represent the individual years. we want to grab the smaller lists and then individually sort out the gamelogs. 
            #for each year within the gameLogs list
            for individualYRlist in pGameLogs: 
                #val needs to be length of an individual pitchers game logs 
                val = len(individualYRlist) // 44
                #if val is not equal to zero then we want to run the code
                if val != 0: 
                    #loop through the game logs and get each individual gl of length 53 items. 
                    #need to make sure this is looping through the game logs and not the pitchers names 
                    for j in range(val):
                        ind_gl = []
                        ind_gl.append(pName)
                        start = j*44
                        end = start+44
                         #remember 0th index 
                        for i in range(start, end): 
                             ind_gl.append(individualYRlist[i])
                            #print(ind_gl)
                        pBigList.append(ind_gl)   
                else: 
                    pBigList.append([]) 
    #print(pBigList)
    return pBigList



def writeCSV(data, filename):
   
    headers = ['Pitcher_Name', 'G_Career', 'G_team', 'Date', 'Team', 'Home/Away', 'Opp', 'Result', 'Inngs_Played',  'Days_Rest',
               'Inngs_Pitched', 'Hits_Allowed', 'Runs_Allowed', 'Earned_Runs', 'Bases_Balls/Walks', 'Strike_Outs', 'HR', 'HBP', 'ERA', 
               'Fielding_Independent_Pitching', 'Batters_Faced', 'Pitches_Thrwn', 'Strikes_Thrown', 'Strikes_Looking', 'Strikes_Swinging', 
               'Ground_Balls', 'Fly_Balls', 'Line_Drives', 'PopUps', 'Unkwn_out',  
               'Stolen_Bases_givenUP', 'Caught_Stealing', 'Pickoffs', 'At_Bats', '2B_allowed', '3B_allowed', 'Intentional_Walks', 
               'GrndDubPlays',  'Sac_Flies', 'Reached_On_Error', 'avg_LeverageIndex', 'WinProbAdded', 'avgchampLI', 'champWinProbAdded', 
               'Base_out_Runs_saved']
    with open(filename, 'w', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(headers)
        for pitcher in data:
            #print(pitcher)
            if pitcher: 
                pitcher_name = pitcher[0] 
                game_logs = pitcher[1:]
               
                #create list to add a to row....
                #as of rn its pitcher, [stats, stats, stats], 
                #we want [pitcher, stats, stats, stats]
                toAdd = []
                toAdd.append(pitcher_name)
                for i in game_logs:
                    toAdd.append(i)
                writer.writerow(toAdd)
                #original working line below
                #writer.writerow([pitcher_name, game_logs])
        print(f"Data has been written to {filename}")



# start call to scrape where it starts at the mlb page and should get the links to each the pitchers individual sites.
HEADERS = []
starting_pitchers = scrape()
#writeCSV(starting_pitchers, "csvPitchD.csv")
