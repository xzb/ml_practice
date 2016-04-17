# install.packages("ipred")
library(ipred)

createModel <- function (trainingData, testData)
{
  col = ncol(trainingData)
  n <- colnames(trainingData)
  formula <- as.formula(paste(n[col], "~", paste(n[1 : col-1], collapse = " + ")))    # last col as class
  
  model <- bagging(formula, data = trainingData, coob = TRUE)
}

findAccuracy <- function (model, testData)
{
  predictResult <- predict(model, newdata = testData)
  
  #accuracy <- 1 - predictResult$error
  col = ncol(testData)
  absError <- sum( abs(predictResult - testData[, col]) ) / nrow(testData)
  accuracy <- 1 - absError
}