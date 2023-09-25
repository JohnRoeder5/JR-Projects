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


def mlbScrape(): 
    r = requests.get("https://www.mlb.com/starting-lineups")
    page = r.text
    #print(page)
    souptoday =BeautifulSoup(page, 'lxml')