# install.packages("bst")
library(bst)

createModel <- function (trainingData, testData)
{
  col = ncol(trainingData)
  n <- colnames(trainingData)
  formula <- as.formula(paste(n[col], "~", paste(n[1 : col-1], collapse = " + ")))    # last col as class
  
  numTrain <- nrow(trainingData)
  allLabel <- factor(c(trainingData[[col]],testData[[col]]))
  trainingData[[col]] <- allLabel[1:numTrain]
  testData[[col]] <- allLabel[-(1:numTrain)]
  
  #model <- adaboost(formula, trainingData, 10)
  
  #model <- boosting(formula, data = trainingData, mfinal = 10, coeflearn = "Zhu",
  #                             control = rpart.control(maxdepth = 5, minsplit = 2))
  
  model <- mada(xtr=trainingData[,-col], ytr=trainingData[,col], xte=testData[,-col], yte=testData[,col])
}

findAccuracy <- function (model, testData)
{
  #predictResult <- predict(model, newdata = testData)
  
  #col = ncol(testData)
  #absError <- sum( abs(predictResult$predictions - testData[, col]) ) / nrow(testData)
  #accuracy <- 1 - absError
  
  #predictResult <- predict.boosting(model, newdata = testData)
  #accuracy <- 1 - predictResult$error
  
  accuracy <- mean(1 - model$err.te)
}