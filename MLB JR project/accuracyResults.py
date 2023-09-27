# import numpy as np
# import pandas as pd
# import datetime
# from bs4 import BeautifulSoup
# import requests
# import csv
# from selenium import webdriver
# from selenium import webdriver
# from selenium.webdriver.common.by import By
# from selenium.webdriver.chrome.service import Service as ChromeService
# from selenium.webdriver.common.keys import Keys
# from selenium.webdriver.chrome.options import Options as ChromeOptions
# from selenium.webdriver.support.ui import WebDriverWait
# from selenium.webdriver.support import expected_conditions as EC
# from bs4 import BeautifulSoup
# import time




# '''
# FUNCTION THAT WILL CREATE A CSV CONTAINING RESULTS. NEED TO BE ABLE TO COMPARE IT TO LINES. will need two different 
# accuracy metrics, one for career based and one for given team based. 
# '''

# def accuracy(predsCareer, predsGivenTeam, oppsList): 
#     #NEW DATA TO BE ADDED
#     dfCareer = pd.DataFrame(predsCareer, columns=["Player", "Prediction" ])
#     dfGiven = pd.DataFrame(predsGivenTeam, columns=["Player", "Prediction"])
    
#     #oppslist is currently a horizontal list. transpose it 
#     numpOpps = np.array(oppsList)
#     transOpps = numpOpps.T
#     dfCareer= pd.concat([dfCareer,pd.DataFrame(transOpps, columns=["Opponent"])], axis=1)
#     print(dfCareer.head())
    
#     dfGiven= pd.concat([dfGiven, pd.DataFrame(transOpps, columns=["Opponenent"])], axis=1  )
#     print(dfGiven.head())
    
    
    
    
    
    
    
    
#     return 0


# def scrapeLines(): 
#     # r = requests.get("https://www.bettingpros.com/mlb/odds/player-props/strikeouts/")
#     # page = r.text
#     # print(page)
#     # souptoday =BeautifulSoup(page, 'lxml')
    
#     options = webdriver.ChromeOptions()
#     options.add_argument('--headless')  # Run Chrome in headless mode
#     driver = webdriver.Chrome(options=options)

#     url = "https://www.bettingpros.com/mlb/odds/player-props/strikeouts/"
#     driver.get(url)
#     driver.implicitly_wait(10)
#     page_source = driver.page_source
#     print(page_source)
#     driver.quit()
#     # Now you can create a BeautifulSoup object and parse the page_source
#     soup = BeautifulSoup(page_source, 'html.parser')
    
#     # print("gathered", table)\
#     rows = soup.find_all("div")
#                               #, class_='grouped-items-with-sticky-footer__content')
    
#     print("THE ROWS FOUND: ", rows)
#     for row in rows: 
#         data=row.find('td', class_="total-prop-row__player-name")
#         print("THE DATA FOUND: ", data)
        
#     return 0

# def scrapeResults(): 
#     return 0

# scrapeLines()