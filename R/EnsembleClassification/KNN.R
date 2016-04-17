# install.packages("kknn")
library(kknn)

createModel <- function (trainingData, testData)
{
  col = ncol(trainingData)
  n <- colnames(trainingData)
  formula <- as.formula(paste(n[col], "~", paste(n[1 : col-1], collapse = " + ")))    # last col as class
  
  model <- kknn(formula, trainingData, testData, k = 7, distance = 2, kernel = "optimal")  
}

findAccuracy <- function (model, testData)
{
  predictResult <- fitted(model)
  
  col = ncol(testData)
  absError <- sum( abs(predictResult - testData[, col]) ) / nrow(testData)
  accuracy <- 1 - absError
}
