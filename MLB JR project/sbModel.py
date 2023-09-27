import Clean
import preprocessing
import ReadInFile
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
from read import READ
from prcess import processdatBih
import SAVstation
import addSAV
import FixNamesCreatDict
import tensorflow as tf
from tensorflow import keras
from keras import Sequential, layers, regularizers
from sklearn.ensemble import BaggingRegressor
import pickle
from datetime import datetime, date
import datetime


'''
GOAL: 
Be able to consistently predict over/under for Strikeouts thrown by a pitcher. 
Preprocess data so that model can be used to eventually predict things like hits, runs, o/u, winner, bases.
be able to recognize when there might be value in a bet according to the difference between lines and predicted values

OVERVIEW/QUESTIONS:
MLB sports betting model...
File needs to be located in VSCODE file but outside of sports betting file...
supervised learning algorithms to choose from (k-nearest neighbors, naive bayes, SVN, ridge..)
Feedforward Neural Network (Multi-Layer Perceptron)
can I input season avgs or avg against certain teams, H/A, to predict strike outs??
needs to be able to handle both catergorical and numerical data
'''





#read file in for reg and bsbal sav
dataset = ReadInFile.readItOnIN()
bsballSAVDF = SAVstation.presentSAVDataFrame()
#call to preprocess... preprocvess all data
#print(df.head())




# bsballSAVDF = pd.read_excel("completeDFCheckRemove0s.xlsx")
# # print(list(bsballSAVDF.dtypes))
# # print(list(df.dtypes))
# print("df shape ", df.shape)
# print("bsball shape ", bsballSAVDF.shape)
for col in bsballSAVDF.columns[1:]:
    bsballSAVDF[col] = bsballSAVDF[col].astype('float32')

# # Print memory usage after the changes
print("Memory usage after:", bsballSAVDF.memory_usage(deep=True).sum() / (1024 * 1024), "MB")




colswNan = dataset.columns[dataset.isna().any()].tolist()
print("cols w nan before", colswNan)



#empty cell count 
emptyCells = dataset.isna().sum() 
##print("before", emptyCells)
    
dataset=preprocessing.preprocess(dataset)
emptyCells2 = dataset.isna().sum() 
#print("after", emptyCells2)
colswNan2 = dataset.columns[dataset.isna().any()].tolist()
#print("cols w nan after", colswNan2)
pnDict = FixNamesCreatDict.fixnamesCreateDict(dataset)
#print("PNDICT", pnDict)


columns_with_inf = dataset.columns[dataset.isin([np.inf]).any()]
columns_with_neginf = dataset.columns[dataset.isin([-np.inf]).any()]
#print("cols w infs: ",list(columns_with_inf))
#print("cols w neg infs: ",list(columns_with_neginf))


regDFList= list(dataset.columns)
compDFList = list(bsballSAVDF.columns)
comboList = regDFList +compDFList
#print(comboList)


#TURN Back on when we need to combine the dfs back, 
# start = datetime.datetime.now()
# dsc = dataset.columns
# dataset=pd.DataFrame(dataset, columns=dsc)
# df, headers = addSAV.combine(dataset, bsballSAVDF, pnDict)
# colMap = {str(i): headers[i] for i in range(len(headers))}
# df.rename(columns=colMap, inplace=True)
# finish = datetime.datetime.now()
# elapsed = finish-start
# print("Time elapsed", elapsed)
# print(df.head)

# useDate = date.today()

# df.to_excel("testMergeDatHOE"+str(useDate)+".xlsx", index = False)
df= pd.read_excel("testMergeDatHOE2023-09-04.xlsx")
# print("saved merged DF to excel")
# print("merged DF", df.head())
# print("Merged DF shape", df.shape)
# df.to_excel('df.xlsx', index = False)
# print("saved to excel")


#WE NEED TO ADD THE COLUMNSBACK TO THE DF
# colMap = {str(i): comboList[i] for i in range(len(comboList))}
# df.rename(columns=colMap, inplace=True)
df.columns=comboList
print(df.head())


#df.to_excel("USESEPT04.xlsx", index = False)


#print(df)
#df.drop(df[df['Inngs_Pitched']< 1.0].index, inplace = True)
#empty val check in df / check info of df
#colscheck = [col for col in df.columns if any(df[col].astype(str).str.contains("---"))]
# print(colscheck)
# print(list(df.columns))
#print(df.info)




print("get rid of the second year: ", list(df.columns))


# columns_with_infSAV = bsballSAVDF.columns[bsballSAVDF.isin([np.inf]).any()]
# columns_with_neginfSAV = bsballSAVDF.columns[bsballSAVDF.isin([-np.inf]).any()]
# print("cols w infs: ",list(columns_with_infSAV))
# print("cols w neg infs: ",list(columns_with_neginfSAV))




columns_with_inf = df.columns[df.isin([np.inf]).any()]
columns_with_neginf = df.columns[df.isin([-np.inf]).any()]
# print("cols w infs: ",list(columns_with_inf))
# print("cols w neg infs: ",list(columns_with_neginf))



#DETERMINING THE FEATURES TO DROP
#inherited runners and inherited score...
#stat only pertains to pitchers that do not start... so these are features that we can drop... 
#Now that pitcher names and team names have been encoded we can drop the actual pitcher names and team names
#, 'Entered', 'Exited'
#, "Ground_Balls", "Fly_Balls", "Line_Drives", "PopUps", "Unkwn_out"
featuresToDrop = ['Pitcher_Name',  'Team', 'Opp', 'G_Career', 'G_team', 'Date', 'Result', 'Inngs_Played', "Unkwn_out", "Stolen_Bases_givenUP", "Caught_Stealing", "Pickoffs", "Intentional_Walks", "GrndDubPlays", "Sac_Flies", "avgchampLI", "avg_LeverageIndex", "champWinProbAdded", "Strike_Outs"]

# Remove leading and trailing spaces from the column names in featuresToDrop
featuresToDrop = [col.strip() for col in featuresToDrop]

# Compare column names, ignoring case
mismatched_columns = [col for col in featuresToDrop if col.lower() not in [c.lower() for c in df.columns]]

if mismatched_columns:
    print(f"The following columns in featuresToDrop do not exist in the DataFrame: {', '.join(mismatched_columns)}")
else:
    print("All columns in featuresToDrop exist in the DataFrame.")

# #get features that we will be using 
# colswNan = bsballSAVDF.columns[bsballSAVDF.isna().any()].tolist()
# print("cols w nan", colswNan)



# #empty cell count 
# emptyCells = bsballSAVDF.isna().sum() 
# print(emptyCells)

# number_inf = bsballSAVDF[bsballSAVDF == np.inf].count()
# print("Contains inf: ")
# print(number_inf)
# numFeatures = list(bsballSAVDF.columns)
# print(numFeatures)

feature_to_drop = df.pop(df.columns[58])

#DROP THE BAD FEATURES 
features = df.drop(featuresToDrop, axis = 1)
#getting the features to use as our X
print(list(features.columns))
#drop the second year 
newFeatures = Clean.clean_dataset(features)
print(list(newFeatures.columns))
#newFeatures.to_excel('nfDS.xlsx', index = False)
print("saved to excel")
#THIS IS WHERE DECIDE Y or VARIABLE WE WANT TO PREDICT 
target = df['Strike_Outs']

print(newFeatures.head())

#see distribution
# plt.hist(df['Strike_Outs'], bins=10)
# plt.title('Strike Out frequencies')
# plt.xlabel('SO')
# plt.ylabel('Frequency')
# plt.show()

#this is only needed if ERA and FIP are involved without replaceing infs 
#target = target.drop(target.tail(9).index)
#print(target.head())


#CHANGE NEWFEATURES AND TARGET INTO NUMPY ARRAYS
newFeatures = np.asarray(newFeatures)
print("newFeatures shape ", newFeatures.shape)

target = np.asarray(target)
print("target shape ", target.shape)
#target = target.reshape(-1, 1)
#print(target)
#NEED TO BE ACCEPTABLE to TRAINTESTSPLIT\
    
# plt.hist(newFeatures.flatten(), bins = 101)
# plt.show()



    
    
    #SCALE can do beofre or after HP tuning 
scaler = StandardScaler()
scaler.fit(newFeatures)
with open('scalerSaved.pk2', 'wb') as scalarSaved: 
    pickle.dump(scaler, scalarSaved)

X_train_Scaled = scaler.fit_transform(newFeatures)



#PCA 
#num features to retain
components = 30
pca = PCA(n_components=components)
X_trainPCA = pca.fit_transform(X_train_Scaled)

#save the PCA to use on the test data: 
joblib.dump(pca, 'pca_model.pkl')




X_train, X_test, y_train, y_test = train_test_split(X_trainPCA, target, test_size=0.2, random_state=42)

print(X_train.shape)
print(X_test.shape)

#HYPERPARAM TUNING 
# grid={
    
#     # 'loss': ['squared_error', 'poisson', 'absolute_error'], 
#     #   'learning_rate': [0.001, 0.01, 0.1], 
#     #   'max_iter': [100, 150, 200]}


#     #   'max_leaf_nodes': [15, 31, 45], 
#     #   'max_depth': [15, 20, 25], 
#     #   'l2_regularization': [0, 0.1, 0.5]
#     }


#BEST LR IS 0.1, use squared error 

#SCALE can do beofre or after HP tuning 
# scaler = StandardScaler()
# scaler.fit(X_train)
# with open('scalerSaved.pk2', 'wb') as scalarSaved: 
#     pickle.dump(scaler, scalarSaved)

# X_train = scaler.fit_transform(X_train)
# X_test = scaler.transform(X_test)








# plt.hist(df['Strike_Outs'], bins=10)
# plt.title('Strike Out frequencies')
# plt.xlabel('SO')
# plt.ylabel('Frequency')
# plt.show()
#good model so far 
# GBR = HistGradientBoostingRegressor(loss='squared_error', learning_rate=0.1, max_depth=7, l2_regularization= 0.1, max_iter=100, max_leaf_nodes=62, random_state=42)
#GBR = HistGradientBoostingRegressor(loss='squared_error', learning_rate=0.1, max_iter=150, l2_regularization=0.5, max_depth=15, max_leaf_nodes=45, random_state=42)

#grab the shape of the input

inputDim = X_train.shape[1]
GBR = Sequential([
    layers.Dense(64, activation='relu', input_shape=(inputDim, ), kernel_regularizer=regularizers.l2(0.001)), 
    # layers.Normalization(axis=-1), 
    layers.Dropout(0.5), 
    layers.Dense(64, activation='relu', kernel_regularizer=regularizers.l2(0.001)), 
    layers.Dropout(0.5),
    layers.Dense(128, activation='relu', kernel_regularizer=regularizers.l2(0.001)), 
    layers.Dropout(0.5),
    layers.Dense(128, activation='relu', kernel_regularizer=regularizers.l2(0.001)), 
    layers.Dropout(0.5),
    layers.Dense(1)
])
GBR.compile(loss= tf.keras.losses.MeanSquaredError() , optimizer= tf.keras.optimizers.Adam(learning_rate=0.001), metrics=['mae', 'mse'])




#Turn off when doing cross val
epochs = 100 
batchSize = 100
history = GBR.fit(X_train, y_train, epochs=epochs, batch_size=batchSize, verbose=0, validation_split=0.2)  


loss = GBR.evaluate(X_test, y_test)
print("loss: ", loss)






# gSearch = GridSearchCV(estimator=GBR, param_grid=grid, cv=5)
#if nothing in the grid then we don't need ot do this 
kf = KFold(n_splits=5, shuffle=True, random_state=42)
# # #fit the mdoel
# #gSearch.fit(X_train, y_train)
# crossV = cross_val_score(GBR, newFeatures, target, cv=kf, scoring='r2')
# print(crossV)

#get parameter s to use, all good on the HISTGRadientBoostingregresor
##paramsToUse = gSearch.best_params_
# print("Params", paramsToUse)
# #score = gSearch.best_score_
# print("g-search best score", score)

#USE THE BEST MODEL HERE WITH THE BEST PARAMETERS
#bestModel = gSearch.best_estimator_
#bestModel.fit(X_train, y_train)

#fit the pca model 
#bestModel = GBR.fit(X_train, y_train)
all_train_loss= []
all_val_loss = []

#CROSS VAL: 
# for trainIndex, valIndex in kf.split(X_train): 
#     X_trainfolded, X_valFolded = X_train[trainIndex], X_train[valIndex]
#     y_trainFolded, Y_valFolded = y_train[trainIndex], y_train[valIndex]
    
#     epochs = 50 
#     batchSize = 32

#     history = GBR.fit(X_trainfolded, y_trainFolded, epochs=epochs, batch_size=batchSize, validation_data=(X_valFolded, Y_valFolded))  

#     train_loss = history.history['loss'][-1]
#     val_loss = history.history['val_loss'][-1]

#     all_train_loss.append(train_loss)
#     all_val_loss.append(val_loss)

# mean_val_loss = np.mean(all_val_loss)
# std_val_loss = np.std(all_val_loss)
# print(f"Mean validation loss across 5 folds: {mean_val_loss}")
# print(f"Standard deviation of validation loss across 5 folds: {std_val_loss}")


#GBR.fit(X_train, y_train)
savedModel = joblib.dump(GBR, 'sbmodelDNN.pk1')


#accuracy = bestModel.score(X_test, y_test)
#print("best model accurracy: ", accuracy)
#print("X_Train ", X_train)

#predict on the test set
yPredictionTrain =GBR.predict(X_train)
rmseTRAIN = sqrt(mean_squared_error(y_train, yPredictionTrain))
r2TRAIN = r2_score(y_train, yPredictionTrain)
maeTRAIN = mean_absolute_error(y_train, yPredictionTrain)


yPredictionTest = GBR.predict(X_test)
print("Prediction for y Test: ", yPredictionTest)
#explanatory metrics
rmseTEST = sqrt(mean_squared_error(y_test, yPredictionTest))
r2TEST = r2_score(y_test, yPredictionTest)
maeTEST = mean_absolute_error(y_test, yPredictionTest)


#show the metric s
print()
print("Results on Train Data")
print("root Mean Squared Error:", round(rmseTRAIN, 4))
print("R2 Score: ", round(r2TRAIN, 4))
print("Mean Absolute Error:", round(maeTRAIN, 4))


#show the metric s
print()
print("Results on Test Data")
print("root Mean Squared Error:", round(rmseTEST, 4))
print("R2 Score: ", round(r2TEST, 4))
print("Mean Absolute Error:", round(maeTEST, 4))

