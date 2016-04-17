# install.packages("ranger")
library(ranger)

createModel <- function (trainingData, testData)
{
  col = ncol(trainingData)
  n <- colnames(trainingData)
  formula <- as.formula(paste(n[col], "~", paste(n[1 : col-1], collapse = " + ")))    # last col as class
  
  model <- ranger(formula, data = trainingData, write.forest = TRUE)
}

findAccuracy <- function (model, testData)
{
  predictResult <- predict(model, testData)
  
  col = ncol(testData)
  absError <- sum( abs(predictResult$predictions - testData[, col]) ) / nrow(testData)
  accuracy <- 1 - absError
}