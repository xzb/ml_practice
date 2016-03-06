DataSets <- list(
  # adult = read.table(file = "data/adult/adult.data", header = FALSE, sep = ","),
  # wine = read.table(file = "data/wine/wine.data", header = FALSE, sep = ","),
  car = read.table(file = "data/car/car.data", header = FALSE, sep = ","),
  heart = read.table(file = "data/heart-disease/processed.cleveland.data", header = FALSE, sep = ","),
  ads = read.table(file = "data/internet_ads/ad.data", header = FALSE, sep = ",")
  )

Algorithms <- list(
  "decisionTree.R",
  "perceptron.R",
  "neuralNet.R"
  #"SVM.R",
  #"naiveBayes.R"
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
    colnames(newData) <- colnames(data)
    
    
    # normalize
    maxs <- apply(newData, 2, max) 
    mins <- apply(newData, 2, min)
    scaledData <- as.data.frame(scale(newData, center = mins, scale = maxs - mins))
    
    preprocessDataSets[[index]] <- scaledData
  }
  preprocessDataSets
  
  
  # fix class variable to end column
  
}

exec <- function()
{
  preprocessDataSets <- preprocessData()
  
  resultMatrix <- NULL
  for (index in 1 : length(preprocessDataSets))
  {
    data <- preprocessDataSets[[index]]
    sub <- sample(1 : nrow(data), round(0.8 * nrow(data)))
    trainingData = data[sub, ]    # create training data
    testData = data[-sub, ]       # create test data
    
    accuracyList <- NULL
    for (algo in Algorithms)
    {
      remove(createModel)
      remove(findAccuracy)
      source(algo)
      
      model <- createModel(trainingData) 
      accuracy <- findAccuracy(model, testData) 
      accuracyList <- c(accuracyList, accuracy)
    }
    
    accuracyList <- as.data.frame(accuracyList)
    dimnames(accuracyList) <- list(unlist(Algorithms), names(DataSets[index]))
    resultMatrix <- cbind(resultMatrix, accuracyList)
  }
  
  resultMatrix
}

resultMatrix <- exec()