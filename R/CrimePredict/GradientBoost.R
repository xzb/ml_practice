# install.packages("bst")
library(bst)

createModel <- function (trainingData, testData)
{
  col = ncol(trainingData)
  n <- colnames(trainingData)
  formula <- as.formula(paste(n[col], "~", paste(n[1 : col-1], collapse = " + ")))    # last col as class
  
  #numTrain <- nrow(trainingData)
  #allLabel <- factor(c(trainingData[[col]],testData[[col]]))
  #trainingData[[col]] <- allLabel[1:numTrain]
  #testData[[col]] <- allLabel[-(1:numTrain)]
  trainingData[[col]] <- factor(trainingData[[col]])
  testData[[col]] <- factor(testData[[col]])
  
  #==transform class to numeric 1,2,3,...
  trainingLevel <- levels(trainingData[[col]]) 
  levels(trainingData[[col]]) <- c(1 : nlevels(trainingData[[col]]))
  trainingData[[col]] <- as.numeric(trainingData[[col]])
  
  model <- mbst(x=trainingData[,-col], y=trainingData[,col])
  
  #==predict
  assignTestLevel <- match(levels(testData[[col]]), trainingLevel)
  levels(testData[[col]]) <- assignTestLevel
  testData[[col]] <- as.numeric(testData[[col]])
  
  predictMatrix <- predict(model, newdata=testData[,-col], newy=testData[,col])
  predictResult <- apply(predictMatrix, 1, which.max)
  
  col = ncol(testData)
  absErrorNA <- abs(predictResult - testData[, col])
  maxError <- length(trainingLevel) - 1
  absError <- ifelse(!is.na(absErrorNA), absErrorNA, maxError)
  absError <- sum( absError ) / nrow(testData) / maxError
  accuracy <- 1 - absError
}

findAccuracy <- function (model, testData)
{
  model
}