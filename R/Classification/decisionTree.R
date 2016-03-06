# install.packages("rpart", dependencies=TRUE)
library(rpart)

createModel <- function (trainingData)
{
  # get the trainingData from Driver
  
  col = ncol(trainingData)
  n <- colnames(trainingData)
  formula <- as.formula(paste(n[col], "~", paste(n[1 : col-1], collapse = " + ")))    # last col as class
  
  model <- rpart(formula, data=trainingData, method = 'class')
  
  prunedTree <- prune(model, cp = 0.010000)
}


findAccuracy <- function (model, testData)
{
  predictResult <- predict(model, testData, type = "class")
  
  col = ncol(testData)
  #table(predictResult, testData[, col])
  
  comp <- predictResult == testData[, col]
  accuracy <- sum(comp == TRUE) / nrow(testData)
}
