DataSets <- list(
  wine = read.table(file = "../Classification/data/wine/wine.data", header = FALSE, sep = ","),
  car = read.table(file = "../Classification/data/car/car.data", header = FALSE, sep = ","),
  heart = read.table(file = "../Classification/data/heart-disease/processed.cleveland.data", header = FALSE, sep = ","),
  arrhythmia = read.table(file = "../Classification/data/arrhythmia/arrhythmia.data", header = FALSE, sep = ","),
  bridges = read.table(file = "../Classification/data/bridges/bridges.data.version1", header = FALSE, sep  = ",")
)

Algorithms <- list(
  "KNN.R",
  "Bagging.R",
  "RandomForests.R",
  "AdaBoost.R",
  "GradientBoost.R"
)

preprocessData <- function()
{
  preprocessDataSets <- list()
  for (index in 1 : length(DataSets))
  {
    data <- DataSets[[index]]
    col = ncol(data)
    newData <- 0
    for (j in 1 : col)
    {
      if (is.factor(data[[j]]))
      {
        # print ("is Factor")
        oneColumn <- as.numeric(data[[j]])
      }
      else
      {
        oneColumn <- data[j]
      }
      newData <- cbind(newData, oneColumn)
    }
    newData <- newData[, -1]
    colnames(newData) <- colnames(data)       # rename columns
    
    
    # ===== normalize =====
    maxs <- apply(newData, 2, max) 
    mins <- apply(newData, 2, min)
    scaledData <- as.data.frame(scale(newData, center = mins, scale = maxs - mins))
    
    if (any(is.na(scaledData)))
    {
      scaledData <- scaledData[, colSums(is.na(scaledData)) != nrow(scaledData)]
    }
    
    # ===== fix class variable to end column =====
    if (names(DataSets[index]) == "wine")
    {
      scaledData <- cbind(scaledData[, 2 : col], scaledData[, 1])
      colnames(scaledData)[col] <- colnames(data)[1]
    }
    
    preprocessDataSets[[index]] <- scaledData
  }
  preprocessDataSets
}

exec <- function()
{
  preprocessDataSets <- preprocessData()
  
  resultMatrix <- 0
  for (index in 1 : length(preprocessDataSets))
  {
    data <- preprocessDataSets[[index]]
    
    for (numSamples in 1:10)
    {
      # 10 fold cross-validation
      sub <- floor((numSamples - 1) * nrow(data) / 10)  + (1 : (nrow(data) / 10))
      trainingData = data[-sub, ]    # create training data
      testData = data[sub, ]       # create test data
      
      accuracyList <- NULL
      for (algo in Algorithms)
      {
        remove(createModel)
        remove(findAccuracy)
        source(algo)
        
        model <- createModel(trainingData, testData) 
        accuracy <- findAccuracy(model, testData) 
        accuracyList <- c(accuracyList, round(accuracy, digits = 3))
      }
      
      #accuracyList <- as.data.frame(accuracyList)
      #dimnames(accuracyList) <- list(unlist(Algorithms), names(DataSets[index]))
      
      # num of total instances, num of attr, percent split, 5 accuracies
      resultItem <- c(length(data[[1]]), length(data), "10", accuracyList)
      resultMatrix <- rbind(resultMatrix, resultItem)
    }
  }
  
  resultMatrix <- resultMatrix[-1, ]
}

resultMatrix <- exec()
#dimnames(resultMatrix) <- list(c("D1", "D2", "D3", "D4", "D5"),
#                               c("Num of total instances", "Num of attribute", "How many fold cross-validation", unlist(Algorithms)))