Algorithms <- list(
  "NaiveBayes.R",
  "NeuralNet.R",
  "Bagging.R",
  "RandomForests.R",
  "GradientBoost.R"
)

preprocessData <- function(useSubset = FALSE)
{
  #raw=read.csv(file.choose(),header=FALSE)
  raw = read.table(file = "data/communities.data", header = FALSE, sep = ",")
  data = raw[, c(-1,-2,-3,-4,-5)]
  data = data[, ! apply(data, 2, function (x) any(x=="?") ) ] # delete column contains ?
  
  # subset selection
  if(useSubset)
  {
    subset = c("V8", "V11", "V12", "V13", "V15", "V17", "V19", "V20", "V21", "V23", "V24",
               "V26", "V27", "V28", "V29", "V32", "V34", "V35", "V37", "V39", "V44", "V45", "V47",
               "V49", "V50", "V53", "V54", "V56", "V57", "V60", "V61", "V64", "V65", "V67", "V69",
               "V70", "V72", "V73", "V74", "V75", "V76", "V77", "V78", "V79", "V80", "V81", "V85",
               "V86", "V88", "V90", "V91", "V92", "V94", "V95", "V96", "V97", "V101", "V121", "V126"
    )
    data = data[, subset]
  }
  data
}


exec <- function(useSubset)
{
  data <- preprocessData(useSubset)
  resultMatrix <- 0
  
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
    
    # subset, num of total instances, num of attr, percent split, 5 accuracies
    subsetFlag <- ifelse(useSubset, "YES", "NO")
    resultItem <- c(subsetFlag, length(data[[1]]), length(data), "10", accuracyList)
    resultMatrix <- rbind(resultMatrix, resultItem)
  }
  
  resultMatrix <- resultMatrix[-1, ]
}

resultMatrix <- exec(FALSE)
resultMatrix <- rbind(resultMatrix, exec(TRUE))