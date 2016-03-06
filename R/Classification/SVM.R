#install.packages("e1071")
library("e1071")

createModel <- function (trainingData)
{
  col = ncol(trainingData)
  n <- colnames(trainingData)
  formula <- as.formula(paste(n[col], "~", paste(n[1 : col-1], collapse = " + ")))    # last col as class
  
  model <- svm(formula, data = trainingData)
}


findAccuracy <- function (model, testData)
{
  col = ncol(testData)
  predictResult <- predict(model, testData[, 1 : col - 1])
  
  errorRate <- sum( (predictResult - testData[, col])^2 ) / nrow(testData)
  
  accuracy <- 1 - errorRate
}
